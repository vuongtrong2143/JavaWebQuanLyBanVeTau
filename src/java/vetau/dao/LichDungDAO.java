package vetau.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import vetau.model.LichDung;
import vetau.util.DBConnection;

public class LichDungDAO {

    private static final String SELECT_BASE = "SELECT id, chuyen_tau_id, ga_id, thu_tu_dung, thoi_gian_den, thoi_gian_di FROM dbo.LICH_DUNG";

    // =========================================================================
    // PHẦN 1: CÁC HÀM CŨ DÙNG CHO PHÍA KHÁCH HÀNG (GIỮ NGUYÊN)
    // =========================================================================

    public List<LichDung> findAll() throws SQLException {
        List<LichDung> list = new ArrayList<>();
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

    public int insert(LichDung item) throws SQLException {
        String sql = "INSERT INTO dbo.LICH_DUNG (chuyen_tau_id, ga_id, thu_tu_dung, thoi_gian_den, thoi_gian_di) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;
            ps.setInt(index++, item.getChuyenTauId());
            ps.setInt(index++, item.getGaId());
            ps.setInt(index++, item.getThuTuDung());
            DaoUtil.setLocalDateTime(ps, index++, item.getThoiGianDen());
            DaoUtil.setLocalDateTime(ps, index++, item.getThoiGianDi());
            ps.executeUpdate();
            return DaoUtil.getGeneratedId(ps);
        }
    }

    public LichDung mapBasicRow(ResultSet rs) throws SQLException {
        LichDung item = new LichDung();
        item.setId(rs.getInt("id"));
        item.setChuyenTauId(rs.getInt("chuyen_tau_id"));
        item.setGaId(rs.getInt("ga_id"));
        item.setThuTuDung(rs.getInt("thu_tu_dung"));
        item.setThoiGianDen(DaoUtil.getLocalDateTime(rs, "thoi_gian_den"));
        item.setThoiGianDi(DaoUtil.getLocalDateTime(rs, "thoi_gian_di"));
        return item;
    }

    // =========================================================================
    // PHẦN 2: CÁC HÀM MỚI BỔ SUNG CHO TRANG QUẢN TRỊ ADMIN
    // =========================================================================

    public List<LichDung> layDanhSachLichDung() throws SQLException {
        List<LichDung> list = new ArrayList<>();
        String sql = "SELECT ld.*, ct.ma_chuyen, g.ten_ga FROM dbo.LICH_DUNG ld " +
                     "JOIN dbo.CHUYEN_TAU ct ON ld.chuyen_tau_id = ct.id " +
                     "JOIN dbo.GA g ON ld.ga_id = g.id " +
                     "ORDER BY ct.ma_chuyen DESC, ld.thu_tu_dung ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapAdminRow(rs));
            }
        }
        return list;
    }

    // Cập nhật findById để lấy thêm mã chuyến và tên ga phục vụ form Admin
    public LichDung findById(int id) throws SQLException {
        String sql = "SELECT ld.*, ct.ma_chuyen, g.ten_ga FROM dbo.LICH_DUNG ld " +
                     "JOIN dbo.CHUYEN_TAU ct ON ld.chuyen_tau_id = ct.id " +
                     "JOIN dbo.GA g ON ld.ga_id = g.id WHERE ld.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapAdminRow(rs) : null;
            }
        }
    }

    public void update(LichDung ld) throws SQLException {
        String sql = "UPDATE dbo.LICH_DUNG SET chuyen_tau_id=?, ga_id=?, thu_tu_dung=?, thoi_gian_den=?, thoi_gian_di=? " +
                     "WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            int index = 1;
            ps.setInt(index++, ld.getChuyenTauId());
            ps.setInt(index++, ld.getGaId());
            ps.setInt(index++, ld.getThuTuDung());
            DaoUtil.setLocalDateTime(ps, index++, ld.getThoiGianDen());
            DaoUtil.setLocalDateTime(ps, index++, ld.getThoiGianDi());
            ps.setInt(index, ld.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        // DELETE CỨNG (Vì bảng này là bảng trung gian chi tiết)
        String sql = "DELETE FROM dbo.LICH_DUNG WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    private LichDung mapAdminRow(ResultSet rs) throws SQLException {
        // Tận dụng hàm map cũ để lấy các trường cơ bản
        LichDung ld = mapBasicRow(rs);
        // Bổ sung thêm các trường từ lệnh JOIN
        ld.setMaChuyen(rs.getString("ma_chuyen"));
        ld.setTenGa(rs.getString("ten_ga"));
        return ld;
    }
}