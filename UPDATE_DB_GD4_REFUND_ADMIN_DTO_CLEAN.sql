USE VeTauDB;
GO

/* =========================================================
   UPDATE_DB_GD4_REFUND_ADMIN_DTO_CLEAN.sql

   Bản "clean" để chạy sau:
   1. VeTauDB_trang_thai_UPDATED_GD1_GD2.sql
   2. UPDATE_DB_GD3_CLEANUP.sql
   3. SEED_15_KICH_BAN_MAU_GD1_GD2_GD3.sql
   4. File này

   Lý do có bản CLEAN:
   - Bản FINAL trước đó chạy thành công nhưng SSMS Error List vẫn có thể báo
     "index already exists" vì IntelliSense/Build parse thấy lệnh CREATE INDEX.
   - Bản này tạo index bằng dynamic SQL sau khi kiểm tra sys.indexes,
     nên hạn chế cảnh báo "already exists" ở Error List.
   ========================================================= */


/* =========================================================
   0. Kiểm tra bảng cần thiết
   ========================================================= */

IF OBJECT_ID(N'dbo.HOAN_TIEN', N'U') IS NULL
BEGIN
    RAISERROR(N'Không tìm thấy bảng dbo.HOAN_TIEN.', 16, 1);
    RETURN;
END
GO

IF OBJECT_ID(N'dbo.LICH_SU_DOI_TRA', N'U') IS NULL
BEGIN
    RAISERROR(N'Không tìm thấy bảng dbo.LICH_SU_DOI_TRA.', 16, 1);
    RETURN;
END
GO


/* =========================================================
   1. Chuẩn hóa dữ liệu trạng thái cũ
   ========================================================= */

UPDATE dbo.HOAN_TIEN
SET trang_thai = N'Chờ xử lý'
WHERE trang_thai IS NULL
   OR LTRIM(RTRIM(trang_thai)) = N''
   OR trang_thai = N'Pending';
GO

UPDATE dbo.HOAN_TIEN
SET trang_thai = N'Hoàn tất'
WHERE trang_thai IN (
    N'Thành công',
    N'Đã hoàn tiền',
    N'Đã hoàn',
    N'Completed',
    N'Success'
);
GO

UPDATE dbo.HOAN_TIEN
SET trang_thai = N'Từ chối'
WHERE trang_thai IN (
    N'Thất bại',
    N'Hủy',
    N'Đã hủy',
    N'Không duyệt',
    N'Rejected',
    N'Failed',
    N'Cancel',
    N'Cancelled'
);
GO


/* =========================================================
   2. Nếu còn trạng thái lạ thì dừng lại
   ========================================================= */

IF EXISTS (
    SELECT 1
    FROM dbo.HOAN_TIEN
    WHERE trang_thai NOT IN (N'Chờ xử lý', N'Hoàn tất', N'Từ chối')
)
BEGIN
    SELECT
        N'Các trạng thái HOAN_TIEN chưa được chuẩn hóa' AS canh_bao,
        trang_thai,
        COUNT(*) AS so_luong
    FROM dbo.HOAN_TIEN
    WHERE trang_thai NOT IN (N'Chờ xử lý', N'Hoàn tất', N'Từ chối')
    GROUP BY trang_thai;

    RAISERROR(N'Còn trạng thái HOAN_TIEN lạ. Hãy chuẩn hóa dữ liệu trước khi tạo CHECK constraint.', 16, 1);
    RETURN;
END
GO


/* =========================================================
   3. Drop CHECK constraint cũ của HOAN_TIEN.trang_thai nếu có
   ========================================================= */

DECLARE @ckName NVARCHAR(255);
DECLARE @sql NVARCHAR(MAX);

SELECT @ckName = cc.name
FROM sys.check_constraints cc
WHERE cc.parent_object_id = OBJECT_ID(N'dbo.HOAN_TIEN')
  AND cc.name = N'CK_HOAN_TIEN_trang_thai';

IF @ckName IS NOT NULL
BEGIN
    SET @sql = N'ALTER TABLE dbo.HOAN_TIEN DROP CONSTRAINT ' + QUOTENAME(@ckName);
    EXEC sp_executesql @sql;
