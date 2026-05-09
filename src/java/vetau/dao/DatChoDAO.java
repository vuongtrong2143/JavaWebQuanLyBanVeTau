package vetau.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import vetau.model.DatCho;
import vetau.util.DBConnection;

public class DatChoDAO {

    private static final String SELECT_BASE = "SELECT id, khach_hang_id, khuyen_mai_id, ma_dat_cho, loai_don_hang, loai_hanh_trinh, ngay_dat, tong_tien_ve_goc, thue_vat, phi_thanh_toan, tong_giam_khuyen_mai, giam_gia_khu_hoi, tong_thanh_toan, thoi_gian_het_han, trang_thai FROM dbo.DAT_CHO";

    public List<DatCho> findAll() throws SQLException {
        List<DatCho> list = new ArrayList<>();
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

    public DatCho findById(int id) throws SQLException {
        String sql = SELECT_BASE + " WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public int insert(DatCho item) throws SQLException {
        String sql = "INSERT INTO dbo.DAT_CHO (khach_hang_id, khuyen_mai_id, ma_dat_cho, loai_don_hang, loai_hanh_trinh, ngay_dat, tong_tien_ve_goc, thue_vat, phi_thanh_toan, tong_giam_khuyen_mai, giam_gia_khu_hoi, tong_thanh_toan, thoi_gian_het_han, trang_thai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;
            ps.setInt(index++, item.getKhachHangId());
            DaoUtil.setInteger(ps, index++, item.getKhuyenMaiId());
            DaoUtil.setString(ps, index++, item.getMaDatCho());
            DaoUtil.setString(ps, index++, item.getLoaiDonHang());
            DaoUtil.setString(ps, index++, item.getLoaiHanhTrinh());
            DaoUtil.setLocalDateTime(ps, index++, item.getNgayDat());
            DaoUtil.setBigDecimal(ps, index++, item.getTongTienVeGoc());
            DaoUtil.setBigDecimal(ps, index++, item.getThueVat());
            DaoUtil.setBigDecimal(ps, index++, item.getPhiThanhToan());
            DaoUtil.setBigDecimal(ps, index++, item.getTongGiamKhuyenMai());
            DaoUtil.setBigDecimal(ps, index++, item.getGiamGiaKhuHoi());
            DaoUtil.setBigDecimal(ps, index++, item.getTongThanhToan());
            DaoUtil.setLocalDateTime(ps, index++, item.getThoiGianHetHan());
            DaoUtil.setString(ps, index++, item.getTrangThai());
            ps.executeUpdate();
            return DaoUtil.getGeneratedId(ps);
        }
    }

    public DatCho mapRow(ResultSet rs) throws SQLException {
        DatCho item = new DatCho();
        item.setId(rs.getInt("id"));
        item.setKhachHangId(rs.getInt("khach_hang_id"));
        item.setKhuyenMaiId(DaoUtil.getInteger(rs, "khuyen_mai_id"));
        item.setMaDatCho(rs.getString("ma_dat_cho"));
        item.setLoaiDonHang(rs.getString("loai_don_hang"));
        item.setLoaiHanhTrinh(rs.getString("loai_hanh_trinh"));
        item.setNgayDat(DaoUtil.getLocalDateTime(rs, "ngay_dat"));
        item.setTongTienVeGoc(rs.getBigDecimal("tong_tien_ve_goc"));
        item.setThueVat(rs.getBigDecimal("thue_vat"));
        item.setPhiThanhToan(rs.getBigDecimal("phi_thanh_toan"));
        item.setTongGiamKhuyenMai(rs.getBigDecimal("tong_giam_khuyen_mai"));
        item.setGiamGiaKhuHoi(rs.getBigDecimal("giam_gia_khu_hoi"));
        item.setTongThanhToan(rs.getBigDecimal("tong_thanh_toan"));
        item.setThoiGianHetHan(DaoUtil.getLocalDateTime(rs, "thoi_gian_het_han"));
        item.setTrangThai(rs.getString("trang_thai"));
        return item;
    }

    public DatCho findByMaDatCho(String maDatCho) throws SQLException {
        String sql = SELECT_BASE + " WHERE ma_dat_cho = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maDatCho);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public void updateTrangThai(int datChoId, String trangThai) throws SQLException {
        String sql = "UPDATE dbo.DAT_CHO SET trang_thai = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setInt(2, datChoId);
            ps.executeUpdate();
        }
    }

}
