INSERT INTO Categories (name) VALUES
                                  ('Dairy'),
                                  ('Bakery'),
                                  ('Fruits');

INSERT INTO Products (name, price, imagePath, categoryId) VALUES
                                  ('Milk', 45.9, 'https://www.freepnglogos.com/uploads/milk/milk-carton-symbol-drinks-talksense-2.png', 1),
                                  ('Yogurt', 23.5, 'https://static.vecteezy.com/system/resources/previews/012/025/561/original/the-cherry-yogurt-png.png', 1),
                                  ('Ice cream', 26.0, 'https://static.vecteezy.com/system/resources/previews/009/366/966/original/cute-ice-cream-design-illustration-png.png', 1),
                                  ('Bread', 15.4, 'https://png.pngtree.com/png-clipart/20220706/ourmid/pngtree-cartoon-long-bread-png-png-image_5723302.png', 2),
                                  ('Croissant', 31.0, 'https://creazilla-store.fra1.digitaloceanspaces.com/cliparts/22586/croissant-clipart-xl.png', 2),
                                  ('Bun', 12.5, 'https://www.shareicon.net/download/2017/04/22/885122_food_512x512.png', 2),
                                  ('Mango', 105.0, 'https://png.pngtree.com/png-vector/20220912/ourmid/pngtree-mango-cartoon-fruit-png-image_6172613.png', 3),
                                  ('Passion fruit', 80.7, 'https://cdn-icons-png.flaticon.com/512/6866/6866572.png', 3),
                                  ('Papaya', 350.2, 'https://www.pngmart.com/files/17/Half-Papaya-PNG-Image.png', 3);

INSERT INTO Users (id, firstName, lastName, password, email, isAdmin) VALUES
                                                                      ('d1325332-039e-472e-923f-df6c5e0e85bf', 'Mark', 'Pearce', 'Qwerty123', 'admin@gmail.com', true),
                                                                      ('5174ec9f-5725-4b78-aa68-7d37189eed58', 'John', 'Smith', 'Qwerty123', 'user@gmail.com', false);

INSERT INTO Buckets (productId, userId) VALUES
                                  (1, '5174ec9f-5725-4b78-aa68-7d37189eed58'),
                                  (4, '5174ec9f-5725-4b78-aa68-7d37189eed58');
