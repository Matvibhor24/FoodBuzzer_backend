package com.example.food_buzzer_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.food_buzzer_backend.dto.ApiResponse;
import com.example.food_buzzer_backend.dto.customer.CustomerResponseDTO;
import com.example.food_buzzer_backend.service.CustomerService;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getCustomers(
            @RequestHeader("userId") Long userId,
            @RequestParam(required = false) Long id) {
        try {
            if (id == null) {
                return ResponseEntity.ok(ApiResponse.success("Customers retrieved successfully", customerService.getAllCustomers(userId)));
            }

            CustomerResponseDTO customer = customerService.getCustomerById(userId, id);
            return ResponseEntity.ok(ApiResponse.success("Customer retrieved successfully", customer));
        } catch (IllegalArgumentException ex) {
            if ("user not found or restaurant not found".equals(ex.getMessage())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(ex.getMessage()));
            }
            if ("customers not found".equals(ex.getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(ex.getMessage()));
        }
    }
}

