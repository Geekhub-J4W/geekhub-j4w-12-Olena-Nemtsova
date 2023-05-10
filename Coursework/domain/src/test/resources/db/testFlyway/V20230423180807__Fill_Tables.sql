INSERT INTO Products (name, calories) VALUES
                                          ('Rice', 130), --1
                                          ('Oatmeal', 90), --2
                                          ('Buckwheat', 335), --3
                                          ('Egg', 155), --4
                                          ('Milk', 64), --5
                                          ('Banana', 89), --6
                                          ('Apple', 45), --7
                                          ('Potato', 80), --8
                                          ('Carrot', 34), --9
                                          ('Tomato', 23), --10
                                          ('Lettuce', 17), --11
                                          ('Beet', 42), --12
                                          ('Cucumber', 14), --13
                                          ('Zucchini', 23), --14
                                          ('Paprika', 26), --15
                                          ('Salmon', 172), --16
                                          ('Tuna', 115), --17
                                          ('Beef', 193), --18
                                          ('Pork', 242), --19
                                          ('Chicken', 175), --20
                                          ('Spaghetti', 293), --21
                                          ('White bread', 231), --22
                                          ('Brown bread', 207), --23
                                          ('Flour', 334), --24
                                          ('Cheese', 352), --25
                                          ('Mozzarella', 264), --26
                                          ('Feta', 264), --27
                                          ('Champignon', 22), --28
                                          ('Avocado', 195), --29
                                          ('Olive oil', 884), --30
                                          ('Butter', 717), --31
                                          ('Sunflower oil', 880), --32
                                          ('Sesame', 573), --33
                                          ('Bean noodles', 351), --34
                                          ('Yogurt', 59), --35
                                          ('Kefir', 50), --36
                                          ('Onion', 40), --37
                                          ('Garlic', 143), --38
                                          ('Water', 0), --39
                                          ('Sugar', 387),--40
                                          ('Salt', 0), --41
                                          ('Cream cheese', 342), --42
                                          ('Cabbage', 25), --43
                                          ('Vermicelli', 335), --44
                                          ('Greenery', 36), --45
                                          ('Soy sauce', 55), --46
                                          ('Parmesan', 431), --47
                                          ('Olives', 166), --48
                                          ('Peas', 95); --49

INSERT INTO Dishes (name) VALUES
                              ('Milk rice porridge'), --1
                              ('Oatmeal pancakes'),
                              ('Omelette with tomatoes'), --3
                              ('Sandwiches with avocado'),
                              ('Borsch'), --5
                              ('Cheese soup'),
                              ('Meatball soup'), --7
                              ('Buckwheat soup'),
                              ('Ramen'), --9
                              ('Caesar salad'),
                              ('Greek salad'), --11
                              ('Salad with chicken and mushrooms'),
                              ('Funchose salad'), --13
                              ('Lasagna'),
                              ('Сarbonara'), --15
                              ('Porridge with mushrooms'),
                              ('Stew with rice'), --17
                              ('Pilaf'), --18
                              ('Vinaigrette salad'), --19
                              ('Roast'); --20

