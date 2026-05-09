USE VeTauDB;
GO

/* =========================================================
   UPDATE_DB_GD5_REPORTING_CLEAN.sql

   Bản sửa cho Giai đoạn 5:
   - Tránh lỗi/cảnh báo "index already exists" trong SSMS Error List.
   - Tạo index bằng dynamic SQL sau khi kiểm tra sys.indexes.
   - Có thể chạy lại nhiều lần.
   - Nếu index đã tồn tại thì chỉ PRINT và bỏ qua.
   ========================================================= */


/* =========================================================
   0. Kiểm tra bảng cần thiết
   ========================================================= */

IF OBJECT_ID(N'dbo.THANH_TOAN', N'U') IS NULL
BEGIN
    RAISERROR(N'Không tìm thấy bảng dbo.THANH_TOAN.', 16, 1);
    RETURN;
END
GO

IF OBJECT_ID(N'dbo.HOAN_TIEN', N'U') IS NULL
BEGIN
    RAISERROR(N'Không tìm thấy bảng dbo.HOAN_TIEN.', 16, 1);
    RETURN;
END
GO

IF OBJECT_ID(N'dbo.DAT_CHO', N'U') IS NULL
BEGIN
    RAISERROR(N'Không tìm thấy bảng dbo.DAT_CHO.', 16, 1);
    RETURN;
END
GO

IF OBJECT_ID(N'dbo.VE', N'U') IS NULL
BEGIN
    RAISERROR(N'Không tìm thấy bảng dbo.VE.', 16, 1);
    RETURN;
END
GO

IF OBJECT_ID(N'dbo.KHACH_HANG', N'U') IS NULL
BEGIN
    RAISERROR(N'Không tìm thấy bảng dbo.KHACH_HANG.', 16, 1);
    RETURN;
END
GO


/* =========================================================
   1. THANH_TOAN: doanh thu theo trạng thái/thời gian
   ========================================================= */

DECLARE @sql NVARCHAR(MAX);

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = N'IX_THANH_TOAN_TrangThai_NgayThanhToan'
      AND object_id = OBJECT_ID(N'dbo.THANH_TOAN')
)
BEGIN
    SET @sql = N'
        CREATE INDEX IX_THANH_TOAN_TrangThai_NgayThanhToan
        ON dbo.THANH_TOAN (trang_thai, ngay_thanh_toan)
        INCLUDE (so_tien, phuong_thuc, dat_cho_id);';

    EXEC sp_executesql @sql;
    PRINT N'Created IX_THANH_TOAN_TrangThai_NgayThanhToan';
END
ELSE
BEGIN
    PRINT N'Đã tồn tại IX_THANH_TOAN_TrangThai_NgayThanhToan - bỏ qua.';
END
GO


/* =========================================================
   2. HOAN_TIEN: hoàn tiền theo trạng thái/thời gian hoàn tất
   ========================================================= */

DECLARE @sql NVARCHAR(MAX);

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = N'IX_HOAN_TIEN_TrangThai_ThoiGianHoanTat'
      AND object_id = OBJECT_ID(N'dbo.HOAN_TIEN')
)
BEGIN
    SET @sql = N'
        CREATE INDEX IX_HOAN_TIEN_TrangThai_ThoiGianHoanTat
        ON dbo.HOAN_TIEN (trang_thai, thoi_gian_hoan_tat)
        INCLUDE (so_tien_hoan, ve_id, thanh_toan_id);';

    EXEC sp_executesql @sql;
    PRINT N'Created IX_HOAN_TIEN_TrangThai_ThoiGianHoanTat';
END
ELSE
BEGIN
    PRINT N'Đã tồn tại IX_HOAN_TIEN_TrangThai_ThoiGianHoanTat - bỏ qua.';
END
GO


/* =========================================================
   3. DAT_CHO: thống kê đơn theo trạng thái/ngày đặt
   ========================================================= */

