package com.example.food_buzzer_backend.controller;

import com.example.food_buzzer_backend.dto.restaurant_tables.ApiRestaurantTableResponse;
import com.example.food_buzzer_backend.dto.restaurant_tables.CreateRestaurantTableRequest;
import com.example.food_buzzer_backend.dto.restaurant_tables.RestaurantTableResponse;
import com.example.food_buzzer_backend.service.RestaurantTableService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant-tables")
public class RestaurantTableController {

    private final RestaurantTableService restaurantTableService;

    public RestaurantTableController(RestaurantTableService restaurantTableService) {
        this.restaurantTableService = restaurantTableService;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createRestaurantTable(
            @RequestHeader("userId") Long userId,
            @Valid @RequestBody CreateRestaurantTableRequest request) {

        RestaurantTableService.ValidationResult validation = restaurantTableService.validateUserForRestaurant(userId);
        if (!validation.isValid()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiRestaurantTableResponse(false, validation.getMessage()));
        }

        RestaurantTableResponse created = restaurantTableService.createTable(validation.getRestaurantId(), request);
        if (created == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiRestaurantTableResponse(false, "user is not active or restraunt not exist"));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiRestaurantTableResponse(true, "table created successfully", created));
    }

    @GetMapping("/active")
    public ResponseEntity<?> getAllActiveTables(
            @RequestHeader("userId") Long userId) {

        RestaurantTableService.ValidationResult validation = restaurantTableService.validateUserForRestaurant(userId);
        if (!validation.isValid()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiRestaurantTableResponse(false, validation.getMessage()));
        }

        List<RestaurantTableResponse> tables = restaurantTableService.getAllActiveTables(validation.getRestaurantId());
        return ResponseEntity.ok(new ApiRestaurantTableResponse(true, "active tables fetched successfully", tables));
    }
}
