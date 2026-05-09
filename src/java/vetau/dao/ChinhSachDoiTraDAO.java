package vetau.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import vetau.model.ChinhSachDoiTra;
import vetau.util.DBConnection;

public class ChinhSachDoiTraDAO {

    private static final String SELECT_BASE = "SELECT id, ten_chinh_sach, loai_don_hang_ap_dung, chieu_tau_ap_dung, truoc_khoi_hanh_tu_gio, truoc_khoi_hanh_den_gio, ty_le_khau_tru, phi_doi_co_dinh, cho_phep_doi, cho_phep_tra, hieu_luc_tu, hieu_luc_den, do_uu_tien, trang_thai FROM dbo.CHINH_SACH_DOI_TRA";

    // =========================================================================
    // PHẦN 1: CÁC HÀM CŨ DÙNG CHO PHÍA KHÁCH HÀNG (GIỮ NGUYÊN)
    // =========================================================================

    public List<ChinhSachDoiTra> findAll() throws SQLException {
        List<ChinhSachDoiTra> list = new ArrayList<>();
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

    public ChinhSachDoiTra findById(int id) throws SQLException {
        String sql = SELECT_BASE + " WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public int insert(ChinhSachDoiTra item) throws SQLException {
        String sql = "INSERT INTO dbo.CHINH_SACH_DOI_TRA (ten_chinh_sach, loai_don_hang_ap_dung, chieu_tau_ap_dung, truoc_khoi_hanh_tu_gio, truoc_khoi_hanh_den_gio, ty_le_khau_tru, phi_doi_co_dinh, cho_phep_doi, cho_phep_tra, hieu_luc_tu, hieu_luc_den, do_uu_tien, trang_thai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;
            DaoUtil.setString(ps, index++, item.getTenChinhSach());
            DaoUtil.setString(ps, index++, item.getLoaiDonHangApDung());
            DaoUtil.setString(ps, index++, item.getChieuTauApDung());
            DaoUtil.setInteger(ps, index++, item.getTruocKhoiHanhTuGio());
            DaoUtil.setInteger(ps, index++, item.getTruocKhoiHanhDenGio());
            DaoUtil.setBigDecimal(ps, index++, item.getTyLeKhauTru());
            DaoUtil.setBigDecimal(ps, index++, item.getPhiDoiCoDinh());
            ps.setBoolean(index++, item.isChoPhepDoi());
            ps.setBoolean(index++, item.isChoPhepTra());
            DaoUtil.setLocalDateTime(ps, index++, item.getHieuLucTu());
            DaoUtil.setLocalDateTime(ps, index++, item.getHieuLucDen());
            ps.setInt(index++, item.getDoUuTien());
            DaoUtil.setString(ps, index++, item.getTrangThai());
            ps.executeUpdate();
            return DaoUtil.getGeneratedId(ps);
        }
    }

    public ChinhSachDoiTra mapRow(ResultSet rs) throws SQLException {
        ChinhSachDoiTra item = new ChinhSachDoiTra();
        item.setId(rs.getInt("id"));
        item.setTenChinhSach(rs.getString("ten_chinh_sach"));
        item.setLoaiDonHangApDung(rs.getString("loai_don_hang_ap_dung"));
        item.setChieuTauApDung(rs.getString("chieu_tau_ap_dung"));
        item.setTruocKhoiHanhTuGio(DaoUtil.getInteger(rs, "truoc_khoi_hanh_tu_gio"));
        item.setTruocKhoiHanhDenGio(DaoUtil.getInteger(rs, "truoc_khoi_hanh_den_gio"));
        item.setTyLeKhauTru(rs.getBigDecimal("ty_le_khau_tru"));
        item.setPhiDoiCoDinh(rs.getBigDecimal("phi_doi_co_dinh"));
        item.setChoPhepDoi(rs.getBoolean("cho_phep_doi"));
        item.setChoPhepTra(rs.getBoolean("cho_phep_tra"));
        item.setHieuLucTu(DaoUtil.getLocalDateTime(rs, "hieu_luc_tu"));
        item.setHieuLucDen(DaoUtil.getLocalDateTime(rs, "hieu_luc_den"));
        item.setDoUuTien(rs.getInt("do_uu_tien"));
        item.setTrangThai(rs.getString("trang_thai"));
        return item;
    }

