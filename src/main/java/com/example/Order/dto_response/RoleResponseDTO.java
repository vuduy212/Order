package com.example.Order.dto_response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object (DTO) for Role information.
 * Used within UserResponseDTO to represent user roles.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDTO {
    private Integer id;
    private String name;
}
