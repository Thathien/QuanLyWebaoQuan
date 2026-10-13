IF OBJECT_ID(N'dbo.chi_tiet_san_pham', N'U') IS NOT NULL
   AND COL_LENGTH(N'dbo.chi_tiet_san_pham', N'gia_von_binh_quan') IS NULL
  ALTER TABLE [dbo].[chi_tiet_san_pham] ADD [gia_von_binh_quan] DECIMAL(19, 2) NULL;

IF OBJECT_ID(N'dbo.chi_tiet_san_pham', N'U') IS NOT NULL
   AND COL_LENGTH(N'dbo.chi_tiet_san_pham', N'ton_toi_thieu') IS NULL
  ALTER TABLE [dbo].[chi_tiet_san_pham] ADD [ton_toi_thieu] INT NULL;

UPDATE [dbo].[chi_tiet_san_pham]
SET [ton_toi_thieu] = 0
WHERE [ton_toi_thieu] IS NULL;

IF EXISTS (
    SELECT 1
    FROM sys.columns
    WHERE [object_id] = OBJECT_ID(N'dbo.chi_tiet_san_pham')
      AND [name] = N'ton_toi_thieu'
      AND [is_nullable] = 1
)
  ALTER TABLE [dbo].[chi_tiet_san_pham] ALTER COLUMN [ton_toi_thieu] INT NOT NULL;

IF OBJECT_ID(N'dbo.chi_tiet_hoa_don', N'U') IS NOT NULL
   AND COL_LENGTH(N'dbo.chi_tiet_hoa_don', N'gia_von') IS NULL
  ALTER TABLE [dbo].[chi_tiet_hoa_don] ADD [gia_von] DECIMAL(19, 2) NULL;

IF OBJECT_ID(N'dbo.hoa_don', N'U') IS NOT NULL
   AND COL_LENGTH(N'dbo.hoa_don', N'created_at') IS NULL
  ALTER TABLE [dbo].[hoa_don] ADD [created_at] DATETIME2 NULL;

IF OBJECT_ID(N'dbo.hoa_don', N'U') IS NOT NULL
   AND COL_LENGTH(N'dbo.hoa_don', N'completed_at') IS NULL
  ALTER TABLE [dbo].[hoa_don] ADD [completed_at] DATETIME2 NULL;

IF EXISTS (
    SELECT 1
    FROM sys.columns
    WHERE [object_id] = OBJECT_ID(N'dbo.hoa_don')
      AND [name] = N'tinhtrang'
      AND [system_type_id] <> 56
)
AND EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE [object_id] = OBJECT_ID(N'dbo.hoa_don')
      AND [name] = N'IX_HoaDon_Status_CompletedAt'
)
  DROP INDEX [IX_HoaDon_Status_CompletedAt] ON [dbo].[hoa_don];

IF EXISTS (
    SELECT 1
    FROM sys.columns
    WHERE [object_id] = OBJECT_ID(N'dbo.hoa_don')
      AND [name] = N'tinhtrang'
      AND [system_type_id] <> 56
)
  ALTER TABLE [dbo].[hoa_don] ALTER COLUMN [tinhtrang] INT;

IF OBJECT_ID(N'dbo.hoa_don', N'U') IS NOT NULL
   AND NOT EXISTS (
     SELECT 1
     FROM sys.indexes
     WHERE [object_id] = OBJECT_ID(N'dbo.hoa_don')
       AND [name] = N'IX_HoaDon_Status_CompletedAt'
   )
  CREATE INDEX [IX_HoaDon_Status_CompletedAt]
    ON [dbo].[hoa_don] ([tinhtrang], [completed_at]);

