-- I. Kullanıcı Tabloları Verileri

-- 3. İlk Rol Verilerini Ekle
INSERT INTO Roles (roleName) VALUES
('Admin'),
('Garson'),
('Kasiyer');

-- Test amaçlı kullanıcı ekleme (roleID 1, 2, 3 varsayılmıştır)
INSERT INTO Users (username, passwordHash, fullName, roleID, isActive) VALUES
('adminuser', 'hash_admin123', 'Ayşe Yılmaz', 1, TRUE),
('garson01', 'hash_garson123', 'Mehmet Demir', 2, TRUE),
('kasiyer02', 'hash_kasiyer123', 'Zeynep Kaya', 3, TRUE),
('deneme_pasif', 'hash_deneme', 'Pasif Kullanıcı', 2, FALSE);

-- II. Kategori ve Ürün Tabloları Verileri

-- 3. İlk Kategori Verilerini Ekle
INSERT INTO Categories (categoryName, sortOrder) VALUES
('İçecekler', 10),
('Ana Yemekler', 20),
('Tatlılar', 30),
('Salatalar', 40);

-- Ürün Verilerini Ekle (categoryID'ler 1, 2, 3, 4 sırasıyla atanmıştır)
INSERT INTO Products (productName, basePrice, categoryID, isKitchenItem, isAvailable) VALUES
-- İçecekler (categoryID: 1)
('Kola (Kutu)', 25.00, 1, FALSE, TRUE),
('Filtre Kahve', 35.50, 1, FALSE, TRUE),

-- Ana Yemekler (categoryID: 2)
('Izgara Köfte', 120.00, 2, TRUE, TRUE),
('Tavuk Sote', 95.00, 2, TRUE, TRUE),

-- Tatlılar (categoryID: 3)
('Sütlaç', 45.00, 3, TRUE, TRUE),
('Kazandibi', 40.00, 3, TRUE, TRUE),
('Çikolatalı Sufle (Satışta Değil)', 60.00, 3, TRUE, FALSE),

-- Salatalar (categoryID: 4)
('Mevsim Salata', 55.00, 4, TRUE, TRUE);