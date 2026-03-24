package com.example.food_buzzer_backend.dto.order;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class OrderRequest {
    @NotBlank(message = "Customer name is required")
    private String customerName;
    @NotBlank(message = "Customer phone is required")
    private String customerPhone;
    private String customerEmail;
    private Long restaurantId;
    private String tableId;
    private Long userId; // Staff taking the order
    
    @NotEmpty(message = "Cart items cannot be empty")
    private List<CartItemDTO> cartItems;
    private Double overrideDiscount; // in case admin manually adds discount, though we auto-calculate loyalty

    @DecimalMin(value = "1.0", message = "Rating must be at least 1.0")
    @DecimalMax(value = "5.0", message = "Rating must be at most 5.0")
    private Double rating;

    public OrderRequest() {}

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public String getCustomerEmail() { return customerEmail; }
    public void setCustomerEmail(String customerEmail) { this.customerEmail = customerEmail; }
    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }
    public String getTableId() { return tableId; }
    public void setTableId(String tableId) { this.tableId = tableId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public List<CartItemDTO> getCartItems() { return cartItems; }
    public void setCartItems(List<CartItemDTO> cartItems) { this.cartItems = cartItems; }
    public Double getOverrideDiscount() { return overrideDiscount; }
    public void setOverrideDiscount(Double overrideDiscount) { this.overrideDiscount = overrideDiscount; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
}
