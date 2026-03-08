-- Tạo database nếu chưa tồn tại
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'MusicShop')
BEGIN
    CREATE DATABASE MusicShop;
END;
GO

USE MusicShop;
GO

-- ROLES TABLE
CREATE TABLE Roles (
    role_id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(50) UNIQUE NOT NULL,
    description NVARCHAR(255)
);
GO

-- USERS TABLE
CREATE TABLE Users (
    user_id INT PRIMARY KEY IDENTITY(1,1),
    full_name NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) UNIQUE NOT NULL,
    password NVARCHAR(256) NOT NULL,
    phone NVARCHAR(20),
    role_id INT DEFAULT 2,
    created_at DATETIME DEFAULT GETDATE(),
    gender INT DEFAULT NULL CHECK (gender IN (1,2,3)), -- 1: Nam, 2: Nữ, 3: Khác
    birthdate DATE DEFAULT '1990-01-01',
    image_url NVARCHAR(255) DEFAULT 'default.png',
    is_active BIT DEFAULT 1,
    account NVARCHAR(100) UNIQUE NOT NULL,
    FOREIGN KEY (role_id) REFERENCES Roles(role_id)
);
GO

-- ADDRESS TABLE
CREATE TABLE Address (
    address_id INT IDENTITY(1,1) PRIMARY KEY,
    user_id INT NOT NULL,
    street NVARCHAR(255) NOT NULL,
    ward NVARCHAR(100),
    district NVARCHAR(100),
    city NVARCHAR(100) NOT NULL,
    type INT DEFAULT 1, --1 home, 2 office, 3 other
	is_deleted BIT NOT NULL DEFAULT 0,
    is_default BIT DEFAULT 0,
    receiver_name NVARCHAR(255),
    receiver_phone NVARCHAR(20),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);
GO

-- CATEGORIES TABLE
CREATE TABLE Categories (
    category_id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) UNIQUE NOT NULL,
    description NVARCHAR(MAX)
);
GO

-- SUBCATEGORIES TABLE
CREATE TABLE Subcategories (
    subcategory_id INT PRIMARY KEY IDENTITY(1,1),
    name NVARCHAR(100) NOT NULL,
    category_id INT,
    FOREIGN KEY (category_id) REFERENCES Categories(category_id)
);
GO

-- BRANDS TABLE
CREATE TABLE Brands (
    brand_id INT PRIMARY KEY IDENTITY(1,1),
    brand_name NVARCHAR(100) NOT NULL
);
GO

-- DISCOUNTS TABLE
CREATE TABLE Discounts (
    discount_id INT PRIMARY KEY IDENTITY(1,1),
    code NVARCHAR(50) UNIQUE NOT NULL,
    description NVARCHAR(MAX),
    discount_type INT DEFAULT 2 CHECK (discount_type IN (1, 2)), -- 1: %, 2: fixed
    discount_value DECIMAL(15,3) CHECK (discount_value >= 0),
    start_date DATE,
    end_date DATE,
    usage_limit INT,
    used_count INT DEFAULT 0,
    is_active BIT DEFAULT 1
);
GO

-- PRODUCTS TABLE
CREATE TABLE Products (
    product_id INT PRIMARY KEY IDENTITY(1,1),
    discount_id INT,
    name NVARCHAR(150) NOT NULL,
    description NVARCHAR(MAX),
    price DECIMAL(15,3) NOT NULL,
    stock_quantity INT CHECK (stock_quantity >= 0),
    category_id INT,
    brand_id INT,
    image_url NVARCHAR(MAX),
    created_at DATETIME DEFAULT GETDATE(),
    sold_quantity INT DEFAULT 0,
    is_active BIT DEFAULT 1,
	made_in NVARCHAR(100),
	manufacturing_year INT,
	material NVARCHAR(100),
    FOREIGN KEY (category_id) REFERENCES Categories(category_id),
    FOREIGN KEY (discount_id) REFERENCES Discounts(discount_id),
    FOREIGN KEY (brand_id) REFERENCES Brands(brand_id)
);
GO

-- PRODUCT IMAGES TABLE
CREATE TABLE ProductImages (
    image_id INT PRIMARY KEY IDENTITY(1,1),
    product_id INT,
    image_url NVARCHAR(MAX) NOT NULL,
    caption NVARCHAR(255),
    is_primary BIT DEFAULT 0,
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE
);
GO


-- PAYMENTS TABLE
CREATE TABLE Payments (
    payment_id INT PRIMARY KEY IDENTITY(1,1),
    payment_method INT DEFAULT 2 CHECK (payment_method IN (1, 2)) -- 1 credit card, 2 money  
);
GO

