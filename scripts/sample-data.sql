-- =============================================================================
-- GutFriendly — MySQL sample data for local testing
-- =============================================================================
-- Database : gutfriendly (see application.properties)
-- Prerequisite: Start the Spring Boot app once so Hibernate creates/updates tables
--               (spring.jpa.hibernate.ddl-auto=update), then run this script.
--
-- Usage (MySQL CLI):
--   mysql -u root -p gutfriendly < scripts/sample-data.sql
--
-- Or from MySQL Workbench: open and execute this file.
-- =============================================================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

USE gutfriendly;

-- ---------------------------------------------------------------------------
-- Clear existing rows (child tables first)
-- ---------------------------------------------------------------------------
DELETE FROM review_keywords;
DELETE FROM shop_reviews;
DELETE FROM order_items;
DELETE FROM user_orders;
DELETE FROM cart_item;
DELETE FROM cart;
DELETE FROM wishlist;
DELETE FROM inspection_test_result;
DELETE FROM inspection_images;
DELETE FROM inspection_details;
DELETE FROM food_images;
DELETE FROM food_items_details;
DELETE FROM shop_images;
DELETE FROM shop_payout;
DELETE FROM certificate;
DELETE FROM shop_details;
DELETE FROM vendor_shop_address;
DELETE FROM user_address;
DELETE FROM user_details;
DELETE FROM vendor_details;
DELETE FROM inspector_details;
DELETE FROM admin_details;
DELETE FROM test_catalog;
DELETE FROM pincode;

-- ---------------------------------------------------------------------------
-- TEST CREDENTIALS (plaintext passwords — dev only)
-- ---------------------------------------------------------------------------
-- User app      : phone 9876510001 / password user123   (Aarav Sharma)
--                 phone 9876510002 / password user123   (Neha Verma)
-- Vendor app    : phone 9876500001 / password vendor123 (Rajesh Kumar — verified shop)
--                 phone 9876500002 / password vendor123 (Priya Singh — pending shop)
-- Inspector app : email rahul@gutfriendly.com  / password inspector123
--                 email priya.inspector@gutfriendly.com / password inspector123
-- Admin UI      : email admin@gutfriendly.com (stub login — any password)
-- ---------------------------------------------------------------------------

-- ---------------------------------------------------------------------------
-- 1. Pincodes (required for user/vendor addresses)
-- ---------------------------------------------------------------------------
INSERT INTO pincode (pin_code, city, state) VALUES
('201301', 'Noida', 'Uttar Pradesh'),
('201305', 'Greater Noida', 'Uttar Pradesh'),
('110001', 'New Delhi', 'Delhi');

-- ---------------------------------------------------------------------------
-- 2. Admin
-- ---------------------------------------------------------------------------
INSERT INTO admin_details (
    id, first_name, last_name, email, password, phone_no,
    created_at, last_login, is_active
) VALUES (
    1, 'Admin', 'User', 'admin@gutfriendly.com', 'admin123', '9999900001',
    '2026-01-15 10:00:00', '2026-08-01 09:00:00', 1
);

-- ---------------------------------------------------------------------------
-- 3. Inspectors
-- ---------------------------------------------------------------------------
INSERT INTO inspector_details (
    inspector_id, first_name, last_name, employee_code, phone_no, email, password,
    status, experience_in_years, assigned_city, assigned_zone, designation,
    address, joining_date
) VALUES
(
    1, 'Rahul', 'Mehta', 'INS001', '9988800001', 'rahul@gutfriendly.com', 'inspector123',
    'ACTIVE', 5, 'Noida', 'Zone A', 'SENIOR_INSPECTOR',
    'Sector 62, Noida', '2025-06-01 10:00:00'
),
(
    2, 'Priya', 'Nair', 'INS002', '9988800002', 'priya.inspector@gutfriendly.com', 'inspector123',
    'ACTIVE', 3, 'Delhi', 'Zone B', 'JUNIOR_INSPECTOR',
    'Connaught Place, Delhi', '2025-09-01 10:00:00'
);

