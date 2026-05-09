package vetau.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import vetau.dao.DatChoDAO;
import vetau.model.DatCho;
import vetau.model.HanhKhach;
import vetau.service.GiaVeService.KetQuaTinhGia;
import vetau.util.DBConnection;
import vetau.util.TrangThai;

public class DatVeService {

    private final DatChoDAO datChoDAO = new DatChoDAO();

    /*
     * Method cũ: giữ lại để tương thích.
     */
    public int taoDatChoChoThanhToan(int khachHangId, Integer khuyenMaiId, BigDecimal tongTienVeGoc,
                                     BigDecimal tongGiamKhuyenMai, BigDecimal tongThanhToan) throws SQLException {
        LocalDateTime now = LocalDateTime.now();

        DatCho dc = new DatCho();
        dc.setKhachHangId(khachHangId);
        dc.setKhuyenMaiId(khuyenMaiId);
        dc.setMaDatCho("DC" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
        dc.setLoaiDonHang("Cá nhân");
        dc.setLoaiHanhTrinh("Một chiều");
        dc.setNgayDat(now);
        dc.setTongTienVeGoc(nvl(tongTienVeGoc));
        dc.setThueVat(BigDecimal.ZERO);
        dc.setPhiThanhToan(BigDecimal.ZERO);
        dc.setTongGiamKhuyenMai(nvl(tongGiamKhuyenMai));
        dc.setGiamGiaKhuHoi(BigDecimal.ZERO);
        dc.setTongThanhToan(nvl(tongThanhToan));
        dc.setThoiGianHetHan(now.plusMinutes(30));
        dc.setTrangThai(TrangThai.DAT_CHO_CHO_THANH_TOAN);

        return datChoDAO.insert(dc);
    }

    /*
     * Method mới:
     * Tạo DAT_CHO + HANH_KHACH + GIU_CHO + VE trong cùng transaction.
     * Vé được tạo ở trạng thái Chờ thanh toán, không cần lưu hành khách vào session nữa.
     */
    public int taoDonGiuChoVaVeChoThanhToan(
            int khachHangId,
            Integer khuyenMaiId,
            int chuyenTauId,
            int gheId,
            int gaDiId,
            int gaDenId,
            HanhKhach hanhKhach,
            Integer doiTuongUuDaiId,
            KetQuaTinhGia ketQuaGia,
            int soPhutGiu
    ) throws SQLException {

        if (hanhKhach == null) {
            throw new IllegalArgumentException("Thiếu thông tin hành khách.");
        }

        if (ketQuaGia == null) {
            throw new IllegalArgumentException("Thiếu thông tin giá vé.");
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime hetHan = now.plusMinutes(soPhutGiu);

        BigDecimal tongTienVeGoc = nvl(ketQuaGia.getGiaCoSo()).add(nvl(ketQuaGia.getPhuThuCaoDiem()));
        BigDecimal tongGiamKhuyenMai = nvl(ketQuaGia.getGiamKhuyenMai());
        BigDecimal tongThanhToan = nvl(ketQuaGia.getThanhTien());

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            try {
                boolean gheTrong = kiemTraGheTrongTheoChang(conn, chuyenTauId, gheId, gaDiId, gaDenId);
                if (!gheTrong) {
                    throw new IllegalStateException("Ghế đã bị giữ hoặc đã bán trên đoạn chặng này.");
                }

                int datChoId = insertDatCho(
                        conn,
                        khachHangId,
                        khuyenMaiId,
                        tongTienVeGoc,
                        tongGiamKhuyenMai,
                        tongThanhToan,
                        now,
                        hetHan
                );

                int hanhKhachId = findOrCreateHanhKhach(conn, hanhKhach);

                insertGiuCho(conn, datChoId, chuyenTauId, gheId, gaDiId, gaDenId, now, hetHan);

                insertVeChoThanhToan(
                        conn,
                        datChoId,
                        chuyenTauId,
                        gheId,
                        hanhKhachId,
                        doiTuongUuDaiId,
                        gaDiId,
                        gaDenId,
                        ketQuaGia
                );

                conn.commit();
                return datChoId;
            } catch (Exception ex) {
                conn.rollback();

                if (ex instanceof SQLException) {
                    throw (SQLException) ex;
                }

                throw new SQLException(ex.getMessage(), ex);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    public void capNhatTrangThai(int datChoId, String trangThai) throws SQLException {
        datChoDAO.updateTrangThai(datChoId, trangThai);
    }

    private int insertDatCho(Connection conn,
                             int khachHangId,
                             Integer khuyenMaiId,
                             BigDecimal tongTienVeGoc,
                             BigDecimal tongGiamKhuyenMai,
                             BigDecimal tongThanhToan,
                             LocalDateTime ngayDat,
                             LocalDateTime thoiGianHetHan) throws SQLException {

        String sql = """
                INSERT INTO dbo.DAT_CHO
                (khach_hang_id, khuyen_mai_id, ma_dat_cho, loai_don_hang, loai_hanh_trinh,
                 ngay_dat, tong_tien_ve_goc, thue_vat, phi_thanh_toan, tong_giam_khuyen_mai,
                 giam_gia_khu_hoi, tong_thanh_toan, thoi_gian_het_han, trang_thai)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;

            ps.setInt(index++, khachHangId);

            if (khuyenMaiId == null) {
                ps.setNull(index++, java.sql.Types.INTEGER);
            } else {
                ps.setInt(index++, khuyenMaiId);
            }

            ps.setString(index++, "DC" + UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase());
            ps.setString(index++, "Cá nhân");
            ps.setString(index++, "Một chiều");
            ps.setTimestamp(index++, Timestamp.valueOf(ngayDat));
            ps.setBigDecimal(index++, nvl(tongTienVeGoc));
            ps.setBigDecimal(index++, BigDecimal.ZERO);
            ps.setBigDecimal(index++, BigDecimal.ZERO);
            ps.setBigDecimal(index++, nvl(tongGiamKhuyenMai));
            ps.setBigDecimal(index++, BigDecimal.ZERO);
            ps.setBigDecimal(index++, nvl(tongThanhToan));
            ps.setTimestamp(index++, Timestamp.valueOf(thoiGianHetHan));
            ps.setString(index++, TrangThai.DAT_CHO_CHO_THANH_TOAN);

            ps.executeUpdate();
            return getGeneratedId(ps);
        }
    }

    private int findOrCreateHanhKhach(Connection conn, HanhKhach hk) throws SQLException {
        String selectSql = """
                SELECT id
                FROM dbo.HANH_KHACH
                WHERE loai_giay_to = ? AND so_giay_to = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(selectSql)) {
            ps.setString(1, hk.getLoaiGiayTo());
            ps.setString(2, hk.getSoGiayTo());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }

        String insertSql = """
                INSERT INTO dbo.HANH_KHACH
                (ho_ten, loai_giay_to, so_giay_to, ngay_sinh, quoc_tich)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, hk.getHoTen());
            ps.setString(2, hk.getLoaiGiayTo());
            ps.setString(3, hk.getSoGiayTo());

            if (hk.getNgaySinh() == null) {
                ps.setNull(4, java.sql.Types.DATE);
            } else {
                ps.setDate(4, Date.valueOf(hk.getNgaySinh()));
            }

            ps.setString(5, hk.getQuocTich());

            ps.executeUpdate();
            return getGeneratedId(ps);
        }
    }

    private void insertGiuCho(Connection conn,
                              int datChoId,
                              int chuyenTauId,
                              int gheId,
                              int gaDiId,
                              int gaDenId,
                              LocalDateTime thoiGianGiu,
                              LocalDateTime thoiGianHetHan) throws SQLException {

        String sql = """
                INSERT INTO dbo.GIU_CHO
                (dat_cho_id, chuyen_tau_id, ghe_id, ga_di_id, ga_den_id,
                 thoi_gian_giu, thoi_gian_het_han, trang_thai)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int index = 1;
            ps.setInt(index++, datChoId);
            ps.setInt(index++, chuyenTauId);
            ps.setInt(index++, gheId);
            ps.setInt(index++, gaDiId);
            ps.setInt(index++, gaDenId);
            ps.setTimestamp(index++, Timestamp.valueOf(thoiGianGiu));
            ps.setTimestamp(index++, Timestamp.valueOf(thoiGianHetHan));
            ps.setString(index++, TrangThai.GIU_CHO_DANG_GIU);
            ps.executeUpdate();
        }
    }

    private void insertVeChoThanhToan(Connection conn,
                                      int datChoId,
                                      int chuyenTauId,
                                      int gheId,
                                      int hanhKhachId,
                                      Integer doiTuongUuDaiId,
                                      int gaDiId,
                                      int gaDenId,
                                      KetQuaTinhGia gia) throws SQLException {

        String sql = """
                INSERT INTO dbo.VE
                (dat_cho_id, chuyen_tau_id, ghe_id, hanh_khach_id, doi_tuong_uu_dai_id,
                 ga_di_id, ga_den_id, ma_ve, gia_co_so, giam_doi_tuong,
                 phu_thu_cao_diem, gia_ve_chi_tiet, trang_thai)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            int index = 1;

            ps.setInt(index++, datChoId);
            ps.setInt(index++, chuyenTauId);
            ps.setInt(index++, gheId);
            ps.setInt(index++, hanhKhachId);

            if (doiTuongUuDaiId == null) {
                ps.setNull(index++, java.sql.Types.INTEGER);
            } else {
                ps.setInt(index++, doiTuongUuDaiId);
            }

            ps.setInt(index++, gaDiId);
            ps.setInt(index++, gaDenId);
            ps.setString(index++, "VE" + UUID.randomUUID().toString().replace("-", "").substring(0, 14).toUpperCase());
            ps.setBigDecimal(index++, nvl(gia.getGiaCoSo()));
            ps.setBigDecimal(index++, nvl(gia.getGiamDoiTuong()));
            ps.setBigDecimal(index++, nvl(gia.getPhuThuCaoDiem()));
            ps.setBigDecimal(index++, nvl(gia.getThanhTien()));
            ps.setString(index++, TrangThai.VE_CHO_THANH_TOAN);

            ps.executeUpdate();
        }
    }

    private boolean kiemTraGheTrongTheoChang(Connection conn,
                                             int chuyenTauId,
                                             int gheId,
                                             int gaDiId,
                                             int gaDenId) throws SQLException {

        String sql = """
                WITH ChangMoi AS (
                    SELECT ld_di.thu_tu_dung AS tu, ld_den.thu_tu_dung AS den
                    FROM dbo.LICH_DUNG ld_di
                    JOIN dbo.LICH_DUNG ld_den
                        ON ld_den.chuyen_tau_id = ld_di.chuyen_tau_id
                    WHERE ld_di.chuyen_tau_id = ?
                      AND ld_di.ga_id = ?
                      AND ld_den.ga_id = ?
                      AND ld_di.thu_tu_dung < ld_den.thu_tu_dung
                ),
                ChangVe AS (
                    SELECT ld_di.thu_tu_dung AS tu, ld_den.thu_tu_dung AS den
                    FROM dbo.VE v WITH (UPDLOCK, HOLDLOCK)
                    JOIN dbo.LICH_DUNG ld_di
                        ON ld_di.chuyen_tau_id = v.chuyen_tau_id
                       AND ld_di.ga_id = v.ga_di_id
                    JOIN dbo.LICH_DUNG ld_den
                        ON ld_den.chuyen_tau_id = v.chuyen_tau_id
                       AND ld_den.ga_id = v.ga_den_id
                    WHERE v.chuyen_tau_id = ?
                      AND v.ghe_id = ?
                      AND v.trang_thai IN (N'Hợp lệ', N'Đã sử dụng')
                ),
                ChangGiu AS (
                    SELECT ld_di.thu_tu_dung AS tu, ld_den.thu_tu_dung AS den
                    FROM dbo.GIU_CHO gc WITH (UPDLOCK, HOLDLOCK)
                    JOIN dbo.LICH_DUNG ld_di
                        ON ld_di.chuyen_tau_id = gc.chuyen_tau_id
                       AND ld_di.ga_id = gc.ga_di_id
                    JOIN dbo.LICH_DUNG ld_den
                        ON ld_den.chuyen_tau_id = gc.chuyen_tau_id
                       AND ld_den.ga_id = gc.ga_den_id
                    WHERE gc.chuyen_tau_id = ?
                      AND gc.ghe_id = ?
                      AND gc.trang_thai = N'Đang giữ'
                      AND gc.thoi_gian_het_han > SYSDATETIME()
                )
                SELECT CASE
                    WHEN EXISTS (SELECT 1 FROM ChangMoi)
                     AND NOT EXISTS (
                         SELECT 1
                         FROM ChangMoi m
                         JOIN ChangVe x ON m.tu < x.den AND x.tu < m.den
                     )
                     AND NOT EXISTS (
                         SELECT 1
                         FROM ChangMoi m
                         JOIN ChangGiu x ON m.tu < x.den AND x.tu < m.den
                     )
                    THEN 1 ELSE 0
                END AS ok
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, chuyenTauId);
            ps.setInt(2, gaDiId);
            ps.setInt(3, gaDenId);
            ps.setInt(4, chuyenTauId);
            ps.setInt(5, gheId);
            ps.setInt(6, chuyenTauId);
            ps.setInt(7, gheId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt("ok") == 1;
            }
        }
    }

    private int getGeneratedId(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.getGeneratedKeys()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }

        throw new SQLException("Không lấy được ID vừa tạo.");
    }

    private BigDecimal nvl(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}