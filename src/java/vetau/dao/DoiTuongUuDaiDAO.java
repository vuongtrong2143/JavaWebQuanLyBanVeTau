package vetau.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import vetau.model.DoiTuongUuDai;
import vetau.util.DBConnection;

public class DoiTuongUuDaiDAO {

    private static final String SELECT_BASE = "SELECT id, ma_doi_tuong, ten_doi_tuong, phan_tram_giam, tuoi_min, tuoi_max, can_giay_to_chung_minh, hieu_luc_tu, hieu_luc_den, trang_thai FROM dbo.DOI_TUONG_UU_DAI";

    public List<DoiTuongUuDai> findAll() throws SQLException {
        List<DoiTuongUuDai> list = new ArrayList<>();
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

    public DoiTuongUuDai findById(int id) throws SQLException {
        String sql = SELECT_BASE + " WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public int insert(DoiTuongUuDai item) throws SQLException {
        String sql = "INSERT INTO dbo.DOI_TUONG_UU_DAI (ma_doi_tuong, ten_doi_tuong, phan_tram_giam, tuoi_min, tuoi_max, can_giay_to_chung_minh, hieu_luc_tu, hieu_luc_den, trang_thai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;
            DaoUtil.setString(ps, index++, item.getMaDoiTuong());
            DaoUtil.setString(ps, index++, item.getTenDoiTuong());
            DaoUtil.setBigDecimal(ps, index++, item.getPhanTramGiam());
            DaoUtil.setInteger(ps, index++, item.getTuoiMin());
            DaoUtil.setInteger(ps, index++, item.getTuoiMax());
            ps.setBoolean(index++, item.isCanGiayToChungMinh());
            DaoUtil.setLocalDateTime(ps, index++, item.getHieuLucTu());
            DaoUtil.setLocalDateTime(ps, index++, item.getHieuLucDen());
            DaoUtil.setString(ps, index++, item.getTrangThai());
            ps.executeUpdate();
            return DaoUtil.getGeneratedId(ps);
        }
    }

    public DoiTuongUuDai mapRow(ResultSet rs) throws SQLException {
        DoiTuongUuDai item = new DoiTuongUuDai();
        item.setId(rs.getInt("id"));
        item.setMaDoiTuong(rs.getString("ma_doi_tuong"));
        item.setTenDoiTuong(rs.getString("ten_doi_tuong"));
        item.setPhanTramGiam(rs.getBigDecimal("phan_tram_giam"));
        item.setTuoiMin(DaoUtil.getInteger(rs, "tuoi_min"));
        item.setTuoiMax(DaoUtil.getInteger(rs, "tuoi_max"));
        item.setCanGiayToChungMinh(rs.getBoolean("can_giay_to_chung_minh"));
        item.setHieuLucTu(DaoUtil.getLocalDateTime(rs, "hieu_luc_tu"));
        item.setHieuLucDen(DaoUtil.getLocalDateTime(rs, "hieu_luc_den"));
        item.setTrangThai(rs.getString("trang_thai"));
        return item;
    }

    public List<DoiTuongUuDai> findAllActive() throws SQLException {
        List<DoiTuongUuDai> list = new ArrayList<>();
        String sql = SELECT_BASE
                + " WHERE trang_thai = N'Hoạt động'"
                + " AND (hieu_luc_tu IS NULL OR hieu_luc_tu <= SYSDATETIME())"
                + " AND (hieu_luc_den IS NULL OR hieu_luc_den >= SYSDATETIME())"
                + " ORDER BY id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

}
