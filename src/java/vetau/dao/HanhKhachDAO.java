package vetau.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import vetau.model.HanhKhach;
import vetau.util.DBConnection;

public class HanhKhachDAO {

    private static final String SELECT_BASE = "SELECT id, ho_ten, loai_giay_to, so_giay_to, ngay_sinh, quoc_tich FROM dbo.HANH_KHACH";

    public List<HanhKhach> findAll() throws SQLException {
        List<HanhKhach> list = new ArrayList<>();
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

    public HanhKhach findById(int id) throws SQLException {
        String sql = SELECT_BASE + " WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public int insert(HanhKhach item) throws SQLException {
        String sql = "INSERT INTO dbo.HANH_KHACH (ho_ten, loai_giay_to, so_giay_to, ngay_sinh, quoc_tich) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;
            DaoUtil.setString(ps, index++, item.getHoTen());
            DaoUtil.setString(ps, index++, item.getLoaiGiayTo());
            DaoUtil.setString(ps, index++, item.getSoGiayTo());
            DaoUtil.setLocalDate(ps, index++, item.getNgaySinh());
            DaoUtil.setString(ps, index++, item.getQuocTich());
            ps.executeUpdate();
            return DaoUtil.getGeneratedId(ps);
        }
    }

    public HanhKhach mapRow(ResultSet rs) throws SQLException {
        HanhKhach item = new HanhKhach();
        item.setId(rs.getInt("id"));
        item.setHoTen(rs.getString("ho_ten"));
        item.setLoaiGiayTo(rs.getString("loai_giay_to"));
        item.setSoGiayTo(rs.getString("so_giay_to"));
        item.setNgaySinh(DaoUtil.getLocalDate(rs, "ngay_sinh"));
        item.setQuocTich(rs.getString("quoc_tich"));
        return item;
    }

    public HanhKhach findByGiayTo(String loaiGiayTo, String soGiayTo) throws SQLException {
        String sql = SELECT_BASE + " WHERE loai_giay_to = ? AND so_giay_to = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, loaiGiayTo);
            ps.setString(2, soGiayTo);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public int findOrCreate(HanhKhach item) throws SQLException {
        HanhKhach existed = findByGiayTo(item.getLoaiGiayTo(), item.getSoGiayTo());
        return existed != null ? existed.getId() : insert(item);
    }

}
