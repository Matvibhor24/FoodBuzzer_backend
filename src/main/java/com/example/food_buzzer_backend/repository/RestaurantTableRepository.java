package com.example.food_buzzer_backend.repository;

import com.example.food_buzzer_backend.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository for {@link RestaurantTable}.
 *
 * Notes:
 * - All reads are restaurant-scoped (restaurant_id is derived from the user header).
 * - "Active" tables are rows where {@code is_delete=false}.
 */
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {

    /**
     * Returns all non-deleted tables for a restaurant.
     */
    List<RestaurantTable> findByRestaurantIdAndIsDeleteFalse(Long restaurantId);
}

