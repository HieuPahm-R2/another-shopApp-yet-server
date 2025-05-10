CREATE DATABASE ecommercedb;
USE ecommerceDB;
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fullName VARCHAR(50) NOT NULL DEFAULT '',
    date_of_birth DATE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    is_active TINYINT(1) DEFAULT 1,
    facebook_account_id INT DEFAULT 0,
    google_account_id INT DEFAULT 0,
);
-- Table for storing tokens for authentication and authorization
CREATE TABLE tokens(
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type VARCHAR(50) NOT NULL,
    expiration_date DATETIME,
    revoked TINYINT(1) DEFAULT 0,
    expired TINYINT(1) DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
--hỗ trợ đăng nhập từ Facebook và Google
CREATE TABLE social_accounts(
    id INT PRIMARY KEY AUTO_INCREMENT,
    provider VARCHAR(20) NOT NULL COMMENT 'Tên nhà social network',
    provider_id VARCHAR(50) NOT NULL,
    email VARCHAR(150) NOT NULL COMMENT 'Email tài khoản',
    name VARCHAR(100) NOT NULL COMMENT 'Tên người dùng',
    user_id int,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
-- Bảng danh mục sản phẩm(Category)
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL DEFAULT '',
);
--Bảng chứa sản phẩm(Product): "Sakamoto days Vol.12", "Naruto light novel vol.11",...
CREATE TABLE products (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(350) COMMENT 'Tên sản phẩm',
    price FLOAT NOT NULL CHECK (price >= 0),
    thumbnail VARCHAR(300) DEFAULT '',
    description LONGTEXT DEFAULT '',
    created_at DATETIME,
    updated_at DATETIME,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES categories (id)
);
CREATE TABLE product_images(
    id INT PRIMARY KEY AUTO_INCREMENT,
    product_id INT,
    FOREIGN KEY (product_id) REFERENCES products(id),
    CONSTRAINT fk_product_images_product_id FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    image_url VARCHAR(300)
);
AlTER TABLE users
ADD COLUMN role_id INT,
ADD FOREIGN KEY (role_id) REFERENCES roles (id);
--Role: "Admin", "User", "Guest"
CREATE TABLE roles (
    id INT PRIMARY KEY ,
    name VARCHAR(50) NOT NULL,
);
-- Bảng chứa thông tin đơn hàng(Order): "Đơn hàng 1", "Đơn hàng 2",...
CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    fullName VARCHAR(50) NOT NULL DEFAULT '',
    email VARCHAR(100),
    phone_number VARCHAR(15) NOT NULL,
    order_date DATETIME CURRENT_TIMESTAMP,
    address VARCHAR(255) NOT NULL,
    note VARCHAR(255) DEFAULT '',
    status VARCHAR(50) DEFAULT 'pending',
    total_money FLOAT NOT NULL CHECK (total_money >= 0),
    payment_method VARCHAR(50) NOT NULL,
    shipping_method VARCHAR(50) NOT NULL,
    shipping_address VARCHAR(255) NOT NULL,
    shipping_date DATE,
    tracking_number VARCHAR(50),
);
-- still lost the order status: "pending", "processing", "shipped", "delivered", "cancelled"

-- Bảng chứa thông tin chi tiết đơn hàng(OrderDetails): "Đơn hàng 1 - sản phẩm 1", "Đơn hàng 1 - sản phẩm 2",...
CREATE TABLE order_details (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    number_of_products INT NOT NULL CHECK (number_of_products > 0),
    price FLOAT NOT NULL CHECK (price >= 0),
    total_money FLOAT NOT NULL CHECK (total_money >= 0),
    color VARCHAR(30) DEFAULT '',
    FOREIGN KEY (order_id) REFERENCES orders (id),
    FOREIGN KEY (product_id) REFERENCES products (id)
);
