# bai_tap_jpa — Login (Cookie/Session) + CRUD Category bằng JPA/Hibernate

Bài tập ngày 27/08/2026: viết lại Bài 1 (Login) và CRUD Category bằng **JPA API (Hibernate)** thay vì JDBC thuần.

## 1. Yêu cầu môi trường
- JDK 17+
- Maven 3.9+
- Tomcat 10.1+ (Jakarta Servlet 6.0)
- SQL Server 2019+

## 2. Cài đặt Database
Mở SSMS, chạy file `create_jpa_db.sql` để tạo database `QLJPA` (rỗng — các bảng sẽ được **Hibernate tự động tạo** nhờ `hibernate.hbm2ddl.auto=update` khi chạy ứng dụng lần đầu).

## 3. Cấu hình kết nối
Sửa lại `src/main/resources/META-INF/persistence.xml` cho khớp SQL Server máy bạn (user/password của tài khoản `sa`).

## 4. Tạo tài khoản test
Vì Hibernate tự tạo bảng rỗng, cần thêm dữ liệu mẫu. Sau khi chạy ứng dụng lần đầu (bảng `app_user` đã được tạo), chạy trong SSMS:
```sql
USE QLJPA;
INSERT INTO app_user (email, username, fullname, password, roleid, phone, createdDate)
VALUES
('admin@example.com', 'admin', N'Quản trị viên', '123456', 1, '0900000000', GETDATE()),
('trungnh@example.com', 'trungnh', N'Nguyễn Hữu Trung', '123456', 3, '0900000001', GETDATE());
```

## 5. Tạo thư mục lưu ảnh upload
```powershell
New-Item -ItemType Directory -Force -Path "C:\upload_jpa"
```

## 6. Import & chạy
- Import vào Eclipse/STS: `File → Import → Existing Maven Projects`
- Maven → Update Project (Alt+F5)
- Run As → Run on Server (Tomcat 10.1)
- Truy cập: `http://localhost:8080/bai_tap_jpa/login`

## Kiến trúc
```
h1.entity      -> JPA Entity (Category, AppUser) — thay cho h1.model ở bản JDBC
h1.config      -> JpaConfig (EntityManagerFactory), Constant
h1.dao         -> Category/User DAO dùng EntityManager (persist/merge/remove/find)
h1.service     -> Business logic (giữ nguyên tư duy tầng Service như bản JDBC)
h1.controller  -> Servlet — có thêm bước kiểm tra đăng nhập (session) trước khi vào /admin/*
```

## Các điểm đã sửa so với tài liệu gốc (lỗi thật trong tài liệu)
1. `JpaConfig` gốc tạo `EntityManagerFactory` mới **mỗi lần gọi** `getEntityManager()` — rất tốn tài nguyên. Đã sửa thành singleton (tạo 1 lần duy nhất).
2. `CategoryDao.findByCategoryname()` gốc gọi `getSingleResult()` rồi mới check `null` — nhưng `getSingleResult()` sẽ **ném exception** nếu không tìm thấy (không bao giờ trả về `null`), khiến đoạn check `null` phía sau vô nghĩa. Đã bắt `NoResultException` và trả về `null` đúng như `CategoryServiceImpl.insert()` mong đợi.
3. `CategoryDao.searchByName()` gốc có bug: JPQL dùng `c.catename` (sai tên field, field thật là `categoryname`) và `setParameter("catename", ...)` không khớp tên tham số `:catname` trong query — sẽ lỗi runtime. Đã sửa khớp lại.
4. Nhiều hàm DAO gốc không gọi `enma.close()` sau khi query xong (rò rỉ kết nối). Đã thêm `try/finally` để đóng `EntityManager` đầy đủ.
5. `CategoryController` gốc **không kiểm tra đăng nhập** trước khi cho vào các trang `/admin/category/*` — ai cũng vào được dù chưa login. Đã thêm kiểm tra session.
6. Đọc field text trong multipart form (`categoryname`, `images`, `categoryid`) qua `Part.getInputStream()` thay vì `request.getParameter()` — vì Tomcat không áp dụng `setCharacterEncoding("UTF-8")` cho multipart, gây lỗi hiển thị tiếng Việt (đã phát hiện lỗi này thực tế ở bản JDBC trước đó).
7. JSP gốc dùng `${cate.images.substring(0,5)=='https'}` — nếu `images` là `null` hoặc chuỗi ngắn hơn 5 ký tự sẽ ném lỗi. Đã đổi sang `fn:startsWith()` của JSTL, an toàn hơn.
