package com.example.food_buzzer_backend.dto.admin;

public class AdminApprovalResponse {

    private Long restaurantId;
    private String approvalStatus;
    private String approvalNotes;

    public AdminApprovalResponse() {
    }

    public AdminApprovalResponse(Long restaurantId, String approvalStatus, String approvalNotes) {
        this.restaurantId = restaurantId;
        this.approvalStatus = approvalStatus;
        this.approvalNotes = approvalNotes;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public String getApprovalNotes() {
        return approvalNotes;
    }
}