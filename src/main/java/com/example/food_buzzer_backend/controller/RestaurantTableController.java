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
            @RequestHeader("X-User-Id") Long userId,
            @Valid @RequestBody CreateRestaurantTableRequest request) {

        RestaurantTableService.ValidationResult validation = restaurantTableService.validateUserForRestaurant(userId);
        if (!validation.isValid()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiRestaurantTableResponse(false, validation.getMessage()));
        }

        RestaurantTableResponse created;
        try {
            created = restaurantTableService.createTable(validation.getRestaurantId(), request);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiRestaurantTableResponse(false, e.getMessage()));
        }

        if (created == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiRestaurantTableResponse(false, "user is not active or restraunt not exist"));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiRestaurantTableResponse(true, "table created successfully", created));
    }

    @GetMapping("/active")
    public ResponseEntity<?> getAllActiveTables(
            @RequestHeader(value = "X-User-Id", required = false) Long userId,
            @RequestParam(value = "restaurantSlug", required = false) String restaurantSlug) {

        Long restaurantId = null;

        if (userId != null) {
            RestaurantTableService.ValidationResult validation = restaurantTableService.validateUserForRestaurant(userId);
            if (!validation.isValid()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiRestaurantTableResponse(false, validation.getMessage()));
            }
            restaurantId = validation.getRestaurantId();
        } else if (restaurantSlug != null) {
            restaurantId = restaurantTableService.getRestaurantIdBySlug(restaurantSlug);
            if (restaurantId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ApiRestaurantTableResponse(false, "Invalid restaurant slug or restaurant is not active"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiRestaurantTableResponse(false, "Either X-User-Id header or restaurantSlug parameter is required"));
        }

        List<RestaurantTableResponse> tables = restaurantTableService.getAllActiveTables(restaurantId);
        return ResponseEntity.ok(new ApiRestaurantTableResponse(true, "active tables fetched successfully", tables));
    }
}
