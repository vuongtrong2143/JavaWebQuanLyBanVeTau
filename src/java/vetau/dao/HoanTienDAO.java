package vetau.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import vetau.model.HoanTien;
import vetau.util.DBConnection;
import vetau.dto.RefundAdminDTO;
import vetau.util.TrangThai;

public class HoanTienDAO {

    private static final String SELECT_BASE = "SELECT id, thanh_toan_id, ve_id, so_tien_hoan, ma_giao_dich_hoan, thoi_gian_yeu_cau, thoi_gian_hoan_tat, trang_thai FROM dbo.HOAN_TIEN";

    public List<HoanTien> findAll() throws SQLException {
        List<HoanTien> list = new ArrayList<>();
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

    public HoanTien findById(int id) throws SQLException {
        String sql = SELECT_BASE + " WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public int insert(HoanTien item) throws SQLException {
        String sql = "INSERT INTO dbo.HOAN_TIEN (thanh_toan_id, ve_id, so_tien_hoan, ma_giao_dich_hoan, thoi_gian_yeu_cau, thoi_gian_hoan_tat, trang_thai) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;
            ps.setInt(index++, item.getThanhToanId());
            ps.setInt(index++, item.getVeId());
            DaoUtil.setBigDecimal(ps, index++, item.getSoTienHoan());
            DaoUtil.setString(ps, index++, item.getMaGiaoDichHoan());
            DaoUtil.setLocalDateTime(ps, index++, item.getThoiGianYeuCau());
            DaoUtil.setLocalDateTime(ps, index++, item.getThoiGianHoanTat());
            DaoUtil.setString(ps, index++, item.getTrangThai());
            ps.executeUpdate();
            return DaoUtil.getGeneratedId(ps);
        }
    }

    public HoanTien mapRow(ResultSet rs) throws SQLException {
        HoanTien item = new HoanTien();
        item.setId(rs.getInt("id"));
        item.setThanhToanId(rs.getInt("thanh_toan_id"));
        item.setVeId(rs.getInt("ve_id"));
        item.setSoTienHoan(rs.getBigDecimal("so_tien_hoan"));
        item.setMaGiaoDichHoan(rs.getString("ma_giao_dich_hoan"));
        item.setThoiGianYeuCau(DaoUtil.getLocalDateTime(rs, "thoi_gian_yeu_cau"));
        item.setThoiGianHoanTat(DaoUtil.getLocalDateTime(rs, "thoi_gian_hoan_tat"));
        item.setTrangThai(rs.getString("trang_thai"));
        return item;
    }

