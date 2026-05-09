package vetau.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import vetau.model.BangGia;
import vetau.util.DBConnection;

public class BangGiaDAO {

    private static final String SELECT_BASE = "SELECT id, ga_di_id, ga_den_id, loai_toa_ap_dung, tang_ap_dung, gia_co_so, phu_thu_cao_diem_mac_dinh, hieu_luc_tu, hieu_luc_den, trang_thai FROM dbo.BANG_GIA";

    // =========================================================================
    // PHẦN 1: CÁC HÀM CŨ DÙNG CHO PHÍA KHÁCH HÀNG (GIỮ NGUYÊN)
    // =========================================================================

    public List<BangGia> findAll() throws SQLException {
        List<BangGia> list = new ArrayList<>();
        String sql = SELECT_BASE + " ORDER BY id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapBasicRow(rs));
            }
        }
        return list;
    }

    public BangGia findById(int id) throws SQLException {
        String sql = SELECT_BASE + " WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapBasicRow(rs) : null;
            }
        }
    }

    public int insert(BangGia item) throws SQLException {
        String sql = "INSERT INTO dbo.BANG_GIA (ga_di_id, ga_den_id, loai_toa_ap_dung, tang_ap_dung, gia_co_so, phu_thu_cao_diem_mac_dinh, hieu_luc_tu, hieu_luc_den, trang_thai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;
            ps.setInt(index++, item.getGaDiId());
            ps.setInt(index++, item.getGaDenId());
            DaoUtil.setString(ps, index++, item.getLoaiToaApDung());
            DaoUtil.setInteger(ps, index++, item.getTangApDung());
            DaoUtil.setBigDecimal(ps, index++, item.getGiaCoSo());
            DaoUtil.setBigDecimal(ps, index++, item.getPhuThuCaoDiemMacDinh());
            DaoUtil.setLocalDateTime(ps, index++, item.getHieuLucTu());
            DaoUtil.setLocalDateTime(ps, index++, item.getHieuLucDen());
            DaoUtil.setString(ps, index++, item.getTrangThai());
            ps.executeUpdate();
            return DaoUtil.getGeneratedId(ps);
        }
    }

    public BangGia mapBasicRow(ResultSet rs) throws SQLException {
        BangGia item = new BangGia();
        item.setId(rs.getInt("id"));
        item.setGaDiId(rs.getInt("ga_di_id"));
        item.setGaDenId(rs.getInt("ga_den_id"));
        item.setLoaiToaApDung(rs.getString("loai_toa_ap_dung"));
        item.setTangApDung(DaoUtil.getInteger(rs, "tang_ap_dung"));
        item.setGiaCoSo(rs.getBigDecimal("gia_co_so"));
        item.setPhuThuCaoDiemMacDinh(rs.getBigDecimal("phu_thu_cao_diem_mac_dinh"));
        item.setHieuLucTu(DaoUtil.getLocalDateTime(rs, "hieu_luc_tu"));
        item.setHieuLucDen(DaoUtil.getLocalDateTime(rs, "hieu_luc_den"));
        item.setTrangThai(rs.getString("trang_thai"));
        return item;
    }

    public BangGia findGiaHienHanh(int gaDiId, int gaDenId, String loaiToa, Integer tang, java.time.LocalDateTime thoiDiem) throws SQLException {
        String sql = SELECT_BASE
                + " WHERE ga_di_id = ? AND ga_den_id = ?"
                + " AND loai_toa_ap_dung = ?"
                + " AND (tang_ap_dung IS NULL OR tang_ap_dung = ?)"
                + " AND hieu_luc_tu <= ?"
                + " AND (hieu_luc_den IS NULL OR hieu_luc_den >= ?)"
                + " AND trang_thai = N'Hoạt động'"
                + " ORDER BY CASE WHEN tang_ap_dung IS NULL THEN 1 ELSE 0 END, hieu_luc_tu DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, gaDiId);
            ps.setInt(2, gaDenId);
            ps.setString(3, loaiToa);
            DaoUtil.setInteger(ps, 4, tang);
            DaoUtil.setLocalDateTime(ps, 5, thoiDiem);
            DaoUtil.setLocalDateTime(ps, 6, thoiDiem);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapBasicRow(rs) : null;
            }
        }
    }

    // =========================================================================
    // PHẦN 2: CÁC HÀM MỚI BỔ SUNG CHO TRANG QUẢN TRỊ ADMIN
    // =========================================================================

    public List<BangGia> layDanhSachBangGia() throws SQLException {
        List<BangGia> list = new ArrayList<>();
        String sql = "SELECT bg.*, g1.ten_ga AS ten_ga_di, g2.ten_ga AS ten_ga_den " +
                     "FROM dbo.BANG_GIA bg " +
                     "JOIN dbo.GA g1 ON bg.ga_di_id = g1.id " +
                     "JOIN dbo.GA g2 ON bg.ga_den_id = g2.id " +
                     "ORDER BY bg.hieu_luc_tu DESC, bg.id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapAdminRow(rs));
            }
        }
        return list;
    }

    public void update(BangGia item) throws SQLException {
        String sql = "UPDATE dbo.BANG_GIA SET ga_di_id=?, ga_den_id=?, loai_toa_ap_dung=?, tang_ap_dung=?, gia_co_so=?, phu_thu_cao_diem_mac_dinh=?, hieu_luc_tu=?, hieu_luc_den=?, trang_thai=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int index = 1;
            ps.setInt(index++, item.getGaDiId());
            ps.setInt(index++, item.getGaDenId());
            DaoUtil.setString(ps, index++, item.getLoaiToaApDung());
            DaoUtil.setInteger(ps, index++, item.getTangApDung());
            DaoUtil.setBigDecimal(ps, index++, item.getGiaCoSo());
            DaoUtil.setBigDecimal(ps, index++, item.getPhuThuCaoDiemMacDinh());
            DaoUtil.setLocalDateTime(ps, index++, item.getHieuLucTu());
            DaoUtil.setLocalDateTime(ps, index++, item.getHieuLucDen());
            DaoUtil.setString(ps, index++, item.getTrangThai());
            ps.setInt(index, item.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        // XÓA MỀM BẢNG GIÁ
        String sql = "UPDATE dbo.BANG_GIA SET trang_thai = N'Tạm dừng' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private BangGia mapAdminRow(ResultSet rs) throws SQLException {
        BangGia item = mapBasicRow(rs);
        item.setTenGaDi(rs.getString("ten_ga_di"));
        item.setTenGaDen(rs.getString("ten_ga_den"));
        return item;
    }
}