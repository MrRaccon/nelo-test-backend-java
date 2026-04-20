-- Initialize database with sample data for MySQL
-- This script runs when MySQL container starts

-- Create tables first (if not exist) to ensure script works even before Hibernate creates them
CREATE TABLE IF NOT EXISTS restaurants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    opening_time TIME,
    closing_time TIME,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS restaurant_endorsements (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id BIGINT,
    endorsement_type VARCHAR(50),
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

CREATE TABLE IF NOT EXISTS restaurant_tables (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id BIGINT,
    capacity INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

CREATE TABLE IF NOT EXISTS reservations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    restaurant_id BIGINT,
    reservation_time TIMESTAMP,
    duration_hours INT DEFAULT 2,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

CREATE TABLE IF NOT EXISTS reservation_tables (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    reservation_id BIGINT,
    table_id BIGINT,
    FOREIGN KEY (reservation_id) REFERENCES reservations(id),
    FOREIGN KEY (table_id) REFERENCES restaurant_tables(id)
);

-- Insert sample restaurants
INSERT INTO restaurants (name, opening_time, closing_time, created_at, updated_at) VALUES
('The Green Garden', '08:00:00', '22:00:00', NOW(), NOW()),
('Steakhouse Prime', '11:00:00', '23:00:00', NOW(), NOW()),
('Vegan Paradise', '07:00:00', '21:00:00', NOW(), NOW()),
('Italian Bistro', '12:00:00', '23:00:00', NOW(), NOW()),
('Seafood Fresh', '11:30:00', '22:30:00', NOW(), NOW());

-- Insert restaurant endorsements
INSERT INTO restaurant_endorsements (restaurant_id, endorsement_type) VALUES
(1, 'VEGETARIAN'), (1, 'VEGAN'), (1, 'GLUTEN_FREE'),
(2, 'PALEO'), (2, 'KETO'),
(3, 'VEGAN'), (3, 'VEGETARIAN'), (3, 'RAW_FOOD'),
(4, 'VEGETARIAN'), (4, 'GLUTEN_FREE'),
(5, 'NUT_FREE'), (5, 'DAIRY_FREE');

-- Insert restaurant tables
INSERT INTO restaurant_tables (restaurant_id, capacity, created_at, updated_at) VALUES
(1, 2, NOW(), NOW()),
(1, 4, NOW(), NOW()),
(1, 6, NOW(), NOW()),
(1, 8, NOW(), NOW()),
(2, 2, NOW(), NOW()),
(2, 4, NOW(), NOW()),
(2, 6, NOW(), NOW()),
(3, 2, NOW(), NOW()),
(3, 4, NOW(), NOW()),
(3, 6, NOW(), NOW()),
(4, 2, NOW(), NOW()),
(4, 4, NOW(), NOW()),
(4, 6, NOW(), NOW()),
(5, 2, NOW(), NOW()),
(5, 4, NOW(), NOW()),
(5, 6, NOW(), NOW());

-- Insert sample reservations (April 19-30, 2026) - Distributed across dates with specific tables
-- The Green Garden reservations
INSERT INTO reservations (restaurant_id, reservation_time, duration_hours, created_at, updated_at) VALUES
(1, '2026-04-19 19:00:00', 2, NOW(), NOW()),
(1, '2026-04-20 19:30:00', 2, NOW(), NOW()),
(1, '2026-04-22 20:00:00', 2, NOW(), NOW()),
(1, '2026-04-25 20:30:00', 2, NOW(), NOW()),
(1, '2026-04-28 19:00:00', 2, NOW(), NOW());

-- Steakhouse Prime reservations  
INSERT INTO reservations (restaurant_id, reservation_time, duration_hours, created_at, updated_at) VALUES
(2, '2026-04-19 18:00:00', 2, NOW(), NOW()),
(2, '2026-04-21 19:00:00', 2, NOW(), NOW()),
(2, '2026-04-24 20:00:00', 2, NOW(), NOW()),
(2, '2026-04-27 18:30:00', 2, NOW(), NOW()),
(2, '2026-04-30 19:30:00', 2, NOW(), NOW());

-- Vegan Paradise reservations
INSERT INTO reservations (restaurant_id, reservation_time, duration_hours, created_at, updated_at) VALUES
(3, '2026-04-20 17:30:00', 2, NOW(), NOW()),
(3, '2026-04-23 18:30:00', 2, NOW(), NOW()),
(3, '2026-04-26 19:30:00', 2, NOW(), NOW()),
(3, '2026-04-29 20:00:00', 2, NOW(), NOW());

-- Italian Bistro reservations
INSERT INTO reservations (restaurant_id, reservation_time, duration_hours, created_at, updated_at) VALUES
(4, '2026-04-19 19:00:00', 2, NOW(), NOW()),
(4, '2026-04-22 20:00:00', 2, NOW(), NOW()),
(4, '2026-04-25 19:30:00', 2, NOW(), NOW());

-- Seafood Fresh reservations
INSERT INTO reservations (restaurant_id, reservation_time, duration_hours, created_at, updated_at) VALUES
(5, '2026-04-20 19:00:00', 2, NOW(), NOW()),
(5, '2026-04-24 20:30:00', 2, NOW(), NOW()),
(5, '2026-04-28 18:00:00', 2, NOW(), NOW());

-- Assign tables to reservations (reservation_tables relationship)
-- The Green Garden: table_id 1 (capacity 2) for reservation 1, table_id 2 (capacity 4) for reservation 2, etc.
INSERT INTO reservation_tables (reservation_id, table_id) VALUES
(1, 1),  -- Reservation 1 at The Green Garden uses table 1 (capacity 2)
(2, 2),  -- Reservation 2 uses table 2 (capacity 4)
(3, 3),  -- Reservation 3 uses table 3 (capacity 6)
(4, 4),  -- Reservation 4 uses table 4 (capacity 8)
(5, 1);  -- Reservation 5 uses table 1 again (different time)

-- Steakhouse Prime: tables 5, 6, 7
INSERT INTO reservation_tables (reservation_id, table_id) VALUES
(6, 5),  -- Reservation 6 at Steakhouse Prime uses table 5 (capacity 2)
(7, 6),  -- Reservation 7 uses table 6 (capacity 4)
(8, 7),  -- Reservation 8 uses table 7 (capacity 6)
(9, 5),  -- Reservation 9 uses table 5
(10, 6); -- Reservation 10 uses table 6

-- Vegan Paradise: tables 8, 9, 10
INSERT INTO reservation_tables (reservation_id, table_id) VALUES
(11, 8),  -- Reservation 11 at Vegan Paradise uses table 8 (capacity 2)
(12, 9),  -- Reservation 12 uses table 9 (capacity 4)
(13, 10), -- Reservation 13 uses table 10 (capacity 6)
(14, 8);  -- Reservation 14 uses table 8

-- Italian Bistro: tables 11, 12, 13
INSERT INTO reservation_tables (reservation_id, table_id) VALUES
(15, 11), -- Reservation 15 at Italian Bistro uses table 11 (capacity 2)
(16, 12), -- Reservation 16 uses table 12 (capacity 4)
(17, 13); -- Reservation 17 uses table 13 (capacity 6)

-- Seafood Fresh: tables 14, 15, 16
INSERT INTO reservation_tables (reservation_id, table_id) VALUES
(18, 14), -- Reservation 18 at Seafood Fresh uses table 14 (capacity 2)
(19, 15), -- Reservation 19 uses table 15 (capacity 4)
(20, 16); -- Reservation 20 uses table 16 (capacity 6)
