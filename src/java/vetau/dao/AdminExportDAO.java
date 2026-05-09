package vetau.dao;

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
public class AdminExportDAO {

    public ExportData exportMonthlyRevenue() throws SQLException {
        String[] headers = {
                "Thang",
                "Doanh thu goc",
                "Hoan tien",
                "Doanh thu thuan",
                "So ve"
        };

        String sql = ""
                + "WITH Months AS ( "
                + "    SELECT 0 AS n, DATEFROMPARTS(YEAR(SYSDATETIME()), MONTH(SYSDATETIME()), 1) AS month_start "
                + "    UNION ALL "
                + "    SELECT n + 1, DATEADD(MONTH, -1, month_start) "
                + "    FROM Months "
                + "    WHERE n + 1 < 12 "
                + "), Revenue AS ( "
                + "    SELECT "
                + "        DATEFROMPARTS(YEAR(ngay_thanh_toan), MONTH(ngay_thanh_toan), 1) AS month_start, "
                + "        SUM(so_tien) AS revenue "
                + "    FROM dbo.THANH_TOAN "
                + "    WHERE trang_thai = N'Thành công' "
                + "      AND ngay_thanh_toan IS NOT NULL "
                + "    GROUP BY DATEFROMPARTS(YEAR(ngay_thanh_toan), MONTH(ngay_thanh_toan), 1) "
                + "), Refunds AS ( "
                + "    SELECT "
                + "        DATEFROMPARTS(YEAR(thoi_gian_hoan_tat), MONTH(thoi_gian_hoan_tat), 1) AS month_start, "
                + "        SUM(so_tien_hoan) AS refund "
                + "    FROM dbo.HOAN_TIEN "
                + "    WHERE trang_thai = N'Hoàn tất' "
                + "      AND thoi_gian_hoan_tat IS NOT NULL "
                + "    GROUP BY DATEFROMPARTS(YEAR(thoi_gian_hoan_tat), MONTH(thoi_gian_hoan_tat), 1) "
                + "), Tickets AS ( "
                + "    SELECT "
                + "        DATEFROMPARTS(YEAR(dc.ngay_dat), MONTH(dc.ngay_dat), 1) AS month_start, "
                + "        COUNT(v.id) AS ticket_count "
                + "    FROM dbo.VE v "
                + "    JOIN dbo.DAT_CHO dc ON dc.id = v.dat_cho_id "
                + "    WHERE v.trang_thai IN (N'Hợp lệ', N'Đã sử dụng', N'Đã trả') "
                + "    GROUP BY DATEFROMPARTS(YEAR(dc.ngay_dat), MONTH(dc.ngay_dat), 1) "
                + ") "
                + "SELECT "
                + "    RIGHT('0' + CAST(MONTH(m.month_start) AS VARCHAR(2)), 2) + '/' + CAST(YEAR(m.month_start) AS VARCHAR(4)) AS thang, "
                + "    COALESCE(r.revenue, 0) AS revenue, "
                + "    COALESCE(f.refund, 0) AS refund, "
                + "    COALESCE(r.revenue, 0) - COALESCE(f.refund, 0) AS net_revenue, "
                + "    COALESCE(t.ticket_count, 0) AS ticket_count "
                + "FROM Months m "
                + "LEFT JOIN Revenue r ON r.month_start = m.month_start "
                + "LEFT JOIN Refunds f ON f.month_start = m.month_start "
                + "LEFT JOIN Tickets t ON t.month_start = m.month_start "
                + "ORDER BY m.month_start ASC "
                + "OPTION (MAXRECURSION 100)";

        List<String[]> rows = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rows.add(new String[]{
                        rs.getString("thang"),
                        rs.getBigDecimal("revenue").toPlainString(),
                        rs.getBigDecimal("refund").toPlainString(),
                        rs.getBigDecimal("net_revenue").toPlainString(),
                        String.valueOf(rs.getInt("ticket_count"))
                });
            }
        }

        return new ExportData(headers, rows);
    }

    public ExportData exportBookings() throws SQLException {
        String[] headers = {
                "Ma don",
                "Khach hang",
                "Email",
                "So dien thoai",
                "Ngay dat",
                "Tong thanh toan",
                "Trang thai"
        };

        String sql = ""
                + "SELECT TOP 500 "
                + "    dc.ma_dat_cho, "
                + "    kh.ho_ten, "
                + "    kh.email, "
                + "    kh.so_dien_thoai, "
                + "    CONVERT(VARCHAR(19), dc.ngay_dat, 120) AS ngay_dat_text, "
                + "    dc.tong_thanh_toan, "
                + "    dc.trang_thai "
                + "FROM dbo.DAT_CHO dc "
                + "LEFT JOIN dbo.KHACH_HANG kh ON kh.id = dc.khach_hang_id "
                + "ORDER BY dc.ngay_dat DESC, dc.id DESC";

        List<String[]> rows = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rows.add(new String[]{
                        rs.getString("ma_dat_cho"),
                        rs.getString("ho_ten"),
                        rs.getString("email"),
                        rs.getString("so_dien_thoai"),
                        rs.getString("ngay_dat_text"),
                        rs.getBigDecimal("tong_thanh_toan") == null ? "0" : rs.getBigDecimal("tong_thanh_toan").toPlainString(),
                        rs.getString("trang_thai")
                });
            }
        }

        return new ExportData(headers, rows);
    }

    public ExportData exportRefunds() throws SQLException {
        String[] headers = {
                "ID hoan tien",
                "Ma ve",
                "Ma don",
                "Khach hang",
                "Hanh khach",
                "So tien hoan",
                "Trang thai",
                "Thoi gian yeu cau",
                "Thoi gian hoan tat",
                "Ma giao dich hoan"
        };

        String sql = ""
                + "SELECT TOP 500 "
                + "    ht.id AS hoan_tien_id, "
                + "    v.ma_ve, "
                + "    dc.ma_dat_cho, "
                + "    kh.ho_ten AS ten_khach_hang, "
                + "    hk.ho_ten AS ten_hanh_khach, "
                + "    ht.so_tien_hoan, "
                + "    ht.trang_thai, "
                + "    CONVERT(VARCHAR(19), ht.thoi_gian_yeu_cau, 120) AS thoi_gian_yeu_cau_text, "
                + "    CONVERT(VARCHAR(19), ht.thoi_gian_hoan_tat, 120) AS thoi_gian_hoan_tat_text, "
                + "    ht.ma_giao_dich_hoan "
                + "FROM dbo.HOAN_TIEN ht "
                + "JOIN dbo.VE v ON v.id = ht.ve_id "
                + "JOIN dbo.DAT_CHO dc ON dc.id = v.dat_cho_id "
                + "LEFT JOIN dbo.KHACH_HANG kh ON kh.id = dc.khach_hang_id "
                + "LEFT JOIN dbo.HANH_KHACH hk ON hk.id = v.hanh_khach_id "
                + "ORDER BY ht.id DESC";

        List<String[]> rows = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rows.add(new String[]{
                        String.valueOf(rs.getInt("hoan_tien_id")),
                        rs.getString("ma_ve"),
                        rs.getString("ma_dat_cho"),
                        rs.getString("ten_khach_hang"),
                        rs.getString("ten_hanh_khach"),
                        rs.getBigDecimal("so_tien_hoan") == null ? "0" : rs.getBigDecimal("so_tien_hoan").toPlainString(),
                        rs.getString("trang_thai"),
                        rs.getString("thoi_gian_yeu_cau_text"),
                        rs.getString("thoi_gian_hoan_tat_text"),
                        rs.getString("ma_giao_dich_hoan")
                });
            }
        }

        return new ExportData(headers, rows);
    }

    public ExportData exportTickets() throws SQLException {
        String[] headers = {
                "Ma ve",
                "Ma don",
                "Hanh khach",
                "So giay to",
                "Ga di",
                "Ga den",
                "Gia ve",
                "Trang thai"
        };

        String sql = ""
                + "SELECT TOP 500 "
                + "    v.ma_ve, "
                + "    dc.ma_dat_cho, "
                + "    hk.ho_ten AS ten_hanh_khach, "
                + "    hk.so_giay_to, "
                + "    gd.ten_ga AS ten_ga_di, "
                + "    ga.ten_ga AS ten_ga_den, "
                + "    v.gia_ve_chi_tiet, "
                + "    v.trang_thai "
                + "FROM dbo.VE v "
                + "JOIN dbo.DAT_CHO dc ON dc.id = v.dat_cho_id "
                + "LEFT JOIN dbo.HANH_KHACH hk ON hk.id = v.hanh_khach_id "
                + "LEFT JOIN dbo.GA gd ON gd.id = v.ga_di_id "
                + "LEFT JOIN dbo.GA ga ON ga.id = v.ga_den_id "
                + "ORDER BY v.id DESC";

        List<String[]> rows = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                rows.add(new String[]{
                        rs.getString("ma_ve"),
                        rs.getString("ma_dat_cho"),
                        rs.getString("ten_hanh_khach"),
                        rs.getString("so_giay_to"),
                        rs.getString("ten_ga_di"),
                        rs.getString("ten_ga_den"),
                        rs.getBigDecimal("gia_ve_chi_tiet") == null ? "0" : rs.getBigDecimal("gia_ve_chi_tiet").toPlainString(),
                        rs.getString("trang_thai")
                });
            }
        }

        return new ExportData(headers, rows);
    }

    public static class ExportData {
        private final String[] headers;
        private final List<String[]> rows;

        public ExportData(String[] headers, List<String[]> rows) {
            this.headers = headers;
            this.rows = rows;
        }

        public String[] getHeaders() {
            return headers;
        }

        public List<String[]> getRows() {
            return rows;
        }
    }
}