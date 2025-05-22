package com.example.Order.repository;

import com.example.Order.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    // Tìm kiếm vai trò theo tên
    Optional<Role> findByName(String name);
}
