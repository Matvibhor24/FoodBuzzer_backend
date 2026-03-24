package com.example.food_buzzer_backend.dto.restaurant;

public class UpdateRestaurantResponse {

    private Long id;
    private String message;

    public UpdateRestaurantResponse(Long id, String message) {
        this.id = id;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
