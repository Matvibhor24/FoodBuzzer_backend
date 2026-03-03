package com.example.food_buzzer_backend.service;

import org.springframework.stereotype.Service;
import java.util.Optional;
import com.example.food_buzzer_backend.model.User;
import com.example.food_buzzer_backend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(User user) {
        user.setisActive(true);
        return userRepository.save(user);
    }

    public String login(String email, String password) {

        Optional<User> userOptional = userRepository.findByEmail(email);
    
        if (userOptional.isEmpty()) {
            return "Invalid credentials";
        }
    
        User user = userOptional.get();
    
        if (!user.getisActive()) {
            return "User is not active";
        }
    
        if (!user.getPassword().equals(password)) {
            return "Invalid credentials";
        }
    
        return String.valueOf(user.getId());
    }
}