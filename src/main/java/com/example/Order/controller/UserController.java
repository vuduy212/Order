package com.example.Order.controller;

import com.example.Order.dto_response.*;
import com.example.Order.entity.User;
import com.example.Order.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

// Lombok imports for DTOs


/**
 * REST Controller to handle user-related requests.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    /**
     * Constructor for UserController.
     *
     * @param userService The service layer for user operations.
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves a list of all users.
     * This API is accessible only by users with the ADMIN role.
     *
     * @return ResponseEntity containing a list of users or an error.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')") // Requires the ADMIN role
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponseDTO> userDTOs = users.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(userDTOs);
    }

    /**
     * Retrieves user information by ID.
     * This API is accessible only by users with the ADMIN role.
     *
     * @param id The ID of the user.
     * @return ResponseEntity containing user information or 404 Not Found.
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Requires the ADMIN role
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(this::convertToDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Helper method to convert a User entity to a UserResponseDTO.
     * This is crucial for controlling what data is exposed via the API,
     * preventing sensitive information like passwords from being sent.
     *
     * @param user The User entity to convert.
     * @return A UserResponseDTO object.
     */
    private UserResponseDTO convertToDto(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getEnabled(),
                user.getCreatedAt(),
                user.getUpdatedAt(),
                user.getRoles().stream()
                        .map(role -> new RoleResponseDTO(role.getId(), role.getName()))
                        .collect(Collectors.toSet())
        );
    }
}