END
GO


/* =========================================================
   4. Tạo CHECK constraint mới
   ========================================================= */

IF NOT EXISTS (
    SELECT 1
    FROM sys.check_constraints
    WHERE name = N'CK_HOAN_TIEN_trang_thai'
      AND parent_object_id = OBJECT_ID(N'dbo.HOAN_TIEN')
)
BEGIN
    ALTER TABLE dbo.HOAN_TIEN
    ADD CONSTRAINT CK_HOAN_TIEN_trang_thai
    CHECK (
        trang_thai IN (
            N'Chờ xử lý',
            N'Hoàn tất',
            N'Từ chối'
        )
    );
END
GO


/* =========================================================
   5. Drop DEFAULT cũ của HOAN_TIEN.trang_thai nếu có
   ========================================================= */

DECLARE @dfName NVARCHAR(255);
DECLARE @sql NVARCHAR(MAX);

SELECT @dfName = dc.name
FROM sys.default_constraints dc
JOIN sys.columns c
    ON c.default_object_id = dc.object_id
WHERE dc.parent_object_id = OBJECT_ID(N'dbo.HOAN_TIEN')
  AND c.name = N'trang_thai';

IF @dfName IS NOT NULL
BEGIN
    SET @sql = N'ALTER TABLE dbo.HOAN_TIEN DROP CONSTRAINT ' + QUOTENAME(@dfName);
    EXEC sp_executesql @sql;
END
GO


/* =========================================================
   6. Tạo DEFAULT mới = Chờ xử lý
   ========================================================= */

IF NOT EXISTS (
    SELECT 1
    FROM sys.default_constraints dc
    JOIN sys.columns c
        ON c.default_object_id = dc.object_id
    WHERE dc.parent_object_id = OBJECT_ID(N'dbo.HOAN_TIEN')
      AND c.name = N'trang_thai'
)
BEGIN
    ALTER TABLE dbo.HOAN_TIEN
    ADD CONSTRAINT DF_HOAN_TIEN_trang_thai
    DEFAULT N'Chờ xử lý' FOR trang_thai;
END
GO


/* =========================================================
   7. Tạo index bằng dynamic SQL để tránh Error List báo "already exists"
   ========================================================= */

DECLARE @sql NVARCHAR(MAX);

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = N'IX_HOAN_TIEN_TrangThai_ThoiGian'
      AND object_id = OBJECT_ID(N'dbo.HOAN_TIEN')
)
BEGIN
    SET @sql = N'
        CREATE INDEX IX_HOAN_TIEN_TrangThai_ThoiGian
        ON dbo.HOAN_TIEN (trang_thai, thoi_gian_yeu_cau DESC, id DESC)
        INCLUDE (
            ve_id,
            thanh_toan_id,
            so_tien_hoan,
            ma_giao_dich_hoan,
            thoi_gian_hoan_tat
        );';
    EXEC sp_executesql @sql;
END
ELSE
BEGIN
    PRINT N'Đã tồn tại IX_HOAN_TIEN_TrangThai_ThoiGian - bỏ qua.';
END
GO

DECLARE @sql NVARCHAR(MAX);

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = N'IX_HOAN_TIEN_VeId_TrangThai'
      AND object_id = OBJECT_ID(N'dbo.HOAN_TIEN')
)
BEGIN
    SET @sql = N'
        CREATE INDEX IX_HOAN_TIEN_VeId_TrangThai
        ON dbo.HOAN_TIEN (ve_id, trang_thai, id DESC)
        INCLUDE (
            thanh_toan_id,
            so_tien_hoan,
            ma_giao_dich_hoan,
            thoi_gian_yeu_cau,
            thoi_gian_hoan_tat
        );';
    EXEC sp_executesql @sql;
END
ELSE
BEGIN
    PRINT N'Đã tồn tại IX_HOAN_TIEN_VeId_TrangThai - bỏ qua.';
END
GO

