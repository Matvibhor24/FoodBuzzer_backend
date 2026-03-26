package com.example.food_buzzer_backend.repository;

import com.example.food_buzzer_backend.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByPhoneAndRestaurantId(String phone, Long restaurantId);
    List<Customer> findByRestaurantId(Long restaurantId);
    Optional<Customer> findByIdAndRestaurantId(Long id, Long restaurantId);
}
