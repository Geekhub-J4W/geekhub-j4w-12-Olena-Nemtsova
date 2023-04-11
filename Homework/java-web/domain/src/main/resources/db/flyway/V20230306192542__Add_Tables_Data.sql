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

INSERT INTO Users (firstName, lastName, password, email, role) VALUES
                                                                      ('Bob', 'Jones', '$2a$12$C6sRmHJySXxnYx/NYMlHyemPlhH7BJ7yi9RvudVLudfI0QnUFDA0y', 'superAdmin@gmail.com', 'SUPER_ADMIN'),
                                                                      ('Jack', 'Brown', '$2a$12$C6sRmHJySXxnYx/NYMlHyemPlhH7BJ7yi9RvudVLudfI0QnUFDA0y', 'admin@gmail.com', 'ADMIN'),
                                                                      ('Mark', 'Pearce', '$2a$12$C6sRmHJySXxnYx/NYMlHyemPlhH7BJ7yi9RvudVLudfI0QnUFDA0y', 'seller@gmail.com', 'SELLER'),
                                                                      ('John', 'Smith', '$2a$12$C6sRmHJySXxnYx/NYMlHyemPlhH7BJ7yi9RvudVLudfI0QnUFDA0y', 'user@gmail.com', 'USER');

INSERT INTO Buckets (productId, userId) VALUES
                                  (1, 4),
                                  (4, 4);
