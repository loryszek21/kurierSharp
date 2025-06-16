insert into 
  "Addresses" (
    "Id", 
    "Street", 
    "City", 
    "Region", 
    "PostalCode", 
    "Country"
  )
values
  
(2, 'Słoneczna 8A', 'Kraków', 'Małopolskie', '30-002', 'Polska'),
(4, 'Długa 120/3', 'Poznań', 'Wielkopolskie', '60-100', 'Polska'),
(5, 'Krótka 1', 'Wrocław', 'Dolnośląskie', '50-500', 'Polska'),
(6, 'Leśna 3', 'Gdańsk', 'Pomorskie', '80-800', 'Polska'),
(7, 'Kwiatowa 15', 'Warszawa', 'Mazowieckie', '01-234', 'Polska');



insert into 
  "Users" (
    "Id", 
    "Name", 
    "Surname", 
    "Email", 
    "PhoneNumber", 
    "AddressId", 
    "Password"
  )
values
(2, 'Piotr', 'Nowak', 'piotr.nowak@example.com', '444555666', 2, 'Password456'),
(3, 'Marek', 'Zieliński', 'marek.zielinski@courier.com', '777888999', 3, 'Password789'),
(5, 'Krzysztof', 'Wiśniewski', 'k.wisniewski@courier.com', '505606707', 5, 'PasswordCourier'),
(6, 'Anna', 'Kowalska', 'anna.kowalska@example.com', '111222333', 1, 'Password123'),
(7, 'Ewa', 'Lewandowska', 'ewa.lewa@example.com', '101202303', 4, 'Password101');


insert into 
  "Packages" (
    "Id", 
    "TrackingNumber", 
    "WeightKg", 
    "Status", 
    "CreatedAt", 
    "DeliveredAt", 
    "AddressId", 
    "SenderId", 
    "RecipientId", 
    "CourierId"
  )
values
(6, 'TRK001XYZ', 2.5, 0, '2024-06-12 10:00:00', NULL, 2, 1, 2, NULL),
(7, 'TRK002ABC', 0.8, 1, '2024-06-14 12:30:00', NULL, 4, 1, 4, 3),
(3, 'TRK003DEF', 5.1, 2, '2024-06-16 08:15:00', NULL, 1, 2, 1, 3),
(4, 'TRK004GHI', 1.2, 3, '2024-06-07 14:00:00', '2024-06-10 11:00:00', 5, 4, 5, 5),
(5, 'TRK005JKL', 3.0, 0, '2024-06-15 16:45:00', NULL, 3, 2, 3, NULL);