USE VeTauDB;
GO

/*=============================================================================
  SEED_15_KICH_BAN_MAU_GD1_GD2_GD3.sql
  Bổ sung dữ liệu mẫu phong phú cho hệ thống bán vé tàu.

  Cách dùng khuyến nghị:
  1) Chạy VeTauDB.sql gốc.
  2) Chạy trang_thai_UPDATED_GD1_GD2.sql.
  3) Chạy UPDATE_DB_GD3_CLEANUP.sql.
  4) Chạy file seed này.

  File này bổ sung:
  - 10 khách hàng demo.
  - 12 hành khách demo.
  - 02 mã khuyến mãi demo bổ sung.
  - 15 kịch bản đơn/vé/thanh toán/giữ chỗ/hoàn tiền để test:
      + Đã thanh toán / Vé hợp lệ
      + Chờ thanh toán / Vé chờ thanh toán / Ghế đang giữ
      + Hết hạn / Vé đã hủy / Giữ chỗ hết hạn
      + Đã hủy
      + Đã sử dụng
      + Đã trả / Hoàn tiền chờ xử lý
      + Đã trả / Hoàn tiền hoàn tất
      + Đã trả / Hoàn tiền từ chối
=============================================================================*/

SET NOCOUNT ON;
GO

/*=============================================================================
  0. KIỂM TRA MIGRATION TRẠNG THÁI CẦN THIẾT
=============================================================================*/
IF NOT EXISTS (
    SELECT 1
    FROM sys.check_constraints
    WHERE name = N'CK_VE_trang_thai'
      AND parent_object_id = OBJECT_ID(N'dbo.VE')
      AND definition LIKE N'%Chờ thanh toán%'
)
BEGIN
    RAISERROR(N'Chưa chạy migration GĐ1-GĐ2: CK_VE_trang_thai chưa cho phép N''Chờ thanh toán''.', 16, 1);
    RETURN;
END;

IF NOT EXISTS (
    SELECT 1
    FROM sys.check_constraints
    WHERE name = N'CK_GIU_CHO_trang_thai'
      AND parent_object_id = OBJECT_ID(N'dbo.GIU_CHO')
      AND definition LIKE N'%Đã hủy%'
)
BEGIN
    RAISERROR(N'Chưa chạy migration GĐ1-GĐ2: CK_GIU_CHO_trang_thai chưa cho phép N''Đã hủy''.', 16, 1);
    RETURN;
END;

IF NOT EXISTS (
    SELECT 1
    FROM sys.check_constraints
    WHERE name = N'CK_HOAN_TIEN_trang_thai'
      AND parent_object_id = OBJECT_ID(N'dbo.HOAN_TIEN')
      AND definition LIKE N'%Chờ xử lý%'
)
BEGIN
    RAISERROR(N'Chưa chạy migration GĐ1-GĐ2: CK_HOAN_TIEN_trang_thai chưa chuyển sang Chờ xử lý / Hoàn tất / Từ chối.', 16, 1);
    RETURN;
END;
GO

DECLARE @now DATETIME2(0) = SYSDATETIME();
DECLARE @hieuLucTu DATETIME2(0) = DATEADD(DAY, -30, @now);
DECLARE @hieuLucDen DATETIME2(0) = DATEADD(YEAR, 1, @now);

