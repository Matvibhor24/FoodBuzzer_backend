package com.example.food_buzzer_backend.dto.restaurant_tables;

/**
 * Request body for creating a new row in {@code restaurant_tables}.
 *
 * Important:
 * - {@code restaurant_id} is NOT sent by client; it is derived from the {@code X-User-Id} header.
 * - {@code is_occupied} is NOT sent by client; it is set to {@code false} by default on creation.
 * - {@code is_delete} is NOT sent by client; it is set to {@code false} by default on creation.
 */
public class CreateRestaurantTableRequest {

    private Integer tableNo;
    private Integer tableSize;
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

