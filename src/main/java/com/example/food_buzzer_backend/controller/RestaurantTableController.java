package com.example.food_buzzer_backend.controller;

import com.example.food_buzzer_backend.dto.restaurant_tables.ApiRestaurantTableResponse;
import com.example.food_buzzer_backend.dto.restaurant_tables.CreateRestaurantTableRequest;
import com.example.food_buzzer_backend.dto.restaurant_tables.RestaurantTableResponse;
import com.example.food_buzzer_backend.service.RestaurantTableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST endpoints for {@code restaurant_tables}.
 *
 * Authentication rule used here (as per your requirement):
 * - Client passes {@code X-User-Id} header
 * - Backend validates user exists + active + has restaurant_id
 * - Backend derives {@code restaurant_id} from users table and scopes all operations to it
 */
@RestController
@RequestMapping("/api/restaurant-tables")
public class RestaurantTableController {

    private final RestaurantTableService restaurantTableService;

    public RestaurantTableController(RestaurantTableService restaurantTableService) {
        this.restaurantTableService = restaurantTableService;
    }

    /**
     * Endpoint: Create a new restaurant table.
     *
     * - Requires {@code X-User-Id} header
     * - restaurant_id is derived from the user (not provided by client)
     * - is_occupied defaults to false
     * - is_delete defaults to false
     */
    @PostMapping("/create")
    public ResponseEntity<?> createRestaurantTable(
            @RequestHeader(name = "X-User-Id", required = true) Long userId,
            @RequestBody CreateRestaurantTableRequest request) {

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

    /**
     * Endpoint: Get all active tables for the user's restaurant.
     *
     * "Active" means {@code is_delete=false}.
     */
    @GetMapping("/active")
    public ResponseEntity<?> getAllActiveTables(
            @RequestHeader(name = "X-User-Id", required = true) Long userId) {

        RestaurantTableService.ValidationResult validation = restaurantTableService.validateUserForRestaurant(userId);
        if (!validation.isValid()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiRestaurantTableResponse(false, validation.getMessage()));
        }

        List<RestaurantTableResponse> tables = restaurantTableService.getAllActiveTables(validation.getRestaurantId());
        return ResponseEntity.ok(new ApiRestaurantTableResponse(true, "active tables fetched successfully", tables));
    }
}

