package mukul.order.repository;

import mukul.order.model.Order;
import mukul.order.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.math.BigDecimal;

public interface OrderRepository extends JpaRepository<Order, String> {
    long countByOrderStatus(OrderStatus status);

    @Query("SELECT SUM(o.totalAmount) FROM Order o")
    BigDecimal sumTotalAmount();

    Page<Order> findByUserIdOrderByOrderTimeDesc(String userId, Pageable pageable);

    Page<Order> findByUserIdAndOrderStatusOrderByOrderTimeDesc(String userId, OrderStatus orderStatus, Pageable pageable);
}
