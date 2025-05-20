-- 🔄 Désactivation des contraintes de clés étrangères
SET FOREIGN_KEY_CHECKS = 0;

-- 🔥 Suppression des tables si elles existent
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS cars;
DROP TABLE IF EXISTS insurances;
DROP TABLE IF EXISTS users;

-- 👤 Table des utilisateurs
CREATE TABLE users (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) DEFAULT NULL,
  email VARCHAR(100) DEFAULT NULL,
  phone VARCHAR(20) DEFAULT NULL,
  address VARCHAR(255) DEFAULT NULL,
  is_admin BOOLEAN DEFAULT FALSE,
  password VARCHAR(255) NOT NULL,
  PRIMARY KEY (id)
);

-- 🚗 Table des voitures
CREATE TABLE cars (
  id BIGINT NOT NULL AUTO_INCREMENT,
  brand VARCHAR(50) NOT NULL,
  model VARCHAR(50) NOT NULL,
  color VARCHAR(30) NOT NULL,
  fuel_level DOUBLE NOT NULL,
  transmission VARCHAR(20) NOT NULL,
  status VARCHAR(20) NOT NULL,
  mileage INT NOT NULL,
  manufacturing_year INT NOT NULL,
  insurance_id BIGINT DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (insurance_id) REFERENCES insurances(id)
);

-- 🛡️ Table des assurances
CREATE TABLE insurances (
  id BIGINT NOT NULL AUTO_INCREMENT,
  provider VARCHAR(100) NOT NULL,
  coverage VARCHAR(255) NOT NULL,
  monthly_price DOUBLE NOT NULL,
  PRIMARY KEY (id)
);

-- 📅 Table des réservations
CREATE TABLE bookings (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  car_id BIGINT NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE NOT NULL,
  daily_price DOUBLE NOT NULL,
  booking_status VARCHAR(50) NOT NULL,
  payment_method VARCHAR(50) NOT NULL,
  security_deposit DOUBLE NOT NULL,
  rating INT DEFAULT NULL,
  review TEXT DEFAULT NULL,
  PRIMARY KEY (id),
  FOREIGN KEY (user_id) REFERENCES users(id),
  FOREIGN KEY (car_id) REFERENCES cars(id)
);

-- 🧪 Données de test : utilisateurs
INSERT INTO users (name, email, phone, address, is_admin, password) VALUES
('user1', 'user1@example.com', '1234567890', '123 Elm Street', FALSE, '$2a$10$e0NRu4g7/FvYaMo6Y9j.3ed13O0I6jxoyQy.cQi9eAlmscdDA6S4a'),
('admin', 'admin@example.com', '9876543210', '456 Oak Avenue', TRUE, '$2a$10$Wz6IfzZpK.rbnxqpjN0H3uKnSzDY5TuQ6fUMyN8eFw3t9LyrBAYqa'),
('user2', 'user2@example.com', '555667788', '789 Pine Road', FALSE, '$2a$10$e0NRu4g7/FvYaMo6Y9j.3ed13O0I6jxoyQy.cQi9eAlmscdDA6S4a'),
('user3', 'user3@example.com', '111222333', '321 Cedar Lane', FALSE, '$2a$10$e0NRu4g7/FvYaMo6Y9j.3ed13O0I6jxoyQy.cQi9eAlmscdDA6S4a');

-- Assurances
INSERT INTO insurances (provider, coverage, monthly_price) VALUES
('Allianz', 'Full Coverage', 50.00),
('GEICO', 'Collision Only', 30.00),
('State Farm', 'Liability', 20.00),
('Progressive', 'Comprehensive', 40.00),
('Liberty Mutual', 'Theft Protection', 35.00);

-- Voitures
INSERT INTO cars (brand, model, color, fuel_level, transmission, status, mileage, manufacturing_year, insurance_id) VALUES
('Toyota', 'Corolla', 'Blue', 75.5, 'Automatic', 'Available', 12000, 2021, 1),
('Honda', 'Civic', 'Red', 50.2, 'Manual', 'Rented', 22000, 2020, 2),
('Ford', 'Focus', 'Black', 80.0, 'Automatic', 'Available', 18000, 2019, 3),
('BMW', '3 Series', 'White', 60.0, 'Automatic', 'Maintenance', 25000, 2022, 1),
('Mercedes', 'C-Class', 'Silver', 90.0, 'Automatic', 'Available', 15000, 2021, 2),
('Audi', 'A4', 'Gray', 85.0, 'Automatic', 'Available', 20000, 2023, 4),
('Tesla', 'Model 3', 'Black', 95.0, 'Automatic', 'Rented', 5000, 2024, 5);

-- Réservations
INSERT INTO bookings (user_id, car_id, start_date, end_date, daily_price, booking_status, payment_method, security_deposit, rating, review) VALUES
(1, 1, '2025-03-01', '2025-03-10', 40.00, 'Confirmed', 'Credit Card', 200.00, 5, 'Great experience!'),
(2, 2, '2025-04-05', '2025-04-15', 50.00, 'Completed', 'PayPal', 250.00, 4, 'Smooth process, will rent again!'),
(1, 3, '2025-05-10', '2025-05-20', 45.00, 'Pending', 'Debit Card', 220.00, 3, 'Okay'),
(2, 4, '2025-06-01', '2025-06-07', 70.00, 'Cancelled', 'Cash', 300.00, 4, 'Great'),
(3, 5, '2025-07-10', '2025-07-15', 100.00, 'Confirmed', 'Credit Card', 500.00, 5, 'Luxury ride!'),
(4, 6, '2025-08-20', '2025-08-30', 90.00, 'Completed', 'PayPal', 400.00, 4, 'Very comfortable car!');

-- ✅ Réactivation des contraintes de clés étrangères
SET FOREIGN_KEY_CHECKS = 1;
