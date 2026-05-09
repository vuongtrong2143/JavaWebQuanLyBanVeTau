package vetau.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import vetau.model.KhuyenMai;
import vetau.util.DBConnection;

public class KhuyenMaiDAO {

    private static final String SELECT_BASE = "SELECT id, ma_khuyen_mai, ten_chuong_trinh, phan_tram_giam, giam_toi_da, gia_tri_don_toi_thieu, phuong_thuc_tt_ap_dung, ngay_bat_dau, ngay_ket_thuc, so_luong_toi_da, trang_thai FROM dbo.KHUYEN_MAI";

    // =========================================================================
    // PHẦN 1: CÁC HÀM CŨ DÙNG CHO KHÁCH HÀNG (GIỮ NGUYÊN)
    // =========================================================================

    public List<KhuyenMai> findAll() throws SQLException {
        List<KhuyenMai> list = new ArrayList<>();
        String sql = SELECT_BASE + " ORDER BY id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public KhuyenMai findById(int id) throws SQLException {
        String sql = SELECT_BASE + " WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public int insert(KhuyenMai item) throws SQLException {
        String sql = "INSERT INTO dbo.KHUYEN_MAI (ma_khuyen_mai, ten_chuong_trinh, phan_tram_giam, giam_toi_da, gia_tri_don_toi_thieu, phuong_thuc_tt_ap_dung, ngay_bat_dau, ngay_ket_thuc, so_luong_toi_da, trang_thai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;
            DaoUtil.setString(ps, index++, item.getMaKhuyenMai());
            DaoUtil.setString(ps, index++, item.getTenChuongTrinh());
            DaoUtil.setBigDecimal(ps, index++, item.getPhanTramGiam());
            DaoUtil.setBigDecimal(ps, index++, item.getGiamToiDa());
            DaoUtil.setBigDecimal(ps, index++, item.getGiaTriDonToiThieu());
            DaoUtil.setString(ps, index++, item.getPhuongThucTtApDung());
            DaoUtil.setLocalDateTime(ps, index++, item.getNgayBatDau());
            DaoUtil.setLocalDateTime(ps, index++, item.getNgayKetThuc());
            DaoUtil.setInteger(ps, index++, item.getSoLuongToiDa());
            DaoUtil.setString(ps, index++, item.getTrangThai());
            ps.executeUpdate();
            return DaoUtil.getGeneratedId(ps);
        }
    }

    public KhuyenMai mapRow(ResultSet rs) throws SQLException {
        KhuyenMai item = new KhuyenMai();
        item.setId(rs.getInt("id"));
        item.setMaKhuyenMai(rs.getString("ma_khuyen_mai"));
        item.setTenChuongTrinh(rs.getString("ten_chuong_trinh"));
        item.setPhanTramGiam(rs.getBigDecimal("phan_tram_giam"));
        item.setGiamToiDa(rs.getBigDecimal("giam_toi_da"));
        item.setGiaTriDonToiThieu(rs.getBigDecimal("gia_tri_don_toi_thieu"));
        item.setPhuongThucTtApDung(rs.getString("phuong_thuc_tt_ap_dung"));
        item.setNgayBatDau(DaoUtil.getLocalDateTime(rs, "ngay_bat_dau"));
        item.setNgayKetThuc(DaoUtil.getLocalDateTime(rs, "ngay_ket_thuc"));
        item.setSoLuongToiDa(DaoUtil.getInteger(rs, "so_luong_toi_da"));
        item.setTrangThai(rs.getString("trang_thai"));
        return item;
    }

    public KhuyenMai findActiveByCode(String maKhuyenMai, String phuongThucThanhToan, java.time.LocalDateTime thoiDiem) throws SQLException {
        String sql = SELECT_BASE
                + " WHERE ma_khuyen_mai = ?"
                + " AND trang_thai = N'Hoạt động'"
                + " AND ngay_bat_dau <= ? AND ngay_ket_thuc >= ?"
                + " AND (phuong_thuc_tt_ap_dung IS NULL OR phuong_thuc_tt_ap_dung = ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maKhuyenMai);
            DaoUtil.setLocalDateTime(ps, 2, thoiDiem);
            DaoUtil.setLocalDateTime(ps, 3, thoiDiem);
            ps.setString(4, phuongThucThanhToan);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    // =========================================================================
    // PHẦN 2: CÁC HÀM MỚI BỔ SUNG CHO TRANG QUẢN TRỊ ADMIN
    // =========================================================================

    public List<KhuyenMai> layDanhSachKhuyenMai() throws SQLException {
        List<KhuyenMai> list = new ArrayList<>();
        // Sắp xếp ưu tiên các chương trình mới nhất lên đầu
        String sql = SELECT_BASE + " ORDER BY ngay_bat_dau DESC, id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public void update(KhuyenMai item) throws SQLException {
        String sql = "UPDATE dbo.KHUYEN_MAI SET ma_khuyen_mai=?, ten_chuong_trinh=?, phan_tram_giam=?, giam_toi_da=?, gia_tri_don_toi_thieu=?, phuong_thuc_tt_ap_dung=?, ngay_bat_dau=?, ngay_ket_thuc=?, so_luong_toi_da=?, trang_thai=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int index = 1;
            DaoUtil.setString(ps, index++, item.getMaKhuyenMai());
            DaoUtil.setString(ps, index++, item.getTenChuongTrinh());
            DaoUtil.setBigDecimal(ps, index++, item.getPhanTramGiam());
            DaoUtil.setBigDecimal(ps, index++, item.getGiamToiDa());
            DaoUtil.setBigDecimal(ps, index++, item.getGiaTriDonToiThieu());
            DaoUtil.setString(ps, index++, item.getPhuongThucTtApDung());
            DaoUtil.setLocalDateTime(ps, index++, item.getNgayBatDau());
            DaoUtil.setLocalDateTime(ps, index++, item.getNgayKetThuc());
            DaoUtil.setInteger(ps, index++, item.getSoLuongToiDa());
            DaoUtil.setString(ps, index++, item.getTrangThai());
            ps.setInt(index, item.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        // Xóa mềm: Chuyển trạng thái thành Tạm dừng
        String sql = "UPDATE dbo.KHUYEN_MAI SET trang_thai = N'Tạm dừng' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}