# Hướng dẫn chạy dự án Web Quản lý Vé Tàu

## 1. Môi trường

- Java JDK 8 trở lên
- Apache Tomcat 9
- NetBeans
- SQL Server
- JDBC Driver SQL Server

## 2. Thứ tự chạy SQL

Chạy theo thứ tự:

1. VeTauDB_trang_thai_UPDATED_GD1_GD2.sql
2. UPDATE_DB_GD3_CLEANUP.sql
3. SEED_15_KICH_BAN_MAU_GD1_GD2_GD3.sql
4. UPDATE_DB_GD4_REFUND_ADMIN_DTO_CLEAN.sql
5. UPDATE_DB_GD5_REPORTING_CLEAN.sql
6. FINAL_VERIFY_DATABASE_GD8.sql

## 3. Cấu hình database

Kiểm tra file:

src/java/vetau/util/DBConnection.java

Đảm bảo đúng:

- Tên database: VeTauDB
- Username/password SQL Server
- Port SQL Server

## 4. Chạy project

1. Mở project bằng NetBeans.
2. Clean and Build.
3. Run project trên Tomcat.
4. Truy cập:

http://localhost:8080/WEB_QUANLYVETAU/home

## 5. Tài khoản admin demo

- URL: /admin/login
- Email: admin@vetau.vn
- Password: admin123

## 6. Các chức năng chính

- Tìm chuyến tàu
- Đặt vé
- Thanh toán
- Tra cứu vé
- Xem lịch sử đặt vé
- Trả vé
- Admin duyệt hoàn tiền
- Admin dashboard
- Báo cáo doanh thu
- Kiểm tra toàn vẹn hệ thống

## 7. Trang kiểm tra hệ thống

Sau khi đăng nhập admin, mở:

/admin/system-check

Trang này kiểm tra:

- Vé trùng mã
- Hoàn tiền chờ xử lý bị trùng
- Trạng thái lạ
- Dữ liệu mồ côi
- Dữ liệu báo cáo