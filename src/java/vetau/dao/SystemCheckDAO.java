package vetau.dao;

import vetau.dto.SystemCheckItemDTO;
import vetau.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("SqlNoDataSourceInspection")
public class SystemCheckDAO {

    public List<SystemCheckItemDTO> runAllChecks() {
        List<SystemCheckItemDTO> checks = new ArrayList<>();

        checks.add(checkDatabaseConnection());

        checks.add(checkDuplicateTicketCode());
        checks.add(checkDuplicatePendingRefund());
        checks.add(checkInvalidDatChoStatus());
        checks.add(checkInvalidVeStatus());
        checks.add(checkInvalidThanhToanStatus());
        checks.add(checkInvalidHoanTienStatus());

        checks.add(checkOrphanTickets());
        checks.add(checkOrphanPayments());
        checks.add(checkTicketTotalMatchesBooking());

        checks.add(checkDashboardHasData());

        return checks;
    }

    public Map<String, Integer> getSummaryCounts() throws SQLException {
        Map<String, Integer> map = new LinkedHashMap<>();

        map.put("Tổng đơn đặt chỗ", queryCount("SELECT COUNT(*) AS total FROM dbo.DAT_CHO"));
        map.put("Tổng vé", queryCount("SELECT COUNT(*) AS total FROM dbo.VE"));
        map.put("Thanh toán thành công", queryCount("SELECT COUNT(*) AS total FROM dbo.THANH_TOAN WHERE trang_thai = N'Thành công'"));
        map.put("Hoàn tiền chờ xử lý", queryCount("SELECT COUNT(*) AS total FROM dbo.HOAN_TIEN WHERE trang_thai = N'Chờ xử lý'"));
        map.put("Khách hàng", queryCount("SELECT COUNT(*) AS total FROM dbo.KHACH_HANG"));
        map.put("Chuyến tàu", queryCount("SELECT COUNT(*) AS total FROM dbo.CHUYEN_TAU"));

        return map;
    }

    private SystemCheckItemDTO checkDatabaseConnection() {
        try (Connection conn = DBConnection.getConnection()) {
            boolean valid = conn != null && !conn.isClosed();

            if (valid) {
                return ok("Database", "Kết nối database", "Kết nối VeTauDB thành công.", 0);
            }

            return error("Database", "Kết nối database", "Không thể kết nối database.", 1);

        } catch (Exception ex) {
            return error("Database", "Kết nối database", "Lỗi kết nối DB: " + ex.getMessage(), 1);
        }
    }

    private SystemCheckItemDTO checkDuplicateTicketCode() {
        String sql = ""
                + "SELECT COUNT(*) AS total "
                + "FROM ( "
                + "    SELECT ma_ve "
                + "    FROM dbo.VE "
                + "    GROUP BY ma_ve "
                + "    HAVING COUNT(*) > 1 "
                + ") x";

        return checkZero(
                "Vé",
                "Mã vé bị trùng",
                sql,
                "Không có mã vé trùng.",
                "Có mã vé bị trùng. Cần kiểm tra lại logic tạo vé/callback."
        );
    }

    private SystemCheckItemDTO checkDuplicatePendingRefund() {
        String sql = ""
                + "SELECT COUNT(*) AS total "
                + "FROM ( "
                + "    SELECT ve_id "
                + "    FROM dbo.HOAN_TIEN "
                + "    WHERE trang_thai = N'Chờ xử lý' "
                + "    GROUP BY ve_id "
                + "    HAVING COUNT(*) > 1 "
                + ") x";

        return checkZero(
                "Hoàn tiền",
                "Nhiều yêu cầu Chờ xử lý cho cùng vé",
                sql,
                "Không có hoàn tiền chờ xử lý bị trùng.",
                "Có vé đang có nhiều yêu cầu hoàn tiền Chờ xử lý."
        );
    }

    private SystemCheckItemDTO checkInvalidDatChoStatus() {
        String sql = ""
                + "SELECT COUNT(*) AS total "
                + "FROM dbo.DAT_CHO "
                + "WHERE trang_thai NOT IN ("
                + "N'Chờ thanh toán', "
                + "N'Đã thanh toán', "
                + "N'Đã hủy', "
                + "N'Hết hạn', "
                + "N'Hoàn tiền một phần', "
                + "N'Hoàn tiền toàn bộ'"
                + ")";

        return checkZero(
                "Trạng thái",
                "DAT_CHO có trạng thái lạ",
                sql,
                "DAT_CHO không có trạng thái lạ.",
                "DAT_CHO có trạng thái không nằm trong danh sách chuẩn."
        );
    }

    private SystemCheckItemDTO checkInvalidVeStatus() {
        String sql = ""
                + "SELECT COUNT(*) AS total "
                + "FROM dbo.VE "
                + "WHERE trang_thai NOT IN ("
                + "N'Chờ thanh toán', "
                + "N'Hợp lệ', "
                + "N'Đã sử dụng', "
                + "N'Đã trả', "
                + "N'Đã hủy', "
                + "N'Hết hạn'"
                + ")";

        return checkZero(
                "Trạng thái",
                "VE có trạng thái lạ",
                sql,
                "VE không có trạng thái lạ.",
                "VE có trạng thái không nằm trong danh sách chuẩn."
        );
    }

