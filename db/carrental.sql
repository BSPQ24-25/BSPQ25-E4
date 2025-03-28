-- MySQL dump 10.13  Distrib 8.0.41, for Win64 (x86_64)
--
-- Host: localhost    Database: carrental
-- ------------------------------------------------------
-- Server version	8.0.41

SET FOREIGN_KEY_CHECKS=0;

-- Table structure for table `users`

DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `is_admin` BOOLEAN DEFAULT FALSE,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `users` (name, email, phone, address, is_admin) VALUES
('user1', 'user1@example.com', '1234567890', '123 Elm Street', FALSE),
('admin', 'admin@example.com', '9876543210', '456 Oak Avenue', TRUE),
('user2', 'user2@example.com', '555667788', '789 Pine Road', FALSE),
('user3', 'user3@example.com', '111222333', '321 Cedar Lane', FALSE);

-- Table structure for table `insurances`

DROP TABLE IF EXISTS `insurances`;
CREATE TABLE `insurances` (
  `id` int NOT NULL AUTO_INCREMENT,
  `provider` varchar(100) NOT NULL,
  `coverage` varchar(255) NOT NULL,
  `monthly_price` double NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `insurances` (provider, coverage, monthly_price) VALUES
('Allianz', 'Full Coverage', 50.00),
('GEICO', 'Collision Only', 30.00),
('State Farm', 'Liability', 20.00),
('Progressive', 'Comprehensive', 40.00),
('Liberty Mutual', 'Theft Protection', 35.00);

-- Table structure for table `cars`

DROP TABLE IF EXISTS `cars`;
CREATE TABLE `cars` (
  `id` int NOT NULL AUTO_INCREMENT,
  `brand` varchar(50) NOT NULL,
  `model` varchar(50) NOT NULL,
  `color` varchar(30) NOT NULL,
  `fuel_level` double NOT NULL,
  `transmission` varchar(20) NOT NULL,
  `status` varchar(20) NOT NULL,
  `mileage` int NOT NULL,
  `manufacturing_year` int NOT NULL,
  `insurance_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`insurance_id`) REFERENCES `insurances`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `cars` (brand, model, color, fuel_level, transmission, status, mileage, manufacturing_year, insurance_id) VALUES
('Toyota', 'Corolla', 'Blue', 75.5, 'Automatic', 'Available', 12000, 2021, 1),
('Honda', 'Civic', 'Red', 50.2, 'Manual', 'Rented', 22000, 2020, 2),
('Ford', 'Focus', 'Black', 80.0, 'Automatic', 'Available', 18000, 2019, 3),
('BMW', '3 Series', 'White', 60.0, 'Automatic', 'Maintenance', 25000, 2022, 1),
('Mercedes', 'C-Class', 'Silver', 90.0, 'Automatic', 'Available', 15000, 2021, 2),
('Audi', 'A4', 'Gray', 85.0, 'Automatic', 'Available', 20000, 2023, 4),
('Tesla', 'Model 3', 'Black', 95.0, 'Automatic', 'Rented', 5000, 2024, 5);

-- Table structure for table `bookings`

DROP TABLE IF EXISTS `bookings`;
CREATE TABLE `bookings` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `car_id` int NOT NULL,
  `start_date` date NOT NULL,
  `end_date` date NOT NULL,
  `daily_price` double NOT NULL,
  `booking_status` varchar(50) NOT NULL,
  `payment_method` varchar(50) NOT NULL,
  `security_deposit` double NOT NULL,
  `rating` int DEFAULT NULL,
  `review` text DEFAULT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  FOREIGN KEY (`car_id`) REFERENCES `cars` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

INSERT INTO `bookings` (user_id, car_id, start_date, end_date, daily_price, booking_status, payment_method, security_deposit, rating, review) VALUES
(1, 1, '2025-03-01', '2025-03-10', 40.00, 'Confirmed', 'Credit Card', 200.00, 5, 'Great experience!'),
(2, 2, '2025-04-05', '2025-04-15', 50.00, 'Completed', 'PayPal', 250.00, 4, 'Smooth process, will rent again!'),
(1, 3, '2025-05-10', '2025-05-20', 45.00, 'Pending', 'Debit Card', 220.00, NULL, NULL),
(2, 4, '2025-06-01', '2025-06-07', 70.00, 'Cancelled', 'Cash', 300.00, NULL, NULL),
(3, 5, '2025-07-10', '2025-07-15', 100.00, 'Confirmed', 'Credit Card', 500.00, 5, 'Luxury ride!'),
(4, 6, '2025-08-20', '2025-08-30', 90.00, 'Completed', 'PayPal', 400.00, 4, 'Very comfortable car!');

SET FOREIGN_KEY_CHECKS=1;
