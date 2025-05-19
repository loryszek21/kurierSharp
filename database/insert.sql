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
  (
    1, 
    '123 Main St', 
    'New York', 
    'NY', 
    '10001', 
    'USA'
  );
 
 
  insert into 
    "Users" (
      "Id", 
      "Name", 
      "Surname", 
      "Email", 
      "PhoneNumber", 
      "AddressId"
    )
  values
    (
      1, 
      'John', 
      'Doe', 
      '6dIgM@example.com', 
      '+123456789', 
        1
    );
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
  (
    1, 
    '0000000001', 
    1.5, 
    1, 
    '2023-10-01 12:00:00', 
    '2023-10-05 12:00:00', 
    1, 
    1, 
    1, 
    1
  );
  



