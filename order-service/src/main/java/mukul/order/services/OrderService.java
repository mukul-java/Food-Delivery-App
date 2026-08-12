package mukul.order.services;

import mukul.order.dto.OrderRequestDto;
import mukul.order.dto.OrderResponseDto;
import mukul.order.model.OrderStatus;

import java.util.List;

public interface OrderService {

    /**
     * Creates a new order and publishes an OrderCreated event.
     *
     * @param request Order creation request.
     * @return Created order details.
     */
    OrderResponseDto createOrder(OrderRequestDto request);

    /**
     * Updates the order after receiving the payment result.
     *
     * paymentInfo:
     * 0 -> Payment ID
     * 1 -> Order ID
     * 2 -> Payment Status
     */
    void updateOrderAfterPayment(List<String> paymentInfo);

    /**
     * Updates the status of an order.
     *
     * @param orderId Order identifier.
     * @param orderStatus New order status.
     */
    void updateOrderStatus(String orderId, OrderStatus orderStatus);
}