    public List<HoanTien> findByVeId(int veId) throws SQLException {
        List<HoanTien> list = new ArrayList<>();
        String sql = SELECT_BASE + " WHERE ve_id = ? ORDER BY id DESC";
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

    public HoanTien findLatestPendingByVeId(int veId) throws SQLException {
        String sql = SELECT_BASE
                + " WHERE ve_id = ?"
                + " AND trang_thai IN (N'Pending', N'Chờ xử lý')"
                + " ORDER BY id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, veId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public List<RefundAdminDTO> findAdminRefunds(String trangThai) throws SQLException {
        StringBuilder sql = new StringBuilder();

        sql.append(
                "SELECT "
              + " ht.id AS hoan_tien_id, "
              + " ht.ve_id, "
              + " ht.thanh_toan_id, "
              + " ht.so_tien_hoan, "
              + " ht.ma_giao_dich_hoan, "
              + " ht.thoi_gian_yeu_cau, "
              + " ht.thoi_gian_hoan_tat, "
              + " ht.trang_thai AS trang_thai_hoan_tien, "
              + " v.ma_ve, "
              + " dc.ma_dat_cho, "
              + " kh.ho_ten AS ten_khach_hang, "
              + " kh.email AS email_khach_hang, "
              + " kh.so_dien_thoai AS sdt_khach_hang, "
              + " hk.ho_ten AS ten_hanh_khach, "
              + " hk.so_giay_to, "
              + " tt.phuong_thuc, "
              + " tt.ma_giao_dich AS ma_giao_dich_goc, "
              + " ls.ly_do, "
              + " ls.ty_le_khau_tru, "
              + " ls.phi_doi "
              + "FROM dbo.HOAN_TIEN ht "
              + "JOIN dbo.VE v ON v.id = ht.ve_id "
              + "JOIN dbo.DAT_CHO dc ON dc.id = v.dat_cho_id "
              + "LEFT JOIN dbo.KHACH_HANG kh ON kh.id = dc.khach_hang_id "
              + "LEFT JOIN dbo.HANH_KHACH hk ON hk.id = v.hanh_khach_id "
              + "LEFT JOIN dbo.THANH_TOAN tt ON tt.id = ht.thanh_toan_id "
              + "OUTER APPLY ( "
              + "    SELECT TOP 1 * "
              + "    FROM dbo.LICH_SU_DOI_TRA ls "
              + "    WHERE ls.ve_id = v.id "
              + "    ORDER BY ls.id DESC "
              + ") ls "
              + "WHERE 1 = 1 "
        );

        if (trangThai != null && !trangThai.trim().isEmpty()) {
            sql.append(" AND ht.trang_thai = ? ");
        }

        sql.append(" ORDER BY ht.thoi_gian_yeu_cau DESC, ht.id DESC ");

        List<RefundAdminDTO> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            if (trangThai != null && !trangThai.trim().isEmpty()) {
                ps.setString(1, trangThai.trim());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRefundAdminDTO(rs));
                }
            }
        }

        return list;
    }

    public RefundAdminDTO findAdminRefundById(int hoanTienId) throws SQLException {
        String sql = ""
                + "SELECT "
                + " ht.id AS hoan_tien_id, "
                + " ht.ve_id, "
                + " ht.thanh_toan_id, "
                + " ht.so_tien_hoan, "
                + " ht.ma_giao_dich_hoan, "
                + " ht.thoi_gian_yeu_cau, "
                + " ht.thoi_gian_hoan_tat, "
                + " ht.trang_thai AS trang_thai_hoan_tien, "
                + " v.ma_ve, "
                + " dc.ma_dat_cho, "
                + " kh.ho_ten AS ten_khach_hang, "
                + " kh.email AS email_khach_hang, "
                + " kh.so_dien_thoai AS sdt_khach_hang, "
                + " hk.ho_ten AS ten_hanh_khach, "
                + " hk.so_giay_to, "
                + " tt.phuong_thuc, "
                + " tt.ma_giao_dich AS ma_giao_dich_goc, "
                + " ls.ly_do, "
                + " ls.ty_le_khau_tru, "
                + " ls.phi_doi "
                + "FROM dbo.HOAN_TIEN ht "
                + "JOIN dbo.VE v ON v.id = ht.ve_id "
                + "JOIN dbo.DAT_CHO dc ON dc.id = v.dat_cho_id "
                + "LEFT JOIN dbo.KHACH_HANG kh ON kh.id = dc.khach_hang_id "
                + "LEFT JOIN dbo.HANH_KHACH hk ON hk.id = v.hanh_khach_id "
                + "LEFT JOIN dbo.THANH_TOAN tt ON tt.id = ht.thanh_toan_id "
                + "OUTER APPLY ( "
                + "    SELECT TOP 1 * "
                + "    FROM dbo.LICH_SU_DOI_TRA ls "
                + "    WHERE ls.ve_id = v.id "
                + "    ORDER BY ls.id DESC "
                + ") ls "
                + "WHERE ht.id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, hoanTienId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRefundAdminDTO(rs) : null;
            }
        }
    }

    public HoanTien findPendingByVeId(int veId) throws SQLException {
        String sql = SELECT_BASE
                + " WHERE ve_id = ? AND trang_thai = N'Chờ xử lý' "
                + " ORDER BY id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, veId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public void markRejected(int hoanTienId) throws SQLException {
        String sql = ""
                + "UPDATE dbo.HOAN_TIEN "
                + "SET trang_thai = ?, "
                + "    thoi_gian_hoan_tat = SYSDATETIME() "
                + "WHERE id = ? "
                + "  AND trang_thai = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, TrangThai.HOAN_TIEN_TU_CHOI);
            ps.setInt(2, hoanTienId);
            ps.setString(3, TrangThai.HOAN_TIEN_CHO_XU_LY);
            ps.executeUpdate();
        }
    }

    public void markCompletedIfPending(int hoanTienId, String maGiaoDichHoan) throws SQLException {
        String sql = ""
                + "UPDATE dbo.HOAN_TIEN "
                + "SET trang_thai = ?, "
                + "    ma_giao_dich_hoan = ?, "
                + "    thoi_gian_hoan_tat = SYSDATETIME() "
                + "WHERE id = ? "
                + "  AND trang_thai = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, TrangThai.HOAN_TIEN_HOAN_TAT);
            ps.setString(2, maGiaoDichHoan);
            ps.setInt(3, hoanTienId);
            ps.setString(4, TrangThai.HOAN_TIEN_CHO_XU_LY);
            ps.executeUpdate();
        }
    }

    private RefundAdminDTO mapRefundAdminDTO(ResultSet rs) throws SQLException {
        RefundAdminDTO dto = new RefundAdminDTO();

        dto.setHoanTienId(rs.getInt("hoan_tien_id"));
        dto.setVeId(rs.getInt("ve_id"));

        int thanhToanId = rs.getInt("thanh_toan_id");
        dto.setThanhToanId(rs.wasNull() ? null : thanhToanId);

        dto.setSoTienHoan(rs.getBigDecimal("so_tien_hoan"));
        dto.setMaGiaoDichHoan(rs.getString("ma_giao_dich_hoan"));

        java.sql.Timestamp tgYeuCau = rs.getTimestamp("thoi_gian_yeu_cau");
        if (tgYeuCau != null) {
            dto.setThoiGianYeuCau(tgYeuCau.toLocalDateTime());
        }

        java.sql.Timestamp tgHoanTat = rs.getTimestamp("thoi_gian_hoan_tat");
        if (tgHoanTat != null) {
            dto.setThoiGianHoanTat(tgHoanTat.toLocalDateTime());
        }

        dto.setTrangThai(rs.getString("trang_thai_hoan_tien"));
        dto.setMaVe(rs.getString("ma_ve"));
        dto.setMaDatCho(rs.getString("ma_dat_cho"));
        dto.setTenKhachHang(rs.getString("ten_khach_hang"));
        dto.setEmailKhachHang(rs.getString("email_khach_hang"));
        dto.setSoDienThoaiKhachHang(rs.getString("sdt_khach_hang"));
        dto.setTenHanhKhach(rs.getString("ten_hanh_khach"));
        dto.setSoGiayTo(rs.getString("so_giay_to"));
        dto.setPhuongThucThanhToan(rs.getString("phuong_thuc"));
        dto.setMaGiaoDichGoc(rs.getString("ma_giao_dich_goc"));
        dto.setLyDoTraVe(rs.getString("ly_do"));
        dto.setTyLeKhauTru(rs.getBigDecimal("ty_le_khau_tru"));
        dto.setPhiDoiTra(rs.getBigDecimal("phi_doi"));

        return dto;
    }
    public HoanTien findLatestByVeId(int veId) throws SQLException {
        String sql = SELECT_BASE
                + " WHERE ve_id = ? "
                + " ORDER BY id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, veId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /*
     * Alias giữ tương thích tên gọi ngắn gọn.
     * AdminRefundService sẽ gọi method này.
     */
    public void markCompleted(int hoanTienId, String maGiaoDichHoan) throws SQLException {
        markCompletedIfPending(hoanTienId, maGiaoDichHoan);
    }
}