DECLARE @sql NVARCHAR(MAX);

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = N'IX_DAT_CHO_TrangThai_NgayDat'
      AND object_id = OBJECT_ID(N'dbo.DAT_CHO')
)
BEGIN
    SET @sql = N'
        CREATE INDEX IX_DAT_CHO_TrangThai_NgayDat
        ON dbo.DAT_CHO (trang_thai, ngay_dat)
        INCLUDE (tong_thanh_toan, khach_hang_id, ma_dat_cho);';

    EXEC sp_executesql @sql;
    PRINT N'Created IX_DAT_CHO_TrangThai_NgayDat';
END
ELSE
BEGIN
    PRINT N'Đã tồn tại IX_DAT_CHO_TrangThai_NgayDat - bỏ qua.';
END
GO


/* =========================================================
   4. VE: thống kê vé theo trạng thái/đơn
   ========================================================= */

DECLARE @sql NVARCHAR(MAX);

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = N'IX_VE_TrangThai_DatCho'
      AND object_id = OBJECT_ID(N'dbo.VE')
)
BEGIN
    SET @sql = N'
        CREATE INDEX IX_VE_TrangThai_DatCho
        ON dbo.VE (trang_thai, dat_cho_id)
        INCLUDE (gia_ve_chi_tiet, ga_di_id, ga_den_id, ghe_id, chuyen_tau_id);';

    EXEC sp_executesql @sql;
    PRINT N'Created IX_VE_TrangThai_DatCho';
END
ELSE
BEGIN
    PRINT N'Đã tồn tại IX_VE_TrangThai_DatCho - bỏ qua.';
END
GO


/* =========================================================
   5. KHACH_HANG: thống kê khách mới
   ========================================================= */

DECLARE @sql NVARCHAR(MAX);

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = N'IX_KHACH_HANG_NgayTao'
      AND object_id = OBJECT_ID(N'dbo.KHACH_HANG')
)
BEGIN
    SET @sql = N'
        CREATE INDEX IX_KHACH_HANG_NgayTao
        ON dbo.KHACH_HANG (ngay_tao)
        INCLUDE (trang_thai);';

    EXEC sp_executesql @sql;
    PRINT N'Created IX_KHACH_HANG_NgayTao';
END
ELSE
BEGIN
    PRINT N'Đã tồn tại IX_KHACH_HANG_NgayTao - bỏ qua.';
END
GO


/* =========================================================
   6. Kiểm tra nhanh index sau khi chạy
   ========================================================= */

SELECT
    i.name AS index_name,
    OBJECT_NAME(i.object_id) AS table_name,
    i.is_unique,
    i.has_filter,
    i.filter_definition
FROM sys.indexes i
WHERE i.name IN (
    N'IX_THANH_TOAN_TrangThai_NgayThanhToan',
    N'IX_HOAN_TIEN_TrangThai_ThoiGianHoanTat',
    N'IX_DAT_CHO_TrangThai_NgayDat',
    N'IX_VE_TrangThai_DatCho',
    N'IX_KHACH_HANG_NgayTao'
)
ORDER BY table_name, index_name;
GO


/* =========================================================
   7. Test nhanh dữ liệu phục vụ dashboard/report
   ========================================================= */

SELECT
    N'THANH_TOAN thành công' AS chi_so,
    COUNT(*) AS so_luong,
    COALESCE(SUM(so_tien), 0) AS tong_tien
FROM dbo.THANH_TOAN
WHERE trang_thai = N'Thành công';
GO

SELECT
    N'VE thống kê báo cáo' AS chi_so,
    COUNT(*) AS so_luong
FROM dbo.VE
WHERE trang_thai IN (N'Hợp lệ', N'Đã sử dụng', N'Đã trả');
GO

SELECT
    N'HOAN_TIEN chờ xử lý' AS chi_so,
    COUNT(*) AS so_luong,
    COALESCE(SUM(so_tien_hoan), 0) AS tong_tien_cho_xu_ly
FROM dbo.HOAN_TIEN
WHERE trang_thai = N'Chờ xử lý';
GO
