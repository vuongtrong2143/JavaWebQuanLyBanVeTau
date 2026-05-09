USE VeTauDB;
GO

/*=============================================================================
  MIGRATION ĐỒNG BỘ DATABASE VỚI CODE GIAI ĐOẠN 1-2
  Chạy file này trên database VeTauDB hiện tại, không cần drop database.

  Nội dung cập nhật:
  1) Chuẩn hóa trạng thái GA / TAU / TOA_TAU.
  2) Cho phép VE.trang_thai = N'Chờ thanh toán'.
  3) Đổi default VE.trang_thai sang N'Chờ thanh toán'.
  4) Chuẩn hóa GIU_CHO: dùng N'Đã hủy' thay cho N'Hủy'.
  5) Chuẩn hóa HOAN_TIEN: Chờ xử lý / Hoàn tất / Từ chối.
  6) Bổ sung phương thức THANH_TOAN ATM / Thẻ ATM để tránh lỗi nếu UI dùng.
=============================================================================*/

SET NOCOUNT ON;
GO

/*=============================================================================
  1. Đảm bảo GA có trang_thai + constraint thống nhất
=============================================================================*/
IF COL_LENGTH('dbo.GA', 'trang_thai') IS NULL
BEGIN
    ALTER TABLE dbo.GA
    ADD trang_thai NVARCHAR(50) NOT NULL
        CONSTRAINT DF_GA_trang_thai DEFAULT N'Hoạt động';
END;
GO

UPDATE dbo.GA
SET trang_thai = CASE
    WHEN trang_thai IS NULL OR LTRIM(RTRIM(trang_thai)) = N'' THEN N'Hoạt động'
    WHEN trang_thai IN (N'Tạm ngưng', N'Ngưng hoạt động', N'Khóa', N'Bảo trì') THEN N'Tạm dừng'
    WHEN trang_thai IN (N'Hoạt động', N'Tạm dừng') THEN trang_thai
    ELSE N'Tạm dừng'
END;
GO

ALTER TABLE dbo.GA ALTER COLUMN trang_thai NVARCHAR(50) NOT NULL;
GO

IF EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = N'CK_GA_trang_thai' AND parent_object_id = OBJECT_ID(N'dbo.GA'))
    ALTER TABLE dbo.GA DROP CONSTRAINT CK_GA_trang_thai;
GO

ALTER TABLE dbo.GA
ADD CONSTRAINT CK_GA_trang_thai
CHECK (trang_thai IN (N'Hoạt động', N'Tạm dừng'));
GO

IF NOT EXISTS (
    SELECT 1
    FROM sys.default_constraints dc
    JOIN sys.columns c ON c.object_id = dc.parent_object_id AND c.column_id = dc.parent_column_id
    WHERE dc.parent_object_id = OBJECT_ID(N'dbo.GA')
      AND c.name = N'trang_thai'
)
BEGIN
    ALTER TABLE dbo.GA
    ADD CONSTRAINT DF_GA_trang_thai DEFAULT N'Hoạt động' FOR trang_thai;
END;
GO

/*=============================================================================
  2. Đảm bảo TAU có trang_thai + constraint thống nhất
=============================================================================*/
IF COL_LENGTH('dbo.TAU', 'trang_thai') IS NULL
BEGIN
    ALTER TABLE dbo.TAU
    ADD trang_thai NVARCHAR(50) NOT NULL
        CONSTRAINT DF_TAU_trang_thai DEFAULT N'Hoạt động';
END;
GO

UPDATE dbo.TAU
SET trang_thai = CASE
    WHEN trang_thai IS NULL OR LTRIM(RTRIM(trang_thai)) = N'' THEN N'Hoạt động'
    WHEN trang_thai IN (N'Tạm ngưng', N'Ngưng hoạt động', N'Khóa', N'Bảo trì') THEN N'Tạm dừng'
    WHEN trang_thai IN (N'Hoạt động', N'Tạm dừng') THEN trang_thai
    ELSE N'Tạm dừng'
END;
GO

ALTER TABLE dbo.TAU ALTER COLUMN trang_thai NVARCHAR(50) NOT NULL;
GO

IF EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = N'CK_TAU_trang_thai' AND parent_object_id = OBJECT_ID(N'dbo.TAU'))
    ALTER TABLE dbo.TAU DROP CONSTRAINT CK_TAU_trang_thai;
GO

ALTER TABLE dbo.TAU
ADD CONSTRAINT CK_TAU_trang_thai
CHECK (trang_thai IN (N'Hoạt động', N'Tạm dừng'));
GO

IF NOT EXISTS (
    SELECT 1
    FROM sys.default_constraints dc
    JOIN sys.columns c ON c.object_id = dc.parent_object_id AND c.column_id = dc.parent_column_id
    WHERE dc.parent_object_id = OBJECT_ID(N'dbo.TAU')
      AND c.name = N'trang_thai'
)
BEGIN
    ALTER TABLE dbo.TAU
    ADD CONSTRAINT DF_TAU_trang_thai DEFAULT N'Hoạt động' FOR trang_thai;