/*=============================================================================
  1. KHÁCH HÀNG MẪU BỔ SUNG
=============================================================================*/
INSERT INTO dbo.KHACH_HANG (ho_ten, email, so_dien_thoai, mat_khau_hash, ngay_sinh, gioi_tinh, dia_chi)
SELECT v.ho_ten, v.email, v.so_dien_thoai, v.mat_khau_hash, v.ngay_sinh, v.gioi_tinh, v.dia_chi
FROM (VALUES
    (N'Hoàng Minh Quân',       N'demo01@vetau.vn', N'0911000001', N'123456_hash_demo', CAST('1998-03-15' AS DATE), N'Nam',  N'Hà Nội'),
    (N'Đỗ Thảo My',            N'demo02@vetau.vn', N'0911000002', N'123456_hash_demo', CAST('2001-07-21' AS DATE), N'Nữ',   N'Huế'),
    (N'Vũ Quốc Huy',           N'demo03@vetau.vn', N'0911000003', N'123456_hash_demo', CAST('1992-11-03' AS DATE), N'Nam',  N'Đà Nẵng'),
    (N'Nguyễn An Nhiên',       N'demo04@vetau.vn', N'0911000004', N'123456_hash_demo', CAST('1988-01-19' AS DATE), N'Nữ',   N'Nha Trang'),
    (N'Trương Gia Bảo',        N'demo05@vetau.vn', N'0911000005', N'123456_hash_demo', CAST('2004-09-09' AS DATE), N'Nam',  N'TP. Hồ Chí Minh'),
    (N'Lâm Phương Linh',       N'demo06@vetau.vn', N'0911000006', N'123456_hash_demo', CAST('1999-12-30' AS DATE), N'Nữ',   N'Nghệ An'),
    (N'Phan Nhật Minh',        N'demo07@vetau.vn', N'0911000007', N'123456_hash_demo', CAST('1970-04-04' AS DATE), N'Nam',  N'Hà Nội'),
    (N'Bùi Khánh Vy',          N'demo08@vetau.vn', N'0911000008', N'123456_hash_demo', CAST('2005-06-25' AS DATE), N'Nữ',   N'Đà Nẵng'),
    (N'Đặng Thanh Tùng',       N'demo09@vetau.vn', N'0911000009', N'123456_hash_demo', CAST('1990-02-12' AS DATE), N'Nam',  N'Huế'),
    (N'Đinh Mai Anh',          N'demo10@vetau.vn', N'0911000010', N'123456_hash_demo', CAST('1996-10-08' AS DATE), N'Nữ',   N'TP. Hồ Chí Minh')
) AS v(ho_ten, email, so_dien_thoai, mat_khau_hash, ngay_sinh, gioi_tinh, dia_chi)
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.KHACH_HANG kh WHERE kh.email = v.email
);

/*=============================================================================
  2. HÀNH KHÁCH MẪU BỔ SUNG
=============================================================================*/
INSERT INTO dbo.HANH_KHACH (ho_ten, loai_giay_to, so_giay_to, ngay_sinh, quoc_tich)
SELECT v.ho_ten, v.loai_giay_to, v.so_giay_to, v.ngay_sinh, v.quoc_tich
FROM (VALUES
    (N'Hoàng Minh Quân',    N'CCCD',           N'001098000101', CAST('1998-03-15' AS DATE), N'Việt Nam'),
    (N'Đỗ Thảo My',         N'CCCD',           N'046101000102', CAST('2001-07-21' AS DATE), N'Việt Nam'),
    (N'Vũ Quốc Huy',        N'CCCD',           N'048092000103', CAST('1992-11-03' AS DATE), N'Việt Nam'),
    (N'Nguyễn An Nhiên',    N'CCCD',           N'056088000104', CAST('1988-01-19' AS DATE), N'Việt Nam'),
    (N'Trương Gia Bảo',     N'CCCD',           N'079204000105', CAST('2004-09-09' AS DATE), N'Việt Nam'),
    (N'Lâm Phương Linh',    N'CCCD',           N'040099000106', CAST('1999-12-30' AS DATE), N'Việt Nam'),
    (N'Phan Nhật Minh',     N'CCCD',           N'001070000107', CAST('1962-04-04' AS DATE), N'Việt Nam'),
    (N'Bùi Khánh Vy',       N'CCCD',           N'048205000108', CAST('2005-06-25' AS DATE), N'Việt Nam'),
    (N'Đặng Thanh Tùng',    N'CCCD',           N'046090000109', CAST('1990-02-12' AS DATE), N'Việt Nam'),
    (N'Đinh Mai Anh',       N'CCCD',           N'079096000110', CAST('1996-10-08' AS DATE), N'Việt Nam'),
    (N'Em bé Nguyễn Minh',   N'Giấy khai sinh', N'GKS-SEED-001', CAST('2018-05-05' AS DATE), N'Việt Nam'),
    (N'Ông Trần Văn Sơn',    N'CCCD',           N'001060000112', CAST('1960-01-01' AS DATE), N'Việt Nam')
) AS v(ho_ten, loai_giay_to, so_giay_to, ngay_sinh, quoc_tich)
WHERE NOT EXISTS (
    SELECT 1 FROM dbo.HANH_KHACH hk
    WHERE hk.loai_giay_to = v.loai_giay_to
      AND hk.so_giay_to = v.so_giay_to
);

