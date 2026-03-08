package com.example.food_buzzer_backend.service;

import com.example.food_buzzer_backend.dto.inventory.CreateInventoryMaterialRequest;
import com.example.food_buzzer_backend.dto.inventory.InventoryMaterialResponse;
import com.example.food_buzzer_backend.model.InventoryMaterial;
import com.example.food_buzzer_backend.model.Restaurant;
import com.example.food_buzzer_backend.repository.InventoryMaterialRepository;
import com.example.food_buzzer_backend.repository.RestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for handling inventory material business logic
 * Contains methods for CRUD operations on inventory items
 */
@Service
public class InventoryService {

    private final InventoryMaterialRepository inventoryMaterialRepository;
    private final RestaurantRepository restaurantRepository;

    public InventoryService(InventoryMaterialRepository inventoryMaterialRepository, 
                           RestaurantRepository restaurantRepository) {
        this.inventoryMaterialRepository = inventoryMaterialRepository;
        this.restaurantRepository = restaurantRepository;
    }

    /**
     * Get all inventory items for a specific restaurant (excluding deleted items)
     * @param restaurantId - ID of the restaurant
     * @return List of InventoryMaterialResponse objects containing all inventory items, or null if restaurant not found
     */
    public List<InventoryMaterialResponse> getAllInventoryItems(Long restaurantId) {
        // Validate that restaurant exists
        if (!restaurantRepository.existsById(restaurantId)) {
            return null; // Restaurant not found - return null
        }
        
        // Fetch all non-deleted inventory items from database
        List<InventoryMaterial> materials = inventoryMaterialRepository.findByRestaurantIdAndIsDeletedFalse(restaurantId);
        
        // Convert to response DTOs and return
        return materials.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Add a new inventory item to the database
     * @param request - CreateInventoryMaterialRequest containing item details (name, sku, category, unit, currentStock, reorderLevel, costPerUnit)
     * @param restaurantId - ID of the restaurant
     * @return InventoryMaterialResponse with created item details, or null if restaurant not found or SKU already exists
     */
    public InventoryMaterialResponse addInventoryItem(CreateInventoryMaterialRequest request, Long restaurantId) {
        // Check if restaurant exists
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        
        if (restaurant == null) {
            return null; // Restaurant not found
        }

        // Check if SKU already exists (SKU must be unique)
        if (inventoryMaterialRepository.findBySku(request.getSku()).isPresent()) {
            return null; // SKU already exists
        }

        // Create new inventory material entity
        InventoryMaterial material = new InventoryMaterial();
        material.setRestaurant(restaurant);
        material.setName(request.getName());
        material.setSku(request.getSku());
        material.setCategory(request.getCategory());
        material.setUnit(request.getUnit());
        material.setCurrentStock(request.getCurrentStock());
        material.setReorderLevel(request.getReorderLevel());
        material.setCostPerUnit(request.getCostPerUnit());
        material.setIsDeleted(false); // By default, item is not deleted
        material.setIsActive(true); // By default, item is active

        // Save to database
        InventoryMaterial savedMaterial = inventoryMaterialRepository.save(material);
        
        // Return response DTO
        return mapToResponse(savedMaterial);
    }

    /**
     * Update the current stock of an inventory item and update the updated_at timestamp
     * @param itemId - ID of the inventory item to update
     * @param restaurantId - ID of the restaurant (to verify ownership)
     * @param newStock - new stock quantity value
     * @return InventoryMaterialResponse with updated item details, or null if item not found or restaurant doesn't match
     */
    public InventoryMaterialResponse updateInventoryStock(Long itemId, Long restaurantId, Double newStock) {
        // Find inventory item by ID
        InventoryMaterial material = inventoryMaterialRepository.findById(itemId).orElse(null);
        
        if (material == null) {
            return null; // Item not found
        }

        // Verify that the item belongs to the specified restaurant
        if (!material.getRestaurant().getId().equals(restaurantId)) {
            return null; // Restaurant ID doesn't match the item's restaurant
        }

        // Update current stock with new value
        material.setCurrentStock(newStock);
        
        // updated_at timestamp is automatically updated by @PreUpdate annotation
        // Save updated item to database
        InventoryMaterial updatedMaterial = inventoryMaterialRepository.save(material);
        
        // Return response DTO
        return mapToResponse(updatedMaterial);
    }

    /**
     * Soft delete an inventory item (sets is_delete to true instead of removing from database)
     * @param itemId - ID of the inventory item to delete
     * @param restaurantId - ID of the restaurant (to verify ownership)
     * @return InventoryMaterialResponse with deleted item details, or null if item not found or restaurant doesn't match
     */
    public InventoryMaterialResponse deleteInventoryItem(Long itemId, Long restaurantId) {
        // Find inventory item by ID
        InventoryMaterial material = inventoryMaterialRepository.findById(itemId).orElse(null);
        
        if (material == null) {
            return null; // Item not found
        }

        // Verify that the item belongs to the specified restaurant
        if (!material.getRestaurant().getId().equals(restaurantId)) {
            return null; // Restaurant ID doesn't match the item's restaurant
        }

        // Mark item as deleted (soft delete - record stays in database)
        material.setIsDeleted(true);
        
        // Save updated item to database
        InventoryMaterial deletedMaterial = inventoryMaterialRepository.save(material);
        
        // Return response DTO
        return mapToResponse(deletedMaterial);
    }

    /**
     * Search for inventory items by exact name (case-insensitive) for a specific restaurant
     * @param restaurantId - ID of the restaurant
     * @param name - Exact name of the item to search for (case-insensitive, must match full name)
     * @return List of InventoryMaterialResponse objects matching the search criteria, or null if restaurant not found
     */
    public List<InventoryMaterialResponse> searchByName(Long restaurantId, String name) {
        // Validate that restaurant exists
        if (!restaurantRepository.existsById(restaurantId)) {
            return null; // Restaurant not found - return null
        }
        
        // Search for items with exact name match (case-insensitive), excluding deleted items
        List<InventoryMaterial> materials = inventoryMaterialRepository
                .findByNameIgnoreCaseAndRestaurantIdAndIsDeletedFalse(name, restaurantId);
        
        // Convert to response DTOs and return (may be empty if no exact match found)
        return materials.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Get all inventory items that are running on low stock (current_stock < 10)
     * @param restaurantId - ID of the restaurant
     * @return List of InventoryMaterialResponse objects for items with low stock, or null if restaurant not found
     */
    public List<InventoryMaterialResponse> getLowStockItems(Long restaurantId) {
        // Validate that restaurant exists
        if (!restaurantRepository.existsById(restaurantId)) {
            return null; // Restaurant not found - return null
        }
        
        // Fetch all items where current_stock < 10 and is_deleted = false
        List<InventoryMaterial> materials = inventoryMaterialRepository.findLowStockItems(restaurantId);
        
        // Convert to response DTOs and return
        return materials.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Helper method to convert InventoryMaterial entity to InventoryMaterialResponse DTO
     * @param material - InventoryMaterial entity from database
     * @return InventoryMaterialResponse DTO with all item information
     */
    private InventoryMaterialResponse mapToResponse(InventoryMaterial material) {
        return new InventoryMaterialResponse(
                material.getId(),
                material.getRestaurant().getId(),
                material.getName(),
                material.getSku(),
                material.getCategory(),
                material.getUnit(),
                material.getCurrentStock(),
                material.getReorderLevel(),
                material.getCostPerUnit(),
                material.getIsActive(),
                material.getIsDeleted(),
                material.getCreatedAt(),
                material.getUpdatedAt()
        );
    }
}
