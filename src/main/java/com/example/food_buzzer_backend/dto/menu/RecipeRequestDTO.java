package com.example.food_buzzer_backend.dto.menu;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class RecipeRequestDTO {
    
    @NotBlank(message = "Recipe name is required")
    private String name;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotEmpty(message = "Recipe items cannot be empty")
    @Valid
    private List<RecipeItemRequestDTO> items;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<RecipeItemRequestDTO> getItems() {
        return items;
    }

    public void setItems(List<RecipeItemRequestDTO> items) {
        this.items = items;
    }

    public static class RecipeItemRequestDTO {
        @NotNull(message = "Raw material ID is required")
        private Long rawMaterialId;
        
        @NotNull(message = "Quantity is required")
        private Double quantity;

        // Getters and Setters
        public Long getRawMaterialId() {
            return rawMaterialId;
        }

        public void setRawMaterialId(Long rawMaterialId) {
            this.rawMaterialId = rawMaterialId;
        }

        public Double getQuantity() {
            return quantity;
        }

        public void setQuantity(Double quantity) {
            this.quantity = quantity;
        }
    }
}
