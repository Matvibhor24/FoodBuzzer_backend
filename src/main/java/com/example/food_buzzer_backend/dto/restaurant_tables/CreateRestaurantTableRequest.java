package com.example.food_buzzer_backend.dto.restaurant_tables;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CreateRestaurantTableRequest {

    @NotNull(message = "Table number is required")
    private Integer tableNo;
    
    @NotNull(message = "Table size is required")
    @Min(value = 1, message = "Table size must be at least 1")
    private Integer tableSize;
    
    @NotNull(message = "Floor is required")
    private Integer floor;

    public CreateRestaurantTableRequest() {}

    public Integer getTableNo() {
        return tableNo;
    }

    public void setTableNo(Integer tableNo) {
        this.tableNo = tableNo;
    }

    public Integer getTableSize() {
        return tableSize;
    }

    public void setTableSize(Integer tableSize) {
        this.tableSize = tableSize;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }
}
