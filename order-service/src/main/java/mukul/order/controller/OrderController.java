package mukul.order.controller;

import mukul.order.dto.CheckoutRequestDto;

import mukul.order.dto.OrderRequestDto;
import mukul.order.dto.OrderResponseDto;
import mukul.order.model.OrderStatus;
import mukul.order.services.OrderServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("api/v1/order")
@RequiredArgsConstructor
public class OrderController {
    @Autowired
    private OrderServiceImpl orderService;

    @PreAuthorize("hasAuthority('ORDER_CREATE')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto placeOrder(@RequestBody OrderRequestDto order) {
        return orderService.createOrder(order);
    }

    @PreAuthorize("hasAuthority('ORDER_CREATE')")
    @PostMapping("/checkout")
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDto checkoutCart(@RequestBody CheckoutRequestDto request) {
        return orderService.checkoutCart(request);
    }

    @PreAuthorize("hasAuthority('ORDER_UPDATE_STATUS')")
    @PutMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public void updateOrderStatus(@RequestParam String orderId,  @RequestBody OrderStatus orderStatus) {
        orderService.updateOrderStatus(orderId, orderStatus);
    }

    @PreAuthorize("hasAuthority('ORDER_READ')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<OrderResponseDto> getAllOrders(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        if (userId != null && !userId.isBlank()) {
            return orderService.getOrdersByUserId(userId, status, page, pageSize);
        }
        return orderService.getAllOrders(page, pageSize);
    }

    @PreAuthorize("hasAuthority('ORDER_READ')")
    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<OrderResponseDto> getOrdersByUserId(
            @PathVariable String userId,
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return orderService.getOrdersByUserId(userId, status, page, pageSize);
    }

    @PreAuthorize("hasAuthority('ORDER_READ')")
    @GetMapping("/{orderId}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponseDto getOrderById(@PathVariable String orderId) {
        return orderService.getOrderById(orderId);
    }

    @PreAuthorize("hasAuthority('ORDER_READ_STATS')")
    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public OrderRequestDto getOrderStats() {
        return orderService.getOrderStats();
    }
}
