-- Eerst een voorbeeldcourier toevoegen
INSERT INTO courier (id, first_name, last_name, email, phone_number, address, iban)
VALUES
    ('fc60bf4a-cf31-4dd2-b013-31505ba04777', 'Jan', 'Jansen', 'jan.jansen@example.com', '0470123456', 'Straat 1, Stad', 'BE12345678901234'),
    ('be84759f-1718-436e-8483-0deb492e5226', 'Hugo', 'Dor', 'hugo.dor@example.com', '0495658992', 'Straat 2, Wilrijk', 'BE343456789876434'),
('11111111-3333-3333-3333-111111111111', 'Cian', 'Van acker', 'Cian.Van.acker@example.com', '0495658992', 'Straat 2, Wilrijk', 'BE343456789876434'),
('11111111-4444-4444-4444-111111111111', 'Lars', 'Peleman', 'lars.Peleman@example.com', '0495658992', 'Straat 2, Wilrijk', 'BE343456789876434');

-- 10 deliveries toevoegen, 9 AVAILABLE, 1 met courier toegewezen
INSERT INTO delivery (id, order_id, courier_id, delivery_status, start_delivery, end_delivery)
VALUES
    ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '10000001-0000-0000-0000-000000000001', NULL, 'AVAILABLE', NULL, NULL),
    ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '10000002-0000-0000-0000-000000000002', NULL, 'AVAILABLE', NULL, NULL),
    ('cccccccc-cccc-cccc-cccc-cccccccccccc', '10000003-0000-0000-0000-000000000003', NULL, 'AVAILABLE', NULL, NULL),
    ('dddddddd-dddd-dddd-dddd-dddddddddddd', '10000004-0000-0000-0000-000000000004', NULL, 'AVAILABLE', NULL, NULL),
    ('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '10000005-0000-0000-0000-000000000005', NULL, 'AVAILABLE', NULL, NULL),
    ('ffffffff-ffff-ffff-ffff-ffffffffffff', '10000006-0000-0000-0000-000000000006', NULL, 'AVAILABLE', NULL, NULL),
    ('11111111-2222-3333-4444-555555555555', '10000007-0000-0000-0000-000000000007', NULL, 'AVAILABLE', NULL, NULL),
    ('22222222-3333-4444-5555-666666666666', '10000008-0000-0000-0000-000000000008', '11111111-3333-3333-3333-111111111111', 'CANCELLED', NOW(), NOW()),
    ('33333333-4444-5555-6666-777777777777', '10000009-0000-0000-0000-000000000009', '11111111-2222-2222-2222-111111111111', 'DELIVERED', NOW(), NOW()),
    ('44444444-5555-6666-7777-888888888888', '10000010-0000-0000-0000-000000000010', '11111111-1111-1111-1111-111111111111', 'AVAILABLE', NOW(), NULL);
