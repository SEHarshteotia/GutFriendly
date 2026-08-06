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
-- 8. Test catalog (FSSAI DART — 44 rapid tests)
-- ---------------------------------------------------------------------------
-- FSSAI DART (Detect Adulteration with Rapid Test) — 44 rapid tests
-- Source: FSSAI DART Handbook
--
-- Schema note: Hibernate stores category as VARCHAR via @Enumerated(EnumType.STRING).
-- No MySQL ENUM ALTER is required for normal Hibernate-managed databases.
-- If you manually converted category to MySQL ENUM earlier, revert it first:
--   ALTER TABLE test_catalog MODIFY COLUMN category VARCHAR(50) NOT NULL;
INSERT INTO test_catalog (
    test_id, category, product_name, test_title, adulterant_name,
    testing_method, positive_indicator, negative_indicator,
    equipment_required, reference_image_pure, reference_image_adulterated,
    max_score, active
) VALUES
(1, 'DAIRY', 'Milk', 'Detection of water in milk', 'Water', '1) Put a drop of milk on a polished slanting surface. 2) Pure milk either stays or flows slowly leaving a white trail behind. 3) Milk adulterated with water will flow immediately without leaving a mark.', 'Milk stays or flows slowly, leaving a white trail behind.', 'Milk flows immediately without leaving any mark.', 'Polished slanting surface', '/images/tests/test1_pure.jpg', '/images/tests/test1_adulterated.jpg', 10.0, 1),
(2, 'DAIRY', 'Milk', 'Detection of detergent in milk', 'Detergent', '1) Take 5 to 10ml of sample with an equal amount of water. 2) Shake the contents thoroughly. 3) If milk is adulterated with detergent, it forms dense lather. 4) Pure milk will form a very thin foam layer due to agitation.', 'Very thin foam layer forms due to agitation.', 'Dense, thick lather forms.', 'Test tube or glass, water', '/images/tests/test2_pure.jpg', '/images/tests/test2_adulterated.jpg', 10.0, 1),
(3, 'DAIRY', 'Milk / Khoya / Chenna / Paneer', 'Detection of starch in milk and milk products', 'Starch', '1) Boil 2-3ml of sample with 5ml of water. 2) Cool and add 2-3 drops of tincture of iodine. 3) Formation of blue colour indicates the presence of starch. (For milk, addition of water and boiling is not required)', 'No colour change; solution remains as is.', 'Blue colour forms.', 'Tincture of iodine, beaker, heat source', '/images/tests/test3_pure.jpg', '/images/tests/test3_adulterated.jpg', 10.0, 1),
(4, 'DAIRY', 'Ghee / Butter', 'Detection of mashed potatoes, sweet potatoes and other starches in ghee/butter', 'Mashed potato / sweet potato / starch', '1) Take 1/2 teaspoon of ghee/butter in a transparent glass bowl. 2) Add 2-3 drops of tincture of iodine. 3) Formation of blue colour indicates the presence of mashed potatoes, sweet potatoes and other starches.', 'No blue colour forms.', 'Blue colour forms.', 'Tincture of iodine, transparent glass bowl', '/images/tests/test4_pure.jpg', '/images/tests/test4_adulterated.jpg', 10.0, 1),
(5, 'OILS_AND_FATS', 'Coconut Oil', 'Detection of other oils in coconut oil', 'Other/foreign oils', '1) Take coconut oil in a transparent glass. 2) Place this glass in a refrigerator for 30 minutes (do not keep in the freezer). 3) After refrigeration, coconut oil solidifies. 4) If coconut oil is adulterated, other oils remain as a separate layer.', 'Oil solidifies uniformly with no separate layer.', 'A separate liquid oil layer remains on top.', 'Refrigerator, transparent glass', '/images/tests/test5_pure.jpg', '/images/tests/test5_adulterated.jpg', 10.0, 1),
(6, 'OILS_AND_FATS', 'Oils and Fats', 'Detection of TOCP (Tri-Ortho-Cresyl-Phosphate) in oils and fats', 'TOCP', '1) Take 2ml of sample oil. 2) Add on a little amount of yellow butter (solid). 3) Immediate formation of red colour indicates the presence of TOCP.', 'No red colour forms.', 'Red colour forms immediately.', 'Yellow solid butter, spoon', '/images/tests/test6_pure.jpg', '/images/tests/test6_adulterated.jpg', 10.0, 1),
(7, 'OILS_AND_FATS', 'Refined Winterized Salad Oils', 'Proper winterization of refined winterized salad oils', 'Improper winterization', '1) Take 100ml sample oil in a bottle, cork tightly and seal with paraffin. 2) Submerge the bottle completely in a bucket containing finely cracked ice; add water until it rises to the top of the bottle. 3) Keep the bucket filled solidly with ice, removing excess water and adding ice as necessary. 4) After 5.5 hours remove the bottle and examine the oil. 5) If properly winterized, the sample will be brilliant, clear and limpid.', 'Sample is brilliant, clear and limpid.', 'Sample appears cloudy or shows solidified/separated material.', 'Bottle, cork, paraffin, ice bucket, cracked ice', '/images/tests/test7_pure.jpg', NULL, 10.0, 1),
(8, 'SUGAR_AND_HONEY', 'Honey', 'Detection of sugar solution in honey', 'Sugar / water', 'Method 1: 1) Take a transparent glass of water. 2) Add a drop of honey to the glass. 3) Pure honey will not disperse in water. 4) If the drop of honey disperses in water, it indicates the presence of added sugar. Method 2: 1) Take a cotton wick dipped in pure honey and light with a match stick. 2) Pure honey will burn. 3) If adulterated, the presence of water will not allow the honey to burn; if it does burn, it will produce a cracking sound.', 'Honey drop does not disperse in water; wick burns cleanly.', 'Honey drop disperses in water; wick fails to burn or crackles.', 'Transparent glass, water, cotton wick, matchstick', '/images/tests/test8_pure.jpg', '/images/tests/test8_adulterated.jpg', 10.0, 1),
(9, 'SUGAR_AND_HONEY', 'Sugar / Pithi Sugar / Jaggery', 'Detection of chalk powder in sugar/pithi sugar/jaggery', 'Chalk powder', '1) Take a transparent glass of water. 2) Dissolve 10g of sample in the water. 3) If the sample is mixed with chalk, the adulterant will settle down at the bottom.', 'No sediment settles at the bottom.', 'Chalk sediment settles at the bottom.', 'Transparent glass, water', '/images/tests/test9_pure.jpg', '/images/tests/test9_adulterated.jpg', 10.0, 1),
(10, 'METALS', 'Silver Leaves (Chandi Vark)', 'Detection of aluminium leaves in silver leaves', 'Aluminium leaves', '1) Take some portion of the leaf and crush it between two fingers. 2) Pure silver leaves will be easily crushed and crumble to powder form, while aluminium leaves will only break into smaller shreds. 3) Further, take the suspected silver leaves, form into a ball, and burn it with a flame. 4) Pure silver leaves burn away completely leaving glistening balls, while aluminium leaves are reduced to grey ash.', 'Crumbles to powder when crushed; leaves glistening balls after burning.', 'Breaks into shreds when crushed; reduces to grey ash after burning.', 'Flame source (candle/spoon)', '/images/tests/test10_pure.jpg', '/images/tests/test10_adulterated.jpg', 10.0, 1),
(11, 'GRAINS_AND_PULSES', 'Food Grains', 'Detection of extraneous matter (dust, pebble, stone, straw, weed seeds, damaged grain, weeviled grain, insects, rodent hair and excreta) in food grains', 'Extraneous matter', '1) Take a small quantity of sample in a glass plate. 2) Examine the impurities visually. 3) Pure food grains will not have any such impurities. 4) Impurities are observed visually in adulterated food grains.', 'No visible impurities.', 'Visible dust, stones, damaged/weeviled grain, insects, rodent hair, etc.', 'Glass plate', '/images/tests/test11_pure.jpg', '/images/tests/test11_adulterated.jpg', 10.0, 1),
(12, 'GRAINS_AND_PULSES', 'Food Grains', 'Detection of dhatura in food grains', 'Dhatura seeds', '1) Take a small quantity of food grains in a glass plate. 2) Examine the impurities visually. 3) Dhatura seeds, which are flat with edges and blackish brown in colour, can be separated out by close examination.', 'No flat, blackish-brown edged seeds present.', 'Flat, blackish-brown edged dhatura seeds found among the grains.', 'Glass plate, magnifying glass (optional)', '/images/tests/test12_pure.jpg', '/images/tests/test12_adulterated.jpg', 10.0, 1),
(13, 'FLOUR', 'Wheat Flour', 'Detection of excess bran in wheat flour', 'Excess bran', '1) Take a transparent glass of water. 2) Sprinkle a spoon of wheat flour on the surface of the water. 3) Pure wheat flour will not show excess bran on the water surface. 4) Adulterated wheat flour shows excess bran floating on the water surface.', 'No excess bran floats on the surface.', 'Excess bran floats visibly on the water surface.', 'Transparent glass, water', '/images/tests/test13_pure.jpg', '/images/tests/test13_adulterated.jpg', 10.0, 1),
(14, 'GRAINS_AND_PULSES', 'Dal (Whole and Split)', 'Detection of khesari dal in dal whole and split', 'Khesari dal', '1) Take a small quantity of dal whole or split in a glass plate. 2) Examine the impurities visually. 3) Khesari dal, which has an edged-type appearance showing a slant on one side and is square in appearance, can be separated out by close examination. 4) Pure dal will not have any such impurities.', 'No slanted, square-edged khesari dal present.', 'Slanted, square-edged khesari dal found among the sample.', 'Glass plate', '/images/tests/test14_pure.jpg', '/images/tests/test14_adulterated.jpg', 10.0, 1),
(15, 'GRAINS_AND_PULSES', 'Food Grains', 'Detection of added colour in food grains', 'Artificial/added colour', '1) Take a transparent glass of water. 2) Add 2 teaspoons of food grains and mix thoroughly. 3) Pure food grains will not leave any colour. 4) Adulterated food grains leave colour immediately in the water.', 'Water remains colourless.', 'Colour leaches into the water immediately.', 'Transparent glass, water', '/images/tests/test15_pure.jpg', '/images/tests/test15_adulterated.jpg', 10.0, 1),
(16, 'GRAINS_AND_PULSES', 'Sella Rice', 'Detection of turmeric in sella rice', 'Turmeric (colour)', '1) Take a teaspoon of rice in a glass plate. 2) Sprinkle a small amount of soaked lime (commonly known as chuna, used in paan) on the rice grains. 3) Pure grains will not form red colour. 4) Adulterated grains will form red colour.', 'No red colour forms on the grains.', 'Red colour forms on the grains.', 'Slaked lime (chuna), glass plate', '/images/tests/test16_pure.jpg', '/images/tests/test16_adulterated.jpg', 10.0, 1),
(17, 'GRAINS_AND_PULSES', 'Ragi', 'Detection of rhodamine B in ragi', 'Rhodamine B', '1) Take a cotton ball soaked in water or vegetable oil (conduct the test separately for each). 2) Rub the outer surface of the ragi. 3) If the cotton absorbs colour, it indicates adulteration with rhodamine B used for colouring the outer surface of ragi.', 'Cotton does not absorb any colour.', 'Cotton absorbs a pink/red colour.', 'Cotton ball, water and vegetable oil', '/images/tests/test17_pure.jpg', '/images/tests/test17_adulterated.jpg', 10.0, 1),
(18, 'GRAINS_AND_PULSES', 'Pulses', 'Detection of chakunda beans in pulses', 'Chakunda beans', '1) Take a small quantity of pulses in a transparent glass plate. 2) Examine the impurities visually. 3) Chakunda beans can be separated out by close examination.', 'No chakunda beans present.', 'Chakunda beans found mixed with the pulses.', 'Transparent glass plate', '/images/tests/test18_pure.jpg', NULL, 10.0, 1),
(19, 'FLOUR', 'Atta / Maida / Suji (Rawa)', 'Detection of sand, soil, insects, webs, lumps, rodent hair and excreta in Atta, Maida, Suji (Rawa)', 'Sand, soil, insects, webs, lumps, rodent hair, excreta', '1) These can be identified by visual examination of the sample.', 'No visible sand, soil, insects, webs, lumps, rodent hair or excreta.', 'Visible sand, soil, insects, webs, lumps, rodent hair or excreta present.', 'None (visual examination)', '/images/tests/test19_pure.jpg', '/images/tests/test19_adulterated.jpg', 10.0, 1),
(20, 'SPICES', 'Asafoetida (Hing)', 'Detection of foreign resin in asafoetida (hing)', 'Foreign resin / non-edible gum', 'Method 1: 1) Burn a small quantity of asafoetida in a stainless steel spoon. 2) Pure asafoetida will burn like camphor. 3) Adulterated asafoetida will not produce a bright flame like camphor. Method 2: 1) Powder a gram of asafoetida and take it in a glass container. 2) Add one teaspoon of water and mix thoroughly by shaking. 3) A milky white solution with no sediments represents pure asafoetida.', 'Burns brightly like camphor; forms milky white solution with no sediment.', 'Does not burn brightly; solution shows sediment.', 'Stainless steel spoon, flame source, glass container, water', '/images/tests/test20_pure.jpg', '/images/tests/test20_adulterated.jpg', 10.0, 1),
(21, 'SPICES', 'Black Pepper', 'Detection of papaya seeds in black pepper', 'Papaya seeds', 'Method 1: 1) Add some amount of black pepper to a glass of water. 2) Pure black pepper settles at the bottom. 3) In adulterated black pepper, papaya seeds float on the surface of the water. Method 2: 1) Spread the spice on white paper. 2) Observe the appearance of the sample using a magnifying glass. 3) Black pepper is brown in colour, has a wrinkled surface, and a characteristic smell and pungent taste. 4) Papaya seeds have a shrunken smooth surface and oval shape; they are greenish brown or blackish brown in colour and have a repulsive flavour.', 'Sample settles fully at the bottom; uniform wrinkled brown appearance.', 'Some seeds float on the surface; smooth oval papaya seeds visible.', 'Glass, water, white paper, magnifying glass', '/images/tests/test21_pure.jpg', '/images/tests/test21_adulterated.jpg', 10.0, 1),
(22, 'SPICES', 'Black Pepper', 'Detection of light black berries in black pepper', 'Light (immature) black berries', '1) Press the berries with the help of fingers. 2) Light berries will break easily while black berries of pepper will not break.', 'Berries resist pressure and do not break.', 'Berries break easily under light pressure.', 'None', '/images/tests/test22_pure.jpg', '/images/tests/test22_adulterated.jpg', 10.0, 1),
(23, 'SPICES', 'Asafoetida (Hing)', 'Detection of soap stone or other earthy matter in asafoetida (hing)', 'Soap stone / earthy matter', '1) Shake a little portion of the sample with water and allow it to settle. 2) Pure asafoetida will not leave any soap stone or other earthy matter at the bottom. 3) If asafoetida is adulterated, soap stone or other earthy matter will settle down at the bottom.', 'No sediment at the bottom.', 'Soap stone/earthy sediment settles at the bottom.', 'Glass, water', '/images/tests/test23_pure.jpg', '/images/tests/test23_adulterated.jpg', 10.0, 1),
(24, 'SPICES', 'Chilli Powder', 'Detection of artificial/water soluble synthetic colours in chilli powder', 'Artificial/synthetic water-soluble colour', '1) Sprinkle chilli powder on the surface of water taken in a glass tumbler. 2) The artificial colourants will immediately start descending in colour streaks.', 'No colour streaks descend; colour stays with the powder.', 'Colour streaks descend immediately into the water.', 'Glass tumbler, water', '/images/tests/test24_pure.jpg', '/images/tests/test24_adulterated.jpg', 10.0, 1),
(25, 'SPICES', 'Black Pepper', 'Detection of light black berries in black pepper (alcohol float test)', 'Light (immature) black berries', '1) Float the sample of black pepper in alcohol (rectified spirit). 2) The mature black pepper berries sink while the light black pepper floats.', 'Berries sink to the bottom.', 'Berries float on the surface.', 'Rectified spirit, glass', '/images/tests/test25_pure.jpg', '/images/tests/test25_adulterated.jpg', 10.0, 1),
(26, 'SPICES', 'Chilli Powder', 'Detection of saw dust in chilli powder', 'Saw dust', '1) Add the sample to water. 2) The saw dust will float at the surface of the water while chilli powder will settle down at the bottom.', 'Powder settles at the bottom with no floating material.', 'Saw dust floats on the surface.', 'Glass, water', '/images/tests/test26_pure.jpg', '/images/tests/test26_adulterated.jpg', 10.0, 1),
(27, 'SPICES', 'Asafoetida (Hing)', 'Detection of starch in asafoetida', 'Starch', '1) Tincture of iodine is added to the sample of asafoetida. 2) Appearance of blue colour shows the presence of starch.', 'No blue colour appears.', 'Blue colour appears.', 'Tincture of iodine', '/images/tests/test27_pure.jpg', '/images/tests/test27_adulterated.jpg', 10.0, 1),
(28, 'SALT', 'Common Salt', 'Detection of chalk in common salt', 'Chalk', '1) Stir a spoonful of the sample of salt in a glass of water. 2) The presence of chalk will make the solution white and other insoluble impurities will settle down.', 'Solution remains clear; no white residue or sediment.', 'Solution turns white; insoluble impurities settle down.', 'Glass, water', '/images/tests/test28_pure.jpg', '/images/tests/test28_adulterated.jpg', 10.0, 1),
(29, 'SPICES', 'Cloves', 'Detection of exhausted cloves in cloves', 'Exhausted (volatile-oil-extracted) cloves', '1) Take some water in a glass and put cloves in it. 2) Genuine cloves will settle down at the bottom while exhausted cloves will float on the surface.', 'Cloves sink to the bottom.', 'Cloves float on the surface.', 'Glass, water', '/images/tests/test29_pure.jpg', '/images/tests/test29_adulterated.jpg', 10.0, 1),
(30, 'SPICES', 'Cinnamon', 'Detection of cassia bark in cinnamon', 'Cassia bark', '1) Take a small quantity of cinnamon in a glass plate. 2) If adulterated, on close visual examination, cassia bark comprising several layers between a rough outer and a smooth innermost layer can be differentiated from cinnamon. 3) Cinnamon bark is very thin and can be rolled around a pencil or pen; it has a distinct smell.', 'Thin bark, single/smooth rolled layer, rolls easily around a pencil, distinct smell.', 'Thick bark with several visible layers between rough outer and smooth inner surface.', 'Glass plate, pencil/pen', '/images/tests/test30_pure.jpg', '/images/tests/test30_adulterated.jpg', 10.0, 1),
(31, 'SPICES', 'Cumin Seeds', 'Detection of grass seeds coloured with charcoal dust in cumin seeds', 'Grass seeds coloured with charcoal dust', '1) Rub a small amount of cumin seeds on the palms. 2) If the palms turn black, adulteration is indicated.', 'Palms remain unstained.', 'Palms turn black.', 'None', '/images/tests/test31_pure.jpg', '/images/tests/test31_adulterated.jpg', 10.0, 1),
(32, 'OILSEEDS', 'Mustard Seeds', 'Detection of argemone seeds in mustard seeds', 'Argemone seeds', '1) Take a small quantity of mustard seeds in a glass plate. 2) Examine visually for argemone seeds. 3) Mustard seeds have a smooth surface and are yellow inside when pressed. 4) Argemone seeds have a grainy, rough, black surface and are white inside when pressed.', 'Seeds are smooth-surfaced and yellow inside when pressed.', 'Rough, grainy black seeds present, white inside when pressed.', 'Glass plate', '/images/tests/test32_pure.jpg', '/images/tests/test32_adulterated.jpg', 10.0, 1),
(33, 'SPICES', 'Turmeric Whole', 'Detection of lead chromate in turmeric whole', 'Lead chromate', '1) Add a small quantity of turmeric whole to a transparent glass of water. 2) Pure turmeric will not leave any colour. 3) Adulterated turmeric appears bright in colour and leaves colour immediately in the water.', 'No colour leaches into the water.', 'Bright colour leaches into the water immediately.', 'Transparent glass, water', '/images/tests/test33_pure.jpg', '/images/tests/test33_adulterated.jpg', 10.0, 1),
(34, 'SPICES', 'Turmeric Powder', 'Detection of artificial colour in turmeric powder', 'Artificial colour', '1) Add a teaspoon of turmeric powder in a glass of water. 2) Natural turmeric powder leaves a light yellow colour while settling down. 3) Adulterated turmeric powder will leave a strong/deep yellow colour in the water while settling down.', 'Light yellow colour left in water while settling.', 'Strong, deep yellow colour left in water while settling.', 'Glass, water', '/images/tests/test34_pure.jpg', '/images/tests/test34_adulterated.jpg', 10.0, 1),
(35, 'SPICES', 'Powdered Spices', 'Detection of sawdust and powdered bran in powdered spices', 'Sawdust / powdered bran', '1) Sprinkle powdered spices on the water surface. 2) Pure spices will not leave any sawdust/powdered bran on the surface of the water. 3) If spices are adulterated, sawdust/powdered bran will float on the surface.', 'No material floats on the surface.', 'Sawdust/bran floats visibly on the surface.', 'Glass, water', '/images/tests/test35_pure.jpg', '/images/tests/test35_adulterated.jpg', 10.0, 1),
(36, 'SALT', 'Common Salt / Iodised Salt', 'Differentiation of common salt and iodised salt', 'N/A (differentiation, not adulteration)', '1) Cut a piece of potato, add salt to the cut surface and wait for a minute. 2) Add two drops of lemon juice. 3) If it is iodised salt, a blue colour will develop. 4) In the case of common salt, there will be no blue colour.', 'Blue colour develops (confirms iodised salt).', 'No blue colour develops (indicates common/non-iodised salt).', 'Potato, knife, lemon', '/images/tests/test36_pure.jpg', '/images/tests/test36_adulterated.jpg', 10.0, 1),
(37, 'SPICES', 'Saffron', 'Detection of coloured dried tendrils of maize cob in saffron', 'Coloured maize cob tendrils (coal tar dye)', '1) Genuine saffron will not break easily like artificial saffron; artificial saffron is prepared by soaking maize cob tendrils in sugar and colouring with coal tar dye. 2) Take a transparent glass of water and add a small quantity of saffron. 3) If saffron is adulterated, the artificial colour dissolves in water rapidly. A bit of pure saffron, when allowed to dissolve in water, will continue to give its saffron colour for as long as it lasts.', 'Colour releases slowly and persists over time.', 'Colour dissolves and disperses rapidly into the water.', 'Transparent glass, water', '/images/tests/test37_pure.jpg', '/images/tests/test37_adulterated.jpg', 10.0, 1),
(38, 'VEGETABLES', 'Green Vegetables (Bitter Gourd, Green Chilli, etc.)', 'Detection of malachite green in green vegetables', 'Malachite green', 'Method 1: 1) Take a cotton piece soaked in water or vegetable oil (conduct the test separately). 2) Rub the outer green surface of a small part of the green vegetable/chilli. 3) If the cotton turns green, it is adulterated with malachite green. Method 2: 1) Take a small part of the sample and place it on a piece of moistened white blotting paper. 2) An impression of colour on the paper indicates the use of malachite green or another low-priced artificial colour.', 'Cotton/blotting paper shows no green colour transfer.', 'Cotton/blotting paper shows a green colour impression.', 'Cotton, water/vegetable oil, white blotting paper', '/images/tests/test38_pure.jpg', '/images/tests/test38_adulterated.jpg', 10.0, 1),
(39, 'VEGETABLES', 'Green Peas', 'Detection of artificial colour on green peas', 'Artificial colour', '1) Take a little amount of green peas in a transparent glass. 2) Add water to it and mix well. 3) Let it stand for half an hour. 4) A clear separation of colour in the water indicates adulteration.', 'Water remains clear; no colour separates out.', 'Colour visibly separates and colours the water.', 'Transparent glass, water', '/images/tests/test39_pure.jpg', '/images/tests/test39_adulterated.jpg', 10.0, 1),
(40, 'VEGETABLES', 'Sweet Potato', 'Detection of rhodamine B in sweet potato', 'Rhodamine B', '1) Take a cotton ball soaked in water or vegetable oil (conduct the test separately). 2) Rub the outer red surface of the sweet potato. 3) If the cotton absorbs colour, it indicates the usage of rhodamine B for colouring the outer surface of the sweet potato.', 'Cotton does not absorb any colour.', 'Cotton absorbs a red/pink colour.', 'Cotton ball, water and vegetable oil', '/images/tests/test40_pure.jpg', '/images/tests/test40_adulterated.jpg', 10.0, 1),
(41, 'BEVERAGES', 'Coffee Powder', 'Detection of clay in coffee powder', 'Clay', '1) Add 1/2 teaspoon of coffee powder in a transparent glass of water. 2) Stir for a minute and keep it aside for 5 minutes. Observe the glass at the bottom. 3) Pure coffee powder will not leave any clay particles at the bottom. 4) If coffee powder is adulterated, clay particles will settle at the bottom.', 'No sediment at the bottom.', 'Clay particles settle at the bottom.', 'Transparent glass, water', '/images/tests/test41_pure.jpg', '/images/tests/test41_adulterated.jpg', 10.0, 1),
(42, 'BEVERAGES', 'Coffee Powder', 'Detection of chicory powder in coffee powder', 'Chicory powder', '1) Take a transparent glass of water. 2) Add a teaspoon of coffee powder. 3) Coffee powder floats over the water but chicory begins to sink.', 'Powder floats on the surface with minimal sinking.', 'Portion of the powder sinks (chicory present).', 'Transparent glass, water', '/images/tests/test42_pure.jpg', '/images/tests/test42_adulterated.jpg', 10.0, 1),
(43, 'BEVERAGES', 'Tea Leaves', 'Detection of exhausted tea in tea leaves', 'Exhausted tea / coal tar colour', 'Method 1: 1) Take a filter paper and spread a few tea leaves on it. 2) Sprinkle with water to wet the filter paper. 3) Wash the filter paper under tap water and observe the stains against light. 4) Pure tea leaves will not stain the filter paper. 5) If coal tar colour is present, it will immediately stain the filter paper. Method 2: 1) Take a small amount of tea leaves/dust and place it in the centre of a filter paper. 2) Add water drop by drop onto the heap of tea leaves/dust. 3) If the tea is adulterated with a coloured tea, the water will dissolve the added colour and leave a streak of colour on the filter paper. Method 3: 1) Spread a little slaked lime on a white porcelain tile or glass plate. 2) Sprinkle a little tea dust on the lime. 3) Red, orange or other shades of colour spreading on the lime show the presence of coal tar colour. 4) In case of genuine tea, there will be only a slight greenish yellow colour due to chlorophyll, which appears after some time.', 'Filter paper/lime shows no staining, or only a slight greenish-yellow tint after some time.', 'Filter paper/lime shows immediate red, orange or other colour staining.', 'Filter paper, water, slaked lime, white porcelain tile/glass plate', '/images/tests/test43_pure.jpg', '/images/tests/test43_adulterated.jpg', 10.0, 1),
(44, 'BEVERAGES', 'Tea Leaves', 'Detection of iron filings in tea leaves', 'Iron filings', '1) Take a small quantity of tea leaves in a glass plate. 2) Move a magnet through the tea leaves. 3) Pure tea leaves will not show any iron filings on the magnet. 4) If adulterated, iron filings will be seen on the magnet.', 'No iron filings stick to the magnet.', 'Iron filings visibly stick to the magnet.', 'Magnet, glass plate', '/images/tests/test44_pure.jpg', '/images/tests/test44_adulterated.jpg', 10.0, 1);

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
ALTER TABLE test_catalog AUTO_INCREMENT = 45;
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
