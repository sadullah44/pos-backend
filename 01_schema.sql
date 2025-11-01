-- I. Kullanıcı Tabloları

-- 1. Roles Tablosu Oluşturma
CREATE TABLE Roles (
    roleID SERIAL PRIMARY KEY,
    roleName VARCHAR(50) UNIQUE NOT NULL
);

-- 2. Users Tablosu Oluşturma
CREATE TABLE Users (
    userID SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    passwordHash VARCHAR(255) NOT NULL,
    fullName VARCHAR(100) NOT NULL,
    roleID INTEGER REFERENCES Roles(roleID) ON DELETE RESTRICT, -- Foreign Key
    isActive BOOLEAN DEFAULT TRUE NOT NULL
);

-- II. Kategori ve Ürün Tabloları

-- 1. Categories Tablosu Oluşturma
CREATE TABLE Categories (
    categoryID SERIAL PRIMARY KEY,
    categoryName VARCHAR(100) UNIQUE NOT NULL,
    sortOrder INTEGER DEFAULT 0 NOT NULL
);

-- 2. Products Tablosu Oluşturma
CREATE TABLE Products (
    productID SERIAL PRIMARY KEY,
    productName VARCHAR(100) UNIQUE NOT NULL,
    basePrice DECIMAL(10, 2) NOT NULL CHECK (basePrice >= 0),
    categoryID INTEGER REFERENCES Categories(categoryID) ON DELETE RESTRICT, -- Foreign Key
    isKitchenItem BOOLEAN DEFAULT TRUE NOT NULL,
    isAvailable BOOLEAN DEFAULT TRUE NOT NULL
);