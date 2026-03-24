package com.example.food_buzzer_backend.dto.analytics;

public class HourlyOrderCountDTO {

    private Integer hour;
    private Integer orderCount;

    public HourlyOrderCountDTO() {}

    public HourlyOrderCountDTO(Integer hour, Integer orderCount) {
        this.hour = hour;
        this.orderCount = orderCount;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }
}
