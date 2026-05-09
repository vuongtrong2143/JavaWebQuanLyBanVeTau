package vetau.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import vetau.model.GiuCho;
import vetau.util.DBConnection;

public class GiuChoDAO {

    private static final String SELECT_BASE = "SELECT id, dat_cho_id, chuyen_tau_id, ghe_id, ga_di_id, ga_den_id, thoi_gian_giu, thoi_gian_het_han, trang_thai FROM dbo.GIU_CHO";

    public List<GiuCho> findAll() throws SQLException {
        List<GiuCho> list = new ArrayList<>();
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

    public GiuCho findById(int id) throws SQLException {
        String sql = SELECT_BASE + " WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public int insert(GiuCho item) throws SQLException {
        String sql = "INSERT INTO dbo.GIU_CHO (dat_cho_id, chuyen_tau_id, ghe_id, ga_di_id, ga_den_id, thoi_gian_giu, thoi_gian_het_han, trang_thai) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;
            ps.setInt(index++, item.getDatChoId());
            ps.setInt(index++, item.getChuyenTauId());
            ps.setInt(index++, item.getGheId());
            ps.setInt(index++, item.getGaDiId());
            ps.setInt(index++, item.getGaDenId());
            DaoUtil.setLocalDateTime(ps, index++, item.getThoiGianGiu());
            DaoUtil.setLocalDateTime(ps, index++, item.getThoiGianHetHan());
            DaoUtil.setString(ps, index++, item.getTrangThai());
            ps.executeUpdate();
            return DaoUtil.getGeneratedId(ps);
        }
    }

    public GiuCho mapRow(ResultSet rs) throws SQLException {
        GiuCho item = new GiuCho();
        item.setId(rs.getInt("id"));
        item.setDatChoId(rs.getInt("dat_cho_id"));
        item.setChuyenTauId(rs.getInt("chuyen_tau_id"));
        item.setGheId(rs.getInt("ghe_id"));
        item.setGaDiId(rs.getInt("ga_di_id"));
        item.setGaDenId(rs.getInt("ga_den_id"));
        item.setThoiGianGiu(DaoUtil.getLocalDateTime(rs, "thoi_gian_giu"));
        item.setThoiGianHetHan(DaoUtil.getLocalDateTime(rs, "thoi_gian_het_han"));
        item.setTrangThai(rs.getString("trang_thai"));
        return item;
    }

    public List<GiuCho> findByDatChoId(int datChoId) throws SQLException {
        List<GiuCho> list = new ArrayList<>();
        String sql = SELECT_BASE + " WHERE dat_cho_id = ? ORDER BY id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, datChoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public void updateTrangThaiByDatChoId(int datChoId, String trangThai) throws SQLException {
        String sql = "UPDATE dbo.GIU_CHO SET trang_thai = ? WHERE dat_cho_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setInt(2, datChoId);
            ps.executeUpdate();
        }
    }

}
