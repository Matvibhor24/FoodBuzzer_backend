package com.example.food_buzzer_backend.dto.order;

import java.util.List;

public class OrderRequest {
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private Long restaurantId;
    private String tableId;
    private Long userId; // Staff taking the order
    private List<CartItemDTO> cartItems;
    private Double overrideDiscount; // in case admin manually adds discount, though we auto-calculate loyalty

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
}
