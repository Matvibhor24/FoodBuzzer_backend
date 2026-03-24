package com.example.food_buzzer_backend.dto.auth;

import com.example.food_buzzer_backend.model.User;

public class UserProfileResponseDTO {
    private String email;
    private String phone;
    private String role;

    public UserProfileResponseDTO() {}

    public UserProfileResponseDTO(User user) {
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.role = user.getRole();
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
