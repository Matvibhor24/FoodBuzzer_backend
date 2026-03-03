package com.example.food_buzzer_backend.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import com.example.food_buzzer_backend.model.User;
import com.example.food_buzzer_backend.service.UserService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
public String login(@RequestBody User loginRequest) {

    return userService.login(
            loginRequest.getEmail(),
            loginRequest.getPassword()
    );
}
}