/*=============================================================================
  3. KHUYẾN MÃI MẪU BỔ SUNG
=============================================================================*/
IF NOT EXISTS (SELECT 1 FROM dbo.KHUYEN_MAI WHERE ma_khuyen_mai = N'KM15MOMO')
BEGIN
    INSERT INTO dbo.KHUYEN_MAI (
        ma_khuyen_mai, ten_chuong_trinh, phan_tram_giam, giam_toi_da,
        gia_tri_don_toi_thieu, phuong_thuc_tt_ap_dung, ngay_bat_dau, ngay_ket_thuc, so_luong_toi_da, trang_thai
    )
    VALUES (
        N'KM15MOMO', N'Giảm 15% cho thanh toán MoMo', 15, 120000,
        250000, N'MoMo', @hieuLucTu, @hieuLucDen, 500, N'Hoạt động'
    );
END;

IF NOT EXISTS (SELECT 1 FROM dbo.KHUYEN_MAI WHERE ma_khuyen_mai = N'KM25REPORT')
BEGIN
    INSERT INTO dbo.KHUYEN_MAI (
        ma_khuyen_mai, ten_chuong_trinh, phan_tram_giam, giam_toi_da,
        gia_tri_don_toi_thieu, phuong_thuc_tt_ap_dung, ngay_bat_dau, ngay_ket_thuc, so_luong_toi_da, trang_thai
    )
    VALUES (
        N'KM25REPORT', N'Giảm 25% dữ liệu mẫu báo cáo', 25, 200000,
        500000, N'VNPay', @hieuLucTu, @hieuLucDen, 100, N'Hoạt động'
    );
END;

/*=============================================================================
  4. BẢNG KỊCH BẢN 15 ĐƠN/VÉ MẪU
=============================================================================*/
DECLARE @Scenario TABLE (
    ma_dat_cho NVARCHAR(30) NOT NULL,
    customer_email NVARCHAR(150) NOT NULL,
    passenger_doc NVARCHAR(50) NOT NULL,
    ma_chuyen NVARCHAR(30) NOT NULL,
    ma_ga_di NVARCHAR(10) NOT NULL,
    ma_ga_den NVARCHAR(10) NOT NULL,
    so_toa INT NOT NULL,
    so_ghe NVARCHAR(10) NOT NULL,
    loai_toa NVARCHAR(40) NOT NULL,
    tang INT NULL,
    ma_uu_dai NVARCHAR(30) NULL,
    ma_khuyen_mai NVARCHAR(30) NULL,
    phuong_thuc NVARCHAR(30) NULL,
    trang_thai_dat_cho NVARCHAR(30) NOT NULL,
    trang_thai_thanh_toan NVARCHAR(30) NULL,
    trang_thai_giu_cho NVARCHAR(20) NOT NULL,
    trang_thai_ve NVARCHAR(20) NOT NULL,
    refund_status NVARCHAR(20) NULL,
    ngay_dat_offset INT NOT NULL
);

INSERT INTO @Scenario VALUES
-- 1-4: Vé đã thanh toán hợp lệ, phục vụ test vé điện tử và báo cáo doanh thu.
(N'DC_SEED_PAID_001',   N'demo01@vetau.vn', N'001098000101', N'SE1-20260510', N'HNO', N'HUE', 1, N'G04', N'Ghế ngồi mềm', NULL, N'SV',  N'KM10ONLINE', N'VNPay',       N'Đã thanh toán', N'Thành công', N'Đã chuyển vé', N'Hợp lệ',       NULL,          -7),
(N'DC_SEED_PAID_002',   N'demo02@vetau.vn', N'046101000102', N'SE1-20260510', N'HUE', N'DNA', 1, N'G05', N'Ghế ngồi mềm', NULL, N'NL',  N'KM5ALL',     N'MoMo',        N'Đã thanh toán', N'Thành công', N'Đã chuyển vé', N'Hợp lệ',       NULL,          -6),
(N'DC_SEED_PAID_003',   N'demo03@vetau.vn', N'048092000103', N'SE1-20260511', N'DNA', N'NTR', 2, N'B01', N'Nằm mềm 4 người', 1, N'NL',  NULL,           N'Chuyển khoản',N'Đã thanh toán', N'Thành công', N'Đã chuyển vé', N'Hợp lệ',       NULL,          -5),
(N'DC_SEED_PAID_004',   N'demo04@vetau.vn', N'056088000104', N'SE2-20260512', N'SGN', N'NTR', 1, N'G04', N'Ghế ngồi mềm', NULL, N'NL',  NULL,           N'Tiền mặt',    N'Đã thanh toán', N'Thành công', N'Đã chuyển vé', N'Hợp lệ',       NULL,          -4),

-- 5: Đơn đang chờ thanh toán, có thanh toán Pending và ghế đang giữ.
(N'DC_SEED_PENDING_001',N'demo05@vetau.vn', N'079204000105', N'SE1-20260513', N'HNO', N'DNA', 1, N'G06', N'Ghế ngồi mềm', NULL, N'SV',  NULL,           N'VNPay',       N'Chờ thanh toán', N'Pending',   N'Đang giữ',     N'Chờ thanh toán', NULL,        0),

