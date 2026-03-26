package com.example.food_buzzer_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.food_buzzer_backend.dto.customer.CustomerResponseDTO;
import com.example.food_buzzer_backend.repository.CustomerRepository;
import com.example.food_buzzer_backend.repository.UserRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    public CustomerService(CustomerRepository customerRepository, UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    private Long getRestaurantIdFromUser(Long userId) {
        return userRepository.findById(userId)
                .filter(user -> user.getRestaurant() != null && user.getRestaurant().getId() != null)
                .map(user -> user.getRestaurant().getId())
                .orElseThrow(() -> new IllegalArgumentException("user not found or restaurant not found"));
    }

    public List<CustomerResponseDTO> getAllCustomers(Long userId) {
        Long restaurantId = getRestaurantIdFromUser(userId);
        List<CustomerResponseDTO> customers = customerRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(CustomerResponseDTO::new)
                .toList();

        if (customers.isEmpty()) {
            throw new IllegalArgumentException("customers not found");
        }
        return customers;
    }

    public CustomerResponseDTO getCustomerById(Long userId, Long id) {
        Long restaurantId = getRestaurantIdFromUser(userId);
        return customerRepository.findByIdAndRestaurantId(id, restaurantId)
                .map(CustomerResponseDTO::new)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found with customer id"));
    }
}

