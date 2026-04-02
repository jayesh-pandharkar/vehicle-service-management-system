-- ============================================================
-- Vehicle Service Management System - Database Schema
-- Hyundai Motor Company | Service Center Module
-- ============================================================

-- Create and select database
CREATE DATABASE IF NOT EXISTS hyundai_vsms;
USE hyundai_vsms;

-- -------------------------------------------------------
-- TABLE: customers
-- Stores customer personal info
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS customers (
    customer_id   INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(100) NOT NULL,
    phone         VARCHAR(15)  NOT NULL,
    email         VARCHAR(100),
    city          VARCHAR(50)  NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- -------------------------------------------------------
-- TABLE: vehicles
-- Each vehicle belongs to one customer (FK → customers)
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS vehicles (
    vehicle_id      INT AUTO_INCREMENT PRIMARY KEY,
    customer_id     INT          NOT NULL,
    model           VARCHAR(100) NOT NULL,
    license_plate   VARCHAR(20)  UNIQUE NOT NULL,
    purchase_year   YEAR         NOT NULL,
    CONSTRAINT fk_vehicle_customer
        FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
        ON DELETE CASCADE
);

-- -------------------------------------------------------
-- TABLE: services
-- Each service record is linked to one vehicle (FK → vehicles)
-- -------------------------------------------------------
CREATE TABLE IF NOT EXISTS services (
    service_id      INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id      INT            NOT NULL,
    service_date    DATE           NOT NULL,
    service_type    VARCHAR(100)   NOT NULL,
    cost            DECIMAL(10, 2) NOT NULL,
    duration_hours  DECIMAL(4, 1)  NOT NULL,
    rating          TINYINT CHECK (rating BETWEEN 1 AND 5),
    notes           TEXT,
    CONSTRAINT fk_service_vehicle
        FOREIGN KEY (vehicle_id) REFERENCES vehicles(vehicle_id)
        ON DELETE CASCADE
);

-- -------------------------------------------------------
-- SAMPLE DATA — Run this after creating the tables
-- -------------------------------------------------------

INSERT INTO customers (name, phone, email, city) VALUES
('Arjun Mehta',    '9876543210', 'arjun@email.com',  'Nashik'),
('Priya Sharma',   '9812345678', 'priya@email.com',   'Pune'),
('Ravi Kumar',     '9823456789', 'ravi@email.com',    'Mumbai'),
('Sneha Patil',    '9834567890', 'sneha@email.com',   'Nagpur'),
('Deepak Joshi',   '9845678901', 'deepak@email.com',  'Nashik');

INSERT INTO vehicles (customer_id, model, license_plate, purchase_year) VALUES
(1, 'Hyundai Creta',   'MH-15-AB-1234', 2021),
(1, 'Hyundai i20',     'MH-15-CD-5678', 2019),
(2, 'Hyundai Verna',   'MH-12-EF-9012', 2022),
(3, 'Hyundai Tucson',  'MH-04-GH-3456', 2020),
(4, 'Hyundai Venue',   'MH-31-IJ-7890', 2023),
(5, 'Hyundai Alcazar', 'MH-15-KL-2345', 2022);

INSERT INTO services (vehicle_id, service_date, service_type, cost, duration_hours, rating, notes) VALUES
(1, '2024-01-15', 'Oil Change',          1500.00, 1.5, 5, 'Synthetic oil used'),
(1, '2024-03-20', 'Tyre Rotation',       800.00,  1.0, 4, 'All 4 tyres rotated'),
(1, '2024-06-10', 'Full Service',        5500.00, 5.0, 5, 'Annual full service'),
(2, '2024-02-10', 'Brake Inspection',    1200.00, 2.0, 3, 'Front pads replaced'),
(2, '2024-05-05', 'Oil Change',          1500.00, 1.5, 4, NULL),
(3, '2024-03-18', 'AC Service',          2500.00, 3.0, 5, 'Gas refilled'),
(3, '2024-07-22', 'Full Service',        6000.00, 6.0, 5, 'Premium service package'),
(4, '2024-04-30', 'Battery Replacement', 4500.00, 1.0, 4, 'OEM battery fitted'),
(5, '2024-08-12', 'Oil Change',          1500.00, 1.5, 5, NULL),
(6, '2024-09-01', 'Wheel Alignment',     1000.00, 1.0, 4, 'Digital alignment done'),
(6, '2024-09-15', 'Full Service',        5800.00, 5.5, 5, 'Pre-monsoon check included');