DECLARE @sql NVARCHAR(MAX);

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = N'IX_HOAN_TIEN_ThanhToanId'
      AND object_id = OBJECT_ID(N'dbo.HOAN_TIEN')
)
BEGIN
    SET @sql = N'
        CREATE INDEX IX_HOAN_TIEN_ThanhToanId
        ON dbo.HOAN_TIEN (thanh_toan_id);';
    EXEC sp_executesql @sql;
END
ELSE
BEGIN
    PRINT N'Đã tồn tại IX_HOAN_TIEN_ThanhToanId - bỏ qua.';
END
GO

DECLARE @sql NVARCHAR(MAX);

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = N'IX_LICH_SU_DOI_TRA_VeId_IdDesc'
      AND object_id = OBJECT_ID(N'dbo.LICH_SU_DOI_TRA')
)
BEGIN
    SET @sql = N'
        CREATE INDEX IX_LICH_SU_DOI_TRA_VeId_IdDesc
        ON dbo.LICH_SU_DOI_TRA (ve_id, id DESC)
        INCLUDE (
            ly_do,
            ty_le_khau_tru,
            phi_doi,
            so_tien_hoan,
            ghi_chu
        );';
    EXEC sp_executesql @sql;
END
ELSE
BEGIN
    PRINT N'Đã tồn tại IX_LICH_SU_DOI_TRA_VeId_IdDesc - bỏ qua.';
END
GO


/* =========================================================
   8. Unique filtered index:
      Chặn nhiều yêu cầu "Chờ xử lý" cho cùng một vé.
   ========================================================= */

DECLARE @sql NVARCHAR(MAX);

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = N'UX_HOAN_TIEN_OnePendingPerTicket'
      AND object_id = OBJECT_ID(N'dbo.HOAN_TIEN')
)
BEGIN
    IF NOT EXISTS (
        SELECT ve_id
        FROM dbo.HOAN_TIEN
        WHERE trang_thai = N'Chờ xử lý'
        GROUP BY ve_id
        HAVING COUNT(*) > 1
    )
    BEGIN
        SET @sql = N'
            CREATE UNIQUE INDEX UX_HOAN_TIEN_OnePendingPerTicket
            ON dbo.HOAN_TIEN (ve_id)
            WHERE trang_thai = N''Chờ xử lý'';';
        EXEC sp_executesql @sql;
    END
    ELSE
    BEGIN
        PRINT N'WARNING: Không tạo UX_HOAN_TIEN_OnePendingPerTicket vì đang có nhiều yêu cầu Chờ xử lý cho cùng một vé.';

        SELECT
            ve_id,
            COUNT(*) AS so_yeu_cau_cho_xu_ly
        FROM dbo.HOAN_TIEN
        WHERE trang_thai = N'Chờ xử lý'
        GROUP BY ve_id
        HAVING COUNT(*) > 1;
    END
END
ELSE
BEGIN
    PRINT N'Đã tồn tại UX_HOAN_TIEN_OnePendingPerTicket - bỏ qua.';
END
GO


/* =========================================================
   9. Kiểm tra nhanh sau migration
   ========================================================= */

SELECT
    N'HOAN_TIEN status summary' AS info,
    trang_thai,
    COUNT(*) AS so_luong
FROM dbo.HOAN_TIEN
GROUP BY trang_thai
ORDER BY trang_thai;
GO

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


/* =========================================================
   10. Kiểm tra danh sách index liên quan
   ========================================================= */

SELECT
    i.name AS index_name,
    OBJECT_NAME(i.object_id) AS table_name,
    i.is_unique,
    i.has_filter,
    i.filter_definition
FROM sys.indexes i
WHERE i.object_id IN (
        OBJECT_ID(N'dbo.HOAN_TIEN'),
        OBJECT_ID(N'dbo.LICH_SU_DOI_TRA')
    )
  AND i.name IN (
        N'IX_HOAN_TIEN_TrangThai_ThoiGian',
        N'IX_HOAN_TIEN_VeId_TrangThai',
        N'IX_HOAN_TIEN_ThanhToanId',
        N'IX_LICH_SU_DOI_TRA_VeId_IdDesc',
        N'UX_HOAN_TIEN_OnePendingPerTicket'
    )
ORDER BY table_name, index_name;
GO
