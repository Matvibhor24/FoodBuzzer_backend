package com.example.food_buzzer_backend.dto.analytics;

public class MonthlyAnalyticsResponse {

    private Integer year;
    private Integer month;
    private Long totalOrders;
    private Double totalRevenue;

    public MonthlyAnalyticsResponse() {}

    public MonthlyAnalyticsResponse(Integer year, Integer month, Long totalOrders, Double totalRevenue) {
        this.year = year;
        this.month = month;
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
