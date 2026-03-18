package com.example.food_buzzer_backend.model;

import jakarta.persistence.*;

/**
 * Entity for the DB table {@code restaurant_tables}.
 *
 * This table stores seating/table metadata for a specific restaurant.
 * - Ownership/scoping: each row belongs to exactly one {@link Restaurant} via {@code restaurant_id}.
 * - Soft delete: {@code is_delete=true} means the row is considered inactive.
 * - Occupancy: {@code is_occupied} is a boolean status (created as {@code false} by default).
 */
@Entity
@Table(name = "restaurant_tables")
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_no", nullable = false)
    private Integer tableNo;

    @Column(name = "table_size", nullable = false)
    private Integer tableSize;

    @Column(name = "floor", nullable = false)
    private Integer floor;

    @Column(name = "is_occupied", nullable = false)
    private Boolean isOccupied = false;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;

    public RestaurantTable() {}

    public Long getId() {
        return id;
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

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}

