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
	subcategory_id INT,
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
    FOREIGN KEY (brand_id) REFERENCES Brands(brand_id),
	FOREIGN KEY (subcategory_id) REFERENCES Subcategories(subcategory_id)
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
('Admin User', 'admin@example.com', 'e10adc3949ba59abbe56e057f20f883e', '0936541256', 1, 1, '1985-01-01', 'admin'),
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


-- Guitar
INSERT INTO Products (discount_id, name, description, price, stock_quantity, category_id, subcategory_id, brand_id, image_url, made_in, manufacturing_year, material)
VALUES
(1, N'Fender FA-125 Acoustic Guitar', N'Guitar acoustic phổ thông, phù hợp cho người mới học.', 3500000, 20, 1, 1, 1, 'fender-fa125.jpg', 'USA', 2024, N'Gỗ spruce'),
(1, N'Gibson Les Paul Standard Electric', N'Guitar điện cao cấp dành cho biểu diễn.', 45000000, 5, 1, 2, 2, 'gibson-lespaul.jpg', 'USA', 2023, N'Mahogany'),
(NULL, N'Yamaha C40 Classic Guitar', N'Guitar classic nylon dây mềm, dễ chơi.', 3200000, 15, 1, 3, 3, 'yamaha-c40.jpg', 'Indonesia', 2024, N'Gỗ spruce'),
(NULL, N'Ibanez GSR200 Bass Guitar', N'Guitar bass dành cho nhạc rock và jazz.', 7800000, 10, 1, 4, 4, 'ibanez-gsr200.jpg', 'China', 2024, N'Alder'),
(NULL, N'Taylor GS Mini Acoustic', N'Mẫu guitar acoustic nhỏ gọn, chất âm ấm áp.', 22000000, 8, 1, 1, 6, 'taylor-gsmini.jpg', 'USA', 2023, N'Gỗ mahogany'),
(NULL, N'Martin D-28 Acoustic Guitar', N'Guitar acoustic cao cấp, âm thanh mạnh mẽ.', 78000000, 4, 1, 1, 7, 'martin-d28.jpg', 'USA', 2024, N'Gỗ rosewood'),
(1, N'Epiphone SG Standard Electric', N'Bản sao Gibson SG điện giá hợp lý.', 9500000, 7, 1, 2, 5, 'epiphone-sg.jpg', 'China', 2024, N'Mahogany'),
(3, N'Gretsch Streamliner G2420', N'Guitar hollow-body dành cho jazz.', 15000000, 6, 1, 2, 8, 'gretsch-g2420.jpg', 'Korea', 2023, N'Maple'),
(3, N'Jackson JS32 King V Electric', N'Mẫu guitar điện dáng V cực ngầu.', 11000000, 6, 1, 2, 9, 'jackson-js32.jpg', 'Indonesia', 2024, N'Basswood'),
(2, N'ESP LTD EC-1000 Deluxe', N'Guitar điện dành cho metal và rock.', 24000000, 3, 1, 2, 10, 'esp-ltd-ec1000.jpg', 'Japan', 2023, N'Mahogany');

