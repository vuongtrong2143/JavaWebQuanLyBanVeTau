package vetau.service;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;
import vetau.dao.ThanhToanDAO;
import vetau.model.ThanhToan;
import vetau.util.DBConnection;
import vetau.util.TrangThai;

public class ThanhToanService {

    private final ThanhToanDAO thanhToanDAO = new ThanhToanDAO();

    public enum CallbackResult {
        NOT_FOUND,
        ALREADY_SUCCESS,
        INVALID_STATE,
        EXPIRED,
        NEWLY_SUCCESS
    }

    public int taoThanhToanPending(int datChoId, String phuongThuc, BigDecimal soTien) throws SQLException {
        if (datChoId <= 0) {
            throw new IllegalArgumentException("Mã đặt chỗ không hợp lệ.");
        }

        if (phuongThuc == null || phuongThuc.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn phương thức thanh toán.");
        }

        if (soTien == null || soTien.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Số tiền thanh toán không hợp lệ.");
        }

        if (daCoThanhToanThanhCong(datChoId)) {
            throw new IllegalStateException("Đơn đặt chỗ này đã được thanh toán.");
        }

        ThanhToan tt = new ThanhToan();
        tt.setDatChoId(datChoId);
        tt.setMaGiaoDich(null);
        tt.setRequestId("REQ" + UUID.randomUUID().toString().replace("-", "").toUpperCase());
        tt.setPhuongThuc(phuongThuc);
        tt.setSoTien(soTien);
        tt.setNgayTao(LocalDateTime.now());
        tt.setNgayThanhToan(null);
        tt.setTrangThai(TrangThai.THANH_TOAN_PENDING);

        return thanhToanDAO.insert(tt);
    }

