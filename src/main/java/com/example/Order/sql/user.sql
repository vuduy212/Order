-- Bảng 'users' để lưu trữ thông tin người dùng
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Mật khẩu đã mã hóa
    email VARCHAR(100) UNIQUE,
    enabled BOOLEAN DEFAULT TRUE, -- Cho phép người dùng hoạt động hay không
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Bảng 'roles' để định nghĩa các vai trò (ví dụ: ADMIN, CLIENT)
CREATE TABLE roles (
    id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL UNIQUE -- Ví dụ: 'ROLE_ADMIN', 'ROLE_CLIENT'
);

-- Bảng liên kết 'user_roles' để quản lý mối quan hệ nhiều-nhiều giữa người dùng và vai trò
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL,
    role_id INT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE
);

-- Chèn dữ liệu vai trò mẫu
INSERT INTO roles (name) VALUES ('ROLE_ADMIN') ON CONFLICT (name) DO NOTHING;
INSERT INTO roles (name) VALUES ('ROLE_CLIENT') ON CONFLICT (name) DO NOTHING;

-- Tạo index cho các cột thường được truy vấn để cải thiện hiệu suất
CREATE INDEX idx_users_username ON users (username);
CREATE INDEX idx_users_email ON users (email);
CREATE INDEX idx_user_roles_user_id ON user_roles (user_id);
CREATE INDEX idx_user_roles_role_id ON user_roles (role_id);
