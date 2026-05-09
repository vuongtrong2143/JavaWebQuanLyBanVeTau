package vetau.dao;

import vetau.dto.PromotionViewDTO;
import vetau.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PublicInfoDAO {

    public List<PromotionViewDTO> findActivePromotions() throws SQLException {
        String sql = ""
                + "SELECT TOP 20 "
                + "    ma_khuyen_mai, ten_chuong_trinh, phan_tram_giam, giam_toi_da, "
                + "    gia_tri_don_toi_thieu, phuong_thuc_tt_ap_dung, ngay_bat_dau, "
                + "    ngay_ket_thuc, so_luong_toi_da "
                + "FROM dbo.KHUYEN_MAI "
                + "WHERE trang_thai = N'Hoạt động' "
                + "  AND ngay_bat_dau <= SYSDATETIME() "
                + "  AND ngay_ket_thuc >= SYSDATETIME() "
                + "ORDER BY ngay_bat_dau DESC";

        List<PromotionViewDTO> result = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.add(mapPromotion(rs));
            }
        }

        return result;
    }

    private PromotionViewDTO mapPromotion(ResultSet rs) throws SQLException {
        PromotionViewDTO dto = new PromotionViewDTO();
        dto.setMaKhuyenMai(rs.getString("ma_khuyen_mai"));
        dto.setTenChuongTrinh(rs.getString("ten_chuong_trinh"));
        dto.setPhanTramGiam(rs.getBigDecimal("phan_tram_giam"));
        dto.setGiamToiDa(rs.getBigDecimal("giam_toi_da"));
        dto.setGiaTriDonToiThieu(rs.getBigDecimal("gia_tri_don_toi_thieu"));
        dto.setPhuongThucThanhToanApDung(rs.getString("phuong_thuc_tt_ap_dung"));

        Timestamp ngayBatDau = rs.getTimestamp("ngay_bat_dau");
        if (ngayBatDau != null) {
            dto.setNgayBatDau(ngayBatDau.toLocalDateTime());
        }

        Timestamp ngayKetThuc = rs.getTimestamp("ngay_ket_thuc");
        if (ngayKetThuc != null) {
            dto.setNgayKetThuc(ngayKetThuc.toLocalDateTime());
        }

        int soLuong = rs.getInt("so_luong_toi_da");
        if (!rs.wasNull()) {
            dto.setSoLuongToiDa(soLuong);
        }

        return dto;
    }
}
