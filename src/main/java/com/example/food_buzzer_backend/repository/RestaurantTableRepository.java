package com.example.food_buzzer_backend.repository;

import com.example.food_buzzer_backend.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    List<RestaurantTable> findByRestaurantIdAndIsDeleteFalse(Long restaurantId);
}
