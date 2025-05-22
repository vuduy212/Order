package com.example.Order.repository;

import com.example.Order.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Tìm kiếm người dùng theo username
    Optional<User> findByUsername(String username);

    // Kiểm tra xem username đã tồn tại chưa
    Boolean existsByUsername(String username);

    // Kiểm tra xem email đã tồn tại chưa
    Boolean existsByEmail(String email);
}