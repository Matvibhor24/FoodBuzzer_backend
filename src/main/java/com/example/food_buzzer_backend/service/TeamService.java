package com.example.food_buzzer_backend.service;

import com.example.food_buzzer_backend.config.AppConstants;
import com.example.food_buzzer_backend.dto.team.*;
import com.example.food_buzzer_backend.model.User;
import com.example.food_buzzer_backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.List;

@Service
public class TeamService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public TeamService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public TeamCreationResponse addTeamMember(Long ownerId, TeamAddRequestDTO dto) {

        TeamCreationResponse response = new TeamCreationResponse();

        User owner = userRepository.findById(ownerId).orElse(null);

        if (owner == null || !AppConstants.ROLE_OWNER.equals(owner.getRole())) {
            response.setMessage(AppConstants.ERROR_USER_NOT_OWNER);
            return response;
        }

        User user = userRepository.findByEmail(dto.getEmail()).orElse(null);

        if (user != null) {
            // If user is active and already belongs to a restaurant, don't allow reassignment
            if (Boolean.TRUE.equals(user.getIsActive()) && user.getRestaurant() != null) {
                response.setMessage("Email already registered and active in a team");
                return response;
            }
        } else {
            // Create a completely new user
            user = new User();
            user.setEmail(dto.getEmail());
        }

        user.setFullName(dto.getFullName());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setPhone(dto.getPhone());
        user.setRestaurant(owner.getRestaurant());
        user.setRole(dto.getRole());
        user.setIsActive(AppConstants.DEFAULT_USER_ACTIVE);

        if (AppConstants.ROLE_STAFF.equalsIgnoreCase(dto.getRole()))
            user.setAccessLevel(AppConstants.ACCESS_LEVEL_STAFF);
        else if (AppConstants.ROLE_CASHIER.equalsIgnoreCase(dto.getRole()))
            user.setAccessLevel(AppConstants.ACCESS_LEVEL_CASHIER);
        else if (AppConstants.ROLE_MANAGER.equalsIgnoreCase(dto.getRole()))
            user.setAccessLevel(AppConstants.ACCESS_LEVEL_MANAGER);

        userRepository.save(user);

        response.setUserId(user.getId());
        response.setUserName(user.getFullName());
        response.setRole(user.getRole());
        response.setMessage("User Created");

        return response;
    }

    public List<TeamMemberResponseDTO> getTeamMembers(Long ownerId) {

        User owner = userRepository.findById(ownerId).orElse(null);

        if (owner == null || owner.getRestaurant() == null)
            return List.of();

        List<User> users = userRepository.findByRestaurantId(owner.getRestaurant().getId());
        return users.stream().map(user -> {
            TeamMemberResponseDTO dto = new TeamMemberResponseDTO();
            dto.setId(user.getId());
            dto.setFullName(user.getFullName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            dto.setRole(user.getRole());
            dto.setAccessLevel(user.getAccessLevel());
            dto.setIsActive(user.getIsActive());
            dto.setCreatedAt(user.getCreatedAt());
            return dto;
        }).toList();
    }

    public TeamCreationResponse deleteMember(Long ownerId, TeamDeleteDTO dto) {

        TeamCreationResponse response = new TeamCreationResponse();

        User owner = userRepository.findById(ownerId).orElse(null);

        if (owner == null || !AppConstants.ROLE_OWNER.equals(owner.getRole()) || owner.getRestaurant() == null) {
            response.setMessage(AppConstants.ERROR_USER_NOT_OWNER);
            return response;
        }

        User user = userRepository.findById(dto.getId()).orElse(null);

        if (user == null) {
            response.setMessage("User Invalid");
            return response;
        }
        
        if (user.getRestaurant() == null || !user.getRestaurant().getId().equals(owner.getRestaurant().getId())) {
             response.setMessage("User does not belong to your restaurant");
             return response;
        }
        
        if (user.getId().equals(ownerId)) {
             response.setMessage("Cannot delete yourself");
             return response;
        }

        user.setRestaurant(null);
        user.setAccessLevel(null);
        user.setIsActive(false);

        userRepository.save(user);

        response.setUserId(user.getId());
        response.setUserName(user.getFullName());
        response.setRole(user.getRole());
        response.setMessage("User Removed");

        return response;
    }

    public TeamCreationResponse updateMember(Long ownerId, TeamUpdateDTO dto) {

        TeamCreationResponse response = new TeamCreationResponse();

        User owner = userRepository.findById(ownerId).orElse(null);

        if (owner == null || !AppConstants.ROLE_OWNER.equals(owner.getRole()) || owner.getRestaurant() == null) {
            response.setMessage(AppConstants.ERROR_USER_NOT_OWNER);
            return response;
        }

        User user = userRepository.findById(dto.getId()).orElse(null);

        if (user == null) {
            response.setMessage("User Invalid");
            return response;
        }
        
        if (user.getRestaurant() == null || !user.getRestaurant().getId().equals(owner.getRestaurant().getId())) {
            response.setMessage("User does not belong to your restaurant");
            return response;
        }
        
        if (user.getId().equals(ownerId)) {
            response.setMessage("Cannot modify your own role");
            return response;
        }

        String newRole = dto.getNewRole();
        if (!AppConstants.ROLE_STAFF.equals(newRole) && 
            !AppConstants.ROLE_CASHIER.equals(newRole) && 
            !AppConstants.ROLE_MANAGER.equals(newRole)) {
            response.setMessage("Invalid role specified");
            return response;
        }

        user.setRole(newRole);
        
        if (AppConstants.ROLE_STAFF.equals(newRole))
            user.setAccessLevel(AppConstants.ACCESS_LEVEL_STAFF);
        else if (AppConstants.ROLE_CASHIER.equals(newRole))
            user.setAccessLevel(AppConstants.ACCESS_LEVEL_CASHIER);
        else if (AppConstants.ROLE_MANAGER.equals(newRole))
            user.setAccessLevel(AppConstants.ACCESS_LEVEL_MANAGER);

        userRepository.save(user);

        response.setUserId(user.getId());
        response.setUserName(user.getFullName());
        response.setRole(user.getRole());
        response.setMessage("Role Updated");

        return response;
    }
}
