package vetau.dao;

import vetau.model.KhachHang;
import vetau.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class KhachHangDAO {

    private static final String SELECT_BASE =
            "SELECT id, ho_ten, email, so_dien_thoai, mat_khau_hash, ngay_sinh, gioi_tinh, dia_chi, ngay_tao, trang_thai "
            + "FROM dbo.KHACH_HANG ";

    public List<KhachHang> findAll() throws SQLException {
        List<KhachHang> list = new ArrayList<>();
        String sql = SELECT_BASE + "ORDER BY id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }

        return list;
    }

    public KhachHang findById(int id) throws SQLException {
        String sql = SELECT_BASE + "WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public KhachHang findByEmail(String email) throws SQLException {
        String sql = SELECT_BASE + "WHERE email = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public KhachHang findBySoDienThoai(String soDienThoai) throws SQLException {
        String sql = SELECT_BASE + "WHERE so_dien_thoai = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, soDienThoai);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public int insert(KhachHang item) throws SQLException {
        String sql = ""
                + "INSERT INTO dbo.KHACH_HANG "
                + "(ho_ten, email, so_dien_thoai, mat_khau_hash, ngay_sinh, gioi_tinh, dia_chi, trang_thai) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            int index = 1;
            ps.setString(index++, item.getHoTen());
            ps.setString(index++, item.getEmail());
            setNullableString(ps, index++, item.getSoDienThoai());
            ps.setString(index++, item.getMatKhauHash());

            if (item.getNgaySinh() == null) {
                ps.setNull(index++, java.sql.Types.DATE);
            } else {
                ps.setDate(index++, Date.valueOf(item.getNgaySinh()));
            }

            setNullableString(ps, index++, item.getGioiTinh());
            setNullableString(ps, index++, item.getDiaChi());
            ps.setString(index++, item.getTrangThai() == null ? "Hoạt động" : item.getTrangThai());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        }
    }

    public void updateProfile(KhachHang item) throws SQLException {
        String sql = ""
                + "UPDATE dbo.KHACH_HANG "
                + "SET ho_ten = ?, so_dien_thoai = ?, ngay_sinh = ?, gioi_tinh = ?, dia_chi = ? "
                + "WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            int index = 1;
            ps.setString(index++, item.getHoTen());
            setNullableString(ps, index++, item.getSoDienThoai());

            if (item.getNgaySinh() == null) {
                ps.setNull(index++, java.sql.Types.DATE);
            } else {
                ps.setDate(index++, Date.valueOf(item.getNgaySinh()));
            }

            setNullableString(ps, index++, item.getGioiTinh());
            setNullableString(ps, index++, item.getDiaChi());
            ps.setInt(index++, item.getId());

            ps.executeUpdate();
        }
    }

    public void updatePasswordHash(int id, String newPasswordHash) throws SQLException {
        String sql = "UPDATE dbo.KHACH_HANG SET mat_khau_hash = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newPasswordHash);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    /*
     * Giữ lại method này để tương thích với code Giai đoạn 3 nếu bạn đã dùng.
     */
    public KhachHang findByEmailAndPasswordHash(String email, String passwordHash) throws SQLException {
        String sql = SELECT_BASE + "WHERE email = ? AND mat_khau_hash = ? AND trang_thai = N'Hoạt động'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, passwordHash);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    private KhachHang mapRow(ResultSet rs) throws SQLException {
        KhachHang item = new KhachHang();

        item.setId(rs.getInt("id"));
        item.setHoTen(rs.getString("ho_ten"));
        item.setEmail(rs.getString("email"));
        item.setSoDienThoai(rs.getString("so_dien_thoai"));
        item.setMatKhauHash(rs.getString("mat_khau_hash"));

        Date ngaySinh = rs.getDate("ngay_sinh");
        if (ngaySinh != null) {
            item.setNgaySinh(ngaySinh.toLocalDate());
        }

        item.setGioiTinh(rs.getString("gioi_tinh"));
        item.setDiaChi(rs.getString("dia_chi"));

        Timestamp ngayTao = rs.getTimestamp("ngay_tao");
        if (ngayTao != null) {
            item.setNgayTao(ngayTao.toLocalDateTime());
        }

        item.setTrangThai(rs.getString("trang_thai"));

        return item;
    }

    private void setNullableString(PreparedStatement ps, int index, String value) throws SQLException {
        if (value == null || value.trim().isEmpty()) {
            ps.setNull(index, java.sql.Types.NVARCHAR);
        } else {
            ps.setString(index, value.trim());
        }
    }
}