-- 6: Đơn hết hạn, phục vụ test Giai đoạn 3 cleanup.
(N'DC_SEED_EXPIRED_001',N'demo06@vetau.vn', N'040099000106', N'SE1-20260514', N'HNO', N'VIN', 1, N'G07', N'Ghế ngồi mềm', NULL, N'NL',  NULL,           N'VNPay',       N'Hết hạn',      N'Thất bại',  N'Hết hạn',      N'Đã hủy',       NULL,          -3),

-- 7-8: Vé đã trả, hoàn tiền chờ xử lý / hoàn tất.
(N'DC_SEED_RETURN_001', N'demo07@vetau.vn', N'001070000107', N'SE1-20260515', N'HNO', N'DNA', 1, N'G08', N'Ghế ngồi mềm', NULL, N'NCT', N'KM5ALL',     N'VNPay',       N'Hoàn tiền một phần', N'Thành công', N'Đã chuyển vé', N'Đã trả', N'Chờ xử lý', -2),
(N'DC_SEED_RETURN_002', N'demo08@vetau.vn', N'048205000108', N'SE1-20260516', N'HNO', N'HUE', 1, N'G09', N'Ghế ngồi mềm', NULL, N'SV',  N'KM15MOMO',   N'MoMo',        N'Hoàn tiền toàn bộ', N'Thành công', N'Đã chuyển vé', N'Đã trả', N'Hoàn tất', -1),

-- 9: Vé đã sử dụng.
(N'DC_SEED_USED_001',   N'demo09@vetau.vn', N'046090000109', N'SE1-20260517', N'VIN', N'HUE', 1, N'G10', N'Ghế ngồi mềm', NULL, N'NL',  NULL,           N'Tiền mặt',    N'Đã thanh toán', N'Thành công', N'Đã chuyển vé', N'Đã sử dụng',   NULL,          -10),

-- 10: Đơn đã hủy.
(N'DC_SEED_CANCEL_001', N'demo10@vetau.vn', N'079096000110', N'SE1-20260518', N'HNO', N'NTR', 1, N'G11', N'Ghế ngồi mềm', NULL, N'NL',  NULL,           N'VNPay',       N'Đã hủy',      N'Thất bại',  N'Đã hủy',      N'Đã hủy',       NULL,          -9),

-- 11-13: Dữ liệu doanh thu đa dạng tuyến/toa/chiều.
(N'DC_SEED_PAID_005',   N'demo03@vetau.vn', N'GKS-SEED-001', N'SE1-20260519', N'HNO', N'SGN', 2, N'B02', N'Nằm mềm 4 người', 2, N'TRE_EM', N'KM25REPORT', N'VNPay',    N'Đã thanh toán', N'Thành công', N'Đã chuyển vé', N'Hợp lệ', NULL, -8),
(N'DC_SEED_PAID_006',   N'demo04@vetau.vn', N'001060000112', N'SE2-20260519', N'SGN', N'HNO', 1, N'G05', N'Ghế ngồi mềm', NULL, N'NCT', N'KM5ALL',      N'MoMo',     N'Đã thanh toán', N'Thành công', N'Đã chuyển vé', N'Hợp lệ', NULL, -7),
(N'DC_SEED_PAID_007',   N'demo05@vetau.vn', N'001098000101', N'TN1-20260520', N'HNO', N'DNA', 1, N'G04', N'Ghế ngồi cứng', NULL, N'NL', NULL,            N'Tiền mặt', N'Đã thanh toán', N'Thành công', N'Đã chuyển vé', N'Hợp lệ', NULL, -6),

-- 14: Đơn đang giữ chỗ của TN1.
(N'DC_SEED_PENDING_002',N'demo06@vetau.vn', N'046101000102', N'TN1-20260521', N'HNO', N'HUE', 1, N'G05', N'Ghế ngồi cứng', NULL, N'SV', NULL,            NULL,        N'Chờ thanh toán', NULL,        N'Đang giữ',     N'Chờ thanh toán', NULL, 0),

-- 15: Vé đã trả nhưng yêu cầu hoàn tiền bị từ chối, phục vụ test admin refund.
(N'DC_SEED_RETURN_003', N'demo07@vetau.vn', N'048092000103', N'SE2-20260522', N'DNA', N'HNO', 1, N'G06', N'Ghế ngồi mềm', NULL, N'NL', NULL,             N'VNPay',    N'Đã thanh toán', N'Thành công', N'Đã chuyển vé', N'Đã trả', N'Từ chối', -5);