-- ---------------------------------------------------------------------------
-- 4. Vendors
-- ---------------------------------------------------------------------------
INSERT INTO vendor_details (
    vendor_id, f_name, m_name, l_name, phone_no, password, email,
    adhar_no, pan_no, joining_date, is_active
) VALUES
(
    1, 'Rajesh', NULL, 'Kumar', '9876500001', 'vendor123', 'rajesh.vendor@gutfriendly.com',
    '123456789012', 'ABCDE1234F', '2025-08-01 10:00:00', 1
),
(
    2, 'Priya', NULL, 'Singh', '9876500002', 'vendor123', 'priya.vendor@gutfriendly.com',
    '234567890123', 'BCDEF2345G', '2026-02-01 10:00:00', 1
);

-- ---------------------------------------------------------------------------
-- 5. Vendor shop addresses
-- ---------------------------------------------------------------------------
INSERT INTO vendor_shop_address (address_id, locality, pin_code, shop_number) VALUES
(1, 'Sector 62, Noida', '201301', 'GF-12'),
(2, 'Alpha 1, Greater Noida', '201305', 'SH-45');

-- ---------------------------------------------------------------------------
-- 6. Shops
--    Shop 1: VERIFIED + open (user ordering, menu browse)
--    Shop 2: PENDING (vendor onboarding / inspection workflow)
--    Shop 3: VERIFIED + open (second trusted vendor)
-- ---------------------------------------------------------------------------
INSERT INTO shop_details (
    shop_id, shop_name, gst_no, category,
    user_trust_score, inspection_trust_score, final_gut_trust_score,
    vendor_id, address_id, last_calculated_at, created_at,
    status, blocked, admin_remarks, verified_at,
    service_availability_status, image_url, is_open, online_orders_enabled,
    open_time, close_time, estimated_prep_time_minutes, rating, rating_count
) VALUES
(
    1, 'Spice Garden', '27AABCU9603R1ZM', 'RESTAURANT',
    4.20, 8.50, 7.85,
    1, 1, '2026-08-01 12:00:00', '2025-09-01 10:00:00',
    'VERIFIED', 0, NULL, '2026-07-15 14:30:00',
    'SERVICEABLE', 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4', 1, 1,
    '09:00:00', '22:00:00', 20, 4.2, 18
),
(
    2, 'Green Bowl Kitchen', '27AABCU9603R2ZN', 'FAST_FOOD',
    0.00, 0.00, 0.00,
    2, 2, '2026-08-01 12:00:00', '2026-07-01 10:00:00',
    'PENDING', 0, NULL, NULL,
    'SERVICEABLE', NULL, 0, 1,
    '10:00:00', '21:00:00', 15, NULL, 0
),
(
    3, 'Sweet Treats Bakery', '27AABCU9603R3ZO', 'BAKERY',
    4.60, 9.10, 8.40,
    1, NULL, '2026-08-01 12:00:00', '2025-11-01 10:00:00',
    'VERIFIED', 0, NULL, '2026-05-10 11:00:00',
    'SERVICEABLE', 'https://images.unsplash.com/photo-1509440159596-0249088772ff', 1, 1,
    '08:00:00', '20:00:00', 25, 4.6, 12
);

-- ---------------------------------------------------------------------------
-- 7. Menu items (verified shops only visible to users)
-- ---------------------------------------------------------------------------
INSERT INTO food_items_details (
    food_id, shop_id, food_name, price, food_desc, food_category,
    available, created_at, updated_at
) VALUES
(1, 1, 'Paneer Butter Masala', 249.00, 'Creamy tomato gravy with fresh paneer.', 'MAIN_COURSE', 1, '2025-09-05 10:00:00', '2026-08-01 10:00:00'),
(2, 1, 'Veg Biryani', 199.00, 'Fragrant basmati rice with mixed vegetables.', 'RICE', 1, '2025-09-05 10:00:00', '2026-08-01 10:00:00'),
(3, 1, 'Masala Dosa', 120.00, 'Crispy dosa with potato filling.', 'MAIN_COURSE', 1, '2025-09-05 10:00:00', '2026-08-01 10:00:00'),
(4, 1, 'Fresh Lime Soda', 60.00, 'Sweet and salted lime soda.', 'BEVERAGE', 1, '2025-09-05 10:00:00', '2026-08-01 10:00:00'),
(5, 3, 'Chocolate Brownie', 89.00, 'Warm fudge brownie.', 'DESSERT', 1, '2025-11-05 10:00:00', '2026-08-01 10:00:00'),
(6, 3, 'Blueberry Muffin', 75.00, 'Freshly baked muffin.', 'DESSERT', 1, '2025-11-05 10:00:00', '2026-08-01 10:00:00'),
(7, 3, 'Cold Coffee', 99.00, 'Iced coffee with cream.', 'BEVERAGE', 1, '2025-11-05 10:00:00', '2026-08-01 10:00:00');

