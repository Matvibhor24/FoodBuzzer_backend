package com.example.food_buzzer_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.food_buzzer_backend.dto.customer.CustomerResponseDTO;
import com.example.food_buzzer_backend.model.Customer;
import com.example.food_buzzer_backend.repository.CustomerRepository;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerResponseDTO::new)
                .toList();
    }

    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id).orElse(null);
        return customer == null ? null : new CustomerResponseDTO(customer);
    }
}

