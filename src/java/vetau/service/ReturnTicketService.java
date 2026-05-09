/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vetau.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import vetau.dao.ChinhSachDoiTraDAO;
import vetau.dao.TicketCheckDAO;
import vetau.dto.TicketCheckResultDTO;
import vetau.model.ChinhSachDoiTra;
import vetau.util.DBConnection;
import vetau.util.TrangThai;

public class ReturnTicketService {

    private final ChinhSachDoiTraDAO chinhSachDAO = new ChinhSachDoiTraDAO();
    private final TicketCheckDAO ticketCheckDAO = new TicketCheckDAO();
    private final DoiTraService doiTraService = new DoiTraService();

    public ReturnPreview xemTruocTraVe(int khachHangId, String maVe) throws SQLException {
        ReturnContext ctx = loadReturnContext(khachHangId, maVe);

        ChinhSachDoiTra chinhSach = timChinhSachTraVe(ctx);
        BigDecimal soTienHoan = doiTraService.tinhTienHoan(ctx.giaVeChiTiet, chinhSach);

        ReturnPreview preview = new ReturnPreview();
        preview.setTicket(ticketCheckDAO.findByMaVeAndSoGiayTo(maVe, null));
        preview.setChinhSach(chinhSach);
        preview.setSoTienHoan(soTienHoan);
        preview.setPhiKhauTru(ctx.giaVeChiTiet.subtract(soTienHoan));
        return preview;
    }

    public int xacNhanTraVe(int khachHangId, String maVe, String lyDo) throws SQLException {
        ReturnContext ctx = loadReturnContext(khachHangId, maVe);

        ChinhSachDoiTra chinhSach = timChinhSachTraVe(ctx);
        BigDecimal soTienHoan = doiTraService.tinhTienHoan(ctx.giaVeChiTiet, chinhSach);

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            try {
                ReturnContext locked = loadReturnContextForUpdate(conn, khachHangId, maVe);

                if (!TrangThai.VE_HOP_LE.equals(locked.trangThaiVe)) {
                    throw new IllegalStateException("Vé không còn ở trạng thái hợp lệ để trả.");
                }

                if (existsPendingRefund(conn, locked.veId)) {
                    throw new IllegalStateException("Vé này đã có yêu cầu hoàn tiền đang chờ xử lý.");
                }

                updateVeDaTra(conn, locked.veId);

                insertLichSuDoiTra(
                        conn,
                        locked.veId,
                        chinhSach.getId(),
                        lyDo,
                        chinhSach,
                        soTienHoan
                );

                int hoanTienId = insertHoanTienChoXuLy(
                        conn,
                        locked.thanhToanId,
                        locked.veId,
                        soTienHoan
                );

                capNhatTrangThaiDatChoSauTraVe(conn, locked.datChoId);

                conn.commit();
                return hoanTienId;

            } catch (Exception ex) {
                conn.rollback();

                if (ex instanceof SQLException) {
                    throw (SQLException) ex;
                }

                throw new SQLException(ex.getMessage(), ex);
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    private ChinhSachDoiTra timChinhSachTraVe(ReturnContext ctx) throws SQLException {
        ChinhSachDoiTra chinhSach = chinhSachDAO.findPhuHop(
                "Cá nhân",
                "Tất cả",
                ctx.soGioTruocKhoiHanh,
                false
        );

        if (chinhSach == null || !chinhSach.isChoPhepTra()) {
            throw new IllegalStateException("Không tìm thấy chính sách trả vé phù hợp hoặc vé không đủ điều kiện trả.");
        }

        return chinhSach;
    }

    private ReturnContext loadReturnContext(int khachHangId, String maVe) throws SQLException {
        String sql = ""
                + "SELECT "
                + " v.id AS ve_id, "
                + " v.dat_cho_id, "
                + " v.trang_thai AS trang_thai_ve, "
                + " v.gia_ve_chi_tiet, "
                + " dc.khach_hang_id, "
                + " ct.thoi_gian_khoi_hanh, "
                + " DATEDIFF(HOUR, SYSDATETIME(), ct.thoi_gian_khoi_hanh) AS so_gio_truoc_khoi_hanh, "
                + " tt.id AS thanh_toan_id "
                + "FROM dbo.VE v "
                + "JOIN dbo.DAT_CHO dc ON dc.id = v.dat_cho_id "
                + "JOIN dbo.CHUYEN_TAU ct ON ct.id = v.chuyen_tau_id "
                + "OUTER APPLY ( "
                + "    SELECT TOP 1 * "
                + "    FROM dbo.THANH_TOAN tt "
                + "    WHERE tt.dat_cho_id = dc.id "
                + "      AND tt.trang_thai = N'Thành công' "
                + "    ORDER BY tt.id DESC "
                + ") tt "
                + "WHERE v.ma_ve = ? "
                + "  AND dc.khach_hang_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, maVe);
            ps.setInt(2, khachHangId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new IllegalArgumentException("Không tìm thấy vé hoặc vé không thuộc tài khoản của bạn.");
                }

                return mapReturnContext(rs);
            }
        }
    }

