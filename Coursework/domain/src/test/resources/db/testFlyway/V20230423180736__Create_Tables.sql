CREATE TABLE IF NOT EXISTS Products(
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    calories INT NOT NULL
);

CREATE TABLE IF NOT EXISTS Dishes(
    id SERIAL PRIMARY KEY,
    name VARCHAR NOT NULL,
    image bytea
);

CREATE TABLE IF NOT EXISTS ProductsDishes(
    productQuantity INT NOT NULL,
    productId INT NOT NULL,
    dishId INT NOT NULL,
    FOREIGN KEY (productId) REFERENCES Products(id) ON DELETE CASCADE,
    FOREIGN KEY (dishId) REFERENCES Dishes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Users(
    id SERIAL PRIMARY KEY,
    firstName VARCHAR,
    lastName VARCHAR,
    password VARCHAR,
    email VARCHAR UNIQUE,
    role VARCHAR NOT NULL
);

CREATE TABLE IF NOT EXISTS UsersParameters(
    age INT NOT NULL,
    weight INT NOT NULL,
    height INT NOT NULL,
    gender VARCHAR NOT NULL,
    activityLevel VARCHAR NOT NULL,
    bodyType VARCHAR NOT NULL,
    aim VARCHAR NOT NULL,
    userId INT UNIQUE NOT NULL,
    FOREIGN KEY (userId) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS UsersAllergicProducts(
    userId INT NOT NULL,
    productId INT NOT NULL,
    FOREIGN KEY (userId) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (productId) REFERENCES Products(id) ON DELETE CASCADE
);

