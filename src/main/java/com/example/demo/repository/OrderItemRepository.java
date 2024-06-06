package com.example.demo.repository;

import com.example.demo.model.OrderItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("SELECT oi FROM OrderItem oi WHERE oi.id = :orderItemId AND oi.order.id = :orderId "
            + "AND oi.order.user.id = :userId")
    Optional<OrderItem> findByIdAndUserIdAndOrderId(@Param("userId") Long userId,
                                                    @Param("orderId") Long orderId,
                                                    @Param("orderItemId") Long orderItemId);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.order.id = :orderId "
            + "AND oi.order.user.id = :userId")
    List<OrderItem> findAllByOrderIdAndUserId(@Param("orderId") Long orderId,
                                       @Param("userId") Long userId, Pageable pageable);
}
