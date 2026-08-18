package mukul.order.services;

import mukul.order.dto.AddToCartRequestDto;
import mukul.order.dto.CartDto;

public interface CartService {
    CartDto getCart(String userId);
    CartDto addToCart(AddToCartRequestDto request);
    CartDto removeFromCart(String userId, String foodItemId);
    void clearCart(String userId);
}
