package vetau.dao;

import vetau.dto.TicketCheckResultDTO;
import vetau.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;

public class TicketCheckDAO {

    public TicketCheckResultDTO findByMaVeAndSoGiayTo(String maVe, String soGiayTo) throws SQLException {
        String sql = ""
                + "SELECT "
                + "    v.ma_ve, v.trang_thai, v.gia_ve_chi_tiet, "
                + "    hk.ho_ten AS ten_hanh_khach, hk.loai_giay_to, hk.so_giay_to, "
                + "    ct.ma_chuyen, t.ma_tau, t.ten_tau, "
                + "    ga_di.ten_ga AS ten_ga_di, ga_den.ten_ga AS ten_ga_den, "
                + "    ld_di.thoi_gian_di, ld_den.thoi_gian_den, "
                + "    toa.so_toa, ghe.so_ghe "
                + "FROM dbo.VE v "
                + "JOIN dbo.HANH_KHACH hk ON hk.id = v.hanh_khach_id "
                + "JOIN dbo.CHUYEN_TAU ct ON ct.id = v.chuyen_tau_id "
                + "JOIN dbo.TAU t ON t.id = ct.tau_id "
                + "JOIN dbo.GHE ghe ON ghe.id = v.ghe_id "
                + "JOIN dbo.TOA_TAU toa ON toa.id = ghe.toa_tau_id "
                + "JOIN dbo.GA ga_di ON ga_di.id = v.ga_di_id "
                + "JOIN dbo.GA ga_den ON ga_den.id = v.ga_den_id "
                + "LEFT JOIN dbo.LICH_DUNG ld_di ON ld_di.chuyen_tau_id = v.chuyen_tau_id AND ld_di.ga_id = v.ga_di_id "
                + "LEFT JOIN dbo.LICH_DUNG ld_den ON ld_den.chuyen_tau_id = v.chuyen_tau_id AND ld_den.ga_id = v.ga_den_id "
                + "WHERE v.ma_ve = ? "
                + "  AND (? IS NULL OR hk.so_giay_to = ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maVe);

            if (soGiayTo == null || soGiayTo.trim().isEmpty()) {
                ps.setNull(2, java.sql.Types.NVARCHAR);
                ps.setNull(3, java.sql.Types.NVARCHAR);
            } else {
                ps.setString(2, soGiayTo.trim());
                ps.setString(3, soGiayTo.trim());
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResult(rs);
                }
            }
        }

        return null;
    }

    private TicketCheckResultDTO mapResult(ResultSet rs) throws SQLException {
        TicketCheckResultDTO dto = new TicketCheckResultDTO();
        dto.setFound(true);
        dto.setMessage("Tìm thấy vé.");

        dto.setMaVe(rs.getString("ma_ve"));
        dto.setTrangThai(rs.getString("trang_thai"));
        dto.setGiaVe(rs.getBigDecimal("gia_ve_chi_tiet"));

        dto.setTenHanhKhach(rs.getString("ten_hanh_khach"));
        dto.setLoaiGiayTo(rs.getString("loai_giay_to"));
        dto.setSoGiayTo(rs.getString("so_giay_to"));

        dto.setMaChuyen(rs.getString("ma_chuyen"));
        dto.setMaTau(rs.getString("ma_tau"));
        dto.setTenTau(rs.getString("ten_tau"));
        dto.setTenGaDi(rs.getString("ten_ga_di"));
        dto.setTenGaDen(rs.getString("ten_ga_den"));

        Timestamp thoiGianDi = rs.getTimestamp("thoi_gian_di");
        if (thoiGianDi != null) {
            dto.setThoiGianDi(thoiGianDi.toLocalDateTime());
        }

        Timestamp thoiGianDen = rs.getTimestamp("thoi_gian_den");
        if (thoiGianDen != null) {
            dto.setThoiGianDen(thoiGianDen.toLocalDateTime());
        }

        int soToa = rs.getInt("so_toa");
        if (!rs.wasNull()) {
            dto.setSoToa(soToa);
        }

        dto.setSoGhe(rs.getString("so_ghe"));

        return dto;
    }
}
