package com.example.Order.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object (DTO) for user registration requests.
 * Contains username, email, and password for new user creation.
 */
@Data
public class RegisterRequest {
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(max = 100)
    @Email
    private String email;

    @NotBlank
    @Size(min = 6, max = 120)
    private String password;

    // You might want to include a field for roles if registration allows role selection,
    // but for simplicity, we'll assign a default role in the service.
    // private Set<String> role;
}
