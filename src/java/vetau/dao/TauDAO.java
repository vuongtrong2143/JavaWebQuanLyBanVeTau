package vetau.dao;

import vetau.model.Tau;
import vetau.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import vetau.util.TrangThai;

public class TauDAO {

    public List<Tau> layDanhSachTau() throws SQLException {
        List<Tau> list = new ArrayList<>();
        String sql = "SELECT * FROM dbo.TAU ORDER BY ma_tau ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public List<Tau> layDanhSachTauHoatDong() throws SQLException {
        List<Tau> list = new ArrayList<>();
        String sql = "SELECT * FROM dbo.TAU WHERE trang_thai = N'Hoạt động' ORDER BY ma_tau ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Tau findById(int id) throws SQLException {
        String sql = "SELECT * FROM dbo.TAU WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public void insert(Tau tau) throws SQLException {
        String sql = "INSERT INTO dbo.TAU (ma_tau, ten_tau, chieu_di, thuoc_tuyen_thong_nhat, mo_ta, trang_thai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setParameters(ps, tau);
            ps.executeUpdate();
        }
    }

    public void update(Tau tau) throws SQLException {
        String sql = "UPDATE dbo.TAU SET ma_tau=?, ten_tau=?, chieu_di=?, thuoc_tuyen_thong_nhat=?, mo_ta=?, trang_thai=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            setParameters(ps, tau);
            ps.setInt(7, tau.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "UPDATE dbo.TAU SET trang_thai = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, TrangThai.TAM_DUNG);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    // Hàm hỗ trợ map dữ liệu
    private Tau mapRow(ResultSet rs) throws SQLException {
        Tau tau = new Tau();
        tau.setId(rs.getInt("id"));
        tau.setMaTau(rs.getString("ma_tau"));
        tau.setTenTau(rs.getString("ten_tau"));
        tau.setChieuDi(rs.getString("chieu_di"));
        tau.setThuocTuyenThongNhat(rs.getBoolean("thuoc_tuyen_thong_nhat"));
        tau.setMoTa(rs.getString("mo_ta"));
        tau.setTrangThai(rs.getString("trang_thai"));
        return tau;
    }

    // Hàm hỗ trợ set tham số (tránh lặp code)
    private void setParameters(PreparedStatement ps, Tau tau) throws SQLException {
        ps.setString(1, tau.getMaTau());
        ps.setString(2, tau.getTenTau());
        ps.setString(3, tau.getChieuDi());
        ps.setBoolean(4, tau.isThuocTuyenThongNhat());
        ps.setString(5, tau.getMoTa());
        ps.setString(6, tau.getTrangThai());
    }
}