/*=============================================================================
  5. XỬ LÝ INSERT 15 KỊCH BẢN
=============================================================================*/
DECLARE
    @maDatCho NVARCHAR(30),
    @customerEmail NVARCHAR(150),
    @passengerDoc NVARCHAR(50),
    @maChuyen NVARCHAR(30),
    @maGaDi NVARCHAR(10),
    @maGaDen NVARCHAR(10),
    @soToa INT,
    @soGhe NVARCHAR(10),
    @loaiToa NVARCHAR(40),
    @tang INT,
    @maUuDai NVARCHAR(30),
    @maKM NVARCHAR(30),
    @phuongThuc NVARCHAR(30),
    @trangThaiDatCho NVARCHAR(30),
    @trangThaiThanhToan NVARCHAR(30),
    @trangThaiGiuCho NVARCHAR(20),
    @trangThaiVe NVARCHAR(20),
    @refundStatus NVARCHAR(20),
    @ngayDatOffset INT;

DECLARE
    @khId INT,
    @hkId INT,
    @ctId INT,
    @tauId INT,
    @gaDiId INT,
    @gaDenId INT,
    @toaId INT,
    @gheId INT,
    @uuDaiId INT,
    @ptUuDai DECIMAL(5,2),
    @kmId INT,
    @ptKM DECIMAL(5,2),
    @giamToiDa DECIMAL(12,2),
    @donMin DECIMAL(12,2),
    @giaCoSo DECIMAL(12,2),
    @phuThu DECIMAL(12,2),
    @giamDoiTuong DECIMAL(12,2),
    @giaVeChiTiet DECIMAL(12,2),
    @giamKM DECIMAL(12,2),
    @tongTienVeGoc DECIMAL(12,2),
    @tongThanhToan DECIMAL(12,2),
    @ngayDat DATETIME2(0),
    @thoiGianGiu DATETIME2(0),
    @thoiGianHetHan DATETIME2(0),
    @datChoId INT,
    @maVe NVARCHAR(40),
    @veId INT,
    @thanhToanId INT,
    @csId INT,
    @tyLeKhauTru DECIMAL(5,2),
    @phiDoi DECIMAL(12,2),
    @soTienHoan DECIMAL(12,2);

DECLARE curScenario CURSOR LOCAL FAST_FORWARD FOR
SELECT ma_dat_cho, customer_email, passenger_doc, ma_chuyen, ma_ga_di, ma_ga_den,
       so_toa, so_ghe, loai_toa, tang, ma_uu_dai, ma_khuyen_mai, phuong_thuc,
       trang_thai_dat_cho, trang_thai_thanh_toan, trang_thai_giu_cho, trang_thai_ve,
       refund_status, ngay_dat_offset
FROM @Scenario
ORDER BY ma_dat_cho;

OPEN curScenario;
FETCH NEXT FROM curScenario INTO
    @maDatCho, @customerEmail, @passengerDoc, @maChuyen, @maGaDi, @maGaDen,
    @soToa, @soGhe, @loaiToa, @tang, @maUuDai, @maKM, @phuongThuc,
    @trangThaiDatCho, @trangThaiThanhToan, @trangThaiGiuCho, @trangThaiVe,
    @refundStatus, @ngayDatOffset;