END;
GO

/*=============================================================================
  3. Đảm bảo TOA_TAU có trang_thai + constraint thống nhất
=============================================================================*/
IF COL_LENGTH('dbo.TOA_TAU', 'trang_thai') IS NULL
BEGIN
    ALTER TABLE dbo.TOA_TAU
    ADD trang_thai NVARCHAR(50) NOT NULL
        CONSTRAINT DF_TOA_TAU_trang_thai DEFAULT N'Hoạt động';
END;
GO

UPDATE dbo.TOA_TAU
SET trang_thai = CASE
    WHEN trang_thai IS NULL OR LTRIM(RTRIM(trang_thai)) = N'' THEN N'Hoạt động'
    WHEN trang_thai IN (N'Tạm ngưng', N'Ngưng hoạt động', N'Khóa', N'Bảo trì') THEN N'Tạm dừng'
    WHEN trang_thai IN (N'Hoạt động', N'Tạm dừng') THEN trang_thai
    ELSE N'Tạm dừng'
END;
GO

ALTER TABLE dbo.TOA_TAU ALTER COLUMN trang_thai NVARCHAR(50) NOT NULL;
GO

IF EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = N'CK_TOA_TAU_trang_thai' AND parent_object_id = OBJECT_ID(N'dbo.TOA_TAU'))
    ALTER TABLE dbo.TOA_TAU DROP CONSTRAINT CK_TOA_TAU_trang_thai;
GO

ALTER TABLE dbo.TOA_TAU
ADD CONSTRAINT CK_TOA_TAU_trang_thai
CHECK (trang_thai IN (N'Hoạt động', N'Tạm dừng'));
GO

IF NOT EXISTS (
    SELECT 1
    FROM sys.default_constraints dc
    JOIN sys.columns c ON c.object_id = dc.parent_object_id AND c.column_id = dc.parent_column_id
    WHERE dc.parent_object_id = OBJECT_ID(N'dbo.TOA_TAU')
      AND c.name = N'trang_thai'
)
BEGIN
    ALTER TABLE dbo.TOA_TAU
    ADD CONSTRAINT DF_TOA_TAU_trang_thai DEFAULT N'Hoạt động' FOR trang_thai;
END;
GO

/*=============================================================================
  4. Cập nhật VE cho luồng Giai đoạn 2: Chờ thanh toán -> Hợp lệ
=============================================================================*/
IF EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = N'CK_VE_trang_thai' AND parent_object_id = OBJECT_ID(N'dbo.VE'))
    ALTER TABLE dbo.VE DROP CONSTRAINT CK_VE_trang_thai;
GO

UPDATE dbo.VE
SET trang_thai = CASE
    WHEN trang_thai IS NULL OR LTRIM(RTRIM(trang_thai)) = N'' THEN N'Chờ thanh toán'
    WHEN trang_thai = N'Đã đổi' THEN N'Đã hủy'
    WHEN trang_thai IN (N'Chờ thanh toán', N'Hợp lệ', N'Đã sử dụng', N'Đã trả', N'Đã hủy') THEN trang_thai
    ELSE N'Đã hủy'
END;
GO

ALTER TABLE dbo.VE ALTER COLUMN trang_thai NVARCHAR(20) NOT NULL;
GO

ALTER TABLE dbo.VE
ADD CONSTRAINT CK_VE_trang_thai
CHECK (trang_thai IN (N'Chờ thanh toán', N'Hợp lệ', N'Đã sử dụng', N'Đã trả', N'Đã hủy'));
GO

DECLARE @dfVe NVARCHAR(128);
DECLARE @sqlVe NVARCHAR(MAX);
SELECT @dfVe = dc.name
FROM sys.default_constraints dc
JOIN sys.columns c ON c.object_id = dc.parent_object_id AND c.column_id = dc.parent_column_id
WHERE dc.parent_object_id = OBJECT_ID(N'dbo.VE')
  AND c.name = N'trang_thai';

IF @dfVe IS NOT NULL
BEGIN
    SET @sqlVe = N'ALTER TABLE dbo.VE DROP CONSTRAINT ' + QUOTENAME(@dfVe);
    EXEC sp_executesql @sqlVe;
END;
GO

ALTER TABLE dbo.VE
ADD CONSTRAINT DF_VE_trang_thai DEFAULT N'Chờ thanh toán' FOR trang_thai;
GO

/*=============================================================================
  5. Cập nhật GIU_CHO: dùng Đã hủy thay cho Hủy
=============================================================================*/
IF EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = N'CK_GIU_CHO_trang_thai' AND parent_object_id = OBJECT_ID(N'dbo.GIU_CHO'))
    ALTER TABLE dbo.GIU_CHO DROP CONSTRAINT CK_GIU_CHO_trang_thai;
GO

UPDATE dbo.GIU_CHO
SET trang_thai = CASE
    WHEN trang_thai = N'Hủy' THEN N'Đã hủy'
    WHEN trang_thai IN (N'Đang giữ', N'Đã chuyển vé', N'Hết hạn', N'Đã hủy') THEN trang_thai
    ELSE N'Đã hủy'