-- ---------------------------------------------------------------------------
-- 8. Test catalog (inspector test workflow)
-- ---------------------------------------------------------------------------
INSERT INTO test_catalog (
    test_id, category, product_name, test_title, adulterant_name,
    testing_method, positive_indicator, negative_indicator,
    equipment_required, reference_image_pure, reference_image_adulterated,
    max_score, active
) VALUES
(
    1, 'MILK_AND_MILK_PRODUCTS', 'Milk', 'Detergent in milk', 'Detergent',
    'Shake sample and observe froth persistence.',
    'Persistent froth indicates adulteration.',
    'Froth dissipates quickly for pure milk.',
    'Test tube, water', NULL, NULL, 10.0, 1
),
(
    2, 'SPICES_AND_CONDIMENTS', 'Turmeric', 'Metanil yellow test', 'Metanil Yellow',
    'Add dilute HCl and observe colour change.',
    'Pink colour indicates adulteration.',
    'No colour change for pure turmeric.',
    'HCl, test tube', NULL, NULL, 10.0, 1
);

-- ---------------------------------------------------------------------------
-- 9. Inspections
--    Inspection 1: Shop 2 — SCHEDULED (admin assign inspector)
--    Inspection 2: Shop 1 — APPROVED (historical, shop already verified)
-- ---------------------------------------------------------------------------
INSERT INTO inspection_details (
    inspection_id, vendor_id, shop_id, inspector_id, inspection_date, completed_at,
    status, overall_inspection_score, recommendation, inspector_remarks,
    admin_remarks, reviewed_by_admin, reviewed_at
) VALUES
(
    1, 2, 2, NULL, '2026-08-10 11:00:00', NULL,
    'SCHEDULED', 0.0, 'PENDING', NULL,
    NULL, 0, NULL
),
(
    2, 1, 1, 1, '2026-06-20 10:00:00', '2026-06-20 14:00:00',
    'APPROVED', 8.5, 'APPROVED', 'Kitchen hygiene and storage were satisfactory.',
    'Approved after review.', 1, '2026-07-15 14:00:00'
);

-- ---------------------------------------------------------------------------
-- 10. Users
-- ---------------------------------------------------------------------------
INSERT INTO user_details (
    user_id, f_name, l_name, phone_no, email, password,
    joining_date, is_active, trusted_user, reward_points
) VALUES
(
    1, 'Aarav', 'Sharma', '9876510001', 'aarav@gutfriendly.com', 'user123',
    '2026-03-01 10:00:00', 1, 0, 25
),
(
    2, 'Neha', 'Verma', '9876510002', 'neha@gutfriendly.com', 'user123',
    '2026-04-15 10:00:00', 1, 0, 10
);

-- ---------------------------------------------------------------------------
-- 11. User addresses
-- ---------------------------------------------------------------------------
INSERT INTO user_address (address_id, user_id, locality, address_type, pin_code) VALUES
(1, 1, 'Sector 18, Noida', 'HOME', '201301'),
(2, 1, 'Cyber Hub, Gurgaon', 'WORK', '201305');

-- ---------------------------------------------------------------------------
-- 12. Wishlist
-- ---------------------------------------------------------------------------
INSERT INTO wishlist (wishlist_id, user_id, shop_id, created_at) VALUES
(1, 1, 3, '2026-07-20 10:00:00'),
(2, 1, 1, '2026-07-21 10:00:00');

-- ---------------------------------------------------------------------------
-- 13. Cart (user 1 — Spice Garden items)
-- ---------------------------------------------------------------------------
INSERT INTO cart (cart_id, user_id, created_at, updated_at) VALUES
(1, 1, '2026-08-05 10:00:00', '2026-08-05 10:00:00');

