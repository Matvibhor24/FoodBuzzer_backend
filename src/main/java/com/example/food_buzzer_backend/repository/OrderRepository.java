package com.example.food_buzzer_backend.repository;

import com.example.food_buzzer_backend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByRestaurantIdOrderByCreatedAtDesc(Long restaurantId);
    List<Order> findByRestaurantIdAndStatusInOrderByCreatedAtDesc(Long restaurantId, List<String> statuses);
}
