USE VeTauDB;
GO

/* =========================================================
   FINAL_VERIFY_DATABASE_GD8.sql
   Kiểm tra toàn vẹn dữ liệu cuối cùng trước khi demo/nộp bài
   ========================================================= */

PRINT N'1. Tổng hợp trạng thái DAT_CHO';
SELECT trang_thai, COUNT(*) AS so_luong
FROM dbo.DAT_CHO
GROUP BY trang_thai
ORDER BY trang_thai;
GO

PRINT N'2. Tổng hợp trạng thái VE';
SELECT trang_thai, COUNT(*) AS so_luong
FROM dbo.VE
GROUP BY trang_thai
ORDER BY trang_thai;
GO

PRINT N'3. Tổng hợp trạng thái THANH_TOAN';
SELECT
    trang_thai,
    COUNT(*) AS so_luong,
    COALESCE(SUM(so_tien), 0) AS tong_tien
FROM dbo.THANH_TOAN
GROUP BY trang_thai
ORDER BY trang_thai;
GO

PRINT N'4. Tổng hợp trạng thái HOAN_TIEN';
SELECT
    trang_thai,
    COUNT(*) AS so_luong,
    COALESCE(SUM(so_tien_hoan), 0) AS tong_tien_hoan
FROM dbo.HOAN_TIEN
GROUP BY trang_thai
ORDER BY trang_thai;
GO

PRINT N'5. Kiểm tra mã vé trùng';
SELECT ma_ve, COUNT(*) AS so_lan
FROM dbo.VE
GROUP BY ma_ve
HAVING COUNT(*) > 1;
GO

PRINT N'6. Kiểm tra hoàn tiền Chờ xử lý bị trùng theo vé';
SELECT ve_id, COUNT(*) AS so_yeu_cau_cho_xu_ly
FROM dbo.HOAN_TIEN
WHERE trang_thai = N'Chờ xử lý'
GROUP BY ve_id
HAVING COUNT(*) > 1;
GO

PRINT N'7. Kiểm tra DAT_CHO có trạng thái lạ';
SELECT id, ma_dat_cho, trang_thai
FROM dbo.DAT_CHO
WHERE trang_thai NOT IN (
    N'Chờ thanh toán',
    N'Đã thanh toán',
    N'Đã hủy',
    N'Hết hạn',
    N'Hoàn tiền một phần',
    N'Hoàn tiền toàn bộ'
);
GO

PRINT N'8. Kiểm tra VE có trạng thái lạ';
SELECT id, ma_ve, trang_thai
FROM dbo.VE
WHERE trang_thai NOT IN (
    N'Chờ thanh toán',
    N'Hợp lệ',
    N'Đã sử dụng',
    N'Đã trả',
    N'Đã hủy',
    N'Hết hạn'
);
GO

PRINT N'9. Kiểm tra THANH_TOAN có trạng thái lạ';
SELECT id, dat_cho_id, trang_thai
FROM dbo.THANH_TOAN
WHERE trang_thai NOT IN (
    N'Chờ xử lý',
    N'Thành công',
    N'Thất bại',
    N'Đã hủy'
);
GO

PRINT N'10. Kiểm tra HOAN_TIEN có trạng thái lạ';
SELECT id, ve_id, trang_thai
FROM dbo.HOAN_TIEN
WHERE trang_thai NOT IN (
    N'Chờ xử lý',
    N'Hoàn tất',
    N'Từ chối'
);
GO

PRINT N'11. Kiểm tra VE không có DAT_CHO';
SELECT v.id, v.ma_ve, v.dat_cho_id
FROM dbo.VE v
LEFT JOIN dbo.DAT_CHO dc ON dc.id = v.dat_cho_id
WHERE dc.id IS NULL;
GO

PRINT N'12. Kiểm tra THANH_TOAN không có DAT_CHO';
SELECT tt.id, tt.dat_cho_id, tt.so_tien, tt.trang_thai
FROM dbo.THANH_TOAN tt
LEFT JOIN dbo.DAT_CHO dc ON dc.id = tt.dat_cho_id
WHERE dc.id IS NULL;
GO

PRINT N'13. Kiểm tra dữ liệu dashboard';
SELECT
    N'Thanh toán thành công' AS chi_so,
    COUNT(*) AS so_luong,
    COALESCE(SUM(so_tien), 0) AS tong_tien
FROM dbo.THANH_TOAN
WHERE trang_thai = N'Thành công';
GO

PRINT N'14. Kiểm tra dữ liệu refund admin';
SELECT TOP 20
    ht.id AS hoan_tien_id,
    ht.ve_id,
    v.ma_ve,
    dc.ma_dat_cho,
    ht.so_tien_hoan,
    ht.trang_thai,
    ht.thoi_gian_yeu_cau,
    ht.thoi_gian_hoan_tat
FROM dbo.HOAN_TIEN ht
JOIN dbo.VE v ON v.id = ht.ve_id
JOIN dbo.DAT_CHO dc ON dc.id = v.dat_cho_id
ORDER BY ht.id DESC;
GO