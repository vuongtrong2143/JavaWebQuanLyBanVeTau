package vetau.dao;

import vetau.dto.BookingHistoryDTO;
import vetau.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyBookingDAO {

    public List<BookingHistoryDTO> findByCustomerId(int khachHangId) throws SQLException {
        String sql = ""
                + "SELECT "
                + "    dc.id, dc.ma_dat_cho, dc.loai_don_hang, dc.loai_hanh_trinh, "
                + "    dc.ngay_dat, dc.tong_thanh_toan, dc.trang_thai AS trang_thai_dat_cho, "
                + "    COALESCE(MAX(tt.trang_thai), N'Chưa có thanh toán') AS trang_thai_thanh_toan, "
                + "    COUNT(DISTINCT v.id) AS so_ve "
                + "FROM dbo.DAT_CHO dc "
                + "LEFT JOIN dbo.THANH_TOAN tt ON tt.dat_cho_id = dc.id "
                + "LEFT JOIN dbo.VE v ON v.dat_cho_id = dc.id "
                + "WHERE dc.khach_hang_id = ? "
                + "GROUP BY dc.id, dc.ma_dat_cho, dc.loai_don_hang, dc.loai_hanh_trinh, "
                + "         dc.ngay_dat, dc.tong_thanh_toan, dc.trang_thai "
                + "ORDER BY dc.ngay_dat DESC";

        List<BookingHistoryDTO> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, khachHangId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }

        return list;
    }

    private BookingHistoryDTO mapRow(ResultSet rs) throws SQLException {
        BookingHistoryDTO dto = new BookingHistoryDTO();

        dto.setId(rs.getInt("id"));
        dto.setMaDatCho(rs.getString("ma_dat_cho"));
        dto.setLoaiDonHang(rs.getString("loai_don_hang"));
        dto.setLoaiHanhTrinh(rs.getString("loai_hanh_trinh"));

        Timestamp ngayDat = rs.getTimestamp("ngay_dat");
        if (ngayDat != null) {
            dto.setNgayDat(ngayDat.toLocalDateTime());
        }

        dto.setTongThanhToan(rs.getBigDecimal("tong_thanh_toan"));
        dto.setTrangThaiDatCho(rs.getString("trang_thai_dat_cho"));
        dto.setTrangThaiThanhToan(rs.getString("trang_thai_thanh_toan"));
        dto.setSoVe(rs.getInt("so_ve"));

        return dto;
    }
}
