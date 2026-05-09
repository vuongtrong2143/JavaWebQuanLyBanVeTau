package vetau.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import vetau.util.DBConnection;
import vetau.util.TrangThai;

public class GiuChoCleanupService {

    /*
     * Dọn toàn bộ đơn chờ thanh toán đã quá hạn.
     * Nên gọi trước khi tìm chuyến, chọn ghế, xem lịch sử đặt vé, vào checkout.
     */
    public CleanupResult huyCacDonQuaHan() throws SQLException {
        return huyDonQuaHanInternal(null);
    }

    /*
     * Dọn riêng một đơn nếu đơn đó đã quá hạn.
     * Nên gọi trước khi checkout/payment cho một datChoId cụ thể.
     */
    public CleanupResult huyDonQuaHan(int datChoId) throws SQLException {
        if (datChoId <= 0) {
            return new CleanupResult(0, 0, 0, 0, 0);
        }

        return huyDonQuaHanInternal(datChoId);
    }

    private CleanupResult huyDonQuaHanInternal(Integer datChoId) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);

            try {
                List<Integer> expiredIds = findExpiredDatChoIds(conn, datChoId);

                int soThanhToanThatBai = 0;
                int soGiuChoHetHan = 0;
                int soVeDaHuy = 0;
                int soDatChoHetHan = 0;

                for (Integer id : expiredIds) {
                    soThanhToanThatBai += updateThanhToanPendingSangThatBai(conn, id);
                    soGiuChoHetHan += updateGiuChoDangGiuSangHetHan(conn, id);
                    soVeDaHuy += updateVeChoThanhToanSangDaHuy(conn, id);
                    soDatChoHetHan += updateDatChoChoThanhToanSangHetHan(conn, id);
                }

                conn.commit();

                return new CleanupResult(
                        expiredIds.size(),
                        soDatChoHetHan,
                        soGiuChoHetHan,
                        soVeDaHuy,
                        soThanhToanThatBai
                );

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

    private List<Integer> findExpiredDatChoIds(Connection conn, Integer datChoId) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("""
                SELECT id
                FROM dbo.DAT_CHO WITH (UPDLOCK, HOLDLOCK)
                WHERE trang_thai = ?
                  AND thoi_gian_het_han IS NOT NULL
                  AND thoi_gian_het_han < SYSDATETIME()
                """);

        if (datChoId != null) {
            sql.append(" AND id = ?");
        }

        List<Integer> ids = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            ps.setString(1, TrangThai.DAT_CHO_CHO_THANH_TOAN);

            if (datChoId != null) {
                ps.setInt(2, datChoId);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ids.add(rs.getInt("id"));
                }
            }
        }

        return ids;
    }

    private int updateThanhToanPendingSangThatBai(Connection conn, int datChoId) throws SQLException {
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

    public static class CleanupResult {
        private final int soDonQuaHan;
        private final int soDonDaCapNhat;
        private final int soGiuChoDaCapNhat;
        private final int soVeDaCapNhat;
        private final int soThanhToanDaCapNhat;

        public CleanupResult(int soDonQuaHan,
                             int soDonDaCapNhat,
                             int soGiuChoDaCapNhat,
                             int soVeDaCapNhat,
                             int soThanhToanDaCapNhat) {
            this.soDonQuaHan = soDonQuaHan;
            this.soDonDaCapNhat = soDonDaCapNhat;
            this.soGiuChoDaCapNhat = soGiuChoDaCapNhat;
            this.soVeDaCapNhat = soVeDaCapNhat;
            this.soThanhToanDaCapNhat = soThanhToanDaCapNhat;
        }

        public int getSoDonQuaHan() {
            return soDonQuaHan;
        }

        public int getSoDonDaCapNhat() {
            return soDonDaCapNhat;
        }

        public int getSoGiuChoDaCapNhat() {
            return soGiuChoDaCapNhat;
        }

        public int getSoVeDaCapNhat() {
            return soVeDaCapNhat;
        }

        public int getSoThanhToanDaCapNhat() {
            return soThanhToanDaCapNhat;
        }

        public boolean hasChanged() {
            return soDonQuaHan > 0
                    || soDonDaCapNhat > 0
                    || soGiuChoDaCapNhat > 0
                    || soVeDaCapNhat > 0
                    || soThanhToanDaCapNhat > 0;
        }
    }
}