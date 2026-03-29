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

    // Used when customer orders directly from their own interface (no user login)
    private String restaurantSlug;

    // Optional: staff may enter a table number, customers ordering directly won't
    private String tableId;

    @NotEmpty(message = "Cart items cannot be empty")
    private List<CartItemDTO> cartItems;

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
    public String getRestaurantSlug() { return restaurantSlug; }
    public void setRestaurantSlug(String restaurantSlug) { this.restaurantSlug = restaurantSlug; }
    public String getTableId() { return tableId; }
    public void setTableId(String tableId) { this.tableId = tableId; }
    public List<CartItemDTO> getCartItems() { return cartItems; }
    public void setCartItems(List<CartItemDTO> cartItems) { this.cartItems = cartItems; }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
}