    public ChinhSachDoiTra findPhuHop(String loaiDonHang, String chieuTau, long soGioTruocKhoiHanh, boolean laDoiVe) throws SQLException {
        String sql = SELECT_BASE
                + " WHERE trang_thai = N'Hoạt động'"
                + " AND hieu_luc_tu <= SYSDATETIME()"
                + " AND (hieu_luc_den IS NULL OR hieu_luc_den >= SYSDATETIME())"
                + " AND (loai_don_hang_ap_dung = N'Tất cả' OR loai_don_hang_ap_dung = ?)"
                + " AND (chieu_tau_ap_dung = N'Tất cả' OR chieu_tau_ap_dung = ?)"
                + " AND (truoc_khoi_hanh_tu_gio IS NULL OR truoc_khoi_hanh_tu_gio <= ?)"
                + " AND (truoc_khoi_hanh_den_gio IS NULL OR truoc_khoi_hanh_den_gio >= ?)"
                + (laDoiVe ? " AND cho_phep_doi = 1" : " AND cho_phep_tra = 1")
                + " ORDER BY do_uu_tien DESC, id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, loaiDonHang);
            ps.setString(2, chieuTau);
            ps.setLong(3, soGioTruocKhoiHanh);
            ps.setLong(4, soGioTruocKhoiHanh);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    // =========================================================================
    // PHẦN 2: CÁC HÀM MỚI BỔ SUNG CHO TRANG QUẢN TRỊ ADMIN
    // =========================================================================

    public List<ChinhSachDoiTra> layDanhSachChinhSach() throws SQLException {
        List<ChinhSachDoiTra> list = new ArrayList<>();
        String sql = SELECT_BASE + " ORDER BY do_uu_tien DESC, id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public void update(ChinhSachDoiTra item) throws SQLException {
        String sql = "UPDATE dbo.CHINH_SACH_DOI_TRA SET ten_chinh_sach=?, loai_don_hang_ap_dung=?, chieu_tau_ap_dung=?, truoc_khoi_hanh_tu_gio=?, truoc_khoi_hanh_den_gio=?, ty_le_khau_tru=?, phi_doi_co_dinh=?, cho_phep_doi=?, cho_phep_tra=?, hieu_luc_tu=?, hieu_luc_den=?, do_uu_tien=?, trang_thai=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int index = 1;
            DaoUtil.setString(ps, index++, item.getTenChinhSach());
            DaoUtil.setString(ps, index++, item.getLoaiDonHangApDung());
            DaoUtil.setString(ps, index++, item.getChieuTauApDung());
            DaoUtil.setInteger(ps, index++, item.getTruocKhoiHanhTuGio());
            DaoUtil.setInteger(ps, index++, item.getTruocKhoiHanhDenGio());
            DaoUtil.setBigDecimal(ps, index++, item.getTyLeKhauTru());
            DaoUtil.setBigDecimal(ps, index++, item.getPhiDoiCoDinh());
            ps.setBoolean(index++, item.isChoPhepDoi());
            ps.setBoolean(index++, item.isChoPhepTra());
            DaoUtil.setLocalDateTime(ps, index++, item.getHieuLucTu());
            DaoUtil.setLocalDateTime(ps, index++, item.getHieuLucDen());
            ps.setInt(index++, item.getDoUuTien());
            DaoUtil.setString(ps, index++, item.getTrangThai());
            ps.setInt(index, item.getId());
            ps.executeUpdate();
        }
    }

public void delete(int id) throws SQLException {
        // Xóa mềm: Chuyển trạng thái thành Tạm dừng
        String sql = "UPDATE dbo.CHINH_SACH_DOI_TRA SET trang_thai = N'Tạm dừng' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}