EXEC(N'
DECLARE @dropSql nvarchar(max);
SELECT @dropSql = STRING_AGG(
    CONVERT(nvarchar(max),
        CASE WHEN i.is_unique_constraint = 1
             THEN N''ALTER TABLE dbo.san_pham DROP CONSTRAINT '' + QUOTENAME(i.name)
             ELSE N''DROP INDEX '' + QUOTENAME(i.name) + N'' ON dbo.san_pham''
        END),
    N'';'')
FROM sys.indexes i
WHERE i.object_id = OBJECT_ID(N''dbo.san_pham'')
  AND i.is_unique = 1
  AND i.is_primary_key = 0
  AND (SELECT COUNT(*)
       FROM sys.index_columns ic
       WHERE ic.object_id = i.object_id
         AND ic.index_id = i.index_id
         AND ic.key_ordinal > 0) = 1
  AND EXISTS (
      SELECT 1
      FROM sys.index_columns ic
      JOIN sys.columns c
        ON c.object_id = ic.object_id AND c.column_id = ic.column_id
      WHERE ic.object_id = i.object_id
        AND ic.index_id = i.index_id
        AND ic.key_ordinal = 1
        AND c.name = N''madanhmucsanpham''
  );
IF @dropSql IS NOT NULL EXEC sys.sp_executesql @dropSql;
');

DELETE FROM [chi_tiet_hoan_tien];
DELETE FROM [hoan_tien];
DELETE FROM [chi_tiet_tra_hang];
DELETE FROM [tra_hang];
DELETE FROM [chi_tiet_hoa_don];
DELETE FROM [hoa_don];
DELETE FROM [bien_dong_kho];
DELETE FROM [chi_tiet_phieu_nhap];
DELETE FROM [phieu_nhap];
DELETE FROM [nha_cung_cap];
DELETE FROM [chi_tiet_san_pham];
DELETE FROM [chitietkhuyenmai];
DELETE FROM [san_pham];
DELETE FROM [danh_muc_san_pham];
DELETE FROM [size_san_pham];
DELETE FROM [mau_san_pham];
DELETE FROM [nhan_vien];
DELETE FROM [chuc_vu];
DELETE FROM [khuyen_mai];

DBCC CHECKIDENT ('chi_tiet_san_pham', RESEED, 0);
DBCC CHECKIDENT ('chi_tiet_hoan_tien', RESEED, 0);
DBCC CHECKIDENT ('hoan_tien', RESEED, 0);
DBCC CHECKIDENT ('chi_tiet_tra_hang', RESEED, 0);
DBCC CHECKIDENT ('tra_hang', RESEED, 0);
DBCC CHECKIDENT ('hoa_don', RESEED, 0);
DBCC CHECKIDENT ('bien_dong_kho', RESEED, 0);
DBCC CHECKIDENT ('chi_tiet_phieu_nhap', RESEED, 0);
DBCC CHECKIDENT ('phieu_nhap', RESEED, 0);
DBCC CHECKIDENT ('nha_cung_cap', RESEED, 0);
DBCC CHECKIDENT ('san_pham', RESEED, 0);
DBCC CHECKIDENT ('danh_muc_san_pham', RESEED, 0);
DBCC CHECKIDENT ('size_san_pham', RESEED, 0);
DBCC CHECKIDENT ('mau_san_pham', RESEED, 0);
DBCC CHECKIDENT ('nhan_vien', RESEED, 0);
DBCC CHECKIDENT ('chuc_vu', RESEED, 0);
DBCC CHECKIDENT ('khuyen_mai', RESEED, 0);

SET IDENTITY_INSERT [chuc_vu] ON;
INSERT INTO [chuc_vu] ([machucvu], [tenchucvu]) VALUES
(1, N'Quan tri vien'),
(2, N'Nhan vien'),
(3, N'Khach hang');
SET IDENTITY_INSERT [chuc_vu] OFF;

SET IDENTITY_INSERT [nhan_vien] ON;
INSERT INTO [nhan_vien] (
    [manhanvien], [hoten], [diachi], [gioitinh], [cmnd], [lock],
    [lydokhoa], [xacthuc], [machucvu], [email], [tendangnhap], [matkhau]
) VALUES
(1, N'Quan tri he thong', N'TP. Ho Chi Minh', 1, N'079000000001', 0, NULL, 1, 1, N'admin@banhang.com', N'admin', N'123456'),
(2, N'Nhan vien ban hang', N'TP. Ho Chi Minh', 0, N'079000000002', 0, NULL, 1, 2, N'staff@banhang.com', N'staff', N'123456'),
(3, N'Khach hang mau', N'Ha Noi', 1, N'079000000003', 0, NULL, 1, 3, N'user@banhang.com', N'user', N'123456');
SET IDENTITY_INSERT [nhan_vien] OFF;

SET IDENTITY_INSERT [danh_muc_san_pham] ON;
INSERT INTO [danh_muc_san_pham] ([madanhmucsanpham], [tendanhmuc], [hinhdanhmuc], [hiden]) VALUES
(1, N'Ao thun', N'product1.jpg', 0),
(2, N'Ao so mi', N'product2.jpg', 0),
(3, N'Quan jean', N'product3.jpg', 0),
(4, N'Ao khoac', N'product4.jpg', 0);
SET IDENTITY_INSERT [danh_muc_san_pham] OFF;

SET IDENTITY_INSERT [size_san_pham] ON;
INSERT INTO [size_san_pham] ([masize], [size]) VALUES
(1, N'S'),
(2, N'M'),
(3, N'L'),
(4, N'XL');
SET IDENTITY_INSERT [size_san_pham] OFF;

SET IDENTITY_INSERT [mau_san_pham] ON;
INSERT INTO [mau_san_pham] ([mamau], [tenmau]) VALUES
(1, N'Trang'),
(2, N'Den'),
(3, N'Xanh navy'),
(4, N'Be');
SET IDENTITY_INSERT [mau_san_pham] OFF;

SET IDENTITY_INSERT [khuyen_mai] ON;
INSERT INTO [khuyen_mai] (
    [makhuyenmai], [giagiam], [tenkhuyenmai], [thoigianbatdau],
    [thoigianketthuc], [mota], [hinhkhuyenmai]
) VALUES
(1, 10, N'Giam 10%', N'2026-01-01', N'2026-12-31', N'Giam gia san pham', N'sale.png'),
(2, 15, N'Giam 15%', N'2026-06-01', N'2026-06-30', N'Khuyen mai he', N'sale.png');
SET IDENTITY_INSERT [khuyen_mai] OFF;

SET IDENTITY_INSERT [san_pham] ON;
INSERT INTO [san_pham] (
    [masanpham], [madanhmucsanpham], [tensanpham], [giatien],
    [mota], [hinhsanpham], [hiden], [doituong]
) VALUES
(1, 1, N'Ao thun basic trang', N'199000', N'Ao thun cotton form regular, de phoi do.', N'product1.jpg', 0, N'Nam'),
(2, 2, N'Ao so mi Oxford', N'349000', N'So mi Oxford lich su dung di lam hoac di choi.', N'product2.jpg', 0, N'Nam'),
(3, 3, N'Quan jean slim fit', N'499000', N'Quan jean co gian nhe, dang slim fit.', N'product3.jpg', 0, N'Nam'),
(4, 4, N'Ao khoac kaki', N'599000', N'Ao khoac kaki nhe, phu hop thoi tiet mat.', N'product4.jpg', 0, N'Nu');
SET IDENTITY_INSERT [san_pham] OFF;

SET IDENTITY_INSERT [san_pham] ON;
;WITH product_numbers AS (
    SELECT 5 AS product_id
    UNION ALL
    SELECT product_id + 1
    FROM product_numbers
    WHERE product_id < 64
)
INSERT INTO [san_pham] (
    [masanpham], [madanhmucsanpham], [tensanpham], [giatien],
    [mota], [hinhsanpham], [hiden], [doituong]
)
SELECT
    product_id,
    ((product_id - 5) % 4) + 1,
    CASE ((product_id - 5) % 4)
        WHEN 0 THEN N'Áo thun cotton mẫu ' + RIGHT(N'00' + CONVERT(nvarchar(2), product_id), 2)
        WHEN 1 THEN N'Áo sơ mi công sở mẫu ' + RIGHT(N'00' + CONVERT(nvarchar(2), product_id), 2)
        WHEN 2 THEN N'Quần jean thời trang mẫu ' + RIGHT(N'00' + CONVERT(nvarchar(2), product_id), 2)
        ELSE N'Áo khoác mùa mới mẫu ' + RIGHT(N'00' + CONVERT(nvarchar(2), product_id), 2)
    END,
    CONVERT(nvarchar(20), 99000 + (product_id * 17000 % 501000)),
    N'Sản phẩm mẫu dùng thử bộ lọc theo danh mục, tên và khoảng giá.',
    N'product' + CONVERT(nvarchar(1), ((product_id - 5) % 4) + 1) + N'.jpg',
    0,
    CASE WHEN product_id % 2 = 0 THEN N'Nam' ELSE N'Nữ' END
FROM product_numbers
OPTION (MAXRECURSION 100);
SET IDENTITY_INSERT [san_pham] OFF;

SET IDENTITY_INSERT [chi_tiet_san_pham] ON;
INSERT INTO [chi_tiet_san_pham] (
    [machitietsanpham], [masanpham], [masize], [mamau], [soluong], [ngaynhap],
    [gia_von_binh_quan], [ton_toi_thieu]
) VALUES
(1, 1, 1, 1, 30, N'2026-06-01', 80000.00, 5),
(2, 1, 2, 1, 45, N'2026-06-01', 80000.00, 8),
(3, 2, 2, 2, 25, N'2026-06-02', 150000.00, 5),
(4, 2, 3, 2, 20, N'2026-06-02', 150000.00, 5),
(5, 3, 2, 3, 18, N'2026-06-03', 230000.00, 4),
(6, 3, 3, 3, 16, N'2026-06-03', 230000.00, 4),
(7, 4, 3, 2, 22, N'2026-06-04', 290000.00, 5),
(8, 4, 4, 2, 14, N'2026-06-04', 290000.00, 4),
(9, 1, 3, 1, 18, N'2026-06-05', 80000.00, 5),
(10, 2, 4, 3, 14, N'2026-06-05', 150000.00, 4),
(11, 3, 1, 2, 20, N'2026-06-06', 230000.00, 5),
(12, 4, 2, 4, 24, N'2026-06-06', 290000.00, 5);
SET IDENTITY_INSERT [chi_tiet_san_pham] OFF;

SET IDENTITY_INSERT [chi_tiet_san_pham] ON;
;WITH product_numbers AS (
    SELECT 5 AS product_id
    UNION ALL
    SELECT product_id + 1
    FROM product_numbers
    WHERE product_id < 64
)
INSERT INTO [chi_tiet_san_pham] (
    [machitietsanpham], [masanpham], [masize], [mamau], [soluong], [ngaynhap],
    [gia_von_binh_quan], [ton_toi_thieu]
)
SELECT
    product_id + 8,
    product_id,
    ((product_id - 5) % 4) + 1,
    ((product_id - 5) % 4) + 1,
    CASE WHEN product_id % 17 = 0 THEN 0 ELSE 10 + (product_id % 21) END,
    N'2026-06-' + RIGHT(N'0' + CONVERT(nvarchar(2), ((product_id - 5) % 28) + 1), 2),
    CASE ((product_id - 5) % 4)
        WHEN 0 THEN 80000.00
        WHEN 1 THEN 150000.00
        WHEN 2 THEN 230000.00
        ELSE 290000.00
    END,
    5 + (product_id % 5)
FROM product_numbers
OPTION (MAXRECURSION 100);
SET IDENTITY_INSERT [chi_tiet_san_pham] OFF;

SET IDENTITY_INSERT [nha_cung_cap] ON;
INSERT INTO [nha_cung_cap] ([id], [ten], [email], [so_dien_thoai], [dia_chi]) VALUES
(1, N'Công ty May Mặc An Phát', N'contact@anphat.example', N'0900000001', N'TP. Hồ Chí Minh'),
(2, N'Xưởng Thời Trang Minh Châu', N'contact@minhchau.example', N'0900000002', N'Hà Nội');
SET IDENTITY_INSERT [nha_cung_cap] OFF;

SET IDENTITY_INSERT [phieu_nhap] ON;
INSERT INTO [phieu_nhap] ([id], [supplier_id], [status], [created_at], [confirmed_at]) VALUES
(1, 1, N'CONFIRMED', '2026-06-01T08:00:00', '2026-06-01T09:00:00'),
(2, 2, N'DRAFT', '2026-06-07T08:00:00', NULL);
SET IDENTITY_INSERT [phieu_nhap] OFF;

INSERT INTO [chi_tiet_phieu_nhap] ([receipt_id], [variant_id], [quantity], [unit_cost]) VALUES
(1, 1, 5, 80000.00),
(1, 2, 5, 80000.00),
(2, 3, 4, 150000.00);

INSERT INTO [bien_dong_kho] (
    [variant_id], [movement_type], [quantity_delta], [unit_cost], [reference_id], [occurred_at], [note]
) VALUES
(1, N'OPENING_BALANCE', 25, 80000.00, N'OPENING:1', '2026-06-01T07:00:00', N'Tồn đầu kỳ mẫu'),
(2, N'OPENING_BALANCE', 40, 80000.00, N'OPENING:2', '2026-06-01T07:00:00', N'Tồn đầu kỳ mẫu'),
(3, N'OPENING_BALANCE', 25, 150000.00, N'OPENING:3', '2026-06-02T07:00:00', N'Tồn đầu kỳ mẫu'),
(4, N'OPENING_BALANCE', 20, 150000.00, N'OPENING:4', '2026-06-02T07:00:00', N'Tồn đầu kỳ mẫu'),
(5, N'OPENING_BALANCE', 18, 230000.00, N'OPENING:5', '2026-06-03T07:00:00', N'Tồn đầu kỳ mẫu'),
(6, N'OPENING_BALANCE', 16, 230000.00, N'OPENING:6', '2026-06-03T07:00:00', N'Tồn đầu kỳ mẫu'),
(7, N'OPENING_BALANCE', 22, 290000.00, N'OPENING:7', '2026-06-04T07:00:00', N'Tồn đầu kỳ mẫu'),
(8, N'OPENING_BALANCE', 14, 290000.00, N'OPENING:8', '2026-06-04T07:00:00', N'Tồn đầu kỳ mẫu'),
(9, N'OPENING_BALANCE', 18, 80000.00, N'OPENING:9', '2026-06-05T07:00:00', N'Tồn đầu kỳ mẫu'),
(10, N'OPENING_BALANCE', 14, 150000.00, N'OPENING:10', '2026-06-05T07:00:00', N'Tồn đầu kỳ mẫu'),
(11, N'OPENING_BALANCE', 20, 230000.00, N'OPENING:11', '2026-06-06T07:00:00', N'Tồn đầu kỳ mẫu'),
(12, N'OPENING_BALANCE', 24, 290000.00, N'OPENING:12', '2026-06-06T07:00:00', N'Tồn đầu kỳ mẫu'),
(1, N'RECEIPT', 5, 80000.00, N'RECEIPT:1', '2026-06-01T09:00:00', N'Xác nhận phiếu nhập mẫu'),
(2, N'RECEIPT', 5, 80000.00, N'RECEIPT:1', '2026-06-01T09:00:00', N'Xác nhận phiếu nhập mẫu');

INSERT INTO [bien_dong_kho] (
    [variant_id], [movement_type], [quantity_delta], [unit_cost], [reference_id], [occurred_at], [note]
)
SELECT
    v.[machitietsanpham],
    N'OPENING_BALANCE',
    v.[soluong],
    v.[gia_von_binh_quan],
    N'OPENING:' + CONVERT(nvarchar(20), v.[machitietsanpham]),
    '2026-06-01T07:00:00',
    N'Tồn đầu kỳ sản phẩm mẫu'
FROM [chi_tiet_san_pham] v
WHERE v.[machitietsanpham] BETWEEN 13 AND 72;

SET IDENTITY_INSERT [hoa_don] ON;
INSERT INTO [hoa_don] (
    [mahoadon], [tenkhachhang], [emailkhachhang], [sdt], [diachigiaohang],
    [tinhtrang], [ghichu], [ngaylap], [created_at], [completed_at], [hinhthucgiaohang]
) VALUES
(1, N'Nguyễn Minh Anh', N'minhanh@example.com', N'0901000001', N'12 Nguyễn Trãi, Quận 1, TP. Hồ Chí Minh', 0, N'Giao giờ hành chính', N'07/10/2026 10:00:00', '2026-10-07T10:00:00', NULL, N'Giao hàng tận nơi'),
(2, N'Trần Quốc Bảo', N'quocbao@example.com', N'0901000002', N'25 Lê Lợi, Quận Hải Châu, Đà Nẵng', 1, N'Gọi trước khi giao', N'08/10/2026 11:30:00', '2026-10-08T11:30:00', NULL, N'Giao hàng tận nơi'),
(3, N'Lê Thu Hà', N'thuha@example.com', N'0901000003', N'18 Trần Phú, Quận Ninh Kiều, Cần Thơ', 2, N'', N'09/10/2026 09:15:00', '2026-10-09T09:15:00', NULL, N'Giao hàng tận nơi'),
(4, N'Phạm Gia Huy', N'giahuy@example.com', N'0901000004', N'48 Cầu Giấy, Hà Nội', 3, N'Đã giao thành công', N'09/10/2026 14:20:00', '2026-10-09T14:20:00', '2026-10-11T16:10:00', N'Giao hàng tận nơi'),
(5, N'Võ Ngọc Lan', N'ngoclan@example.com', N'0901000005', N'06 Nguyễn Huệ, TP. Huế', 3, N'Khách đã nhận hàng', N'10/10/2026 08:45:00', '2026-10-10T08:45:00', '2026-10-12T13:00:00', N'Nhận tại cửa hàng'),
(6, N'Đặng Tuấn Kiệt', N'tuankiet@example.com', N'0901000006', N'31 Lạch Tray, Hải Phòng', 4, N'Khách đổi ý trước khi gửi', N'10/10/2026 15:00:00', '2026-10-10T15:00:00', NULL, N'Giao hàng tận nơi'),
(7, N'Bùi Thanh Tâm', N'thanhtam@example.com', N'0901000007', N'09 Phan Đình Phùng, Đà Lạt', 3, N'Đã nhận hàng', N'11/10/2026 12:10:00', '2026-10-11T12:10:00', '2026-10-13T10:30:00', N'Giao hàng tận nơi'),
(8, N'Ngô Hải Yến', N'haiyen@example.com', N'0901000008', N'73 Điện Biên Phủ, Bình Thạnh, TP. Hồ Chí Minh', 3, N'Hoàn tiền một phần', N'12/10/2026 16:40:00', '2026-10-12T16:40:00', '2026-10-13T09:00:00', N'Giao hàng tận nơi');
SET IDENTITY_INSERT [hoa_don] OFF;

INSERT INTO [chi_tiet_hoa_don] (
    [mahoadon], [machitietsanpham], [soluong], [giatien], [gia_von]
) VALUES
(1, 1, 2, N'199000', 80000.00),
(2, 2, 1, N'199000', 80000.00),
(3, 3, 2, N'349000', 150000.00),
(4, 4, 1, N'349000', 150000.00),
(5, 5, 2, N'499000', 230000.00),
(6, 6, 1, N'499000', 230000.00),
(7, 7, 1, N'599000', 290000.00),
(8, 8, 1, N'599000', 290000.00);

SET IDENTITY_INSERT [hoan_tien] ON;
INSERT INTO [hoan_tien] ([id], [invoice_id], [refunded_at], [reason])
VALUES (1, 8, '2026-10-13T11:00:00', N'Bù trừ phí giao hàng');
SET IDENTITY_INSERT [hoan_tien] OFF;

INSERT INTO [chi_tiet_hoan_tien] ([refund_id], [invoice_id], [variant_id], [amount], [quantity])
VALUES (1, 8, 8, 50000.00, 1);

SET IDENTITY_INSERT [tra_hang] ON;
INSERT INTO [tra_hang] ([id], [invoice_id], [received_at], [reason])
VALUES (1, 7, '2026-10-13T12:00:00', N'Khách đổi size');
SET IDENTITY_INSERT [tra_hang] OFF;

INSERT INTO [chi_tiet_tra_hang] ([return_id], [invoice_id], [variant_id], [quantity], [unit_cost])
VALUES (1, 7, 7, 1, 290000.00);

INSERT INTO [bien_dong_kho] (
    [variant_id], [movement_type], [quantity_delta], [unit_cost], [reference_id], [occurred_at], [note]
) VALUES
(1, N'SALE', -2, 80000.00, N'ORDER:1', '2026-10-07T10:00:00', N'Đơn hàng mẫu #1'),
(2, N'SALE', -1, 80000.00, N'ORDER:2', '2026-10-08T11:30:00', N'Đơn hàng mẫu #2'),
(3, N'SALE', -2, 150000.00, N'ORDER:3', '2026-10-09T09:15:00', N'Đơn hàng mẫu #3'),
(4, N'SALE', -1, 150000.00, N'ORDER:4', '2026-10-09T14:20:00', N'Đơn hàng mẫu #4'),
(5, N'SALE', -2, 230000.00, N'ORDER:5', '2026-10-10T08:45:00', N'Đơn hàng mẫu #5'),
(6, N'SALE', -1, 230000.00, N'ORDER:6', '2026-10-10T15:00:00', N'Đơn hàng mẫu #6'),
(6, N'ORDER_CANCELLATION', 1, 230000.00, N'ORDER:6', '2026-10-10T16:00:00', N'Hoàn tồn đơn mẫu #6'),
(7, N'SALE', -1, 290000.00, N'ORDER:7', '2026-10-11T12:10:00', N'Đơn hàng mẫu #7'),
(7, N'CUSTOMER_RETURN', 1, 290000.00, N'ORDER:7', '2026-10-13T12:00:00', N'Nhập lại hàng khách trả'),
(8, N'SALE', -1, 290000.00, N'ORDER:8', '2026-10-12T16:40:00', N'Đơn hàng mẫu #8');

UPDATE [chi_tiet_san_pham]
SET [soluong] = CASE [machitietsanpham]
    WHEN 1 THEN 28
    WHEN 2 THEN 44
    WHEN 3 THEN 23
    WHEN 4 THEN 19
    WHEN 5 THEN 16
    WHEN 6 THEN 16
    WHEN 7 THEN 22
    WHEN 8 THEN 13
END
WHERE [machitietsanpham] BETWEEN 1 AND 8;

INSERT INTO [chitietkhuyenmai] ([masanpham], [makhuyenmai]) VALUES
(1, 1),
(2, 1),
(3, 2),
(4, 2);