    private SystemCheckItemDTO checkInvalidThanhToanStatus() {
        String sql = ""
                + "SELECT COUNT(*) AS total "
                + "FROM dbo.THANH_TOAN "
                + "WHERE trang_thai NOT IN ("
                + "N'Chờ xử lý', "
                + "N'Thành công', "
                + "N'Thất bại', "
                + "N'Đã hủy'"
                + ")";

        return checkZero(
                "Trạng thái",
                "THANH_TOAN có trạng thái lạ",
                sql,
                "THANH_TOAN không có trạng thái lạ.",
                "THANH_TOAN có trạng thái không nằm trong danh sách chuẩn."
        );
    }

    private SystemCheckItemDTO checkInvalidHoanTienStatus() {
        String sql = ""
                + "SELECT COUNT(*) AS total "
                + "FROM dbo.HOAN_TIEN "
                + "WHERE trang_thai NOT IN ("
                + "N'Chờ xử lý', "
                + "N'Hoàn tất', "
                + "N'Từ chối'"
                + ")";

        return checkZero(
                "Trạng thái",
                "HOAN_TIEN có trạng thái lạ",
                sql,
                "HOAN_TIEN không có trạng thái lạ.",
                "HOAN_TIEN có trạng thái không nằm trong danh sách chuẩn."
        );
    }

    private SystemCheckItemDTO checkOrphanTickets() {
        String sql = ""
                + "SELECT COUNT(*) AS total "
                + "FROM dbo.VE v "
                + "LEFT JOIN dbo.DAT_CHO dc ON dc.id = v.dat_cho_id "
                + "WHERE dc.id IS NULL";

        return checkZero(
                "Liên kết dữ liệu",
                "Vé không có đơn đặt chỗ",
                sql,
                "Không có vé mồ côi.",
                "Có vé không liên kết được với DAT_CHO."
        );
    }

    private SystemCheckItemDTO checkOrphanPayments() {
        String sql = ""
                + "SELECT COUNT(*) AS total "
                + "FROM dbo.THANH_TOAN tt "
                + "LEFT JOIN dbo.DAT_CHO dc ON dc.id = tt.dat_cho_id "
                + "WHERE dc.id IS NULL";

        return checkZero(
                "Liên kết dữ liệu",
                "Thanh toán không có đơn đặt chỗ",
                sql,
                "Không có thanh toán mồ côi.",
                "Có thanh toán không liên kết được với DAT_CHO."
        );
    }

    private SystemCheckItemDTO checkTicketTotalMatchesBooking() {
        String sql = ""
                + "SELECT COUNT(*) AS total "
                + "FROM dbo.DAT_CHO dc "
                + "WHERE dc.trang_thai IN (N'Đã thanh toán', N'Hoàn tiền một phần', N'Hoàn tiền toàn bộ') "
                + "  AND ABS( "
                + "      COALESCE(dc.tong_thanh_toan, 0) - "
                + "      COALESCE(( "
                + "          SELECT SUM(v.gia_ve_chi_tiet) "
                + "          FROM dbo.VE v "
                + "          WHERE v.dat_cho_id = dc.id "
                + "      ), 0) "
                + "  ) > 1000";

        return checkZero(
                "Đối soát tiền",
                "Tổng tiền đơn lệch tổng vé",
                sql,
                "Tổng tiền đơn khớp tổng tiền vé.",
                "Có đơn đã thanh toán nhưng tổng tiền lệch tổng giá vé."
        );
    }

    private SystemCheckItemDTO checkDashboardHasData() {
        String sql = ""
                + "SELECT COUNT(*) AS total "
                + "FROM dbo.THANH_TOAN "
                + "WHERE trang_thai = N'Thành công'";

        try {
            int count = queryCount(sql);

            if (count > 0) {
                return ok("Báo cáo", "Dữ liệu dashboard", "Có dữ liệu thanh toán thành công để hiển thị báo cáo.", count);
            }

            return warning("Báo cáo", "Dữ liệu dashboard", "Chưa có thanh toán thành công. Dashboard/report có thể ít dữ liệu.", count);

        } catch (Exception ex) {
            return error("Báo cáo", "Dữ liệu dashboard", "Lỗi kiểm tra dữ liệu dashboard: " + ex.getMessage(), 1);
        }
    }

    private SystemCheckItemDTO checkZero(String groupName,
                                         String checkName,
                                         String sql,
                                         String okMessage,
                                         String errorMessage) {
        try {
            int count = queryCount(sql);

            if (count == 0) {
                return ok(groupName, checkName, okMessage, count);
            }

            return error(groupName, checkName, errorMessage, count);

        } catch (Exception ex) {
            return error(groupName, checkName, "Lỗi kiểm tra: " + ex.getMessage(), 1);
        }
    }

    private int queryCount(String sql) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt("total") : 0;
        }
    }

    private SystemCheckItemDTO ok(String group, String name, String message, int count) {
        return new SystemCheckItemDTO(group, name, "OK", message, count);
    }

    private SystemCheckItemDTO warning(String group, String name, String message, int count) {
        return new SystemCheckItemDTO(group, name, "WARNING", message, count);
    }

    private SystemCheckItemDTO error(String group, String name, String message, int count) {
        return new SystemCheckItemDTO(group, name, "ERROR", message, count);
    }
}