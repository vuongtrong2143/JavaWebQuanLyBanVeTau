package vetau.dao;

import vetau.dto.ChuyenTauSearchResultDTO;
import vetau.model.ChuyenTau;
import vetau.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ChuyenTauDAO {

    // =========================================================================
    // PHẦN 1: CÁC HÀM DÀNH CHO WEBSITE KHÁCH HÀNG (TRẢ VỀ DTO)
    // =========================================================================
    
    public List<ChuyenTauSearchResultDTO> searchByRouteAndDate(int gaDiId, int gaDenId, LocalDate ngayDi) throws SQLException {
        String sql = """
                WITH Chang AS (
                    SELECT
                        ct.id AS chuyen_tau_id,
                        ct.tau_id,
                        ct.ma_chuyen,
                        t.ma_tau,
                        t.ten_tau,
                        g_di.ten_ga AS ga_di_ten,
                        g_den.ten_ga AS ga_den_ten,
                        ld_di.thu_tu_dung AS thu_tu_ga_di,
                        ld_den.thu_tu_dung AS thu_tu_ga_den,
                        COALESCE(ld_di.thoi_gian_di, ct.gio_khoi_hanh) AS thoi_gian_di,
                        COALESCE(ld_den.thoi_gian_den, ct.gio_den_du_kien) AS thoi_gian_den
                    FROM dbo.CHUYEN_TAU ct
                    JOIN dbo.TAU t ON t.id = ct.tau_id
                    JOIN dbo.LICH_DUNG ld_di
                        ON ld_di.chuyen_tau_id = ct.id
                       AND ld_di.ga_id = ?
                    JOIN dbo.LICH_DUNG ld_den
                        ON ld_den.chuyen_tau_id = ct.id
                       AND ld_den.ga_id = ?
                    JOIN dbo.GA g_di ON g_di.id = ld_di.ga_id
                    JOIN dbo.GA g_den ON g_den.id = ld_den.ga_id
                    WHERE ct.ngay_chay = ?
                      AND ct.trang_thai = N'Hoạt động'
                      AND ld_di.thu_tu_dung < ld_den.thu_tu_dung
                      AND COALESCE(ld_di.thoi_gian_di, ct.gio_khoi_hanh) > DATEADD(MINUTE, 60, SYSDATETIME())
                )
                SELECT
                    c.*,
                    (
                        SELECT MIN(bg.gia_co_so + bg.phu_thu_cao_diem_mac_dinh)
                        FROM dbo.BANG_GIA bg
                        WHERE bg.ga_di_id = ?
                          AND bg.ga_den_id = ?
                          AND bg.trang_thai = N'Hoạt động'
                          AND bg.hieu_luc_tu <= SYSDATETIME()
                          AND (bg.hieu_luc_den IS NULL OR bg.hieu_luc_den >= SYSDATETIME())
                    ) AS gia_thap_nhat,
                    (
                        SELECT COUNT(*)
                        FROM dbo.TOA_TAU toa
                        JOIN dbo.GHE ghe ON ghe.toa_tau_id = toa.id
                        WHERE toa.tau_id = c.tau_id
                          AND ghe.trang_thai = N'Hoạt động'
                    ) AS so_ghe_hoat_dong,
                    (
                        SELECT COUNT(DISTINCT x.ghe_id)
                        FROM (
                            SELECT v.ghe_id
                            FROM dbo.VE v
                            JOIN dbo.LICH_DUNG v_di
                                ON v_di.chuyen_tau_id = v.chuyen_tau_id
                               AND v_di.ga_id = v.ga_di_id
                            JOIN dbo.LICH_DUNG v_den
                                ON v_den.chuyen_tau_id = v.chuyen_tau_id
                               AND v_den.ga_id = v.ga_den_id
                            WHERE v.chuyen_tau_id = c.chuyen_tau_id
                              AND v.trang_thai IN (N'Hợp lệ', N'Đã sử dụng')
                              AND c.thu_tu_ga_di < v_den.thu_tu_dung
                              AND v_di.thu_tu_dung < c.thu_tu_ga_den

                            UNION

                            SELECT gc.ghe_id
                            FROM dbo.GIU_CHO gc
                            JOIN dbo.LICH_DUNG gc_di
                                ON gc_di.chuyen_tau_id = gc.chuyen_tau_id
                               AND gc_di.ga_id = gc.ga_di_id
                            JOIN dbo.LICH_DUNG gc_den
                                ON gc_den.chuyen_tau_id = gc.chuyen_tau_id
                               AND gc_den.ga_id = gc.ga_den_id
                            WHERE gc.chuyen_tau_id = c.chuyen_tau_id
                              AND gc.trang_thai = N'Đang giữ'
                              AND gc.thoi_gian_het_han > SYSDATETIME()
                              AND c.thu_tu_ga_di < gc_den.thu_tu_dung
                              AND gc_di.thu_tu_dung < c.thu_tu_ga_den
                        ) x
                    ) AS so_ghe_da_bi_chiem
                FROM Chang c
                ORDER BY c.thoi_gian_di ASC
                """;

        List<ChuyenTauSearchResultDTO> results = new ArrayList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, gaDiId);
            statement.setInt(2, gaDenId);
            statement.setDate(3, Date.valueOf(ngayDi));
            statement.setInt(4, gaDiId);
            statement.setInt(5, gaDenId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    results.add(mapSearchResultRow(resultSet));
                }
            }
        }
        return results;
    }

    public ChuyenTauSearchResultDTO findTripDetailByIdAndRoute(int chuyenTauId, int gaDiId, int gaDenId) throws SQLException {
        String sql = """
                WITH Chang AS (
                    SELECT
                        ct.id AS chuyen_tau_id,
                        ct.tau_id,
                        ct.ma_chuyen,
                        t.ma_tau,
                        t.ten_tau,
                        g_di.ten_ga AS ga_di_ten,
                        g_den.ten_ga AS ga_den_ten,
                        ld_di.thu_tu_dung AS thu_tu_ga_di,
                        ld_den.thu_tu_dung AS thu_tu_ga_den,
                        COALESCE(ld_di.thoi_gian_di, ct.gio_khoi_hanh) AS thoi_gian_di,
                        COALESCE(ld_den.thoi_gian_den, ct.gio_den_du_kien) AS thoi_gian_den
                    FROM dbo.CHUYEN_TAU ct
                    JOIN dbo.TAU t ON t.id = ct.tau_id
                    JOIN dbo.LICH_DUNG ld_di
                        ON ld_di.chuyen_tau_id = ct.id
                       AND ld_di.ga_id = ?
                    JOIN dbo.LICH_DUNG ld_den
                        ON ld_den.chuyen_tau_id = ct.id
                       AND ld_den.ga_id = ?
                    JOIN dbo.GA g_di ON g_di.id = ld_di.ga_id
                    JOIN dbo.GA g_den ON g_den.id = ld_den.ga_id
                    WHERE ct.id = ?
                      AND ct.trang_thai = N'Hoạt động'
                      AND ld_di.thu_tu_dung < ld_den.thu_tu_dung
                )
                SELECT
                    c.*,
                    (
                        SELECT MIN(bg.gia_co_so + bg.phu_thu_cao_diem_mac_dinh)
                        FROM dbo.BANG_GIA bg
                        WHERE bg.ga_di_id = ?
                          AND bg.ga_den_id = ?
                          AND bg.trang_thai = N'Hoạt động'
                          AND bg.hieu_luc_tu <= SYSDATETIME()
                          AND (bg.hieu_luc_den IS NULL OR bg.hieu_luc_den >= SYSDATETIME())
                    ) AS gia_thap_nhat,
                    (
                        SELECT COUNT(*)
                        FROM dbo.TOA_TAU toa
                        JOIN dbo.GHE ghe ON ghe.toa_tau_id = toa.id
                        WHERE toa.tau_id = c.tau_id
                          AND ghe.trang_thai = N'Hoạt động'
                    ) AS so_ghe_hoat_dong,
                    0 AS so_ghe_da_bi_chiem
                FROM Chang c
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, gaDiId);
            statement.setInt(2, gaDenId);
            statement.setInt(3, chuyenTauId);
            statement.setInt(4, gaDiId);
            statement.setInt(5, gaDenId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapSearchResultRow(resultSet);
                }
            }
        }
        return null;
    }

    private ChuyenTauSearchResultDTO mapSearchResultRow(ResultSet resultSet) throws SQLException {
        ChuyenTauSearchResultDTO dto = new ChuyenTauSearchResultDTO();
        dto.setChuyenTauId(resultSet.getInt("chuyen_tau_id"));
        dto.setMaChuyen(resultSet.getString("ma_chuyen"));
        dto.setMaTau(resultSet.getString("ma_tau"));
        dto.setTenTau(resultSet.getString("ten_tau"));
        dto.setGaDiTen(resultSet.getString("ga_di_ten"));
        dto.setGaDenTen(resultSet.getString("ga_den_ten"));
        dto.setThuTuGaDi(resultSet.getInt("thu_tu_ga_di"));
        dto.setThuTuGaDen(resultSet.getInt("thu_tu_ga_den"));
        dto.setThoiGianDi(toLocalDateTime(resultSet.getTimestamp("thoi_gian_di")));
        dto.setThoiGianDen(toLocalDateTime(resultSet.getTimestamp("thoi_gian_den")));
        dto.setGiaThapNhat(resultSet.getBigDecimal("gia_thap_nhat"));
        dto.setSoGheHoatDong(resultSet.getInt("so_ghe_hoat_dong"));
        dto.setSoGheDaBiChiem(resultSet.getInt("so_ghe_da_bi_chiem"));
        return dto;
    }

    // =========================================================================
    // PHẦN 2: CÁC HÀM DÀNH CHO TRANG QUẢN TRỊ ADMIN (TRẢ VỀ MODEL CRUD)
    // =========================================================================

    public List<ChuyenTau> layDanhSachChuyenTau() throws SQLException {
        List<ChuyenTau> list = new ArrayList<>();
        String sql = "SELECT ct.*, t.ten_tau FROM dbo.CHUYEN_TAU ct " +
                     "JOIN dbo.TAU t ON ct.tau_id = t.id " +
                     "ORDER BY ct.ngay_chay DESC, ct.gio_khoi_hanh DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapModelRow(rs));
            }
        }
        return list;
    }

    public ChuyenTau findById(int id) throws SQLException {
        String sql = "SELECT ct.*, t.ten_tau FROM dbo.CHUYEN_TAU ct " +
                     "JOIN dbo.TAU t ON ct.tau_id = t.id WHERE ct.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapModelRow(rs);
                }
            }
        }
        return null;
    }

    public void insert(ChuyenTau ct) throws SQLException {
        String sql = "INSERT INTO dbo.CHUYEN_TAU (tau_id, ma_chuyen, ngay_chay, gio_khoi_hanh, gio_den_du_kien, trang_thai) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setParameters(ps, ct);
            ps.executeUpdate();
        }
    }

    public void update(ChuyenTau ct) throws SQLException {
        String sql = "UPDATE dbo.CHUYEN_TAU SET tau_id=?, ma_chuyen=?, ngay_chay=?, gio_khoi_hanh=?, gio_den_du_kien=?, trang_thai=? " +
                     "WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setParameters(ps, ct);
            ps.setInt(7, ct.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        // XÓA MỀM (Hủy chuyến)
        String sql = "UPDATE dbo.CHUYEN_TAU SET trang_thai = N'Hủy' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private ChuyenTau mapModelRow(ResultSet rs) throws SQLException {
        ChuyenTau ct = new ChuyenTau();
        ct.setId(rs.getInt("id"));
        ct.setTauId(rs.getInt("tau_id"));
        ct.setMaChuyen(rs.getString("ma_chuyen"));
        ct.setNgayChay(rs.getDate("ngay_chay").toLocalDate());
        ct.setGioKhoiHanh(rs.getTimestamp("gio_khoi_hanh").toLocalDateTime());
        
        Timestamp gioDen = rs.getTimestamp("gio_den_du_kien");
        if (gioDen != null) {
            ct.setGioDenDuKien(gioDen.toLocalDateTime());
        }
        
        ct.setTrangThai(rs.getString("trang_thai"));
        ct.setTenTau(rs.getString("ten_tau")); // Lấy từ JOIN để hiển thị
        return ct;
    }

    private void setParameters(PreparedStatement ps, ChuyenTau ct) throws SQLException {
        ps.setInt(1, ct.getTauId());
        ps.setString(2, ct.getMaChuyen());
        ps.setDate(3, Date.valueOf(ct.getNgayChay()));
        ps.setTimestamp(4, Timestamp.valueOf(ct.getGioKhoiHanh()));
        
        if (ct.getGioDenDuKien() != null) {
            ps.setTimestamp(5, Timestamp.valueOf(ct.getGioDenDuKien()));
        } else {
            ps.setNull(5, java.sql.Types.TIMESTAMP);
        }
        ps.setString(6, ct.getTrangThai());
    }

    // Hàm tiện ích chung dùng cho cả Khách và Admin
    private LocalDateTime toLocalDateTime(Timestamp timestamp) {
        return timestamp == null ? null : timestamp.toLocalDateTime();
    }
}