package com.example.food_buzzer_backend.dto.menu;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public class ProductRequestDTO {

    @NotBlank(message = "Product name is required")
    private String name;
    

    
    @NotBlank(message = "Product category is required")
    private String category;
    
    @NotNull(message = "Product price is required")
    private BigDecimal price;
    private Boolean isLive = true;
    private Boolean isBestSeller;
    private Boolean isVeg;
    
    // We will pass the IDs of the recipes this product is composed of
    private List<ProductRecipeRequestDTO> recipes;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getIsLive() {
        return isLive;
    }

    public void setIsLive(Boolean isLive) {
        this.isLive = isLive;
    }

    public Boolean getIsBestSeller() {
        return isBestSeller;
    }

    public void setIsBestSeller(Boolean isBestSeller) {
        this.isBestSeller = isBestSeller;
    }

     public Boolean getIsVeg() {
        return isVeg;
    }

    public void setIsVeg(Boolean isVeg) {
        this.isVeg = isVeg;
    }

    public List<ProductRecipeRequestDTO> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<ProductRecipeRequestDTO> recipes) {
        this.recipes = recipes;
    }

    // Inner class mapping a product to a recipe with an specific quantity
    public static class ProductRecipeRequestDTO {
        private Long recipeId;
        private Double quantity;

        // Getters and Setters
        public Long getRecipeId() {
            return recipeId;
        }

        public void setRecipeId(Long recipeId) {
            this.recipeId = recipeId;
        }

        public Double getQuantity() {
            return quantity;
        }

        public void setQuantity(Double quantity) {
            this.quantity = quantity;
        }
    }
}
