USE VeTauDB;
GO

/* Index phục vụ tìm đơn quá hạn */
IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = 'IX_DAT_CHO_Cleanup'
      AND object_id = OBJECT_ID('dbo.DAT_CHO')
)
BEGIN
    CREATE INDEX IX_DAT_CHO_Cleanup
    ON dbo.DAT_CHO (trang_thai, thoi_gian_het_han);
END
GO

/* Index phục vụ update giữ chỗ theo đơn */
IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = 'IX_GIU_CHO_DatCho_TrangThai'
      AND object_id = OBJECT_ID('dbo.GIU_CHO')
)
BEGIN
    CREATE INDEX IX_GIU_CHO_DatCho_TrangThai
    ON dbo.GIU_CHO (dat_cho_id, trang_thai);
END
GO

/* Index phục vụ update vé theo đơn */
IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = 'IX_VE_DatCho_TrangThai'
      AND object_id = OBJECT_ID('dbo.VE')
)
BEGIN
    CREATE INDEX IX_VE_DatCho_TrangThai
    ON dbo.VE (dat_cho_id, trang_thai);
END
GO

/* Index phục vụ update thanh toán theo đơn */
IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = 'IX_THANH_TOAN_DatCho_TrangThai'
      AND object_id = OBJECT_ID('dbo.THANH_TOAN')
)
BEGIN
    CREATE INDEX IX_THANH_TOAN_DatCho_TrangThai
    ON dbo.THANH_TOAN (dat_cho_id, trang_thai);
END
GO

/* Index hỗ trợ kiểm tra ghế đã bán/giữ theo chuyến và ghế */
IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = 'IX_VE_Chuyen_Ghe_TrangThai'
      AND object_id = OBJECT_ID('dbo.VE')
)
BEGIN
    CREATE INDEX IX_VE_Chuyen_Ghe_TrangThai
    ON dbo.VE (chuyen_tau_id, ghe_id, trang_thai);
END
GO

IF NOT EXISTS (
    SELECT 1
    FROM sys.indexes
    WHERE name = 'IX_GIU_CHO_Chuyen_Ghe_TrangThai_HetHan'
      AND object_id = OBJECT_ID('dbo.GIU_CHO')
)
BEGIN
    CREATE INDEX IX_GIU_CHO_Chuyen_Ghe_TrangThai_HetHan
    ON dbo.GIU_CHO (chuyen_tau_id, ghe_id, trang_thai, thoi_gian_het_han);
END
GO