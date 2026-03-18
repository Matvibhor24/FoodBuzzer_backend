package com.example.food_buzzer_backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.food_buzzer_backend.dto.ApiResponse;
import com.example.food_buzzer_backend.dto.admin.AdminApprovalRequest;
import com.example.food_buzzer_backend.dto.admin.AdminApprovalResponse;
import com.example.food_buzzer_backend.dto.admin.AdminDashboardResponse;
import com.example.food_buzzer_backend.dto.admin.AdminRestaurantByUserResponse;
import com.example.food_buzzer_backend.service.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/requests")
    public ResponseEntity<ApiResponse> getRequestByStatus(
            @RequestHeader(value = "X-User-Id", required = true) Long adminUserId,
            @RequestParam(required = false) String status
    ) {
        AdminDashboardResponse response = adminService.getRequestsByStatus(adminUserId, status);
        return ResponseEntity.ok(ApiResponse.success("Requests fetched", response));
    }

    @GetMapping("/restaurants/by-user/{userId}")
    public ResponseEntity<ApiResponse> getRestaurantByUserId(
            @RequestHeader(value = "X-User-Id", required = true) Long adminUserId,
            @PathVariable("userId") Long userId
    ) {
        AdminRestaurantByUserResponse response = adminService.getRestaurantByOwnerUserId(adminUserId, userId);
        return ResponseEntity.ok(ApiResponse.success("Restaurants fetched", response));
    }

    @PostMapping("/restaurants/approval")
    public ResponseEntity<ApiResponse> updateApproval(
            @RequestHeader(value = "X-User-Id", required = true) Long adminUserId,
            @RequestBody AdminApprovalRequest request
    ) {
        AdminApprovalResponse response = adminService.updateApproval(adminUserId, request);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success("Approval updated", response));
    }
}