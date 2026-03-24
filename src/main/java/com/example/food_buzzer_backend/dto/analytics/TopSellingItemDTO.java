package com.example.food_buzzer_backend.dto.analytics;

public class TopSellingItemDTO {

    private Long productId;
    private String itemName;
    private Integer quantitySold;
    private Double revenue;

    public TopSellingItemDTO() {}

    public TopSellingItemDTO(Long productId, String itemName, Integer quantitySold, Double revenue) {
        this.productId = productId;
        this.itemName = itemName;
        this.quantitySold = quantitySold;
        this.revenue = revenue;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(Integer quantitySold) {
        this.quantitySold = quantitySold;
    }

    public Double getRevenue() {
        return revenue;
    }

    public void setRevenue(Double revenue) {
        this.revenue = revenue;
    }
}
