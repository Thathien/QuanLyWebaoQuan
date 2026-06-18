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

SET IDENTITY_INSERT [chi_tiet_san_pham] ON;
INSERT INTO [chi_tiet_san_pham] (
    [machitietsanpham], [masanpham], [masize], [mamau], [soluong], [ngaynhap]
) VALUES
(1, 1, 1, 1, 30, N'2026-06-01'),
(2, 1, 2, 1, 45, N'2026-06-01'),
(3, 2, 2, 2, 25, N'2026-06-02'),
(4, 2, 3, 2, 20, N'2026-06-02'),
(5, 3, 2, 3, 18, N'2026-06-03'),
(6, 3, 3, 3, 16, N'2026-06-03'),
(7, 4, 3, 2, 22, N'2026-06-04'),
(8, 4, 4, 2, 14, N'2026-06-04'),
(9, 1, 3, 1, 18, N'2026-06-05'),
(10, 2, 4, 3, 14, N'2026-06-05'),
(11, 3, 1, 2, 20, N'2026-06-06'),
(12, 4, 2, 4, 24, N'2026-06-06');
SET IDENTITY_INSERT [chi_tiet_san_pham] OFF;

INSERT INTO [chitietkhuyenmai] ([masanpham], [makhuyenmai]) VALUES
(1, 1),
(2, 1),
(3, 2),
(4, 2);
