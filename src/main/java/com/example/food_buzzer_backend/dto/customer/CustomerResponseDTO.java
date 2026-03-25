package com.example.food_buzzer_backend.dto.customer;

import java.time.LocalDateTime;

import com.example.food_buzzer_backend.model.Customer;

public class CustomerResponseDTO {

    private Long id;
    private Long restaurantId;
    private String name;
    private String phone;
    private String emailId;
    private Integer loyaltyPoints;
    private LocalDateTime createdAt;

    public CustomerResponseDTO() {}

    public CustomerResponseDTO(Customer customer) {
        this.id = customer.getId();
        this.restaurantId = customer.getRestaurant() != null ? customer.getRestaurant().getId() : null;
        this.name = customer.getName();
        this.phone = customer.getPhone();
        this.emailId = customer.getEmailId();
        this.loyaltyPoints = customer.getLoyaltyPoints();
        this.createdAt = customer.getCreatedAt();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public Integer getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(Integer loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

