package com.example.food_buzzer_backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.food_buzzer_backend.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId);
    List<Order> findByRestaurantIdAndStatusInOrderByCreatedAtDesc(Long restaurantId, List<String> statuses);

    @Query("""
            select o
            from Order o
            left join fetch o.cart
            where o.restaurant.id = :restaurantId
              and o.createdAt >= :startDateTime
              and o.createdAt < :endDateTime
              and o.status = :status
            order by o.createdAt asc
            """)
    List<Order> findAnalyticsOrdersByRestaurantAndDateRange(
            @Param("restaurantId") Long restaurantId,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("status") String status
    );
}