INSERT INTO cart_item (cart_item_id, cart_id, food_id, quantity, unit_price) VALUES
(1, 1, 1, 2, 249.00),
(2, 1, 4, 1, 60.00);

-- ---------------------------------------------------------------------------
-- 14. Orders
-- ---------------------------------------------------------------------------
INSERT INTO user_orders (
    order_id, status, user_id, shop_id, payment_id,
    delivery_address, total_amount, payment_method, payment_status,
    ordered_at, updated_at, delivered_at
) VALUES
(
    1, 'DELIVERED', 1, 1, NULL,
    'Flat 402, Tower B, Sector 18, Noida - 201301',
    558.00, 'COD', 'SUCCESS',
    '2026-07-25 19:30:00', '2026-07-25 20:45:00', '2026-07-25 20:45:00'
),
(
    2, 'PREPARING', 1, 1, NULL,
    'Flat 402, Tower B, Sector 18, Noida - 201301',
    199.00, 'COD', 'PENDING',
    '2026-08-05 13:00:00', '2026-08-05 13:15:00', NULL
),
(
    3, 'ORDER_PLACED', 2, 3, NULL,
    'House 12, Alpha 1, Greater Noida - 201305',
    164.00, 'COD', 'PENDING',
    '2026-08-05 14:00:00', '2026-08-05 14:00:00', NULL
);

INSERT INTO order_items (
    order_item_id, order_id, food_id, food_name, quantity, price, item_total
) VALUES
(1, 1, 1, 'Paneer Butter Masala', 2, 249.00, 498.00),
(2, 1, 4, 'Fresh Lime Soda', 1, 60.00, 60.00),
(3, 2, 2, 'Veg Biryani', 1, 199.00, 199.00),
(4, 3, 5, 'Chocolate Brownie', 1, 89.00, 89.00),
(5, 3, 6, 'Blueberry Muffin', 1, 75.00, 75.00);

-- ---------------------------------------------------------------------------
-- 15. Shop review (delivered order #1)
-- ---------------------------------------------------------------------------
INSERT INTO shop_reviews (
    review_id, order_id, user_id, shop_id, rating, comment,
    review_type, points_awarded, active, created_at, updated_at, reward_granted
) VALUES
(
    1, 1, 1, 1, 5,
    'Excellent hygiene and tasty food. Packaging was neat and delivery was on time.',
    'DETAILED', 15, 1, '2026-07-26 10:00:00', '2026-07-26 10:00:00', 1
);

INSERT INTO review_keywords (review_id, keyword) VALUES
(1, 'GOOD_HYGIENE'),
(1, 'TASTY'),
(1, 'ON_TIME_DELIVERY');

-- ---------------------------------------------------------------------------
-- Reset AUTO_INCREMENT to continue after seeded IDs
-- ---------------------------------------------------------------------------
ALTER TABLE pincode AUTO_INCREMENT = 1;
ALTER TABLE admin_details AUTO_INCREMENT = 2;
ALTER TABLE inspector_details AUTO_INCREMENT = 3;
ALTER TABLE vendor_details AUTO_INCREMENT = 3;
ALTER TABLE vendor_shop_address AUTO_INCREMENT = 3;
ALTER TABLE shop_details AUTO_INCREMENT = 4;
ALTER TABLE food_items_details AUTO_INCREMENT = 8;
ALTER TABLE test_catalog AUTO_INCREMENT = 3;
ALTER TABLE inspection_details AUTO_INCREMENT = 3;
ALTER TABLE user_details AUTO_INCREMENT = 3;
ALTER TABLE user_address AUTO_INCREMENT = 3;
ALTER TABLE wishlist AUTO_INCREMENT = 3;
ALTER TABLE cart AUTO_INCREMENT = 2;
ALTER TABLE cart_item AUTO_INCREMENT = 3;
ALTER TABLE user_orders AUTO_INCREMENT = 4;
ALTER TABLE order_items AUTO_INCREMENT = 6;
ALTER TABLE shop_reviews AUTO_INCREMENT = 2;

SET FOREIGN_KEY_CHECKS = 1;

-- =============================================================================
-- End of sample data
-- =============================================================================