-- Piano
INSERT INTO Products (discount_id, name, description, price, stock_quantity, category_id, subcategory_id, brand_id, image_url, made_in, manufacturing_year, material)
VALUES
(1, N'Yamaha CFX Grand Piano', N'Đàn grand piano cao cấp, âm thanh xuất sắc.', 350000000, 2, 2, 5, 11, 'yamaha-cfx.jpg', 'Japan', 2023, N'Gỗ spruce'),
(NULL, N'Kawai GX-2 Grand Piano', N'Grand piano tầm trung với âm thanh mượt mà.', 220000000, 2, 2, 5, 12, 'kawai-gx2.jpg', 'Japan', 2024, N'Gỗ maple'),
(NULL, N'Steinway Model D Concert Grand', N'Dòng concert piano nổi tiếng thế giới.', 800000000, 1, 2, 5, 13, 'steinway-d.jpg', 'Germany', 2023, N'Gỗ spruce'),
(NULL, N'Casio AP-470 Digital Piano', N'Piano điện giá rẻ, tính năng hiện đại.', 25000000, 6, 2, 7, 14, 'casio-ap470.jpg', 'Japan', 2024, N'Plastic + Gỗ'),
(NULL, N'Roland FP-30X Digital Piano', N'Piano điện nhỏ gọn, phím nhạy.', 18000000, 10, 2, 7, 15, 'roland-fp30x.jpg', 'Japan', 2024, N'Plastic + Gỗ'),
(NULL, N'Korg LP-380 Digital Piano', N'Piano điện slim-line, kiểu dáng hiện đại.', 17000000, 7, 2, 7, 16, 'korg-lp380.jpg', 'Japan', 2024, N'Plastic + Gỗ'),
(2, N'Samick JS-115 Upright Piano', N'Upright piano chất lượng, phù hợp gia đình.', 68000000, 3, 2, 6, 18, 'samick-js115.jpg', 'Korea', 2023, N'Gỗ spruce'),
(1, N'Petrof P118 Upright Piano', N'Upright piano cao cấp đến từ Châu Âu.', 140000000, 2, 2, 6, 19, 'petrof-p118.jpg', 'Czech', 2023, N'Gỗ spruce'),
(1, N'Young Chang Y131 Upright Piano', N'Upright piano giá hợp lý.', 55000000, 4, 2, 6, 20, 'youngchang-y131.jpg', 'Korea', 2024, N'Gỗ spruce'),
(3, N'Yamaha DGX-670 Keyboard', N'Keyboard 88 phím, đa năng cho mọi thể loại.', 24000000, 8, 2, 8, 11, 'yamaha-dgx670.jpg', 'Japan', 2024, N'Plastic + Gỗ');

-- Violin
INSERT INTO Products (discount_id, name, description, price, stock_quantity, category_id, subcategory_id, brand_id, image_url, made_in, manufacturing_year, material)
VALUES
(3, N'Stentor Student I Acoustic Violin', N'Violin acoustic cơ bản dành cho học sinh.', 3500000, 25, 3, 9, 21, 'stentor-student1.jpg', 'China', 2024, N'Gỗ maple + spruce'),
(1, N'Stentor Conservatoire Acoustic Violin', N'Violin cao cấp dành cho biểu diễn.', 12500000, 8, 3, 9, 21, 'stentor-conservatoire.jpg', 'China', 2024, N'Gỗ spruce'),
(NULL, N'Cremona SV-500 Acoustic Violin', N'Violin acoustic tầm trung, âm sáng.', 7500000, 10, 3, 9, 22, 'cremona-sv500.jpg', 'China', 2024, N'Gỗ spruce'),
(NULL, N'Yamaha SV-200 Electric Violin', N'Violin điện hiện đại, âm thanh đa dạng.', 18000000, 5, 3, 10, 23, 'yamaha-sv200.jpg', 'Japan', 2024, N'Gỗ maple'),
(NULL, N'Fiddlerman Concert Violin', N'Violin acoustic chuẩn biểu diễn.', 8500000, 7, 3, 9, 24, 'fiddlerman-concert.jpg', 'China', 2024, N'Gỗ maple'),
(3, N'Cecilio CVN-300 Semi-electric Violin', N'Violin bán điện, phù hợp luyện tập.', 5500000, 12, 3, 11, 25, 'cecilio-cvn300.jpg', 'USA', 2024, N'Gỗ spruce + maple'),
(3, N'Eastman VL305 Acoustic Violin', N'Violin handmade cao cấp.', 14500000, 4, 3, 9, 26, 'eastman-vl305.jpg', 'China', 2024, N'Gỗ maple'),
(NULL, N'D Z Strad Model 509 Violin', N'Violin được yêu thích cho người chơi bán chuyên.', 12000000, 6, 3, 9, 27, 'dzstrad-509.jpg', 'China', 2024, N'Gỗ spruce'),
(NULL, N'Scott Cao STV-750 Violin', N'Violin thủ công chất lượng cao.', 18500000, 3, 3, 9, 28, 'scottcao-stv750.jpg', 'China', 2024, N'Gỗ spruce'),
(1, N'Knilling Bucharest Violin', N'Violin chuyên nghiệp âm thanh rõ ràng.', 16500000, 3, 3, 9, 30, 'knilling-bucharest.jpg', 'Romania', 2024, N'Gỗ maple');

-- Review
INSERT INTO Reviews (product_id, user_id, rating, comment)
VALUES
(1, 2, 5, N'Rất hài lòng với chất lượng sản phẩm'),
(2, 2, 4, N'Sản phẩm tốt nhưng giao hàng hơi chậm');
