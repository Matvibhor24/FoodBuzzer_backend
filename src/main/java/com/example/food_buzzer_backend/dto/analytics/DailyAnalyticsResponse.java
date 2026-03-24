package com.example.food_buzzer_backend.dto.analytics;

import java.time.LocalDate;
import java.util.List;

public class DailyAnalyticsResponse {

    private LocalDate date;
    private Long totalOrders;
    private Double totalRevenue;
    private List<TopSellingItemDTO> topSellingItems;
    private List<HourlyOrderCountDTO> hourlyOrders;

    public DailyAnalyticsResponse() {}

    public DailyAnalyticsResponse(
            LocalDate date,
            Long totalOrders,
            Double totalRevenue,
            List<TopSellingItemDTO> topSellingItems,
            List<HourlyOrderCountDTO> hourlyOrders
    ) {
        this.date = date;
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
        this.topSellingItems = topSellingItems;
        this.hourlyOrders = hourlyOrders;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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

    public List<TopSellingItemDTO> getTopSellingItems() {
        return topSellingItems;
    }

    public void setTopSellingItems(List<TopSellingItemDTO> topSellingItems) {
        this.topSellingItems = topSellingItems;
    }

    public List<HourlyOrderCountDTO> getHourlyOrders() {
        return hourlyOrders;
    }

    public void setHourlyOrders(List<HourlyOrderCountDTO> hourlyOrders) {
        this.hourlyOrders = hourlyOrders;
    }
}
