
CREATE TABLE IF NOT EXISTS Categories(
    id SERIAL PRIMARY KEY,
    name VARCHAR
);

CREATE TABLE IF NOT EXISTS Products(
    id SERIAL PRIMARY KEY,
    name VARCHAR,
    price NUMERIC CHECK(price>=0.00),
    imagePath VARCHAR,
    quantity INT CHECK (quantity>=0),
    categoryId INT NOT NULL,
    FOREIGN KEY (categoryId) REFERENCES Categories(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Users(
    id VARCHAR PRIMARY KEY,
    firstName VARCHAR,
    lastName VARCHAR,
    password VARCHAR,
    email VARCHAR UNIQUE,
    isAdmin BOOLEAN
);

CREATE TABLE IF NOT EXISTS Buckets(
    id SERIAL PRIMARY KEY,
    productId INT NOT NULL,
    userId VARCHAR NOT NULL,
    FOREIGN KEY (productId) REFERENCES Products(id) ON DELETE CASCADE,
    FOREIGN KEY (userId) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Orders(
    id SERIAL PRIMARY KEY,
    date TIMESTAMP NOT NULL DEFAULT NOW() CHECK (date<=NOW()),
    totalPrice NUMERIC CHECK(totalPrice>=0.00),
    status VARCHAR,
    userId VARCHAR NOT NULL,
    FOREIGN KEY (userId) REFERENCES Users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS ProductsOrders(
    id SERIAL PRIMARY KEY,
    productId INT NOT NULL,
    orderID INT NOT NULL,
    FOREIGN KEY (productId) REFERENCES Products(id) ON DELETE CASCADE,
    FOREIGN KEY (orderId) REFERENCES Orders(id) ON DELETE CASCADE
);