-- ORDERS TABLE
CREATE TABLE Orders (
    order_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT,
    order_date DATETIME DEFAULT GETDATE(),
    status INT DEFAULT 1 CHECK (status BETWEEN 1 AND 5),
    total_amount DECIMAL(15,3) NOT NULL,
	is_return BIT DEFAULT 0,
    discount_id INT,
    address_id INT,
	payment_id INT NOT NULL,
	shipped_date DATETIME,
    estimated_delivery DATETIME,
	is_review INT DEFAULT 0,
    FOREIGN KEY (address_id) REFERENCES Address(address_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (discount_id) REFERENCES Discounts(discount_id),
	FOREIGN KEY (payment_id) REFERENCES Payments(payment_id)
);
GO

CREATE TABLE ReturnOrder (
	return_id INT PRIMARY KEY IDENTITY(1,1),
	order_id INT NOT NULL,
	user_id INT NOT NULL,
	reason NVARCHAR(150) NOT NULL,
	return_date DATETIME DEFAULT GETDATE(),
	return_status INT DEFAULT 1, -- 1: pending, 2: accept, 3: reject
	FOREIGN KEY (order_id) REFERENCES Orders(order_id),
    FOREIGN KEY (user_id) REFERENCES Users(user_id)
);
GO

-- ORDER DETAILS TABLE
CREATE TABLE OrderDetails (
    order_detail_id INT PRIMARY KEY IDENTITY(1,1),
    order_id INT,
    product_id INT,
	price DECIMAL(15,3) NOT NULL,
	quantity INT DEFAULT 1 NOT NULL,
    FOREIGN KEY (order_id) REFERENCES Orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(product_id)
);
GO

-- CART TABLE
CREATE TABLE Carts (
    cart_id INT PRIMARY KEY IDENTITY(1,1),
    user_id INT,
    product_id INT,
    quantity INT NOT NULL,
    added_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE,
    CONSTRAINT unique_cart_user_product UNIQUE (user_id, product_id)
);
GO

-- REVIEWS TABLE
CREATE TABLE Reviews (
    review_id INT PRIMARY KEY IDENTITY(1,1),
    product_id INT,
    user_id INT,
    rating INT CHECK (rating BETWEEN 1 AND 5),
    comment NVARCHAR(MAX),
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (product_id) REFERENCES Products(product_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);
GO

-- DISCOUNT USERS TABLE
CREATE TABLE DiscountUsers (
    id INT PRIMARY KEY IDENTITY(1,1),
    discount_id INT,
    user_id INT,
    used_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (discount_id) REFERENCES Discounts(discount_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES Users(user_id) ON DELETE CASCADE
);
GO


-- INDEXES
CREATE INDEX idx_users_email ON Users(email);
CREATE INDEX idx_discounts_code ON Discounts(code);
CREATE INDEX idx_products_category_id ON Products(category_id);
CREATE INDEX idx_products_brand_id ON Products(brand_id);
CREATE INDEX idx_orderdetails_order_id ON OrderDetails(order_id);
CREATE INDEX idx_productimages_product_id ON ProductImages(product_id);
CREATE INDEX idx_orders_user_id ON Orders(user_id);
CREATE INDEX idx_orderdetails_product_id ON OrderDetails(product_id);
CREATE INDEX idx_carts_product_id ON Carts(product_id);
CREATE INDEX idx_reviews_product_id ON Reviews(product_id);
CREATE INDEX idx_address_user_id ON Address(user_id);
GO

-- Procedure
CREATE OR ALTER PROCEDURE sp_CreateOrder
    @user_id INT,
    @address_id INT,
    @product_id INT,
    @quantity INT,
    @discount_id INT = NULL,
    @payment_id INT,
    @order_id INT OUTPUT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        -- Bắt đầu giao dịch
        BEGIN TRANSACTION;
        SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;

        -- Kiểm tra user_id hợp lệ
        IF NOT EXISTS (SELECT 1 FROM Users WHERE user_id = @user_id AND is_active = 1)
            THROW 50001, 'Invalid or inactive user', 1;

        -- Kiểm tra address_id hợp lệ
        IF NOT EXISTS (SELECT 1 FROM Address WHERE address_id = @address_id AND user_id = @user_id)
            THROW 50002, 'Invalid address for this user', 1;

        -- Kiểm tra product_id và lấy giá, số lượng tồn kho
        DECLARE @product_price DECIMAL(15,3); -- Sửa kiểu dữ liệu để khớp với bảng Products
        DECLARE @stock INT;
        SELECT @product_price = price, @stock = stock_quantity
        FROM Products WITH (UPDLOCK)
        WHERE product_id = @product_id AND is_active = 1;

        IF @product_price IS NULL
            THROW 50003, 'Product not found or inactive', 1;

        IF @stock < @quantity
            THROW 50004, 'Insufficient stock', 1;

        -- Tính tổng giá sản phẩm
        DECLARE @product_total_price DECIMAL(15,3) = @product_price * @quantity;
        DECLARE @discount_amount DECIMAL(15,3) = 0;

        -- Kiểm tra và áp dụng mã giảm giá (nếu có)
        IF @discount_id IS NOT NULL
        BEGIN
            DECLARE @discount_type INT;
            DECLARE @discount_value DECIMAL(15,3);
            DECLARE @start_date DATE;
            DECLARE @end_date DATE;
            DECLARE @is_active BIT;
            DECLARE @usage_limit INT;
            DECLARE @used_count INT;

            SELECT @discount_type = discount_type, 
                   @discount_value = discount_value, 
                   @start_date = start_date, 
                   @end_date = end_date, 
                   @is_active = is_active,
                   @usage_limit = usage_limit, 
                   @used_count = used_count
            FROM Discounts
            WHERE discount_id = @discount_id;

            IF @discount_type IS NULL
                THROW 50005, 'Invalid or expired discount', 1;

            -- Sửa điều kiện kiểm tra mã giảm giá
            IF @is_active = 0 OR 
               @start_date > GETDATE() OR 
               @end_date < GETDATE() OR 
               (@usage_limit > 0 AND @used_count >= @usage_limit)
                THROW 50005, 'Invalid or expired discount', 1;

            -- Tính giá trị giảm giá
            IF @discount_type = 1 -- Percent
                SET @discount_amount = @product_total_price * (@discount_value / 100.0);
            ELSE -- Fixed
                SET @discount_amount = @discount_value;
        END;

        -- Tính tổng giá trị đơn hàng
        DECLARE @total_amount DECIMAL(15,3) = @product_total_price - @discount_amount;

        -- Chèn vào bảng Orders
        INSERT INTO Orders (user_id, address_id, discount_id, payment_id, total_amount, order_date, status)
        VALUES (@user_id, @address_id, @discount_id, @payment_id, @total_amount, GETDATE(), 1);

        -- Lấy order_id vừa tạo
        SET @order_id = SCOPE_IDENTITY();

        -- Chèn vào bảng OrderDetails
        INSERT INTO OrderDetails (order_id, product_id, quantity, price)
        VALUES (@order_id, @product_id, @quantity, @product_price);

        -- Cập nhật số lượng tồn kho
        UPDATE Products
        SET stock_quantity = stock_quantity - @quantity
        WHERE product_id = @product_id;

        -- Commit giao dịch
        COMMIT TRANSACTION;
    END TRY
    BEGIN CATCH
        -- Rollback giao dịch nếu có lỗi
        IF @@TRANCOUNT > 0
            ROLLBACK TRANSACTION;

        -- Ném lỗi với thông tin chi tiết
        DECLARE @ErrorMessage NVARCHAR(4000) = ERROR_MESSAGE();
        DECLARE @ErrorSeverity INT = ERROR_SEVERITY();
        DECLARE @ErrorState INT = ERROR_STATE();

        THROW @ErrorSeverity, @ErrorMessage, @ErrorState;
    END CATCH;
END;
GO

CREATE OR ALTER TRIGGER tr_UpdateDiscountUsage
ON Orders
AFTER INSERT
AS
BEGIN
    SET NOCOUNT ON;
    BEGIN TRY
        -- Update used_count in Discounts
        UPDATE d
        SET d.used_count = d.used_count + 1
        FROM Discounts d
        INNER JOIN inserted i ON d.discount_id = i.discount_id
        WHERE i.discount_id IS NOT NULL;

        -- Insert into DiscountUsers
        INSERT INTO DiscountUsers (discount_id, user_id, used_at)
        SELECT i.discount_id, i.user_id, GETDATE()
        FROM inserted i
        WHERE i.discount_id IS NOT NULL;
    END TRY
    BEGIN CATCH
        THROW;
    END CATCH;
END;


-- Role
INSERT INTO Roles (name, description) VALUES
('Admin', 'Administrator role with full permissions'),
('User', 'Registered customer with standard permissions');

-- USERS
INSERT INTO Users (full_name, email, password, phone, role_id, gender, birthdate, account)
VALUES 
('Admin User', 'admin@example.com', 'admin123', 'e10adc3949ba59abbe56e057f20f883e', 1, 1, '1985-01-01', 'adminuser'),
('Rem', 'rem@gmail.com', '93a6be90e81a902c15ab3103e2990ecb', '0987654321', 2, 1, '1995-06-15', 'remcute');  -- user_id = 2

-- Category
INSERT INTO Categories (name, description) VALUES
('Guitar', 'All types of guitars including acoustic, electric, bass, etc.'),
('Piano', 'Digital, acoustic and grand pianos.'),
('Violin', 'Various violins for all skill levels.');


-- Brand Guitar
INSERT INTO Brands (brand_name) VALUES
('Fender'),
('Gibson'),
('Yamaha'),
('Ibanez'),
('Epiphone'),
('Taylor'),
('Martin'),
('Gretsch'),
('Jackson'),
('ESP');

-- Brand Piano
INSERT INTO Brands (brand_name) VALUES
('Yamaha'),
('Kawai'),
('Steinway & Sons'),
('Casio'),
('Roland'),
('Korg'),
('Bösendorfer'),
('Samick'),
('Petrof'),
('Young Chang');

-- Brand Violin 
INSERT INTO Brands (brand_name) VALUES
('Stentor'),
('Cremona'),
('Yamaha'),
('Fiddlerman'),
('Cecilio'),
('Eastman'),
('D Z Strad'),
('Scott Cao'),
('Gliga'),
('Knilling');

-- Subcategory Guitar
INSERT INTO Subcategories (name, category_id) VALUES
('Acoustic', 1),
('Electric', 1),
('Classic', 1),
('Bass', 1);

-- Subcategry Piano
INSERT INTO Subcategories (name, category_id) VALUES
('Grand Piano', 2),
('Upright Piano', 2),
('Digital Piano', 2),
('Keyboard', 2);


-- Subcategory Violin
INSERT INTO Subcategories (name, category_id) VALUES
('Acoustic', 3),
('Electric', 3),
('Semi-electric', 3);

-- Discount
INSERT INTO Discounts (code, description, discount_type, discount_value, start_date, end_date, usage_limit)
VALUES
('WELCOME10', N'10% off for new customers', 1, 10.0, '2025-01-01', '2026-01-01', 100),
('SUMMER50', N'Giảm 50k cho đơn hàng trên 500k', 2, 50000, '2025-06-01', '2025-08-31', 200),
('VIP20', N'20% for VIP customers', 1, 20.0, '2025-01-01', '2026-12-31', 50);

-- Payment
INSERT INTO Payments (payment_method) VALUES
(1), -- Credit Card
(2); -- Cash


-- Product
-- Insert 50 Guitar products
INSERT INTO Products (name, description, price, stock_quantity, category_id, brand_id, image_url, made_in, manufacturing_year, material, discount_id)
VALUES
(N'Fender Stratocaster Standard', N'Classic electric guitar with maple neck', 15000000.000, 50, 1, 1, 'guitar_1.jpg', N'USA', 2022, N'Maple', NULL),
(N'Gibson Les Paul Classic', N'Iconic electric guitar with rosewood fretboard', 25000000.000, 30, 1, 2, 'guitar_2.jpg', N'USA', 2021, N'Rosewood', NULL),
(N'Yamaha FG800 Acoustic', N'Acoustic guitar for beginners', 6000000.000, 80, 1, 3, 'guitar_3.jpg', N'Japan', 2023, N'Spruce', NULL),
(N'Ibanez RG550', N'Electric guitar for rock enthusiasts', 12000000.000, 40, 1, 4, 'guitar_4.jpg', N'Japan', 2020, N'Maple', NULL),
(N'Epiphone SG Standard', N'Affordable electric guitar with mahogany body', 8000000.000, 60, 1, 5, 'guitar_5.jpg', N'China', 2022, N'Mahogany', NULL),
(N'Taylor 214ce', N'Premium acoustic-electric guitar', 22000000.000, 25, 1, 6, 'guitar_6.jpg', N'USA', 2023, N'Rosewood', NULL),
(N'Martin D-28', N'High-end acoustic guitar', 35000000.000, 20, 1, 7, 'guitar_7.jpg', N'USA', 2021, N'Spruce', NULL),
(N'Gretsch G2622 Streamliner', N'Semi-hollow electric guitar', 14000000.000, 35, 1, 8, 'guitar_8.jpg', N'China', 2022, N'Maple', NULL),
(N'Jackson Soloist SL1', N'High-performance electric guitar', 18000000.000, 30, 1, 9, 'guitar_9.jpg', N'USA', 2020, N'Maple', NULL),
(N'ESP LTD EC-256', N'Electric guitar with versatile tones', 10000000.000, 45, 1, 10, 'guitar_10.jpg', N'China', 2023, N'Mahogany', NULL),
(N'Fender Telecaster American', N'Classic telecaster with ash body', 20000000.000, 40, 1, 1, 'guitar_11.jpg', N'USA', 2022, N'Ash', NULL),
(N'Gibson SG Special', N'Electric guitar with dual humbuckers', 17000000.000, 50, 1, 2, 'guitar_12.jpg', N'USA', 2021, N'Mahogany', NULL),
(N'Yamaha Pacifica 112V', N'Versatile electric guitar', 7000000.000, 70, 1, 3, 'guitar_13.jpg', N'Japan', 2023, N'Alder', NULL),
(N'Ibanez GRX70QA', N'Budget-friendly electric guitar', 5500000.000, 90, 1, 4, 'guitar_14.jpg', N'China', 2022, N'Poplar', NULL),
(N'Epiphone Les Paul Studio', N'Electric guitar with classic design', 9000000.000, 60, 1, 5, 'guitar_15.jpg', N'China', 2021, N'Mahogany', NULL),
(N'Taylor GS Mini', N'Compact acoustic guitar', 12000000.000, 50, 1, 6, 'guitar_16.jpg', N'USA', 2023, N'Spruce', NULL),
(N'Martin OM-28', N'Orchestral model acoustic guitar', 30000000.000, 20, 1, 7, 'guitar_17.jpg', N'USA', 2022, N'Rosewood', NULL),
(N'Gretsch G5420T', N'Hollow body electric guitar', 16000000.000, 30, 1, 8, 'guitar_18.jpg', N'China', 2021, N'Maple', NULL),
(N'Jackson Dinky JS32', N'High-speed electric guitar', 11000000.000, 45, 1, 9, 'guitar_19.jpg', N'China', 2023, N'Poplar', NULL),
(N'ESP LTD Viper-50', N'Electric guitar for metal players', 8500000.000, 60, 1, 10, 'guitar_20.jpg', N'China', 2022, N'Mahogany', NULL),
(N'Fender Player Stratocaster', N'Versatile electric guitar', 14000000.000, 50, 1, 1, 'guitar_21.jpg', N'Mexico', 2023, N'Alder', NULL),
(N'Gibson ES-335', N'Semi-hollow electric guitar', 28000000.000, 25, 1, 2, 'guitar_22.jpg', N'USA', 2021, N'Maple', NULL),
(N'Yamaha APX600', N'Acoustic-electric guitar', 8000000.000, 70, 1, 3, 'guitar_23.jpg', N'Japan', 2022, N'Spruce', NULL),
(N'Ibanez AW54', N'Acoustic guitar with dreadnought body', 6000000.000, 80, 1, 4, 'guitar_24.jpg', N'China', 2023, N'Mahogany', NULL),
(N'Epiphone Hummingbird', N'Acoustic guitar with iconic design', 11000000.000, 50, 1, 5, 'guitar_25.jpg', N'China', 2022, N'Spruce', NULL),
(N'Taylor 314ce', N'Premium acoustic-electric guitar', 24000000.000, 30, 1, 6, 'guitar_26.jpg', N'USA', 2023, N'Sapele', NULL),
(N'Martin D-18', N'Classic dreadnought acoustic guitar', 32000000.000, 20, 1, 7, 'guitar_27.jpg', N'USA', 2021, N'Spruce', NULL),
(N'Gretsch G5220', N'Electromatic jet guitar', 13000000.000, 40, 1, 8, 'guitar_28.jpg', N'China', 2022, N'Mahogany', NULL),
(N'Jackson Pro Series Soloist', N'Electric guitar for shredders', 19000000.000, 35, 1, 9, 'guitar_29.jpg', N'USA', 2023, N'Maple', NULL),
(N'ESP LTD M-1000', N'High-end electric guitar', 20000000.000, 30, 1, 10, 'guitar_30.jpg', N'Japan', 2022, N'Alder', NULL),
(N'Fender American Ultra Strat', N'Premium electric guitar', 25000000.000, 25, 1, 1, 'guitar_31.jpg', N'USA', 2023, N'Alder', NULL),
(N'Gibson Les Paul Studio', N'Electric guitar with modern design', 18000000.000, 40, 1, 2, 'guitar_32.jpg', N'USA', 2021, N'Mahogany', NULL),
(N'Yamaha C40', N'Classical guitar for beginners', 4000000.000, 90, 1, 3, 'guitar_33.jpg', N'Japan', 2022, N'Spruce', NULL),
(N'Ibanez RG421', N'Electric guitar with fixed bridge', 9000000.000, 60, 1, 4, 'guitar_34.jpg', N'China', 2023, N'Mahogany', NULL),
(N'Epiphone Casino', N'Semi-hollow electric guitar', 14000000.000, 50, 1, 5, 'guitar_35.jpg', N'China', 2022, N'Maple', NULL),
(N'Taylor 110e', N'Acoustic-electric guitar', 16000000.000, 45, 1, 6, 'guitar_36.jpg', N'USA', 2023, N'Sapele', NULL),
(N'Martin X Series', N'Affordable acoustic guitar', 12000000.000, 60, 1, 7, 'guitar_37.jpg', N'USA', 2021, N'HPL', NULL),
(N'Gretsch G2627T', N'Streamliner center block guitar', 15000000.000, 40, 1, 8, 'guitar_38.jpg', N'China', 2022, N'Maple', NULL),
(N'Jackson JS Series Dinky', N'Entry-level electric guitar', 6000000.000, 80, 1, 9, 'guitar_39.jpg', N'China', 2023, N'Poplar', NULL),
(N'ESP LTD Snakebyte', N'Signature electric guitar', 22000000.000, 30, 1, 10, 'guitar_40.jpg', N'Japan', 2022, N'Mahogany', NULL),
(N'Fender Player Telecaster', N'Versatile telecaster guitar', 14000000.000, 50, 1, 1, 'guitar_41.jpg', N'Mexico', 2023, N'Alder', NULL),
(N'Gibson Flying V', N'Iconic electric guitar', 20000000.000, 35, 1, 2, 'guitar_42.jpg', N'USA', 2021, N'Mahogany', NULL),
(N'Yamaha FS800', N'Compact acoustic guitar', 7000000.000, 70, 1, 3, 'guitar_43.jpg', N'Japan', 2022, N'Spruce', NULL),
(N'Ibanez AEG50N', N'Acoustic-electric classical guitar', 8000000.000, 60, 1, 4, 'guitar_44.jpg', N'China', 2023, N'Spruce', NULL),
(N'Epiphone Dove Pro', N'Acoustic-electric guitar', 10000000.000, 50, 1, 5, 'guitar_45.jpg', N'China', 2022, N'Maple', NULL),
(N'Taylor Academy 10', N'Entry-level acoustic guitar', 13000000.000, 45, 1, 6, 'guitar_46.jpg', N'USA', 2023, N'Sapele', NULL),
(N'Martin D-10E', N'Acoustic-electric dreadnought', 20000000.000, 30, 1, 7, 'guitar_47.jpg', N'USA', 2021, N'Spruce', NULL),
(N'Gretsch G5230T', N'Electromatic jet guitar', 14000000.000, 40, 1, 8, 'guitar_48.jpg', N'China', 2022, N'Mahogany', NULL),
(N'Jackson King V', N'Electric guitar for metal', 16000000.000, 35, 1, 9, 'guitar_49.jpg', N'USA', 2023, N'Alder', NULL),
(N'ESP LTD EC-1000', N'Premium electric guitar', 23000000.000, 25, 1, 10, 'guitar_50.jpg', N'Japan', 2022, N'Mahogany', NULL);

-- Insert 50 Piano products
INSERT INTO Products (name, description, price, stock_quantity, category_id, brand_id, image_url, made_in, manufacturing_year, material, discount_id)
VALUES
(N'Yamaha U1 Upright', N'Professional upright piano', 85000000.000, 15, 2, 11, 'piano_1.jpg', N'Japan', 2022, N'Spruce', NULL),
(N'Kawai K-300', N'High-quality upright piano', 90000000.000, 12, 2, 12, 'piano_2.jpg', N'Japan', 2021, N'Wood', NULL),
(N'Steinway Model D', N'Concert grand piano', 450000000.000, 5, 2, 13, 'piano_3.jpg', N'USA', 2023, N'Spruce', NULL),
(N'Casio CDP-S160', N'Compact digital piano', 12000000.000, 30, 2, 14, 'piano_4.jpg', N'China', 2022, N'Plastic', NULL),
(N'Roland FP-30X', N'Portable digital piano', 18000000.000, 25, 2, 15, 'piano_5.jpg', N'China', 2023, N'Plastic', NULL),
(N'Korg B2', N'Digital piano for beginners', 10000000.000, 40, 2, 16, 'piano_6.jpg', N'China', 2022, N'Plastic', NULL),
(N'Bösendorfer 200', N'Premium grand piano', 300000000.000, 8, 2, 17, 'piano_7.jpg', N'Austria', 2021, N'Spruce', NULL),
(N'Samick JS-121FD', N'Upright piano for intermediates', 60000000.000, 20, 2, 18, 'piano_8.jpg', N'Korea', 2022, N'Wood', NULL),
(N'Petrof P125', N'Classic upright piano', 95000000.000, 10, 2, 19, 'piano_9.jpg', N'Czech Republic', 2023, N'Spruce', NULL),
(N'Young Chang Y121', N'Affordable upright piano', 55000000.000, 25, 2, 20, 'piano_10.jpg', N'Korea', 2022, N'Wood', NULL),
(N'Yamaha P-125', N'Portable digital piano', 15000000.000, 35, 2, 11, 'piano_11.jpg', N'Japan', 2023, N'Plastic', NULL),
(N'Kawai CN29', N'Digital piano with authentic touch', 22000000.000, 20, 2, 12, 'piano_12.jpg', N'Japan', 2022, N'Plastic', NULL),
(N'Steinway Model B', N'Grand piano for professionals', 350000000.000, 6, 2, 13, 'piano_13.jpg', N'USA', 2021, N'Spruce', NULL),
(N'Casio AP-270', N'Celviano digital piano', 20000000.000, 25, 2, 14, 'piano_14.jpg', N'China', 2023, N'Plastic', NULL),
(N'Roland HP704', N'High-end digital piano', 30000000.000, 15, 2, 15, 'piano_15.jpg', N'China', 2022, N'Plastic', NULL),
(N'Korg SP-280', N'Portable digital piano', 14000000.000, 30, 2, 16, 'piano_16.jpg', N'China', 2023, N'Plastic', NULL),
(N'Bösendorfer 185', N'Grand piano with rich tone', 280000000.000, 7, 2, 17, 'piano_17.jpg', N'Austria', 2021, N'Spruce', NULL),
(N'Samick NSG-158', N'Mid-size grand piano', 120000000.000, 10, 2, 18, 'piano_18.jpg', N'Korea', 2022, N'Wood', NULL),
(N'Petrof P131', N'Tall upright piano', 100000000.000, 12, 2, 19, 'piano_19.jpg', N'Czech Republic', 2023, N'Spruce', NULL),
(N'Young Chang Y150', N'Grand piano for studios', 150000000.000, 8, 2, 20, 'piano_20.jpg', N'Korea', 2022, N'Wood', NULL),
(N'Yamaha YDP-144', N'Digital piano for home use', 18000000.000, 25, 2, 11, 'piano_21.jpg', N'Japan', 2023, N'Plastic', NULL),
(N'Kawai K-500', N'Premium upright piano', 110000000.000, 10, 2, 12, 'piano_22.jpg', N'Japan', 2021, N'Wood', NULL),
(N'Steinway Model A', N'Classic grand piano', 320000000.000, 5, 2, 13, 'piano_23.jpg', N'USA', 2022, N'Spruce', NULL),
(N'Casio CDP-S350', N'Compact digital piano', 16000000.000, 30, 2, 14, 'piano_24.jpg', N'China', 2023, N'Plastic', NULL),
(N'Roland FP-60X', N'Portable digital piano', 25000000.000, 20, 2, 15, 'piano_25.jpg', N'China', 2022, N'Plastic', NULL),
(N'Korg C1 Air', N'Slim digital piano', 13000000.000, 35, 2, 16, 'piano_26.jpg', N'China', 2023, N'Plastic', NULL),
(N'Bösendorfer 170', N'Compact grand piano', 260000000.000, 6, 2, 17, 'piano_27.jpg', N'Austria', 2021, N'Spruce', NULL),
(N'Samick JS-143', N'Upright piano for schools', 70000000.000, 15, 2, 18, 'piano_28.jpg', N'Korea', 2022, N'Wood', NULL),
(N'Petrof P118', N'Entry-level upright piano', 80000000.000, 12, 2, 19, 'piano_29.jpg', N'Czech Republic', 2023, N'Spruce', NULL),
(N'Young Chang Y114', N'Compact upright piano', 50000000.000, 20, 2, 20, 'piano_30.jpg', N'Korea', 2022, N'Wood', NULL),
(N'Yamaha CLP-735', N'Clavinova digital piano', 35000000.000, 15, 2, 11, 'piano_31.jpg', N'Japan', 2023, N'Plastic', NULL),
(N'Kawai CA59', N'High-end digital piano', 40000000.000, 10, 2, 12, 'piano_32.jpg', N'Japan', 2022, N'Plastic', NULL),
(N'Steinway Model M', N'Medium grand piano', 300000000.000, 5, 2, 13, 'piano_33.jpg', N'USA', 2021, N'Spruce', NULL),
(N'Casio AP-470', N'Celviano digital piano', 22000000.000, 25, 2, 14, 'piano_34.jpg', N'China', 2023, N'Plastic', NULL),
(N'Roland HP702', N'Home digital piano', 28000000.000, 20, 2, 15, 'piano_35.jpg', N'China', 2022, N'Plastic', NULL),
(N'Korg LP-380', N'Slim digital piano', 15000000.000, 30, 2, 16, 'piano_36.jpg', N'China', 2023, N'Plastic', NULL),
(N'Bösendorfer 214VC', N'Concert grand piano', 400000000.000, 4, 2, 17, 'piano_37.jpg', N'Austria', 2021, N'Spruce', NULL),
(N'Samick NSG-175', N'Grand piano for professionals', 140000000.000, 8, 2, 18, 'piano_38.jpg', N'Korea', 2022, N'Wood', NULL),
(N'Petrof P135', N'Tall upright piano', 110000000.000, 10, 2, 19, 'piano_39.jpg', N'Czech Republic', 2023, N'Spruce', NULL),
(N'Young Chang Y131', N'Upright piano for studios', 65000000.000, 15, 2, 20, 'piano_40.jpg', N'Korea', 2022, N'Wood', NULL),
(N'Yamaha YDP-164', N'Digital piano with authentic sound', 20000000.000, 25, 2, 11, 'piano_41.jpg', N'Japan', 2023, N'Plastic', NULL),
(N'Kawai K-200', N'Entry-level upright piano', 75000000.000, 12, 2, 12, 'piano_42.jpg', N'Japan', 2022, N'Wood', NULL),
(N'Steinway Model S', N'Compact grand piano', 250000000.000, 6, 2, 13, 'piano_43.jpg', N'USA', 2021, N'Spruce', NULL),
(N'Casio CDP-S100', N'Ultra-slim digital piano', 10000000.000, 35, 2, 14, 'piano_44.jpg', N'China', 2023, N'Plastic', NULL),
(N'Roland FP-10', N'Entry-level digital piano', 12000000.000, 30, 2, 15, 'piano_45.jpg', N'China', 2022, N'Plastic', NULL),
(N'Korg B1SP', N'Digital piano with stand', 11000000.000, 40, 2, 16, 'piano_46.jpg', N'China', 2023, N'Plastic', NULL),
(N'Bösendorfer 155', N'Small grand piano', 240000000.000, 7, 2, 17, 'piano_47.jpg', N'Austria', 2021, N'Spruce', NULL),
(N'Samick JS-115', N'Compact upright piano', 55000000.000, 20, 2, 18, 'piano_48.jpg', N'Korea', 2022, N'Wood', NULL),
(N'Petrof P122', N'Upright piano for home', 85000000.000, 12, 2, 19, 'piano_49.jpg', N'Czech Republic', 2023, N'Spruce', NULL),
(N'Young Chang Y116', N'Affordable upright piano', 60000000.000, 15, 2, 20, 'piano_50.jpg', N'Korea', 2022, N'Wood', NULL);

-- Insert 50 Violin products
INSERT INTO Products (name, description, price, stock_quantity, category_id, brand_id, image_url, made_in, manufacturing_year, material, discount_id)
VALUES
(N'Stentor Student I', N'Beginner violin outfit', 4000000.000, 50, 3, 21, 'violin_1.jpg', N'China', 2022, N'Spruce', NULL),
(N'Cremona SV-75', N'Entry-level violin for students', 3500000.000, 60, 3, 22, 'violin_2.jpg', N'China', 2023, N'Maple', NULL),
(N'Yamaha V3SKA', N'Student violin with case', 7000000.000, 40, 3, 23, 'violin_3.jpg', N'Japan', 2022, N'Spruce', NULL),
(N'Fiddlerman Apprentice', N'Violin for beginners', 5000000.000, 45, 3, 24, 'violin_4.jpg', N'China', 2023, N'Maple', NULL),
(N'Cecilio CVN-300', N'Intermediate violin outfit', 6000000.000, 50, 3, 25, 'violin_5.jpg', N'China', 2022, N'Spruce', NULL),
(N'Eastman VL80', N'Student violin with ebony fittings', 8000000.000, 35, 3, 26, 'violin_6.jpg', N'China', 2023, N'Maple', NULL),
(N'D Z Strad Model 101', N'Handcrafted student violin', 10000000.000, 30, 3, 27, 'violin_7.jpg', N'China', 2022, N'Spruce', NULL),
(N'Scott Cao STV-017', N'Intermediate violin', 12000000.000, 25, 3, 28, 'violin_8.jpg', N'China', 2023, N'Maple', NULL),
(N'Gliga Gems 2', N'Handmade violin for intermediates', 15000000.000, 20, 3, 29, 'violin_9.jpg', N'Romania', 2022, N'Spruce', NULL),
(N'Knilling Bucharest', N'Professional violin outfit', 18000000.000, 15, 3, 30, 'violin_10.jpg', N'China', 2023, N'Maple', NULL),
(N'Stentor Student II', N'Upgraded student violin', 4500000.000, 50, 3, 21, 'violin_11.jpg', N'China', 2022, N'Spruce', NULL),
(N'Cremona SV-150', N'Violin for advancing students', 5000000.000, 45, 3, 22, 'violin_12.jpg', N'China', 2023, N'Maple', NULL),
(N'Yamaha V5SC', N'Acoustic violin for intermediates', 9000000.000, 35, 3, 23, 'violin_13.jpg', N'Japan', 2022, N'Spruce', NULL),
(N'Fiddlerman Soloist', N'Professional-grade violin', 12000000.000, 30, 3, 24, 'violin_14.jpg', N'China', 2023, N'Maple', NULL),
(N'Cecilio CVN-500', N'Advanced student violin', 7000000.000, 40, 3, 25, 'violin_15.jpg', N'China', 2022, N'Spruce', NULL),
(N'Eastman VL100', N'Intermediate violin outfit', 10000000.000, 30, 3, 26, 'violin_16.jpg', N'China', 2023, N'Maple', NULL),
(N'D Z Strad Model 220', N'Handcrafted violin for performers', 14000000.000, 25, 3, 27, 'violin_17.jpg', N'China', 2022, N'Spruce', NULL),
(N'Scott Cao STV-750', N'Professional violin', 16000000.000, 20, 3, 28, 'violin_18.jpg', N'China', 2023, N'Maple', NULL),
(N'Gliga Gems 1', N'Handmade professional violin', 20000000.000, 15, 3, 29, 'violin_19.jpg', N'Romania', 2022, N'Spruce', NULL),
(N'Knilling Sinfonia', N'High-end student violin', 11000000.000, 30, 3, 30, 'violin_20.jpg', N'China', 2023, N'Maple', NULL),
(N'Stentor Conservatoire', N'Advanced student violin', 6000000.000, 40, 3, 21, 'violin_21.jpg', N'China', 2022, N'Spruce', NULL),
(N'Cremona SV-175', N'Violin for intermediate players', 5500000.000, 45, 3, 22, 'violin_22.jpg', N'China', 2023, N'Maple', NULL),
(N'Yamaha V7SG', N'Acoustic violin for performers', 11000000.000, 30, 3, 23, 'violin_23.jpg', N'Japan', 2022, N'Spruce', NULL),
(N'Fiddlerman Concert', N'Concert-level violin', 13000000.000, 25, 3, 24, 'violin_24.jpg', N'China', 2023, N'Maple', NULL),
(N'Cecilio CVN-600', N'Professional violin outfit', 8000000.000, 35, 3, 25, 'violin_25.jpg', N'China', 2022, N'Spruce', NULL),
(N'Eastman VL200', N'Advanced violin with ebony fittings', 12000000.000, 30, 3, 26, 'violin_26.jpg', N'China', 2023, N'Maple', NULL),
(N'D Z Strad Model 300', N'Handcrafted professional violin', 16000000.000, 20, 3, 27, 'violin_27.jpg', N'China', 2022, N'Spruce', NULL),
(N'Scott Cao STV-850', N'Master violin for soloists', 18000000.000, 15, 3, 28, 'violin_28.jpg', N'China', 2023, N'Maple', NULL),
(N'Gliga Maestro', N'Handmade master violin', 22000000.000, 10, 3, 29, 'violin_29.jpg', N'Romania', 2022, N'Spruce', NULL),
(N'Knilling Perfection', N'High-end violin for professionals', 15000000.000, 20, 3, 30, 'violin_30.jpg', N'China', 2023, N'Maple', NULL),
(N'Stentor Verona', N'Advanced violin outfit', 7000000.000, 35, 3, 21, 'violin_31.jpg', N'China', 2022, N'Spruce', NULL),
(N'Cremona SV-200', N'Intermediate violin with case', 6000000.000, 40, 3, 22, 'violin_32.jpg', N'China', 2023, N'Maple', NULL),
(N'Yamaha V10G', N'Professional acoustic violin', 14000000.000, 25, 3, 23, 'violin_33.jpg', N'Japan', 2022, N'Spruce', NULL),
(N'Fiddlerman Master', N'Master-level violin', 16000000.000, 20, 3, 24, 'violin_34.jpg', N'China', 2023, N'Maple', NULL),
(N'Cecilio CVN-EAV', N'Electric-acoustic violin', 9000000.000, 30, 3, 25, 'violin_35.jpg', N'China', 2022, N'Spruce', NULL),
(N'Eastman VL305', N'Professional violin outfit', 13000000.000, 25, 3, 26, 'violin_36.jpg', N'China', 2023, N'Maple', NULL),
(N'D Z Strad Model 400', N'Handcrafted concert violin', 17000000.000, 20, 3, 27, 'violin_37.jpg', N'China', 2022, N'Spruce', NULL),
(N'Scott Cao STV-950', N'Master violin for orchestras', 20000000.000, 15, 3, 28, 'violin_38.jpg', N'China', 2023, N'Maple', NULL),
(N'Gliga Professional', N'Handmade professional violin', 24000000.000, 10, 3, 29, 'violin_39.jpg', N'Romania', 2022, N'Spruce', NULL),
(N'Knilling Heritage', N'High-end student violin', 12000000.000, 25, 3, 30, 'violin_40.jpg', N'China', 2023, N'Maple', NULL),
(N'Stentor Messina', N'Advanced violin for performers', 8000000.000, 30, 3, 21, 'violin_41.jpg', N'China', 2022, N'Spruce', NULL),
(N'Cremona SV-500', N'Professional violin outfit', 10000000.000, 35, 3, 22, 'violin_42.jpg', N'China', 2023, N'Maple', NULL),
(N'Yamaha EV-104', N'Electric violin for modern players', 15000000.000, 20, 3, 23, 'violin_43.jpg', N'Japan', 2022, N'Spruce', NULL),
(N'Fiddlerman Artisan', N'Handcrafted artisan violin', 14000000.000, 25, 3, 24, 'violin_44.jpg', N'China', 2023, N'Maple', NULL),
(N'Cecilio CVN-700', N'Advanced professional violin', 11000000.000, 30, 3, 25, 'violin_45.jpg', N'China', 2022, N'Spruce', NULL),
(N'Eastman VL405', N'Master violin with ebony fittings', 16000000.000, 20, 3, 26, 'violin_46.jpg', N'China', 2023, N'Maple', NULL),
(N'D Z Strad Model 500', N'Concert-level violin', 18000000.000, 15, 3, 27, 'violin_47.jpg', N'China', 2022, N'Spruce', NULL),
(N'Scott Cao STV-1000', N'Master violin for soloists', 22000000.000, 10, 3, 28, 'violin_48.jpg', N'China', 2023, N'Maple', NULL),
(N'Gliga Gems 3', N'Handmade master violin', 25000000.000, 10, 3, 29, 'violin_49.jpg', N'Romania', 2022, N'Spruce', NULL),
(N'Knilling Maestro', N'Professional violin outfit', 17000000.000, 15, 3, 30, 'violin_50.jpg', N'China', 2023, N'Maple', NULL);

-- Product Image
INSERT INTO ProductImages (product_id, image_url, caption, is_primary)
VALUES 
(1, 'guitar1.jpg', 'Front view of guitar', 1),
(1, 'guitar1-side.jpg', 'Side view of guitar', 0),
(2, 'piano1.jpg', 'Piano with bench', 1);

-- Review
INSERT INTO Reviews (product_id, user_id, rating, comment)
VALUES
(1, 2, 5, N'Rất hài lòng với chất lượng sản phẩm'),
(2, 2, 4, N'Sản phẩm tốt nhưng giao hàng hơi chậm');
