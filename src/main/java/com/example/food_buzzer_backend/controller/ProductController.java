package com.example.food_buzzer_backend.controller;

import jakarta.validation.Valid;
import com.example.food_buzzer_backend.dto.ApiResponse;
import com.example.food_buzzer_backend.dto.menu.ProductRequestDTO;
import com.example.food_buzzer_backend.dto.menu.ProductResponseDTO;
import com.example.food_buzzer_backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse> createProduct(
            @RequestHeader(name = "X-User-Id", required = true) Long userId,
            @Valid @RequestBody ProductRequestDTO requestDTO) {
        ProductResponseDTO responseDTO = productService.createProduct(userId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Product created successfully", responseDTO));
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllProducts(
            @RequestHeader(name = "X-User-Id", required = true) Long userId) {
        List<ProductResponseDTO> products = productService.getAllProducts(userId);
        return ResponseEntity.ok(ApiResponse.success("Products retrieved successfully", products));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse> getProductById(
            @RequestHeader(name = "X-User-Id", required = true) Long userId,
            @PathVariable Long productId) {
        ProductResponseDTO product = productService.getProductById(userId, productId);
        return ResponseEntity.ok(ApiResponse.success("Product retrieved successfully", product));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ApiResponse> updateProduct(
            @RequestHeader(name = "X-User-Id", required = true) Long userId,
            @PathVariable Long productId,
            @Valid @RequestBody ProductRequestDTO requestDTO) {
        ProductResponseDTO responseDTO = productService.updateProduct(userId, productId, requestDTO);
        return ResponseEntity.ok(ApiResponse.success("Product updated successfully", responseDTO));
    }
}
