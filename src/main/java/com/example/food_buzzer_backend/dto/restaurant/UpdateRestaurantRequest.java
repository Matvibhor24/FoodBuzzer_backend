package com.example.food_buzzer_backend.dto.restaurant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class UpdateRestaurantRequest {

    @NotBlank(message = "Name is required")
    private String name;

    private String email;

    @NotBlank(message = "Address is required")
    private String address;

    private String city;

    @NotBlank(message = "Zipcode is required")
    private String zipcode;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotNull(message = "isLive status is required")
    private Boolean isLive;

    public UpdateRestaurantRequest() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getIsLive() {
        return isLive;
    }

    public void setIsLive(Boolean isLive) {
        this.isLive = isLive;
    }
}
