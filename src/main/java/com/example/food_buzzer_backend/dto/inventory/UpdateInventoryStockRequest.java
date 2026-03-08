package com.example.food_buzzer_backend.dto.inventory;

/**
 * DTO for updating the current stock of an inventory item
 * Parameters to pass in PUT request:
 * - currentStock: The new stock quantity value (this will replace the existing value)
 *
 * Example:
 * {
 *   "currentStock": 15.5
 * }
 */
public class UpdateInventoryStockRequest {

    private Double currentStock;

    public UpdateInventoryStockRequest() {}

    // Getter and Setter for currentStock
    public Double getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Double currentStock) {
        this.currentStock = currentStock;
    }
}
