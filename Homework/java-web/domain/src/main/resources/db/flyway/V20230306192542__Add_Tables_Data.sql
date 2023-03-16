INSERT INTO Categories (name) VALUES
                                  ('Dairy'),
                                  ('Bakery'),
                                  ('Fruits');

INSERT INTO Products (name, price, imagePath, categoryId) VALUES
                                  ('Milk', 45.9, 'https://scontent.fkbp1-1.fna.fbcdn.net/v/t39.30808-6/335923648_968751164496664_2304005216340375030_n.jpg?_nc_cat=107&ccb=1-7&_nc_sid=0debeb&_nc_ohc=fZJ5Te-2ImoAX-toBMv&_nc_ht=scontent.fkbp1-1.fna&oh=00_AfDHzgCCEATMHtUqhRjSaEiN1Wdr7xyHgq50CT2QoYGT-Q&oe=6417C19B', 1),
                                  ('Yogurt', 23.5, 'https://scontent.fkbp1-1.fna.fbcdn.net/v/t39.30808-6/335965639_697109675528691_5530034567989117488_n.jpg?_nc_cat=104&ccb=1-7&_nc_sid=0debeb&_nc_ohc=Rk6nzu8x9CAAX8YMoFb&_nc_ht=scontent.fkbp1-1.fna&oh=00_AfArk80fAiXjCYNLCgv32z9vPTapRFWySBjsHNAIg8AfrQ&oe=6416E5E4', 1),
                                  ('Ice cream', 26.0, 'https://scontent.fkbp1-1.fna.fbcdn.net/v/t39.30808-6/335934074_576019554464477_9042414683163746200_n.jpg?_nc_cat=110&ccb=1-7&_nc_sid=0debeb&_nc_ohc=3M-0f2rzzEEAX_AeSJ0&_nc_ht=scontent.fkbp1-1.fna&oh=00_AfB0JaJQMXzQnzmK3uvolKj-3ldnq-VlMIKJ8uiawKaG1g&oe=6417FFEE', 1),
                                  ('Bread', 15.4, 'https://scontent.fkbp1-1.fna.fbcdn.net/v/t39.30808-6/335942954_925074258942164_4038720601966484725_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=0debeb&_nc_ohc=ZSsHdloNLxMAX9dYqNT&_nc_ht=scontent.fkbp1-1.fna&oh=00_AfAUxrjobhRQLKwtawONXSjtfabJmFzghgNuY4A6--X1eA&oe=6416FD05', 2),
                                  ('Croissant', 31.0, 'https://scontent.fkbp1-1.fna.fbcdn.net/v/t39.30808-6/333636507_3719295765015801_8098944293888575390_n.jpg?_nc_cat=109&ccb=1-7&_nc_sid=0debeb&_nc_ohc=20TOpxcIJWgAX8541ty&_nc_ht=scontent.fkbp1-1.fna&oh=00_AfD8cE_Tq7D3YQNL_spKNhGOqa1mujVwIHMLLF3ryPqU6w&oe=64178312', 2),
                                  ('Bun', 12.5, 'https://scontent.fkbp1-1.fna.fbcdn.net/v/t39.30808-6/335927074_8958991087475995_7783132343483951056_n.jpg?_nc_cat=111&ccb=1-7&_nc_sid=0debeb&_nc_ohc=3ebpYqiFc6QAX_LJjzI&_nc_ht=scontent.fkbp1-1.fna&oh=00_AfAxteVhWv_3hsohpVWpcosH66qTxWP4rRx3CqKQGxWPwQ&oe=6417929F', 2),
                                  ('Mango', 105.0, 'https://scontent.fkbp1-1.fna.fbcdn.net/v/t39.30808-6/336051680_988058542578669_2848798781964719879_n.jpg?_nc_cat=101&ccb=1-7&_nc_sid=0debeb&_nc_ohc=I3AEZM5BrS8AX8A20Vu&_nc_ht=scontent.fkbp1-1.fna&oh=00_AfDOxQYYn46GI70002VhoDqd31YxtIU303vVLhn_y7KpdQ&oe=6418275E', 3),
                                  ('Passion fruit', 80.7, 'https://scontent.fkbp1-1.fna.fbcdn.net/v/t39.30808-6/336013240_2902282283236666_2882835861272162558_n.jpg?_nc_cat=100&ccb=1-7&_nc_sid=0debeb&_nc_ohc=ZwUj75vNpi8AX9OUBRT&_nc_oc=AQmmWGf_VkqMWgdgHGYcELBIuBDHAU3VwAUAcJFroSJnyvJJpTz9bUPRqfAioBhJFes&_nc_ht=scontent.fkbp1-1.fna&oh=00_AfD-chGQ76mxNTTKkO1st3cXV4Kv1dmK9_3LGr0wR5FeJA&oe=6417A394', 3),
                                  ('Papaya', 350.2, 'https://scontent.fkbp1-1.fna.fbcdn.net/v/t39.30808-6/335647114_2495926480570184_6531907748782445674_n.jpg?_nc_cat=103&ccb=1-7&_nc_sid=0debeb&_nc_ohc=jpHfZLNGnZAAX9NvadY&_nc_ht=scontent.fkbp1-1.fna&oh=00_AfCgqZFGUJ9I5n3QeirPX7oqw4uyNdQ1dRqL1y6cSYA9xA&oe=6417AC0E', 3);

INSERT INTO Users (id, firstName, lastName, password, email, isAdmin) VALUES
                                                                      ('d1325332-039e-472e-923f-df6c5e0e85bf', 'Mark', 'Pearce', 'Qwerty123', 'admin@gmail.com', true),
                                                                      ('5174ec9f-5725-4b78-aa68-7d37189eed58', 'John', 'Smith', 'Qwerty123', 'user@gmail.com', false);

INSERT INTO Buckets (productId, userId) VALUES
                                  (1, '5174ec9f-5725-4b78-aa68-7d37189eed58'),
                                  (4, '5174ec9f-5725-4b78-aa68-7d37189eed58');
