package com.example.food_buzzer_backend.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.food_buzzer_backend.model.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}