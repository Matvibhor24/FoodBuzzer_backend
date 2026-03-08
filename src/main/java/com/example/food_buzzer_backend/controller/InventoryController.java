package com.example.food_buzzer_backend.controller;

import com.example.food_buzzer_backend.dto.inventory.ApiInventoryResponse;
import com.example.food_buzzer_backend.dto.inventory.CreateInventoryMaterialRequest;
import com.example.food_buzzer_backend.dto.inventory.InventoryMaterialResponse;
import com.example.food_buzzer_backend.dto.inventory.UpdateInventoryStockRequest;
import com.example.food_buzzer_backend.service.InventoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for inventory material endpoints
 * Handles all HTTP requests related to inventory management
 */
@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Endpoint 1: Show all inventory items with their information
     * URL: GET /api/inventory/all
     * Required Parameters:
     *   - X-Restaurant-Id (header): ID of the restaurant
     * 
     * Returns JSON array of all inventory items for the restaurant
     * 
     * Example Response:
     * {
     *   "success": true,
     *   "message": "All inventory items fetched successfully",
     *   "data": [
     *     {
     *       "id": 1,
     *       "restaurantId": 101,
     *       "name": "Tomatoes",
     *       "sku": "VEG-TOM-001",
     *       "category": "VEGETABLE",
     *       "unit": "kg",
     *       "currentStock": 25.5,
     *       "reorderLevel": 10,
     *       "costPerUnit": 30,
     *       "isActive": true,
     *       "isDeleted": false,
     *       "createdAt": "2026-03-01T10:00:00",
     *       "updatedAt": "2026-03-05T12:00:00"
     *     }
     *   ]
     * }
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllInventoryItems(
            @RequestHeader(name = "X-Restaurant-Id", required = true) Long restaurantId) {

        List<InventoryMaterialResponse> items = inventoryService.getAllInventoryItems(restaurantId);

        // Check if restaurant ID is valid
        if (items == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiInventoryResponse(false, "Wrong restaurant id"));
        }

        return ResponseEntity.ok(new ApiInventoryResponse(true, "All inventory items fetched successfully", items));
    }

    /**
     * Endpoint 2: Add a new item to inventory
     * URL: POST /api/inventory/add
     * Required Parameters:
     *   - X-Restaurant-Id (header): ID of the restaurant
     *   - Request Body (JSON): CreateInventoryMaterialRequest with fields:
     *     * name: String - Name of the material (e.g., "Tomatoes")
     *     * sku: String - Unique SKU code (e.g., "VEG-TOM-001")
     *     * category: String - Category (e.g., "VEGETABLE")
     *     * unit: String - Unit of measurement (e.g., "kg")
     *     * currentStock: Double - Initial stock quantity (e.g., 25.5)
     *     * reorderLevel: Double - Minimum stock before reorder (e.g., 10)
     *     * costPerUnit: BigDecimal - Cost per unit (e.g., 30)
     * 
     * Example Request:
     * {
     *   "name": "Tomatoes",
     *   "sku": "VEG-TOM-001",
     *   "category": "VEGETABLE",
     *   "unit": "kg",
     *   "currentStock": 25.5,
     *   "reorderLevel": 10,
     *   "costPerUnit": 30
     * }
     * 
     * Example Response (Success):
     * {
     *   "success": true,
     *   "message": "Inventory item added successfully",
     *   "data": {
     *     "id": 1,
     *     "restaurantId": 101,
     *     "name": "Tomatoes",
     *     "sku": "VEG-TOM-001",
     *     "category": "VEGETABLE",
     *     "unit": "kg",
     *     "currentStock": 25.5,
     *     "reorderLevel": 10,
     *     "costPerUnit": 30,
     *     "isActive": true,
     *     "isDeleted": false,
     *     "createdAt": "2026-03-01T10:00:00",
     *     "updatedAt": "2026-03-01T10:00:00"
     *   }
     * }
     */
    @PostMapping("/add")
    public ResponseEntity<?> addInventoryItem(
            @RequestHeader(name = "X-Restaurant-Id", required = true) Long restaurantId,
            @RequestBody CreateInventoryMaterialRequest request) {

        InventoryMaterialResponse response = inventoryService.addInventoryItem(request, restaurantId);

        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiInventoryResponse(false, "Failed to add inventory item. SKU might already exist or restaurant not found."));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiInventoryResponse(true, "Inventory item added successfully", response));
    }

    /**
     * Endpoint 3: Update current stock of an inventory item
     * URL: PUT /api/inventory/update-stock/{itemId}
     * Required Parameters:
     *   - itemId (path variable): ID of the inventory item to update
     *   - Request Body (JSON): UpdateInventoryStockRequest with field:
     *     * currentStock: Double - New stock quantity value
     * 
     * Note: The updated_at timestamp is automatically updated by the system
     * 
     * Example Request:
     * {
     *   "currentStock": 15.5
     * }
     * 
     * Example Response:
     * {
     *   "success": true,
     *   "message": "Inventory stock updated successfully",
     *   "data": {
     *     "id": 1,
     *     "restaurantId": 101,
     *     "name": "Tomatoes",
     *     "sku": "VEG-TOM-001",
     *     "category": "VEGETABLE",
     *     "unit": "kg",
     *     "currentStock": 15.5,
     *     "reorderLevel": 10,
     *     "costPerUnit": 30,
     *     "isActive": true,
     *     "isDeleted": false,
     *     "createdAt": "2026-03-01T10:00:00",
     *     "updatedAt": "2026-03-07T14:30:00"
     *   }
     * }
     */
    @PutMapping("/update-stock/{itemId}")
    public ResponseEntity<?> updateInventoryStock(
            @PathVariable Long itemId,
            @RequestHeader(name = "X-Restaurant-Id", required = true) Long restaurantId,
            @RequestBody UpdateInventoryStockRequest request) {

        InventoryMaterialResponse response = inventoryService.updateInventoryStock(itemId, restaurantId, request.getCurrentStock());

        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiInventoryResponse(false, "Wrong restaurant id or item id"));
        }

        return ResponseEntity.ok(new ApiInventoryResponse(true, "Inventory stock updated successfully", response));
    }

    /**
     * Endpoint 4: Delete/soft delete an inventory item
     * URL: DELETE /api/inventory/delete/{itemId}
     * Required Parameters:
     *   - itemId (path variable): ID of the inventory item to delete
     * 
     * Note: This is a soft delete - the record is not removed from database,
     * but the is_delete column is set to true
     * 
     * Example Response:
     * {
     *   "success": true,
     *   "message": "Inventory item deleted successfully",
     *   "data": {
     *     "id": 1,
     *     "restaurantId": 101,
     *     "name": "Tomatoes",
     *     "sku": "VEG-TOM-001",
     *     "category": "VEGETABLE",
     *     "unit": "kg",
     *     "currentStock": 15.5,
     *     "reorderLevel": 10,
     *     "costPerUnit": 30,
     *     "isActive": true,
     *     "isDeleted": true,
     *     "createdAt": "2026-03-01T10:00:00",
     *     "updatedAt": "2026-03-07T14:35:00"
     *   }
     * }
     */
    @DeleteMapping("/delete/{itemId}")
    public ResponseEntity<?> deleteInventoryItem(
            @PathVariable Long itemId,
            @RequestHeader(name = "X-Restaurant-Id", required = true) Long restaurantId) {

        InventoryMaterialResponse response = inventoryService.deleteInventoryItem(itemId, restaurantId);

        if (response == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiInventoryResponse(false, "Wrong restaurant id or item id"));
        }

        return ResponseEntity.ok(new ApiInventoryResponse(true, "Inventory item deleted successfully", response));
    }

    /**
     * Endpoint 5: Search for inventory items by exact name
     * URL: GET /api/inventory/search
     * Required Parameters:
     *   - X-Restaurant-Id (header): ID of the restaurant
     *   - name (query parameter): Exact name of the item to search for (case-insensitive, must match full name)
     * 
     * Example URL: GET /api/inventory/search?name=Tomatoes
     * 
     * Returns JSON array of matching items (exact name match only)
     * 
     * Example Response:
     * {
     *   "success": true,
     *   "message": "Search results",
     *   "data": [
     *     {
     *       "id": 1,
     *       "restaurantId": 101,
     *       "name": "Tomatoes",
     *       "sku": "VEG-TOM-001",
     *       "category": "VEGETABLE",
     *       "unit": "kg",
     *       "currentStock": 15.5,
     *       "reorderLevel": 10,
     *       "costPerUnit": 30,
     *       "isActive": true,
     *       "isDeleted": false,
     *       "createdAt": "2026-03-01T10:00:00",
     *       "updatedAt": "2026-03-07T14:30:00"
     *     }
     *   ]
     * }
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchByName(
            @RequestHeader(name = "X-Restaurant-Id", required = true) Long restaurantId,
            @RequestParam(name = "name", required = true) String name) {

        List<InventoryMaterialResponse> items = inventoryService.searchByName(restaurantId, name);
// Check if restaurant ID is valid - returns null if invalid
        if (items == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiInventoryResponse(false, "Wrong restaurant id"));
        }

        // Check if items were found with the given name
        if (items.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiInventoryResponse(false, "Wrong name entered"));
        }

        
        return ResponseEntity.ok(new ApiInventoryResponse(true, "Search results", items));
    }

    /**
     * Endpoint 6: Get items running on low stock
     * URL: GET /api/inventory/low-stock
     * Required Parameters:
     *   - X-Restaurant-Id (header): ID of the restaurant
     * 
     * Returns JSON array of items with current_stock < 10
     * 
     * Example Response:
     * {
     *   "success": true,
     *   "message": "Low stock items retrieved successfully",
     *   "data": [
     *     {
     *       "id": 1,
     *       "restaurantId": 101,
     *       "name": "Tomatoes",
     *       "sku": "VEG-TOM-001",
     *       "category": "VEGETABLE",
     *       "unit": "kg",
     *       "currentStock": 5.5,
     *       "reorderLevel": 10,
     *       "costPerUnit": 30,
     *       "isActive": true,
     *  // Check if restaurant ID is valid
        if (items == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiInventoryResponse(false, "Wrong restaurant id"));
        }

             "isDeleted": false,
     *       "createdAt": "2026-03-01T10:00:00",
     *       "updatedAt": "2026-03-07T14:30:00"
     *     }
     *   ]
     * }
     */
    @GetMapping("/low-stock")
    public ResponseEntity<?> getLowStockItems(
            @RequestHeader(name = "X-Restaurant-Id", required = true) Long restaurantId) {

        List<InventoryMaterialResponse> items = inventoryService.getLowStockItems(restaurantId);

        // Check if restaurant ID is valid - returns null if invalid
        if (items == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiInventoryResponse(false, "Wrong restaurant id"));
        }

        return ResponseEntity.ok(new ApiInventoryResponse(true, "Low stock items retrieved successfully", items));
    }
}
