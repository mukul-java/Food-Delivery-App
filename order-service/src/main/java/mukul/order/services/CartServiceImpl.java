package mukul.order.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mukul.order.dto.AddToCartRequestDto;
import mukul.order.dto.CartDto;
import mukul.order.dto.CartItemDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements CartService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final String CART_KEY_PREFIX = "cart:";
    private static final long CART_TTL_HOURS = 24;

    @Override
    public CartDto getCart(Long userId) {
        String key = CART_KEY_PREFIX + userId;
        String cartJson = redisTemplate.opsForValue().get(key);
        
        if (cartJson != null) {
            try {
                return objectMapper.readValue(cartJson, CartDto.class);
            } catch (JsonProcessingException e) {
                log.error("Error deserializing cart for user {}", userId, e);
            }
        }
        
        return CartDto.builder()
                .cartId(UUID.randomUUID().toString())
                .userId(userId)
                .build();
    }

    @Override
    public CartDto addToCart(AddToCartRequestDto request) {
        CartDto cart = getCart(request.getUserId());
        
        // Single Restaurant Rule
        if (cart.getRestaurantId() != null && !cart.getRestaurantId().equals(request.getRestaurantId())) {
            // If the user tries to add an item from a different restaurant, clear the cart.
            cart.getItems().clear();
        }
        
        cart.setRestaurantId(request.getRestaurantId());
        
        Optional<CartItemDto> existingItemOpt = cart.getItems().stream()
                .filter(item -> item.getFoodItemId().equals(request.getFoodItemId()))
                .findFirst();

        if (existingItemOpt.isPresent()) {
            CartItemDto existingItem = existingItemOpt.get();
            int newQuantity = existingItem.getQuantity() + request.getQuantity();
            if (newQuantity <= 0) {
                cart.getItems().remove(existingItem);
            } else {
                existingItem.setQuantity(newQuantity);
            }
        } else if (request.getQuantity() > 0) {
            cart.getItems().add(CartItemDto.builder()
                    .foodItemId(request.getFoodItemId())
                    .name(request.getName())
                    .price(request.getPrice())
                    .quantity(request.getQuantity())
                    .specialInstructions(request.getSpecialInstructions())
                    .build());
        }

        cart.setTotalAmount(calculateTotalAmount(cart));
        cart.setUpdatedAt(new Date());
        
        saveCart(cart);
        return cart;
    }

    @Override
    public CartDto removeFromCart(Long userId, String foodItemId) {
        CartDto cart = getCart(userId);
        
        cart.getItems().removeIf(item -> item.getFoodItemId().equals(foodItemId));
        
        if (cart.getItems().isEmpty()) {
            clearCart(userId);
            return cart;
        }
        
        cart.setTotalAmount(calculateTotalAmount(cart));
        cart.setUpdatedAt(new Date());
        saveCart(cart);
        
        return cart;
    }

    @Override
    public void clearCart(Long userId) {
        String key = CART_KEY_PREFIX + userId;
        redisTemplate.delete(key);
    }
    
    private void saveCart(CartDto cart) {
        try {
            String key = CART_KEY_PREFIX + cart.getUserId();
            String cartJson = objectMapper.writeValueAsString(cart);
            redisTemplate.opsForValue().set(key, cartJson, CART_TTL_HOURS, TimeUnit.HOURS);
        } catch (JsonProcessingException e) {
            log.error("Error serializing cart for user {}", cart.getUserId(), e);
            throw new RuntimeException("Could not save cart", e);
        }
    }
    
    private BigDecimal calculateTotalAmount(CartDto cart) {
        return BigDecimal.valueOf(
                cart.getItems().stream()
                        .mapToInt(item -> item.getPrice() * item.getQuantity())
                        .sum()
        );
    }
}
