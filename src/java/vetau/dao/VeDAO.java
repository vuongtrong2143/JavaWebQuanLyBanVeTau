package vetau.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import vetau.model.Ve;
import vetau.util.DBConnection;

public class VeDAO {

    private static final String SELECT_BASE = "SELECT id, dat_cho_id, chuyen_tau_id, ghe_id, hanh_khach_id, doi_tuong_uu_dai_id, ga_di_id, ga_den_id, ma_ve, gia_co_so, giam_doi_tuong, phu_thu_cao_diem, gia_ve_chi_tiet, trang_thai FROM dbo.VE";

    public List<Ve> findAll() throws SQLException {
        List<Ve> list = new ArrayList<>();
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

    public Ve findById(int id) throws SQLException {
        String sql = SELECT_BASE + " WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public int insert(Ve item) throws SQLException {
        String sql = "INSERT INTO dbo.VE (dat_cho_id, chuyen_tau_id, ghe_id, hanh_khach_id, doi_tuong_uu_dai_id, ga_di_id, ga_den_id, ma_ve, gia_co_so, giam_doi_tuong, phu_thu_cao_diem, gia_ve_chi_tiet, trang_thai) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;
            ps.setInt(index++, item.getDatChoId());
            ps.setInt(index++, item.getChuyenTauId());
            ps.setInt(index++, item.getGheId());
            ps.setInt(index++, item.getHanhKhachId());
            DaoUtil.setInteger(ps, index++, item.getDoiTuongUuDaiId());
            ps.setInt(index++, item.getGaDiId());
            ps.setInt(index++, item.getGaDenId());
            DaoUtil.setString(ps, index++, item.getMaVe());
            DaoUtil.setBigDecimal(ps, index++, item.getGiaCoSo());
            DaoUtil.setBigDecimal(ps, index++, item.getGiamDoiTuong());
            DaoUtil.setBigDecimal(ps, index++, item.getPhuThuCaoDiem());
            DaoUtil.setBigDecimal(ps, index++, item.getGiaVeChiTiet());
            DaoUtil.setString(ps, index++, item.getTrangThai());
            ps.executeUpdate();
            return DaoUtil.getGeneratedId(ps);
        }
    }

    public Ve mapRow(ResultSet rs) throws SQLException {
        Ve item = new Ve();
        item.setId(rs.getInt("id"));
        item.setDatChoId(rs.getInt("dat_cho_id"));
        item.setChuyenTauId(rs.getInt("chuyen_tau_id"));
        item.setGheId(rs.getInt("ghe_id"));
        item.setHanhKhachId(rs.getInt("hanh_khach_id"));
        item.setDoiTuongUuDaiId(DaoUtil.getInteger(rs, "doi_tuong_uu_dai_id"));
        item.setGaDiId(rs.getInt("ga_di_id"));
        item.setGaDenId(rs.getInt("ga_den_id"));
        item.setMaVe(rs.getString("ma_ve"));
        item.setGiaCoSo(rs.getBigDecimal("gia_co_so"));
        item.setGiamDoiTuong(rs.getBigDecimal("giam_doi_tuong"));
        item.setPhuThuCaoDiem(rs.getBigDecimal("phu_thu_cao_diem"));
        item.setGiaVeChiTiet(rs.getBigDecimal("gia_ve_chi_tiet"));
        item.setTrangThai(rs.getString("trang_thai"));
        return item;
    }

    public Ve findByMaVe(String maVe) throws SQLException {
        String sql = SELECT_BASE + " WHERE ma_ve = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maVe);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public List<Ve> findByDatChoId(int datChoId) throws SQLException {
        List<Ve> list = new ArrayList<>();
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
    public void updateTrangThai(int veId, String trangThai) throws SQLException {
        String sql = "UPDATE dbo.VE SET trang_thai = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, trangThai);
            ps.setInt(2, veId);
            ps.executeUpdate();
        }
    }
}
