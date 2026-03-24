package com.example.food_buzzer_backend.service;

import com.example.food_buzzer_backend.config.AppConstants;
import com.example.food_buzzer_backend.dto.analytics.DailyAnalyticsResponse;
import com.example.food_buzzer_backend.dto.analytics.HourlyOrderCountDTO;
import com.example.food_buzzer_backend.dto.analytics.MonthlyAnalyticsResponse;
import com.example.food_buzzer_backend.dto.analytics.TopSellingItemDTO;
import com.example.food_buzzer_backend.dto.order.CartItemDTO;
import com.example.food_buzzer_backend.exception.ResourceNotFoundException;
import com.example.food_buzzer_backend.model.Order;
import com.example.food_buzzer_backend.model.User;
import com.example.food_buzzer_backend.repository.OrderRepository;
import com.example.food_buzzer_backend.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Transactional(readOnly = true)
    public DailyAnalyticsResponse getDailyAnalytics(Long userId, LocalDate date) {
        User user = validateUserAndGetRestaurantScopedUser(userId);

        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.plusDays(1).atStartOfDay();

        List<Order> orders = orderRepository.findAnalyticsOrdersByRestaurantAndDateRange(
                user.getRestaurant().getId(),
                startDateTime,
                endDateTime,
                AppConstants.ORDER_STATUS_COMPLETED
        );

        double totalRevenue = calculateTotalRevenue(orders);
        List<HourlyOrderCountDTO> hourlyOrders = buildHourlyOrders(orders);
        List<TopSellingItemDTO> topSellingItems = buildTopSellingItems(orders, 5);

        return new DailyAnalyticsResponse(
                date,
                (long) orders.size(),
                totalRevenue,
                topSellingItems,
                hourlyOrders
        );
    }

    @Transactional(readOnly = true)
    public MonthlyAnalyticsResponse getCurrentMonthAnalytics(Long userId) {
        User user = validateUserAndGetRestaurantScopedUser(userId);

        YearMonth currentMonth = YearMonth.now();
        LocalDateTime startDateTime = currentMonth.atDay(1).atStartOfDay();
        LocalDateTime endDateTime = currentMonth.plusMonths(1).atDay(1).atStartOfDay();

        List<Order> orders = orderRepository.findAnalyticsOrdersByRestaurantAndDateRange(
                user.getRestaurant().getId(),
                startDateTime,
                endDateTime,
                AppConstants.ORDER_STATUS_COMPLETED
        );

        return new MonthlyAnalyticsResponse(
                currentMonth.getYear(),
                currentMonth.getMonthValue(),
                (long) orders.size(),
                calculateTotalRevenue(orders)
        );
    }

    private User validateUserAndGetRestaurantScopedUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (!user.getIsActive()) {
            throw new IllegalArgumentException("User is not active");
        }

        if (user.getRestaurant() == null) {
            throw new IllegalArgumentException("User does not belong to any restaurant");
        }

        return user;
    }

    private double calculateTotalRevenue(List<Order> orders) {
        return orders.stream()
                .map(order -> order.getGrandTotal() != null ? order.getGrandTotal() : 0.0)
                .reduce(0.0, Double::sum);
    }

    private List<HourlyOrderCountDTO> buildHourlyOrders(List<Order> orders) {
        int[] hourlyBuckets = new int[24];

        for (Order order : orders) {
            if (order.getCreatedAt() != null) {
                hourlyBuckets[order.getCreatedAt().getHour()]++;
            }
        }

        List<HourlyOrderCountDTO> hourlyOrders = new ArrayList<>();
        for (int hour = 0; hour < 24; hour++) {
            hourlyOrders.add(new HourlyOrderCountDTO(hour, hourlyBuckets[hour]));
        }

        return hourlyOrders;
    }

    private List<TopSellingItemDTO> buildTopSellingItems(List<Order> orders, int limit) {
        Map<String, ItemAggregate> itemAggregates = new HashMap<>();

        for (Order order : orders) {
            if (order.getCart() == null || order.getCart().getCartItems() == null || order.getCart().getCartItems().isBlank()) {
                continue;
            }

            try {
                List<CartItemDTO> cartItems = objectMapper.readValue(
                        order.getCart().getCartItems(),
                        new TypeReference<List<CartItemDTO>>() {}
                );

                for (CartItemDTO cartItem : cartItems) {
                    if (cartItem == null) {
                        continue;
                    }

                    Long productId = cartItem.getProductId();
                    String itemName = cartItem.getName() != null && !cartItem.getName().isBlank()
                            ? cartItem.getName()
                            : "Item " + (productId != null ? productId : "Unknown");
                    int quantity = cartItem.getQuantity() != null ? cartItem.getQuantity() : 0;

                    double itemRevenue;
                    if (cartItem.getTotalPrice() != null) {
                        itemRevenue = cartItem.getTotalPrice();
                    } else {
                        double unitPrice = cartItem.getUnitPrice() != null ? cartItem.getUnitPrice() : 0.0;
                        itemRevenue = unitPrice * quantity;
                    }

                    String itemKey = (productId != null ? productId.toString() : "name:" + itemName.toLowerCase()) + "|" + itemName;
                    ItemAggregate aggregate = itemAggregates.computeIfAbsent(itemKey, key -> new ItemAggregate(productId, itemName));
                    aggregate.quantitySold += quantity;
                    aggregate.revenue += itemRevenue;
                }
            } catch (Exception ignored) {
                // Skip malformed cart JSON so analytics remains available for valid orders.
            }
        }

        return itemAggregates.values().stream()
                .sorted(Comparator.comparingInt(ItemAggregate::getQuantitySold).reversed()
                        .thenComparing(ItemAggregate::getItemName, String.CASE_INSENSITIVE_ORDER))
                .limit(limit)
                .map(aggregate -> new TopSellingItemDTO(
                        aggregate.productId,
                        aggregate.itemName,
                        aggregate.quantitySold,
                        aggregate.revenue
                ))
                .toList();
    }

    private static class ItemAggregate {
        private final Long productId;
        private final String itemName;
        private int quantitySold;
        private double revenue;

        private ItemAggregate(Long productId, String itemName) {
            this.productId = productId;
            this.itemName = itemName;
        }

        private int getQuantitySold() {
            return quantitySold;
        }

        private String getItemName() {
            return itemName;
        }
    }
}