WHILE @@FETCH_STATUS = 0
BEGIN
    IF EXISTS (SELECT 1 FROM dbo.DAT_CHO WHERE ma_dat_cho = @maDatCho)
    BEGIN
        PRINT N'Bỏ qua vì đã tồn tại: ' + @maDatCho;
        FETCH NEXT FROM curScenario INTO
            @maDatCho, @customerEmail, @passengerDoc, @maChuyen, @maGaDi, @maGaDen,
            @soToa, @soGhe, @loaiToa, @tang, @maUuDai, @maKM, @phuongThuc,
            @trangThaiDatCho, @trangThaiThanhToan, @trangThaiGiuCho, @trangThaiVe,
            @refundStatus, @ngayDatOffset;
        CONTINUE;
    END;

    SET @khId = (SELECT id FROM dbo.KHACH_HANG WHERE email = @customerEmail);
    SET @hkId = (SELECT id FROM dbo.HANH_KHACH WHERE so_giay_to = @passengerDoc);
    SET @ctId = (SELECT id FROM dbo.CHUYEN_TAU WHERE ma_chuyen = @maChuyen);
    SET @tauId = (SELECT tau_id FROM dbo.CHUYEN_TAU WHERE id = @ctId);
    SET @gaDiId = (SELECT id FROM dbo.GA WHERE ma_ga = @maGaDi);
    SET @gaDenId = (SELECT id FROM dbo.GA WHERE ma_ga = @maGaDen);
    SET @toaId = (SELECT id FROM dbo.TOA_TAU WHERE tau_id = @tauId AND so_toa = @soToa);
    SET @gheId = (SELECT id FROM dbo.GHE WHERE toa_tau_id = @toaId AND so_ghe = @soGhe);
    SET @uuDaiId = (SELECT id FROM dbo.DOI_TUONG_UU_DAI WHERE ma_doi_tuong = @maUuDai);
    SET @ptUuDai = ISNULL((SELECT phan_tram_giam FROM dbo.DOI_TUONG_UU_DAI WHERE id = @uuDaiId), 0);
    SET @kmId = (SELECT id FROM dbo.KHUYEN_MAI WHERE ma_khuyen_mai = @maKM);
    SET @ptKM = ISNULL((SELECT phan_tram_giam FROM dbo.KHUYEN_MAI WHERE id = @kmId), 0);
    SET @giamToiDa = (SELECT giam_toi_da FROM dbo.KHUYEN_MAI WHERE id = @kmId);
    SET @donMin = ISNULL((SELECT gia_tri_don_toi_thieu FROM dbo.KHUYEN_MAI WHERE id = @kmId), 0);

    SET @giaCoSo = (
        SELECT TOP 1 gia_co_so
        FROM dbo.BANG_GIA
        WHERE ga_di_id = @gaDiId
          AND ga_den_id = @gaDenId
          AND loai_toa_ap_dung = @loaiToa
          AND ((@tang IS NULL AND tang_ap_dung IS NULL) OR tang_ap_dung = @tang)
          AND trang_thai = N'Hoạt động'
        ORDER BY CASE WHEN tang_ap_dung IS NULL THEN 1 ELSE 0 END, id
    );

    SET @phuThu = (
        SELECT TOP 1 phu_thu_cao_diem_mac_dinh
        FROM dbo.BANG_GIA
        WHERE ga_di_id = @gaDiId
          AND ga_den_id = @gaDenId
          AND loai_toa_ap_dung = @loaiToa
          AND ((@tang IS NULL AND tang_ap_dung IS NULL) OR tang_ap_dung = @tang)
          AND trang_thai = N'Hoạt động'
        ORDER BY CASE WHEN tang_ap_dung IS NULL THEN 1 ELSE 0 END, id
    );

    IF @khId IS NULL OR @hkId IS NULL OR @ctId IS NULL OR @gaDiId IS NULL OR @gaDenId IS NULL OR @toaId IS NULL OR @gheId IS NULL OR @giaCoSo IS NULL
    BEGIN
        PRINT N'Không thể tạo scenario vì thiếu dữ liệu tham chiếu: ' + @maDatCho;
        FETCH NEXT FROM curScenario INTO
            @maDatCho, @customerEmail, @passengerDoc, @maChuyen, @maGaDi, @maGaDen,
            @soToa, @soGhe, @loaiToa, @tang, @maUuDai, @maKM, @phuongThuc,
            @trangThaiDatCho, @trangThaiThanhToan, @trangThaiGiuCho, @trangThaiVe,
            @refundStatus, @ngayDatOffset;
        CONTINUE;
    END;

    SET @giamDoiTuong = ROUND(@giaCoSo * @ptUuDai / 100.0, 0);
    SET @giaVeChiTiet = @giaCoSo - @giamDoiTuong + ISNULL(@phuThu, 0);
    SET @giamKM = 0;

    IF @kmId IS NOT NULL AND @giaVeChiTiet >= @donMin
    BEGIN
        SET @giamKM = ROUND(@giaVeChiTiet * @ptKM / 100.0, 0);
        IF @giamToiDa IS NOT NULL AND @giamKM > @giamToiDa
            SET @giamKM = @giamToiDa;
    END;

    SET @tongTienVeGoc = @giaCoSo + ISNULL(@phuThu, 0);
    SET @tongThanhToan = @giaVeChiTiet - @giamKM;
    IF @tongThanhToan < 0 SET @tongThanhToan = 0;

    SET @ngayDat = DATEADD(DAY, @ngayDatOffset, @now);
    SET @thoiGianGiu = NULL;
    SET @thoiGianHetHan = NULL;

    IF @trangThaiDatCho = N'Hết hạn' OR @trangThaiDatCho = N'Đã hủy'
    BEGIN
        SET @thoiGianGiu = DATEADD(MINUTE, -80, @now);
        SET @thoiGianHetHan = DATEADD(MINUTE, -40, @now);
    END
    ELSE
    BEGIN
        SET @thoiGianGiu = DATEADD(MINUTE, -10, @now);
        SET @thoiGianHetHan = DATEADD(MINUTE, 30, @now);
    END;

    INSERT INTO dbo.DAT_CHO (
        khach_hang_id, khuyen_mai_id, ma_dat_cho, loai_don_hang, loai_hanh_trinh,
        ngay_dat, tong_tien_ve_goc, thue_vat, phi_thanh_toan, tong_giam_khuyen_mai,
        giam_gia_khu_hoi, tong_thanh_toan, thoi_gian_het_han, trang_thai
    )
    VALUES (
        @khId, @kmId, @maDatCho, N'Cá nhân', N'Một chiều',
        @ngayDat, @tongTienVeGoc, 0, 0, @giamKM,
        0, @tongThanhToan, @thoiGianHetHan, @trangThaiDatCho
    );

    SET @datChoId = SCOPE_IDENTITY();
    SET @maVe = REPLACE(@maDatCho, N'DC_', N'VE_');

    INSERT INTO dbo.GIU_CHO (
        dat_cho_id, chuyen_tau_id, ghe_id, ga_di_id, ga_den_id,
        thoi_gian_giu, thoi_gian_het_han, trang_thai
    )
    VALUES (
        @datChoId, @ctId, @gheId, @gaDiId, @gaDenId,
        @thoiGianGiu, @thoiGianHetHan, @trangThaiGiuCho
    );

    INSERT INTO dbo.VE (
        dat_cho_id, chuyen_tau_id, ghe_id, hanh_khach_id, doi_tuong_uu_dai_id,
        ga_di_id, ga_den_id, ma_ve, gia_co_so, giam_doi_tuong,
        phu_thu_cao_diem, gia_ve_chi_tiet, trang_thai
    )
    VALUES (
        @datChoId, @ctId, @gheId, @hkId, @uuDaiId,
        @gaDiId, @gaDenId, @maVe, @giaCoSo, @giamDoiTuong,
        ISNULL(@phuThu, 0), @giaVeChiTiet, @trangThaiVe
    );

    SET @veId = SCOPE_IDENTITY();
    SET @thanhToanId = NULL;

    IF @trangThaiThanhToan IS NOT NULL AND @phuongThuc IS NOT NULL
    BEGIN
        INSERT INTO dbo.THANH_TOAN (
            dat_cho_id, ma_giao_dich, request_id, phuong_thuc, so_tien,
            ngay_tao, ngay_thanh_toan, trang_thai
        )
        VALUES (
            @datChoId,
            CASE WHEN @trangThaiThanhToan = N'Thành công' THEN N'GD_' + @maDatCho ELSE NULL END,
            N'REQ_' + @maDatCho,
            @phuongThuc,
            @tongThanhToan,
            @ngayDat,
            CASE WHEN @trangThaiThanhToan = N'Thành công' THEN DATEADD(MINUTE, 5, @ngayDat) ELSE NULL END,
            @trangThaiThanhToan
        );

        SET @thanhToanId = SCOPE_IDENTITY();
    END;

    IF @refundStatus IS NOT NULL AND @thanhToanId IS NOT NULL
    BEGIN
        SET @csId = (
            SELECT TOP 1 id FROM dbo.CHINH_SACH_DOI_TRA
            WHERE trang_thai = N'Hoạt động' AND cho_phep_tra = 1
            ORDER BY do_uu_tien
        );
        SET @tyLeKhauTru = ISNULL((SELECT ty_le_khau_tru FROM dbo.CHINH_SACH_DOI_TRA WHERE id = @csId), 10);
        SET @phiDoi = ISNULL((SELECT phi_doi_co_dinh FROM dbo.CHINH_SACH_DOI_TRA WHERE id = @csId), 20000);
        SET @soTienHoan = @giaVeChiTiet - ROUND(@giaVeChiTiet * @tyLeKhauTru / 100.0, 0) - @phiDoi;
        IF @soTienHoan < 0 SET @soTienHoan = 0;

        IF NOT EXISTS (SELECT 1 FROM dbo.LICH_SU_DOI_TRA WHERE ve_id = @veId AND loai_giao_dich = N'Trả vé')
        BEGIN
            INSERT INTO dbo.LICH_SU_DOI_TRA (
                ve_id, nhan_vien_id, chinh_sach_id, loai_giao_dich, ly_do,
                phi_doi, ty_le_khau_tru, so_tien_hoan, thoi_gian_xu_ly, ghi_chu
            )
            VALUES (
                @veId, NULL, @csId, N'Trả vé', N'Dữ liệu mẫu kiểm thử hoàn tiền',
                @phiDoi, @tyLeKhauTru, @soTienHoan, DATEADD(MINUTE, 20, @ngayDat), @refundStatus
            );
        END;

        IF NOT EXISTS (SELECT 1 FROM dbo.HOAN_TIEN WHERE ve_id = @veId)
        BEGIN
            INSERT INTO dbo.HOAN_TIEN (
                thanh_toan_id, ve_id, so_tien_hoan, ma_giao_dich_hoan,
                thoi_gian_yeu_cau, thoi_gian_hoan_tat, trang_thai
            )
            VALUES (
                @thanhToanId,
                @veId,
                @soTienHoan,
                CASE WHEN @refundStatus = N'Hoàn tất' THEN N'REF_' + @maDatCho ELSE NULL END,
                DATEADD(MINUTE, 21, @ngayDat),
                CASE WHEN @refundStatus IN (N'Hoàn tất', N'Từ chối') THEN DATEADD(MINUTE, 45, @ngayDat) ELSE NULL END,
                @refundStatus
            );
        END;
    END;

    PRINT N'Đã thêm scenario: ' + @maDatCho + N' / Vé: ' + @maVe;

    FETCH NEXT FROM curScenario INTO
        @maDatCho, @customerEmail, @passengerDoc, @maChuyen, @maGaDi, @maGaDen,
        @soToa, @soGhe, @loaiToa, @tang, @maUuDai, @maKM, @phuongThuc,
        @trangThaiDatCho, @trangThaiThanhToan, @trangThaiGiuCho, @trangThaiVe,
        @refundStatus, @ngayDatOffset;
