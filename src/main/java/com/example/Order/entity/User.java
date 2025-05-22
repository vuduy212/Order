package com.example.Order.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import lombok.*;

/**
 * Lớp Entity cho bảng 'users'.
 * Đại diện cho thông tin người dùng trong hệ thống.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor // Lombok sẽ tự động tạo constructor không tham số
@AllArgsConstructor // Lombok sẽ tự động tạo constructor với tất cả các tham số
@ToString(exclude = {"roles"}) // Lombok sẽ tự động tạo phương thức toString, loại trừ trường 'roles' để tránh lỗi vòng lặp vô hạn
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password", nullable = false, length = 255)
    private String password; // Mật khẩu đã mã hóa

    @Column(name = "email", unique = true, length = 100)
    private String email;

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Temporal(TemporalType.TIMESTAMP)
    private OffsetDateTime updatedAt;

    // Quan hệ Many-to-Many với Role thông qua bảng user_roles
    @ManyToMany(fetch = FetchType.EAGER) // Tải vai trò ngay lập tức
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // Helper method to add a role
    public void addRole(Role role) {
        this.roles.add(role);
        // Đảm bảo cập nhật cả hai phía của mối quan hệ
        if (!role.getUsers().contains(this)) {
            role.getUsers().add(this);
        }
    }

    // Helper method to remove a role
    public void removeRole(Role role) {
        this.roles.remove(role);
        // Đảm bảo cập nhật cả hai phía của mối quan hệ
        if (role.getUsers().contains(this)) {
            role.getUsers().remove(this);
        }
    }

    @PrePersist
    protected void onCreate() {
        if (this.createdAt == null) { // Chỉ đặt nếu chưa được đặt
            this.createdAt = OffsetDateTime.now();
        }
        if (this.updatedAt == null) { // Chỉ đặt nếu chưa được đặt
            this.updatedAt = OffsetDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        // So sánh dựa trên ID và username để đảm bảo tính duy nhất
        return Objects.equals(id, user.id) && Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        // Hash code dựa trên ID và username
        return Objects.hash(id, username);
    }
}


