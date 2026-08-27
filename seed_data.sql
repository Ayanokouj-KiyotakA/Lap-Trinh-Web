-- Chạy sau khi đã khởi động ứng dụng ít nhất 1 lần (để Hibernate tự tạo bảng app_user, categories)
USE QLJPA;
GO

INSERT INTO app_user (email, username, fullname, password, roleid, phone, createdDate)
VALUES
('admin@example.com', 'admin', N'Quản trị viên', '123456', 1, '0900000000', GETDATE()),
('trungnh@example.com', 'trungnh', N'Nguyễn Hữu Trung', '123456', 3, '0900000001', GETDATE());
GO

INSERT INTO categories (categoryname, images, status) VALUES
(N'Quần áo nam', 'avatar.png', 1),
(N'Quần áo nữ', 'avatar.png', 1);
GO