    private ReturnContext loadReturnContextForUpdate(Connection conn, int khachHangId, String maVe) throws SQLException {
        String sql = ""
                + "SELECT "
                + " v.id AS ve_id, "
                + " v.dat_cho_id, "
                + " v.trang_thai AS trang_thai_ve, "
                + " v.gia_ve_chi_tiet, "
                + " dc.khach_hang_id, "
                + " ct.thoi_gian_khoi_hanh, "
                + " DATEDIFF(HOUR, SYSDATETIME(), ct.thoi_gian_khoi_hanh) AS so_gio_truoc_khoi_hanh, "
                + " tt.id AS thanh_toan_id "
                + "FROM dbo.VE v WITH (UPDLOCK, HOLDLOCK) "
                + "JOIN dbo.DAT_CHO dc WITH (UPDLOCK, HOLDLOCK) ON dc.id = v.dat_cho_id "
                + "JOIN dbo.CHUYEN_TAU ct ON ct.id = v.chuyen_tau_id "
                + "OUTER APPLY ( "
                + "    SELECT TOP 1 * "
                + "    FROM dbo.THANH_TOAN tt "
                + "    WHERE tt.dat_cho_id = dc.id "
                + "      AND tt.trang_thai = N'Thành công' "
                + "    ORDER BY tt.id DESC "
                + ") tt "
                + "WHERE v.ma_ve = ? "
                + "  AND dc.khach_hang_id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, maVe);
            ps.setInt(2, khachHangId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    throw new IllegalArgumentException("Không tìm thấy vé hoặc vé không thuộc tài khoản của bạn.");
                }

                return mapReturnContext(rs);
            }
        }
    }

    private ReturnContext mapReturnContext(ResultSet rs) throws SQLException {
        ReturnContext ctx = new ReturnContext();

        ctx.veId = rs.getInt("ve_id");
        ctx.datChoId = rs.getInt("dat_cho_id");
        ctx.trangThaiVe = rs.getString("trang_thai_ve");
        ctx.giaVeChiTiet = rs.getBigDecimal("gia_ve_chi_tiet");
        ctx.khachHangId = rs.getInt("khach_hang_id");
        ctx.soGioTruocKhoiHanh = rs.getInt("so_gio_truoc_khoi_hanh");

        int thanhToanId = rs.getInt("thanh_toan_id");
        if (rs.wasNull()) {
            throw new IllegalStateException("Không tìm thấy giao dịch thanh toán thành công của vé này.");
        }
        ctx.thanhToanId = thanhToanId;

        if (!TrangThai.VE_HOP_LE.equals(ctx.trangThaiVe)) {
            throw new IllegalStateException("Chỉ vé hợp lệ mới được phép trả.");
        }

        if (ctx.soGioTruocKhoiHanh < 0) {
            throw new IllegalStateException("Chuyến tàu đã khởi hành, không thể trả vé.");
        }

        return ctx;
    }

    private boolean existsPendingRefund(Connection conn, int veId) throws SQLException {
        String sql = ""
                + "SELECT COUNT(*) AS total "
                + "FROM dbo.HOAN_TIEN "
                + "WHERE ve_id = ? "
                + "  AND trang_thai IN (N'Pending', N'Chờ xử lý')";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, veId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt("total") > 0;
            }
        }
    }

    private void updateVeDaTra(Connection conn, int veId) throws SQLException {
        String sql = ""
                + "UPDATE dbo.VE "
                + "SET trang_thai = ? "
                + "WHERE id = ? "
                + "  AND trang_thai = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, TrangThai.VE_DA_TRA);
            ps.setInt(2, veId);
            ps.setString(3, TrangThai.VE_HOP_LE);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new IllegalStateException("Không thể cập nhật trạng thái vé. Vé có thể đã được xử lý trước đó.");
            }
        }
    }

    private void insertLichSuDoiTra(Connection conn,
                                    int veId,
                                    Integer chinhSachId,
                                    String lyDo,
                                    ChinhSachDoiTra chinhSach,
                                    BigDecimal soTienHoan) throws SQLException {
        String sql = ""
                + "INSERT INTO dbo.LICH_SU_DOI_TRA "
                + "(ve_id, nhan_vien_id, chinh_sach_id, loai_giao_dich, ly_do, "
                + " phi_doi, ty_le_khau_tru, so_tien_hoan, thoi_gian_xu_ly, ghi_chu) "
                + "VALUES (?, NULL, ?, N'Trả vé', ?, ?, ?, ?, SYSDATETIME(), ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, veId);

            if (chinhSachId == null) {
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(2, chinhSachId);
            }

            ps.setString(3, lyDo);
            ps.setBigDecimal(4, chinhSach.getPhiDoiCoDinh());
            ps.setBigDecimal(5, chinhSach.getTyLeKhauTru());
            ps.setBigDecimal(6, soTienHoan);
            ps.setString(7, TrangThai.HOAN_TIEN_CHO_XU_LY);
            ps.executeUpdate();
        }
    }

    private int insertHoanTienChoXuLy(Connection conn,
                                      int thanhToanId,
                                      int veId,
                                      BigDecimal soTienHoan) throws SQLException {
        String sql = ""
                + "INSERT INTO dbo.HOAN_TIEN "
                + "(thanh_toan_id, ve_id, so_tien_hoan, ma_giao_dich_hoan, "
                + " thoi_gian_yeu_cau, thoi_gian_hoan_tat, trang_thai) "
                + "VALUES (?, ?, ?, NULL, SYSDATETIME(), NULL, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, thanhToanId);
            ps.setInt(2, veId);
            ps.setBigDecimal(3, soTienHoan);
            ps.setString(4, TrangThai.HOAN_TIEN_CHO_XU_LY);

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

            throw new SQLException("Không lấy được ID yêu cầu hoàn tiền.");
        }
    }

    private void capNhatTrangThaiDatChoSauTraVe(Connection conn, int datChoId) throws SQLException {
        String countSql = ""
                + "SELECT "
                + " SUM(CASE WHEN trang_thai = N'Đã trả' THEN 1 ELSE 0 END) AS so_ve_da_tra, "
                + " COUNT(*) AS tong_ve "
                + "FROM dbo.VE "
                + "WHERE dat_cho_id = ?";

        int soVeDaTra = 0;
        int tongVe = 0;

        try (PreparedStatement ps = conn.prepareStatement(countSql)) {
            ps.setInt(1, datChoId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    soVeDaTra = rs.getInt("so_ve_da_tra");
                    tongVe = rs.getInt("tong_ve");
                }
            }
        }

        String trangThaiMoi;
        if (tongVe > 0 && soVeDaTra == tongVe) {
            trangThaiMoi = "Hoàn tiền toàn bộ";
        } else {
            trangThaiMoi = "Hoàn tiền một phần";
        }

        String updateSql = ""
                + "UPDATE dbo.DAT_CHO "
                + "SET trang_thai = ? "
                + "WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
            ps.setString(1, trangThaiMoi);
            ps.setInt(2, datChoId);
            ps.executeUpdate();
        }
    }

    private static class ReturnContext {
        int veId;
        int datChoId;
        int khachHangId;
        int thanhToanId;
        int soGioTruocKhoiHanh;
        String trangThaiVe;
        BigDecimal giaVeChiTiet;
    }

    public static class ReturnPreview {
        private TicketCheckResultDTO ticket;
        private ChinhSachDoiTra chinhSach;
        private BigDecimal soTienHoan;
        private BigDecimal phiKhauTru;

        public TicketCheckResultDTO getTicket() {
            return ticket;
        }

        public void setTicket(TicketCheckResultDTO ticket) {
            this.ticket = ticket;
        }

        public ChinhSachDoiTra getChinhSach() {
            return chinhSach;
        }

        public void setChinhSach(ChinhSachDoiTra chinhSach) {
            this.chinhSach = chinhSach;
        }

        public BigDecimal getSoTienHoan() {
            return soTienHoan;
        }

        public void setSoTienHoan(BigDecimal soTienHoan) {
            this.soTienHoan = soTienHoan;
        }

        public BigDecimal getPhiKhauTru() {
            return phiKhauTru;
        }

        public void setPhiKhauTru(BigDecimal phiKhauTru) {
            this.phiKhauTru = phiKhauTru;
        }
    }
}