package com.example.Order.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

/**
 * Lớp Entity cho bảng 'roles'.
 * Đại diện cho các vai trò người dùng trong hệ thống (ví dụ: ADMIN, CLIENT).
 */
@Entity
@Table(name = "roles")
@Getter // Lombok sẽ tự động tạo các phương thức getter cho tất cả các trường
@Setter // Lombok sẽ tự động tạo các phương thức setter cho tất cả các trường
@NoArgsConstructor // Lombok sẽ tự động tạo constructor không tham số
@AllArgsConstructor // Lombok sẽ tự động tạo constructor với tất cả các tham số
@ToString(exclude = {"users"}) // Lombok sẽ tự động tạo phương thức toString, loại trừ trường 'users' để tránh lỗi vòng lặp vô hạn
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 50)
    private String name; // Ví dụ: 'ROLE_ADMIN', 'ROLE_CLIENT'

    // Quan hệ Many-to-Many ngược lại với User
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    // Constructor tùy chỉnh nếu cần các logic khởi tạo đặc biệt
    // Nếu bạn muốn sử dụng constructor này, hãy bỏ chú thích và đảm bảo Lombok không tạo constructor trùng lặp
    // public Role(String name) {
    //     this.name = name;
    // }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        // So sánh dựa trên ID và tên để đảm bảo tính duy nhất
        return Objects.equals(id, role.id) && Objects.equals(name, role.name);
    }

    @Override
    public int hashCode() {
        // Hash code dựa trên ID và tên
        return Objects.hash(id, name);
    }
}

