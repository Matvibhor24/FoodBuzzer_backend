package com.example.food_buzzer_backend.dto.order;

import com.example.food_buzzer_backend.model.Order;
import java.time.LocalDateTime;

public class OrderResponse {
    private Long id;
    private String customerName;
    private String customerPhone;
    private Double cartTotal;
    private Double discount;
    private Double grandTotal;
    private String status;
    private String tableId;
    private Integer remainingLoyaltyPoints;
    private LocalDateTime createdAt;
    private String cartItems;
    private Double rating;

    public OrderResponse() {}

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.customerName = order.getCustomer().getName();
        this.customerPhone = order.getCustomer().getPhone();
        this.cartTotal = order.getCartTotal();
        this.discount = order.getDiscount();
        this.grandTotal = order.getGrandTotal();
        this.status = order.getStatus();
        this.tableId = order.getTableId();
        this.createdAt = order.getCreatedAt();
        this.rating = order.getRating();
        this.remainingLoyaltyPoints = order.getCustomer().getLoyaltyPoints();
        if (order.getCart()!=null && order.getCart().getCartItems()!=null){
            this.cartItems = order.getCart().getCartItems();
        }
        else{
            this.cartItems = null;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }
    public Double getCartTotal() { return cartTotal; }
    public void setCartTotal(Double cartTotal) { this.cartTotal = cartTotal; }
    public Double getDiscount() { return discount; }
    public void setDiscount(Double discount) { this.discount = discount; }
    public Double getGrandTotal() { return grandTotal; }
    public void setGrandTotal(Double grandTotal) { this.grandTotal = grandTotal; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTableId() { return tableId; }
    public void setTableId(String tableId) { this.tableId = tableId; }
    public Integer getRemainingLoyaltyPoints() { return remainingLoyaltyPoints; }
    public void setRemainingLoyaltyPoints(Integer remainingLoyaltyPoints) { this.remainingLoyaltyPoints = remainingLoyaltyPoints; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getCartItems(){
        return cartItems;
    }
    public void setCartItems(String cartItems){
        this.cartItems = cartItems;
    }
    public Double getRating() { return rating; }
    public void setRating(Double rating) { this.rating = rating; }
}
