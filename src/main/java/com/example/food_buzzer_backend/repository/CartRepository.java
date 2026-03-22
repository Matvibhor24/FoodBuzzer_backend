package com.example.food_buzzer_backend.repository;

import com.example.food_buzzer_backend.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
