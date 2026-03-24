package com.example.food_buzzer_backend.controller;

import com.example.food_buzzer_backend.dto.analytics.DailyAnalyticsResponse;
import com.example.food_buzzer_backend.dto.analytics.MonthlyAnalyticsResponse;
import com.example.food_buzzer_backend.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/analytics")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/daily")
    public ResponseEntity<DailyAnalyticsResponse> getDailyAnalytics(
            @RequestHeader("X-User-Id") Long userId,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        DailyAnalyticsResponse response = analyticsService.getDailyAnalytics(userId, date);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/monthly/current")
    public ResponseEntity<MonthlyAnalyticsResponse> getCurrentMonthAnalytics(
            @RequestHeader("X-User-Id") Long userId) {
        MonthlyAnalyticsResponse response = analyticsService.getCurrentMonthAnalytics(userId);
        return ResponseEntity.ok(response);
    }
}
