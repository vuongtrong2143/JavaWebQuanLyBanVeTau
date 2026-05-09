package vetau.dao;

import vetau.model.Ga;
import vetau.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import vetau.util.TrangThai;

public class GaDAO {

    // 1. Lấy TẤT CẢ danh sách ga (Dùng cho trang Quản trị Admin)
    public List<Ga> layDanhSachGa() throws SQLException {
        List<Ga> list = new ArrayList<>();
        String sql = "SELECT * FROM dbo.GA ORDER BY ly_trinh_km ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Ga ga = new Ga();
                ga.setId(rs.getInt("id"));
                ga.setMaGa(rs.getString("ma_ga"));
                ga.setTenGa(rs.getString("ten_ga"));
                ga.setTinhThanh(rs.getString("tinh_thanh"));
                ga.setLyTrinhKm(rs.getInt("ly_trinh_km"));
                ga.setTrangThai(rs.getString("trang_thai")); // Bổ sung map trạng thái
                list.add(ga);
            }
        }
        return list;
    }

    // 2. Lấy danh sách ga CHỈ ĐANG HOẠT ĐỘNG (Dùng cho trang Khách hàng tìm chuyến)
    public List<Ga> layDanhSachGaHoatDong() throws SQLException {
        List<Ga> list = new ArrayList<>();
        String sql = "SELECT * FROM dbo.GA WHERE trang_thai = N'Hoạt động' ORDER BY ly_trinh_km ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Ga ga = new Ga();
                ga.setId(rs.getInt("id"));
                ga.setMaGa(rs.getString("ma_ga"));
                ga.setTenGa(rs.getString("ten_ga"));
                ga.setTinhThanh(rs.getString("tinh_thanh"));
                ga.setLyTrinhKm(rs.getInt("ly_trinh_km"));
                ga.setTrangThai(rs.getString("trang_thai"));
                list.add(ga);
            }
        }
        return list;
    }

    // 3. Tìm Ga theo ID
    public Ga findById(int id) throws SQLException {
        String sql = "SELECT * FROM dbo.GA WHERE id = ?";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Ga ga = new Ga();
                    ga.setId(rs.getInt("id"));
                    ga.setMaGa(rs.getString("ma_ga"));
                    ga.setTenGa(rs.getString("ten_ga"));
                    ga.setTinhThanh(rs.getString("tinh_thanh"));
                    ga.setLyTrinhKm(rs.getInt("ly_trinh_km"));
                    ga.setTrangThai(rs.getString("trang_thai")); // Bổ sung map trạng thái
                    return ga;
                }
            }
        }
        return null;
    }

    // 4. Thêm mới Ga
    public void insert(Ga ga) throws SQLException {
        String sql = "INSERT INTO dbo.GA (ma_ga, ten_ga, tinh_thanh, ly_trinh_km, trang_thai) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ga.getMaGa());
            ps.setString(2, ga.getTenGa());
            ps.setString(3, ga.getTinhThanh());
            ps.setInt(4, ga.getLyTrinhKm());
            ps.setString(5, ga.getTrangThai()); // Insert trạng thái
            ps.executeUpdate();
        }
    }

    // 5. Cập nhật Ga
    public void update(Ga ga) throws SQLException {
        String sql = "UPDATE dbo.GA SET ma_ga=?, ten_ga=?, tinh_thanh=?, ly_trinh_km=?, trang_thai=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ga.getMaGa());
            ps.setString(2, ga.getTenGa());
            ps.setString(3, ga.getTinhThanh());
            ps.setInt(4, ga.getLyTrinhKm());
            ps.setString(5, ga.getTrangThai()); // Update trạng thái
            ps.setInt(6, ga.getId());
            ps.executeUpdate();
        }
    }

    // 6. Xóa Ga
    public void delete(int id) throws SQLException {
        String sql = "UPDATE dbo.GA SET trang_thai = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, TrangThai.TAM_DUNG);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }
}