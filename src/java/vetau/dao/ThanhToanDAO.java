package vetau.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import vetau.model.ThanhToan;
import vetau.util.DBConnection;

public class ThanhToanDAO {

    private static final String SELECT_BASE = "SELECT id, dat_cho_id, ma_giao_dich, request_id, phuong_thuc, so_tien, ngay_tao, ngay_thanh_toan, trang_thai FROM dbo.THANH_TOAN";

    public List<ThanhToan> findAll() throws SQLException {
        List<ThanhToan> list = new ArrayList<>();
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

    public ThanhToan findById(int id) throws SQLException {
        String sql = SELECT_BASE + " WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public int insert(ThanhToan item) throws SQLException {
        String sql = "INSERT INTO dbo.THANH_TOAN (dat_cho_id, ma_giao_dich, request_id, phuong_thuc, so_tien, ngay_tao, ngay_thanh_toan, trang_thai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;
            ps.setInt(index++, item.getDatChoId());
            DaoUtil.setString(ps, index++, item.getMaGiaoDich());
            DaoUtil.setString(ps, index++, item.getRequestId());
            DaoUtil.setString(ps, index++, item.getPhuongThuc());
            DaoUtil.setBigDecimal(ps, index++, item.getSoTien());
            DaoUtil.setLocalDateTime(ps, index++, item.getNgayTao());
            DaoUtil.setLocalDateTime(ps, index++, item.getNgayThanhToan());
            DaoUtil.setString(ps, index++, item.getTrangThai());
            ps.executeUpdate();
            return DaoUtil.getGeneratedId(ps);
        }
    }

    public ThanhToan mapRow(ResultSet rs) throws SQLException {
        ThanhToan item = new ThanhToan();
        item.setId(rs.getInt("id"));
        item.setDatChoId(rs.getInt("dat_cho_id"));
        item.setMaGiaoDich(rs.getString("ma_giao_dich"));
        item.setRequestId(rs.getString("request_id"));
        item.setPhuongThuc(rs.getString("phuong_thuc"));
        item.setSoTien(rs.getBigDecimal("so_tien"));
        item.setNgayTao(DaoUtil.getLocalDateTime(rs, "ngay_tao"));
        item.setNgayThanhToan(DaoUtil.getLocalDateTime(rs, "ngay_thanh_toan"));
        item.setTrangThai(rs.getString("trang_thai"));
        return item;
    }

    public ThanhToan findByRequestId(String requestId) throws SQLException {
        String sql = SELECT_BASE + " WHERE request_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public void markSuccess(int id, String maGiaoDich) throws SQLException {
        String sql = "UPDATE dbo.THANH_TOAN SET trang_thai = N'Thành công', ma_giao_dich = ?, ngay_thanh_toan = SYSDATETIME() WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maGiaoDich);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    public void markFailed(int id) throws SQLException {
        String sql = "UPDATE dbo.THANH_TOAN SET trang_thai = N'Thất bại' WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

}