INSERT INTO ProductsDishes (productQuantity, productId, dishId) VALUES
                                                                    (80, 1, 1),
                                                                    (180, 5, 1),
                                                                    (80, 39, 1),
                                                                    (15, 31, 1),
                                                                    (10, 40, 1),
                                                                    (50, 2, 2),
                                                                    (100, 4, 2),
                                                                    (100, 6, 2),
                                                                    (15, 5, 2),
                                                                    (10, 32, 2),
                                                                    (150, 4, 3),
                                                                    (100, 5, 3),
                                                                    (80, 10, 3),
                                                                    (3, 41, 3),
                                                                    (150, 23, 4),
                                                                    (80, 42, 4),
                                                                    (80, 10, 4),
                                                                    (3, 38, 4),
                                                                    (50, 29, 4),
                                                                    (65, 19, 5),
                                                                    (60, 8, 5),
                                                                    (15, 37, 5),
                                                                    (20, 9, 5),
                                                                    (50, 12, 5),
                                                                    (10, 10, 5),
                                                                    (10, 32, 5),
                                                                    (400, 39, 5),
                                                                    (3, 41, 5),
                                                                    (3, 38, 5),
                                                                    (50, 20, 6),
                                                                    (50, 8, 6),
                                                                    (15, 37, 6),
                                                                    (400, 39, 6),
                                                                    (30, 42, 6),
                                                                    (20, 9, 6),
                                                                    (3, 41, 6),
                                                                    (20, 28, 6),
                                                                    (70, 20, 7),
                                                                    (40, 8, 7),
                                                                    (15, 37, 7),
                                                                    (30, 9, 7),
                                                                    (20, 44, 7),
                                                                    (5, 32, 7),
                                                                    (3, 41, 7),
                                                                    (400, 39, 8),
                                                                    (40, 3, 8),
                                                                    (50, 8, 8),
                                                                    (25, 9, 8),
                                                                    (15, 37, 8),
                                                                    (5, 32, 8),
                                                                    (3, 41, 8),
                                                                    (10, 45, 8),
                                                                    (120, 20, 9),
                                                                    (50, 4, 9),
                                                                    (20, 46, 9),
                                                                    (80, 34, 9),
                                                                    (5, 38, 9),
                                                                    (30, 28, 9),
                                                                    (10, 45, 9),
                                                                    (3, 41, 9),
                                                                    (10, 33, 9),
                                                                    (80, 39, 9),
                                                                    (70, 11, 10),
                                                                    (100, 20, 10),
                                                                    (30, 22, 10),
                                                                    (30, 47, 10),
                                                                    (50, 4, 10),
                                                                    (3, 38, 10),
                                                                    (50, 10, 10),
                                                                    (25, 30, 10),
                                                                    (3, 41, 10),
                                                                    (85, 27, 11),
                                                                    (100, 10, 11),
                                                                    (100, 13, 11),
                                                                    (30, 37, 11),
                                                                    (25, 30, 11),
                                                                    (20, 45, 11),
                                                                    (3, 41, 11),
                                                                    (70, 48, 11),
                                                                    (90, 20, 12),
                                                                    (60, 28, 12),
                                                                    (45, 4, 12),
                                                                    (25, 25, 12),
                                                                    (30, 35, 12),
                                                                    (10, 32, 12),
                                                                    (3, 41, 12),
                                                                    (130, 34, 13),
                                                                    (50, 15, 13),
                                                                    (50, 9, 13),
                                                                    (30, 13, 13),
                                                                    (25, 46, 13),
                                                                    (5, 38, 13),
                                                                    (30, 30, 13),
                                                                    (3, 41, 13),
                                                                    (3, 40, 13),
                                                                    (10, 33, 13),
                                                                    (20, 37, 14),
                                                                    (85, 18, 14),
                                                                    (10, 45, 14),
                                                                    (60, 24, 14),
                                                                    (9, 31, 14),
                                                                    (10, 47, 14),
                                                                    (7, 30, 14),
                                                                    (85, 10, 14),
                                                                    (65, 5, 14),
                                                                    (20, 4, 14),
                                                                    (3, 41, 14),
                                                                    (130, 21, 15),
                                                                    (40, 19, 15),
                                                                    (25, 37, 15),
                                                                    (25, 47, 15),
                                                                    (50, 4, 15),
                                                                    (5, 38, 15),
                                                                    (10, 30, 15),
                                                                    (10, 45, 15),
                                                                    (10, 31, 15),
                                                                    (3, 41, 15),
                                                                    (70, 3, 16),
                                                                    (10, 31, 16),
                                                                    (10, 32, 16),
                                                                    (50, 28, 16),
                                                                    (3, 41, 16),
                                                                    (80, 1, 17),
                                                                    (50, 43, 17),
                                                                    (50, 9, 17),
                                                                    (50, 14, 17),
                                                                    (20, 37, 17),
                                                                    (50, 15, 17),
                                                                    (5, 32, 17),
                                                                    (3, 41, 17),
                                                                    (60, 39, 17),
                                                                    (80, 19, 18),
                                                                    (30, 37, 18),
                                                                    (35, 9, 18),
                                                                    (5, 38, 18),
                                                                    (100, 1, 18),
                                                                    (5, 32, 18),
                                                                    (3, 41, 18),
                                                                    (60, 39, 18),
                                                                    (80, 12, 19),
                                                                    (50, 9, 19),
                                                                    (80, 8, 19),
                                                                    (40, 49, 19),
                                                                    (40, 13, 19),
                                                                    (20, 37, 19),
                                                                    (5, 32, 19),
                                                                    (3, 41, 19),
                                                                    (100, 18, 20),
                                                                    (150, 8, 20),
                                                                    (30, 9, 20),
                                                                    (20, 37, 20),
                                                                    (80, 39, 20),
                                                                    (3, 41, 20);

INSERT INTO Users (firstName, lastName, password, email, role) VALUES
                                                                   ('Sam', 'Brown', 'pass', 'super_admin@gmail.com', 'SUPER_ADMIN'),
                                                                   ('Mark', 'Pearce', 'pass', 'admin@gmail.com', 'ADMIN'),
                                                                   ('John', 'Smith', 'pass', 'user@gmail.com', 'USER');

INSERT INTO UsersParameters(age, weight, height, gender, activityLevel, bodyType, aim, userId) VALUES
                                                                                                    (24, 75, 190, 'MALE', 'LOW', 'ASTHENIC', 'NONE', 3);

INSERT INTO UsersAllergicProducts(userId, productId) VALUES
                                                         (3, 40);

INSERT INTO Chats(userId, adminId) VALUES
                                       (3, 2);

INSERT INTO Chats(userId) VALUES
                              (2);

INSERT INTO Messages(text, chatId, senderId) VALUES
                                                 ('message #1', 1, 3),
                                                 ('message #2', 1, 2);























