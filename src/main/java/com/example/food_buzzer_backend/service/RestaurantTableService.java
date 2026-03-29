package com.example.food_buzzer_backend.service;

import com.example.food_buzzer_backend.dto.restaurant_tables.CreateRestaurantTableRequest;
import com.example.food_buzzer_backend.dto.restaurant_tables.RestaurantTableResponse;
import com.example.food_buzzer_backend.model.Restaurant;
import com.example.food_buzzer_backend.model.RestaurantTable;
import com.example.food_buzzer_backend.model.User;
import com.example.food_buzzer_backend.repository.RestaurantRepository;
import com.example.food_buzzer_backend.repository.RestaurantTableRepository;
import com.example.food_buzzer_backend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestaurantTableService {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantTableService.class);

    private final RestaurantTableRepository restaurantTableRepository;
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    public RestaurantTableService(RestaurantTableRepository restaurantTableRepository,
                                 RestaurantRepository restaurantRepository,
                                 UserRepository userRepository) {
        this.restaurantTableRepository = restaurantTableRepository;
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    public ValidationResult validateUserForRestaurant(Long userId) {
        logger.info("Validating user ID: {}", userId);

        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ValidationResult.error("user does not exist");
        }

        if (!user.getIsActive() || user.getRestaurant() == null || user.getRestaurant().getId() == null) {
            return ValidationResult.error("user is not active or restraunt not exist");
        }

        return ValidationResult.ok(user.getRestaurant().getId());
    }

    public Long getRestaurantIdBySlug(String slug) {
        Restaurant restaurant = restaurantRepository.findBySlugAndIsActiveTrueAndIsLiveTrue(slug).orElse(null);
        return restaurant != null ? restaurant.getId() : null;
    }

    public RestaurantTableResponse createTable(Long restaurantId, CreateRestaurantTableRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant == null) {
            logger.warn("Restaurant {} not found", restaurantId);
            return null;
        }

        if (restaurantTableRepository.existsByRestaurantIdAndTableNoAndFloorAndIsDeleteFalse(restaurantId, request.getTableNo(), request.getFloor())) {
            throw new IllegalStateException("Table number " + request.getTableNo() + " on floor " + request.getFloor() + " already exists for this restaurant");
        }

        RestaurantTable table = new RestaurantTable();
        table.setRestaurant(restaurant);
        table.setTableNo(request.getTableNo());
        table.setTableSize(request.getTableSize());
        table.setFloor(request.getFloor());
        table.setIsOccupied(false);
        table.setIsDelete(false);

        RestaurantTable saved = restaurantTableRepository.save(table);
        return mapToResponse(saved);
    }

    public List<RestaurantTableResponse> getAllActiveTables(Long restaurantId) {
        List<RestaurantTable> tables = restaurantTableRepository.findByRestaurantIdAndIsDeleteFalse(restaurantId);

        return tables.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private RestaurantTableResponse mapToResponse(RestaurantTable table) {
        return new RestaurantTableResponse(
                table.getId(),
                table.getRestaurant().getId(),
                table.getTableNo(),
                table.getTableSize(),
                table.getFloor(),
                table.getIsOccupied(),
                table.getIsDelete()
        );
    }

    public static class ValidationResult {
        private final boolean valid;
        private final String message;
        private final Long restaurantId;

        private ValidationResult(boolean valid, String message, Long restaurantId) {
            this.valid = valid;
            this.message = message;
            this.restaurantId = restaurantId;
        }

        public static ValidationResult ok(Long restaurantId) {
            return new ValidationResult(true, null, restaurantId);
        }

        public static ValidationResult error(String message) {
            return new ValidationResult(false, message, null);
        }

        public boolean isValid() {
            return valid;
        }

        public String getMessage() {
            return message;
        }

        public Long getRestaurantId() {
            return restaurantId;
        }
    }
}