END;
GO

ALTER TABLE dbo.GIU_CHO
ADD CONSTRAINT CK_GIU_CHO_trang_thai
CHECK (trang_thai IN (N'Đang giữ', N'Đã chuyển vé', N'Hết hạn', N'Đã hủy'));
GO

/*=============================================================================
  6. Cập nhật HOAN_TIEN: Chờ xử lý / Hoàn tất / Từ chối
=============================================================================*/
IF EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = N'CK_HOAN_TIEN_trang_thai' AND parent_object_id = OBJECT_ID(N'dbo.HOAN_TIEN'))
    ALTER TABLE dbo.HOAN_TIEN DROP CONSTRAINT CK_HOAN_TIEN_trang_thai;
GO

UPDATE dbo.HOAN_TIEN
SET trang_thai = CASE
    WHEN trang_thai IS NULL OR LTRIM(RTRIM(trang_thai)) = N'' THEN N'Chờ xử lý'
    WHEN trang_thai = N'Pending' THEN N'Chờ xử lý'
    WHEN trang_thai IN (N'Thành công', N'Đã hoàn tiền') THEN N'Hoàn tất'
    WHEN trang_thai IN (N'Thất bại', N'Hủy') THEN N'Từ chối'
    WHEN trang_thai IN (N'Chờ xử lý', N'Hoàn tất', N'Từ chối') THEN trang_thai
    ELSE N'Chờ xử lý'
END;
GO

ALTER TABLE dbo.HOAN_TIEN ALTER COLUMN trang_thai NVARCHAR(20) NOT NULL;
GO

ALTER TABLE dbo.HOAN_TIEN
ADD CONSTRAINT CK_HOAN_TIEN_trang_thai
CHECK (trang_thai IN (N'Chờ xử lý', N'Hoàn tất', N'Từ chối'));
GO

DECLARE @dfHoanTien NVARCHAR(128);
DECLARE @sqlHoanTien NVARCHAR(MAX);
SELECT @dfHoanTien = dc.name
FROM sys.default_constraints dc
JOIN sys.columns c ON c.object_id = dc.parent_object_id AND c.column_id = dc.parent_column_id
WHERE dc.parent_object_id = OBJECT_ID(N'dbo.HOAN_TIEN')
  AND c.name = N'trang_thai';

IF @dfHoanTien IS NOT NULL
BEGIN
    SET @sqlHoanTien = N'ALTER TABLE dbo.HOAN_TIEN DROP CONSTRAINT ' + QUOTENAME(@dfHoanTien);
    EXEC sp_executesql @sqlHoanTien;
END;
GO

ALTER TABLE dbo.HOAN_TIEN
ADD CONSTRAINT DF_HOAN_TIEN_trang_thai DEFAULT N'Chờ xử lý' FOR trang_thai;
GO

/*=============================================================================
  7. Cập nhật THANH_TOAN phuong_thuc để tránh lỗi nếu UI dùng ATM/Thẻ ATM
=============================================================================*/
IF EXISTS (SELECT 1 FROM sys.check_constraints WHERE name = N'CK_THANH_TOAN_phuong_thuc' AND parent_object_id = OBJECT_ID(N'dbo.THANH_TOAN'))
    ALTER TABLE dbo.THANH_TOAN DROP CONSTRAINT CK_THANH_TOAN_phuong_thuc;
GO

ALTER TABLE dbo.THANH_TOAN
ADD CONSTRAINT CK_THANH_TOAN_phuong_thuc
CHECK (phuong_thuc IN (N'VNPay', N'MoMo', N'OnePay', N'Tiền mặt', N'Chuyển khoản', N'ATM', N'Thẻ ATM'));
GO

/*=============================================================================
  8. Kiểm tra nhanh sau migration
=============================================================================*/
SELECT N'VE' AS bang, trang_thai, COUNT(*) AS so_dong
FROM dbo.VE
GROUP BY trang_thai
UNION ALL
SELECT N'GIU_CHO', trang_thai, COUNT(*)
FROM dbo.GIU_CHO
GROUP BY trang_thai
UNION ALL
SELECT N'HOAN_TIEN', trang_thai, COUNT(*)
FROM dbo.HOAN_TIEN
GROUP BY trang_thai
ORDER BY bang, trang_thai;
GO

SELECT
    cc.name AS constraint_name,
    OBJECT_NAME(cc.parent_object_id) AS table_name,
    cc.definition
FROM sys.check_constraints cc
WHERE cc.parent_object_id IN (
    OBJECT_ID(N'dbo.VE'),
    OBJECT_ID(N'dbo.GIU_CHO'),
    OBJECT_ID(N'dbo.HOAN_TIEN'),
    OBJECT_ID(N'dbo.THANH_TOAN'),
    OBJECT_ID(N'dbo.GA'),
    OBJECT_ID(N'dbo.TAU'),
    OBJECT_ID(N'dbo.TOA_TAU')
)
ORDER BY table_name, constraint_name;
GO

PRINT N'Đã cập nhật database VeTauDB theo Giai đoạn 1-2.';
GO
