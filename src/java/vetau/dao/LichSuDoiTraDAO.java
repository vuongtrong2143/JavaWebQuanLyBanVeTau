package vetau.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import vetau.model.LichSuDoiTra;
import vetau.util.DBConnection;

public class LichSuDoiTraDAO {

    private static final String SELECT_BASE = "SELECT id, ve_id, nhan_vien_id, chinh_sach_id, loai_giao_dich, ly_do, phi_doi, ty_le_khau_tru, so_tien_hoan, thoi_gian_xu_ly, ghi_chu FROM dbo.LICH_SU_DOI_TRA";

    public List<LichSuDoiTra> findAll() throws SQLException {
        List<LichSuDoiTra> list = new ArrayList<>();
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

    public LichSuDoiTra findById(int id) throws SQLException {
        String sql = SELECT_BASE + " WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public int insert(LichSuDoiTra item) throws SQLException {
        String sql = "INSERT INTO dbo.LICH_SU_DOI_TRA (ve_id, nhan_vien_id, chinh_sach_id, loai_giao_dich, ly_do, phi_doi, ty_le_khau_tru, so_tien_hoan, thoi_gian_xu_ly, ghi_chu) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;
            ps.setInt(index++, item.getVeId());
            DaoUtil.setInteger(ps, index++, item.getNhanVienId());
            DaoUtil.setInteger(ps, index++, item.getChinhSachId());
            DaoUtil.setString(ps, index++, item.getLoaiGiaoDich());
            DaoUtil.setString(ps, index++, item.getLyDo());
            DaoUtil.setBigDecimal(ps, index++, item.getPhiDoi());
            DaoUtil.setBigDecimal(ps, index++, item.getTyLeKhauTru());
            DaoUtil.setBigDecimal(ps, index++, item.getSoTienHoan());
            DaoUtil.setLocalDateTime(ps, index++, item.getThoiGianXuLy());
            DaoUtil.setString(ps, index++, item.getGhiChu());
            ps.executeUpdate();
            return DaoUtil.getGeneratedId(ps);
        }
    }

    public LichSuDoiTra mapRow(ResultSet rs) throws SQLException {
        LichSuDoiTra item = new LichSuDoiTra();
        item.setId(rs.getInt("id"));
        item.setVeId(rs.getInt("ve_id"));
        item.setNhanVienId(DaoUtil.getInteger(rs, "nhan_vien_id"));
        item.setChinhSachId(DaoUtil.getInteger(rs, "chinh_sach_id"));
        item.setLoaiGiaoDich(rs.getString("loai_giao_dich"));
        item.setLyDo(rs.getString("ly_do"));
        item.setPhiDoi(rs.getBigDecimal("phi_doi"));
        item.setTyLeKhauTru(rs.getBigDecimal("ty_le_khau_tru"));
        item.setSoTienHoan(rs.getBigDecimal("so_tien_hoan"));
        item.setThoiGianXuLy(DaoUtil.getLocalDateTime(rs, "thoi_gian_xu_ly"));
        item.setGhiChu(rs.getString("ghi_chu"));
        return item;
    }

    public List<LichSuDoiTra> findByVeId(int veId) throws SQLException {
        List<LichSuDoiTra> list = new ArrayList<>();
        String sql = SELECT_BASE + " WHERE ve_id = ? ORDER BY thoi_gian_xu_ly DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, veId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }
    public void updateGhiChu(int id, String ghiChu) throws SQLException {
        String sql = "UPDATE dbo.LICH_SU_DOI_TRA SET ghi_chu = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            DaoUtil.setString(ps, 1, ghiChu);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }
}
