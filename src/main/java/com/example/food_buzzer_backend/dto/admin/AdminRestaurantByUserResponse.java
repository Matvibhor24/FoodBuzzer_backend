package com.example.food_buzzer_backend.dto.admin;

import java.util.List;

public class AdminRestaurantByUserResponse {

    private List<RestaurantDetailsResponse> restaurants;

    public AdminRestaurantByUserResponse() {
    }

    public AdminRestaurantByUserResponse(List<RestaurantDetailsResponse> restaurants) {
        this.restaurants = restaurants;
    }

    public List<RestaurantDetailsResponse> getRestaurants() {
        return restaurants;
    }
}