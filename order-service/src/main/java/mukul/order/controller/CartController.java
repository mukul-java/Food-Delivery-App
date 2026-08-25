package mukul.order.controller;

import lombok.RequiredArgsConstructor;
import mukul.order.dto.AddToCartRequestDto;
import mukul.order.dto.CartDto;
import mukul.order.services.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PreAuthorize("hasAuthority('CART_WRITE')")
    @PostMapping("/add")
    @ResponseStatus(HttpStatus.OK)
    public CartDto addToCart(@RequestBody AddToCartRequestDto request) {
        return cartService.addToCart(request);
    }

    @PreAuthorize("hasAuthority('CART_READ')")
    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public CartDto getCart(@PathVariable Long userId) {
        return cartService.getCart(userId);
    }

    @PreAuthorize("hasAuthority('CART_WRITE')")
    @DeleteMapping("/{userId}/item/{foodItemId}")
    @ResponseStatus(HttpStatus.OK)
    public CartDto removeFromCart(@PathVariable Long userId, @PathVariable String foodItemId) {
        return cartService.removeFromCart(userId, foodItemId);
    }

    @PreAuthorize("hasAuthority('CART_WRITE')")
    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void clearCart(@PathVariable Long userId) {
        cartService.clearCart(userId);
    }
}