    /*
     * Callback thành công:
     * - Không phụ thuộc session.
     * - Không tạo vé mới.
     * - Không kích hoạt vé nếu đơn đã quá hạn.
     * - Có idempotency: callback lần 2 không làm lại.
     */
    public CallbackResult xuLyCallbackThanhCong(String requestId, String maGiaoDich) throws SQLException {
        if (requestId == null || requestId.trim().isEmpty()) {
            return CallbackResult.NOT_FOUND;
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);

            try {
                PaymentRow payment = findPaymentForUpdate(conn, requestId);

                if (payment == null) {
                    conn.commit();
                    return CallbackResult.NOT_FOUND;
                }

                if (TrangThai.THANH_TOAN_THANH_CONG.equalsIgnoreCase(payment.trangThai)) {
                    conn.commit();
                    return CallbackResult.ALREADY_SUCCESS;
                }

                if (!TrangThai.THANH_TOAN_PENDING.equalsIgnoreCase(payment.trangThai)) {
                    conn.commit();
                    return CallbackResult.INVALID_STATE;
                }

                DatChoPaymentRow datCho = findDatChoForUpdate(conn, payment.datChoId);

                if (datCho == null) {
                    conn.commit();
                    return CallbackResult.INVALID_STATE;
                }

                if (!TrangThai.DAT_CHO_CHO_THANH_TOAN.equals(datCho.trangThai)) {
                    conn.commit();
                    return CallbackResult.INVALID_STATE;
                }

                if (datCho.thoiGianHetHan != null && datCho.thoiGianHetHan.isBefore(LocalDateTime.now())) {
                    expireDatChoTrongTransaction(conn, payment.datChoId);
                    conn.commit();
                    return CallbackResult.EXPIRED;
                }

                updateThanhToanThanhCong(conn, payment.id, maGiaoDich);
                updateDatChoDaThanhToan(conn, payment.datChoId);
                updateGiuChoDaChuyenVe(conn, payment.datChoId);

                int soVeCapNhat = updateVeHopLe(conn, payment.datChoId);
                if (soVeCapNhat == 0) {
                    throw new IllegalStateException("Không tìm thấy vé chờ thanh toán để kích hoạt.");
                }

                conn.commit();
                return CallbackResult.NEWLY_SUCCESS;

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

    /*
     * Callback thất bại:
     * Chỉ đánh dấu THANH_TOAN = Thất bại.
     * Không hủy đơn ngay nếu vẫn còn thời gian giữ chỗ.
     */
    public void xuLyCallbackThatBai(String requestId) throws SQLException {
        if (requestId == null || requestId.trim().isEmpty()) {
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try {
                PaymentRow payment = findPaymentForUpdate(conn, requestId);

                if (payment == null) {
                    conn.commit();
                    return;
                }

                if (TrangThai.THANH_TOAN_PENDING.equalsIgnoreCase(payment.trangThai)) {
                    updateThanhToanThatBai(conn, payment.id);
                }

                conn.commit();
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

    private boolean daCoThanhToanThanhCong(int datChoId) throws SQLException {
        String sql = """
                SELECT COUNT(*) AS total
                FROM dbo.THANH_TOAN
                WHERE dat_cho_id = ?
                  AND trang_thai = N'Thành công'
                """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, datChoId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt("total") > 0;
            }
        }
    }

    private PaymentRow findPaymentForUpdate(Connection conn, String requestId) throws SQLException {
        String sql = """
                SELECT id, dat_cho_id, trang_thai
                FROM dbo.THANH_TOAN WITH (UPDLOCK, HOLDLOCK)
                WHERE request_id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, requestId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                PaymentRow row = new PaymentRow();
                row.id = rs.getInt("id");
                row.datChoId = rs.getInt("dat_cho_id");
                row.trangThai = rs.getString("trang_thai");
                return row;
            }
        }
    }

    private DatChoPaymentRow findDatChoForUpdate(Connection conn, int datChoId) throws SQLException {
        String sql = """
                SELECT id, trang_thai, thoi_gian_het_han
                FROM dbo.DAT_CHO WITH (UPDLOCK, HOLDLOCK)
                WHERE id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, datChoId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                DatChoPaymentRow row = new DatChoPaymentRow();
                row.id = rs.getInt("id");
                row.trangThai = rs.getString("trang_thai");

                Timestamp ts = rs.getTimestamp("thoi_gian_het_han");
                if (ts != null) {
                    row.thoiGianHetHan = ts.toLocalDateTime();
                }

                return row;
            }
        }
    }

    private void updateThanhToanThanhCong(Connection conn, int thanhToanId, String maGiaoDich) throws SQLException {
        String sql = """
                UPDATE dbo.THANH_TOAN
                SET trang_thai = ?,
                    ma_giao_dich = ?,
                    ngay_thanh_toan = SYSDATETIME()
                WHERE id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, TrangThai.THANH_TOAN_THANH_CONG);
            ps.setString(2, maGiaoDich);
            ps.setInt(3, thanhToanId);
            ps.executeUpdate();
        }
    }

    private void updateThanhToanThatBai(Connection conn, int thanhToanId) throws SQLException {
        String sql = """
                UPDATE dbo.THANH_TOAN
                SET trang_thai = ?
                WHERE id = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, TrangThai.THANH_TOAN_THAT_BAI);
            ps.setInt(2, thanhToanId);
            ps.executeUpdate();
        }
    }

    private void updateDatChoDaThanhToan(Connection conn, int datChoId) throws SQLException {
        String sql = """
                UPDATE dbo.DAT_CHO
                SET trang_thai = ?
                WHERE id = ?
                  AND trang_thai = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, TrangThai.DAT_CHO_DA_THANH_TOAN);
            ps.setInt(2, datChoId);
            ps.setString(3, TrangThai.DAT_CHO_CHO_THANH_TOAN);
            ps.executeUpdate();
        }
    }

    private void updateGiuChoDaChuyenVe(Connection conn, int datChoId) throws SQLException {
        String sql = """
                UPDATE dbo.GIU_CHO
                SET trang_thai = ?
                WHERE dat_cho_id = ?
                  AND trang_thai = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, TrangThai.GIU_CHO_DA_CHUYEN_VE);
            ps.setInt(2, datChoId);
            ps.setString(3, TrangThai.GIU_CHO_DANG_GIU);
            ps.executeUpdate();
        }
    }

    private int updateVeHopLe(Connection conn, int datChoId) throws SQLException {
        String sql = """
                UPDATE dbo.VE
                SET trang_thai = ?
                WHERE dat_cho_id = ?
                  AND trang_thai = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, TrangThai.VE_HOP_LE);
            ps.setInt(2, datChoId);
            ps.setString(3, TrangThai.VE_CHO_THANH_TOAN);
            return ps.executeUpdate();
        }
    }

    /*
     * Dùng khi callback thành công tới sau khi đơn đã hết hạn.
     */
    private void expireDatChoTrongTransaction(Connection conn, int datChoId) throws SQLException {
        updateThanhToanPendingTheoDatChoSangThatBai(conn, datChoId);
        updateGiuChoDangGiuSangHetHan(conn, datChoId);
        updateVeChoThanhToanSangDaHuy(conn, datChoId);
        updateDatChoChoThanhToanSangHetHan(conn, datChoId);
    }

    private int updateThanhToanPendingTheoDatChoSangThatBai(Connection conn, int datChoId) throws SQLException {
        String sql = """
                UPDATE dbo.THANH_TOAN
                SET trang_thai = ?
                WHERE dat_cho_id = ?
                  AND trang_thai = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, TrangThai.THANH_TOAN_THAT_BAI);
            ps.setInt(2, datChoId);
            ps.setString(3, TrangThai.THANH_TOAN_PENDING);
            return ps.executeUpdate();
        }
    }

    private int updateGiuChoDangGiuSangHetHan(Connection conn, int datChoId) throws SQLException {
        String sql = """
                UPDATE dbo.GIU_CHO
                SET trang_thai = ?
                WHERE dat_cho_id = ?
                  AND trang_thai = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, TrangThai.GIU_CHO_HET_HAN);
            ps.setInt(2, datChoId);
            ps.setString(3, TrangThai.GIU_CHO_DANG_GIU);
            return ps.executeUpdate();
        }
    }

    private int updateVeChoThanhToanSangDaHuy(Connection conn, int datChoId) throws SQLException {
        String sql = """
                UPDATE dbo.VE
                SET trang_thai = ?
                WHERE dat_cho_id = ?
                  AND trang_thai = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, TrangThai.VE_DA_HUY);
            ps.setInt(2, datChoId);
            ps.setString(3, TrangThai.VE_CHO_THANH_TOAN);
            return ps.executeUpdate();
        }
    }

    private int updateDatChoChoThanhToanSangHetHan(Connection conn, int datChoId) throws SQLException {
        String sql = """
                UPDATE dbo.DAT_CHO
                SET trang_thai = ?
                WHERE id = ?
                  AND trang_thai = ?
                """;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, TrangThai.DAT_CHO_HET_HAN);
            ps.setInt(2, datChoId);
            ps.setString(3, TrangThai.DAT_CHO_CHO_THANH_TOAN);
            return ps.executeUpdate();
        }
    }

    private static class PaymentRow {
        int id;
        int datChoId;
        String trangThai;
    }

    private static class DatChoPaymentRow {
        int id;
        String trangThai;
        LocalDateTime thoiGianHetHan;
    }
}