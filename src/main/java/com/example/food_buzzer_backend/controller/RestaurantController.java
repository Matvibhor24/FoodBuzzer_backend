package com.example.food_buzzer_backend.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.food_buzzer_backend.dto.restaurant.CreateRestaurantRequest;
import com.example.food_buzzer_backend.dto.restaurant.CreateRestaurantResponse;
import com.example.food_buzzer_backend.dto.restaurant.MyRestaurantResponse;
import com.example.food_buzzer_backend.dto.restaurant.UpdateRestaurantRequest;
import com.example.food_buzzer_backend.dto.restaurant.UpdateRestaurantResponse;
import com.example.food_buzzer_backend.service.RestaurantService;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService){
        this.restaurantService = restaurantService;
    }

    @PostMapping("/create-restaurant")
    public CreateRestaurantResponse createRestaurant(
            @RequestHeader(name = "X-User-Id", required = true) Long userId,
            @Valid @RequestBody CreateRestaurantRequest request){
        return restaurantService.createRestaurant(request, userId);
    }

    @GetMapping("/me")
    public ResponseEntity<MyRestaurantResponse> getMyRestaurant(
            @RequestHeader(name = "X-User-Id", required = true) Long userId) {
        return ResponseEntity.ok(restaurantService.getMyRestaurant(userId));
    }

    @PutMapping("/update")
    public ResponseEntity<UpdateRestaurantResponse> updateRestaurant(
            @RequestHeader(name = "X-User-Id", required = true) Long userId,
            @Valid @RequestBody UpdateRestaurantRequest request) {
        return ResponseEntity.ok(restaurantService.updateRestaurant(request, userId));
    }
}