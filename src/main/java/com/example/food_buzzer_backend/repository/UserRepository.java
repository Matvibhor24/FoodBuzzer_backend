package com.example.food_buzzer_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.food_buzzer_backend.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}