CREATE DATABASE IF NOT EXISTS showroom_db;

CREATE TABLE IF NOT EXISTS showroom_db.vehicles (
    id INT PRIMARY KEY AUTO_INCREMENT,
    brand VARCHAR(40) NOT NULL,
    model VARCHAR(40) NOT NULL,
    type VARCHAR(20),
    fuel VARCHAR(20),
    year INT,
    price DOUBLE NOT NULL,
    status VARCHAR(15) DEFAULT 'Available'
);

INSERT INTO showroom_db.vehicles (brand, model, type, fuel, year, price) VALUES
('Maruti', 'Swift', 'Car', 'Petrol', 2024, 750000),
('Hyundai', 'Creta', 'Car', 'Diesel', 2024, 1500000),
('Honda', 'Activa', 'Scooter', 'Petrol', 2025, 85000);