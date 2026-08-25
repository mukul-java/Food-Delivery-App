package mukul.order.services;

import mukul.order.dto.AddToCartRequestDto;
import mukul.order.dto.CartDto;

public interface CartService {
    CartDto getCart(Long userId);
    CartDto addToCart(AddToCartRequestDto request);
    CartDto removeFromCart(Long userId, String foodItemId);
    void clearCart(Long userId);
}
