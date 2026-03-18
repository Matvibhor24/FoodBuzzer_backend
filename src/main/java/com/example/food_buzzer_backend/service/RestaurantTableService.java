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

/**
 * Business logic for the {@code restaurant_tables} feature.
 *
 * This service is intentionally "user-header driven":
 * - Controllers pass {@code X-User-Id}.
 * - We validate that the user exists and is active.
 * - We derive {@code restaurant_id} from the user's row (user.restaurant.id).
 * - Then we create/read {@code restaurant_tables} only inside that restaurant scope.
 *
 * Error rules required by you:
 * - If user ID does not exist: message "user does not exist"
 * - If user is inactive OR restaurant_id is null: message "user is not active or restraunt not exist"
 */
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

    /**
     * Validates the user and returns the derived restaurantId (from users table).
     * This is used by controllers so they can return the exact required message.
     */
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

    /**
     * Creates a new restaurant table row for the given restaurantId.
     * Defaults enforced here:
     * - is_occupied = false
     * - is_delete = false
     */
    public RestaurantTableResponse createTable(Long restaurantId, CreateRestaurantTableRequest request) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElse(null);
        if (restaurant == null) {
            logger.warn("Restaurant {} not found", restaurantId);
            return null;
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

    /**
     * Returns all active (not deleted) tables for a restaurant.
     * "Active" means is_delete=false.
     */
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

        /**
         * Small helper type for returning either:
         * - valid=true and restaurantId
         * - valid=false and an error message required by API spec
         */
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

