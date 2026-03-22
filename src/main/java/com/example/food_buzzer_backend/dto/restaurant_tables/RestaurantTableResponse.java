package com.example.food_buzzer_backend.dto.restaurant_tables;

public class RestaurantTableResponse {

    private Long id;
    private Long restaurantId;
    private Integer tableNo;
    private Integer tableSize;
    private Integer floor;
    private Boolean isOccupied;
    private Boolean isDelete;

    public RestaurantTableResponse() {}

    public RestaurantTableResponse(Long id,
                                 Long restaurantId,
                                 Integer tableNo,
                                 Integer tableSize,
                                 Integer floor,
                                 Boolean isOccupied,
                                 Boolean isDelete) {
        this.id = id;
        this.restaurantId = restaurantId;
        this.tableNo = tableNo;
        this.tableSize = tableSize;
        this.floor = floor;
        this.isOccupied = isOccupied;
        this.isDelete = isDelete;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }

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

    public Boolean getIsOccupied() {
        return isOccupied;
    }

    public void setIsOccupied(Boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }
}
