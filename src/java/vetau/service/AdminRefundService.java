/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vetau.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import vetau.util.DBConnection;
import vetau.util.TrangThai;

public class AdminRefundService {

    public void duyetHoanTien(int hoanTienId) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            try {
                RefundRow row = findRefundForUpdate(conn, hoanTienId);

                if (row == null) {
                    throw new IllegalArgumentException("Không tìm thấy yêu cầu hoàn tiền.");
                }

                if (!isPending(row.trangThai)) {
                    throw new IllegalStateException("Yêu cầu hoàn tiền này đã được xử lý trước đó.");
                }

                String maGiaoDichHoan = "REFUND_" + System.currentTimeMillis();

                updateHoanTienHoanTat(conn, hoanTienId, maGiaoDichHoan);
                updateLichSuDoiTraGhiChuGanNhat(conn, row.veId, TrangThai.HOAN_TIEN_HOAN_TAT);

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

    public void tuChoiHoanTien(int hoanTienId) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            try {
                RefundRow row = findRefundForUpdate(conn, hoanTienId);

                if (row == null) {
                    throw new IllegalArgumentException("Không tìm thấy yêu cầu hoàn tiền.");
                }

                if (!isPending(row.trangThai)) {
                    throw new IllegalStateException("Yêu cầu hoàn tiền này đã được xử lý trước đó.");
                }

                updateHoanTienTuChoi(conn, hoanTienId);
                updateLichSuDoiTraGhiChuGanNhat(conn, row.veId, TrangThai.HOAN_TIEN_TU_CHOI);

                /*
                 * Không chuyển VE từ Đã trả về Hợp lệ.
                 * Vé đã được khách yêu cầu trả, admin chỉ duyệt/từ chối tiền hoàn.
                 */

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

    private boolean isPending(String trangThai) {
        return TrangThai.HOAN_TIEN_CHO_XU_LY.equals(trangThai)
                || "Pending".equalsIgnoreCase(trangThai);
    }

    private RefundRow findRefundForUpdate(Connection conn, int hoanTienId) throws SQLException {
        String sql = ""
                + "SELECT id, ve_id, trang_thai "
                + "FROM dbo.HOAN_TIEN WITH (UPDLOCK, HOLDLOCK) "
                + "WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, hoanTienId);

            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }

                RefundRow row = new RefundRow();
                row.id = rs.getInt("id");
                row.veId = rs.getInt("ve_id");
                row.trangThai = rs.getString("trang_thai");
                return row;
            }
        }
    }

    private void updateHoanTienHoanTat(Connection conn, int hoanTienId, String maGiaoDichHoan) throws SQLException {
        String sql = ""
                + "UPDATE dbo.HOAN_TIEN "
                + "SET trang_thai = ?, "
                + "    ma_giao_dich_hoan = ?, "
                + "    thoi_gian_hoan_tat = SYSDATETIME() "
                + "WHERE id = ? "
                + "  AND trang_thai IN (N'Pending', N'Chờ xử lý')";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, TrangThai.HOAN_TIEN_HOAN_TAT);
            ps.setString(2, maGiaoDichHoan);
            ps.setInt(3, hoanTienId);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new IllegalStateException("Yêu cầu hoàn tiền không còn ở trạng thái chờ xử lý.");
            }
        }
    }

    private void updateHoanTienTuChoi(Connection conn, int hoanTienId) throws SQLException {
        String sql = ""
                + "UPDATE dbo.HOAN_TIEN "
                + "SET trang_thai = ?, "
                + "    thoi_gian_hoan_tat = SYSDATETIME() "
                + "WHERE id = ? "
                + "  AND trang_thai IN (N'Pending', N'Chờ xử lý')";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, TrangThai.HOAN_TIEN_TU_CHOI);
            ps.setInt(2, hoanTienId);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new IllegalStateException("Yêu cầu hoàn tiền không còn ở trạng thái chờ xử lý.");
            }
        }
    }

    private void updateLichSuDoiTraGhiChuGanNhat(Connection conn, int veId, String ghiChu) throws SQLException {
        String sql = ""
                + "UPDATE dbo.LICH_SU_DOI_TRA "
                + "SET ghi_chu = ? "
                + "WHERE id = ( "
                + "    SELECT TOP 1 id "
                + "    FROM dbo.LICH_SU_DOI_TRA "
                + "    WHERE ve_id = ? "
                + "    ORDER BY id DESC "
                + ")";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, ghiChu);
            ps.setInt(2, veId);
            ps.executeUpdate();
        }
    }

    private static class RefundRow {
        int id;
        int veId;
        String trangThai;
    }
}
