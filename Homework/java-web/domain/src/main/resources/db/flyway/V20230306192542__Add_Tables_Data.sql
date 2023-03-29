INSERT INTO Categories (name) VALUES
                                  ('Dairy'),
                                  ('Bakery'),
                                  ('Fruits');

INSERT INTO Products (name, price, categoryId, quantity) VALUES
                                  ('Milk', 45.9, 1, 10),
                                  ('Yogurt', 23.5, 1, 8),
                                  ('Ice cream', 26.0, 1, 6),
                                  ('Bread', 15.4, 2, 7),
                                  ('Croissant', 31.0, 2, 8),
                                  ('Bun', 12.5, 2, 0),
                                  ('Mango', 105.0, 3, 12),
                                  ('Passion fruit', 80.7, 3, 9),
                                  ('Papaya', 350.2, 3, 4);

INSERT INTO Users (id, firstName, lastName, password, email, isAdmin) VALUES
                                                                      ('d1325332-039e-472e-923f-df6c5e0e85bf', 'Mark', 'Pearce', 'Qwerty123', 'admin@gmail.com', true),
                                                                      ('5174ec9f-5725-4b78-aa68-7d37189eed58', 'John', 'Smith', 'Qwerty123', 'user@gmail.com', false);

INSERT INTO Buckets (productId, userId) VALUES
                                  (1, '5174ec9f-5725-4b78-aa68-7d37189eed58'),
                                  (4, '5174ec9f-5725-4b78-aa68-7d37189eed58');
