package com.example.food_buzzer_backend.dto.restaurant;

import com.example.food_buzzer_backend.model.Restaurant;
import java.time.LocalDateTime;

public class MyRestaurantResponse {
    private Long id;
    private String name;
    private String slug;
    private String address;
    private String gst;
    private String zipcode;
    private String phone;
    private String approvalStatus;
    private String approvalNote;
    private boolean isLive;
    private boolean isActive;
    private LocalDateTime createdAt;

    public MyRestaurantResponse() {}

    public MyRestaurantResponse(Restaurant restaurant) {
        this.id = restaurant.getId();
        this.name = restaurant.getName();
        this.slug = restaurant.getSlug();
        this.address = restaurant.getAddress();
        this.gst = restaurant.getGST();
        this.zipcode = restaurant.getZipcode();
        this.phone = restaurant.getPhone();
        this.approvalStatus = restaurant.getApprovalStatus();
        this.approvalNote = restaurant.getApprovalNote();
        this.isLive = restaurant.getIsLive();
        this.isActive = restaurant.getIsActive();
        this.createdAt = restaurant.getCreatedAt();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSlug() { return slug; }
    public void setSlug(String slug) { this.slug = slug; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getGst() { return gst; }
    public void setGst(String gst) { this.gst = gst; }
    public String getZipcode() { return zipcode; }
    public void setZipcode(String zipcode) { this.zipcode = zipcode; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getApprovalStatus() { return approvalStatus; }
    public void setApprovalStatus(String approvalStatus) { this.approvalStatus = approvalStatus; }
    public String getApprovalNote() { return approvalNote; }
    public void setApprovalNote(String approvalNote) { this.approvalNote = approvalNote; }
    public boolean getIsLive() { return isLive; }
    public void setIsLive(boolean isLive) { this.isLive = isLive; }
    public boolean getIsActive() { return isActive; }
    public void setIsActive(boolean isActive) { this.isActive = isActive; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
