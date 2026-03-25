package com.example.food_buzzer_backend.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.food_buzzer_backend.model.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    List<Restaurant> findByApprovalStatus(String approvalStatus);

    List<Restaurant> findByOwner_Id(Long ownerUserId);

    long countByApprovalStatus(String approvalStatus);

    boolean existsBySlug(String slug);

    boolean existsByNameAndZipcode(String name, String zipcode);

    Optional<Restaurant> findByIdAndIsLiveTrue(Long id);
    
    boolean existsByIdAndIsLiveTrue(Long id);

    Optional<Restaurant> findByIdAndIsActiveTrue(Long id);
    
    boolean existsByIdAndIsActiveTrue(Long id);

    Optional<Restaurant> findBySlugAndIsActiveTrueAndIsLiveTrue(String slug);

    
}