END;

CLOSE curScenario;
DEALLOCATE curScenario;

/*=============================================================================
  6. TRUY VẤN KIỂM TRA NHANH SAU KHI CHẠY SEED
=============================================================================*/
PRINT N'Hoàn tất seed 15 kịch bản dữ liệu mẫu.';

SELECT N'KHACH_HANG' AS bang, COUNT(*) AS so_dong FROM dbo.KHACH_HANG
UNION ALL SELECT N'HANH_KHACH', COUNT(*) FROM dbo.HANH_KHACH
UNION ALL SELECT N'DAT_CHO', COUNT(*) FROM dbo.DAT_CHO
UNION ALL SELECT N'GIU_CHO', COUNT(*) FROM dbo.GIU_CHO
UNION ALL SELECT N'VE', COUNT(*) FROM dbo.VE
UNION ALL SELECT N'THANH_TOAN', COUNT(*) FROM dbo.THANH_TOAN
UNION ALL SELECT N'HOAN_TIEN', COUNT(*) FROM dbo.HOAN_TIEN
UNION ALL SELECT N'LICH_SU_DOI_TRA', COUNT(*) FROM dbo.LICH_SU_DOI_TRA;

SELECT
    dc.ma_dat_cho,
    dc.trang_thai AS trang_thai_dat_cho,
    tt.trang_thai AS trang_thai_thanh_toan,
    gc.trang_thai AS trang_thai_giu_cho,
    v.ma_ve,
    v.trang_thai AS trang_thai_ve,
    ht.trang_thai AS trang_thai_hoan_tien,
    dc.tong_thanh_toan
FROM dbo.DAT_CHO dc
LEFT JOIN dbo.THANH_TOAN tt ON tt.dat_cho_id = dc.id
LEFT JOIN dbo.GIU_CHO gc ON gc.dat_cho_id = dc.id
LEFT JOIN dbo.VE v ON v.dat_cho_id = dc.id
LEFT JOIN dbo.HOAN_TIEN ht ON ht.ve_id = v.id
WHERE dc.ma_dat_cho LIKE N'DC_SEED_%'
ORDER BY dc.ma_dat_cho;

SELECT
    dc.trang_thai AS trang_thai_dat_cho,
    COUNT(*) AS so_don,
    SUM(dc.tong_thanh_toan) AS tong_tien
FROM dbo.DAT_CHO dc
WHERE dc.ma_dat_cho LIKE N'DC_SEED_%'
GROUP BY dc.trang_thai
ORDER BY dc.trang_thai;
GO
