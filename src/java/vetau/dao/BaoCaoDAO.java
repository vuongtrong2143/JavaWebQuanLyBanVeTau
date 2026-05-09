package vetau.dao;

import vetau.dto.DashboardStatsDTO;
import vetau.dto.LabelValueDTO;
import vetau.dto.MonthlyRevenueDTO;
import vetau.dto.RecentBookingDTO;
import vetau.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BaoCaoDAO {

    public DashboardStatsDTO getDashboardStats() throws SQLException {
        DashboardStatsDTO dto = new DashboardStatsDTO();

        dto.setRevenueToday(getRevenueToday());
        dto.setRevenueThisMonth(getRevenueThisMonth());
        dto.setRefundThisMonth(getRefundThisMonth());

        dto.setNetRevenueThisMonth(
                dto.getRevenueThisMonth().subtract(dto.getRefundThisMonth())
        );

        dto.setTicketsThisMonth(countTicketsThisMonth());
        dto.setOrdersWaitingPayment(countOrdersWaitingPayment());
        dto.setPaidOrdersThisMonth(countPaidOrdersThisMonth());
        dto.setNewCustomersThisMonth(countNewCustomersThisMonth());
        dto.setPendingRefunds(countPendingRefunds());

        return dto;
    }

    private BigDecimal getRevenueToday() throws SQLException {
        String sql = ""
                + "SELECT COALESCE(SUM(so_tien), 0) AS total "
                + "FROM dbo.THANH_TOAN "
                + "WHERE trang_thai = N'Thành công' "
                + "  AND CAST(ngay_thanh_toan AS DATE) = CAST(SYSDATETIME() AS DATE)";

        return queryBigDecimal(sql);
    }

    private BigDecimal getRevenueThisMonth() throws SQLException {
        String sql = ""
                + "SELECT COALESCE(SUM(so_tien), 0) AS total "
                + "FROM dbo.THANH_TOAN "
                + "WHERE trang_thai = N'Thành công' "
                + "  AND ngay_thanh_toan >= DATEFROMPARTS(YEAR(SYSDATETIME()), MONTH(SYSDATETIME()), 1) "
                + "  AND ngay_thanh_toan < DATEADD(MONTH, 1, DATEFROMPARTS(YEAR(SYSDATETIME()), MONTH(SYSDATETIME()), 1))";

        return queryBigDecimal(sql);
    }

    private BigDecimal getRefundThisMonth() throws SQLException {
        String sql = ""
                + "SELECT COALESCE(SUM(so_tien_hoan), 0) AS total "
                + "FROM dbo.HOAN_TIEN "
                + "WHERE trang_thai = N'Hoàn tất' "
                + "  AND thoi_gian_hoan_tat >= DATEFROMPARTS(YEAR(SYSDATETIME()), MONTH(SYSDATETIME()), 1) "
                + "  AND thoi_gian_hoan_tat < DATEADD(MONTH, 1, DATEFROMPARTS(YEAR(SYSDATETIME()), MONTH(SYSDATETIME()), 1))";

        return queryBigDecimal(sql);
    }

    private int countTicketsThisMonth() throws SQLException {
        String sql = ""
                + "SELECT COUNT(*) AS total "
                + "FROM dbo.VE v "
                + "JOIN dbo.DAT_CHO dc ON dc.id = v.dat_cho_id "
                + "WHERE v.trang_thai IN (N'Hợp lệ', N'Đã sử dụng', N'Đã trả') "
                + "  AND dc.ngay_dat >= DATEFROMPARTS(YEAR(SYSDATETIME()), MONTH(SYSDATETIME()), 1) "
                + "  AND dc.ngay_dat < DATEADD(MONTH, 1, DATEFROMPARTS(YEAR(SYSDATETIME()), MONTH(SYSDATETIME()), 1))";

        return queryInt(sql);
    }

    private int countOrdersWaitingPayment() throws SQLException {
        String sql = ""
                + "SELECT COUNT(*) AS total "
                + "FROM dbo.DAT_CHO "
                + "WHERE trang_thai = N'Chờ thanh toán'";

        return queryInt(sql);
    }

    private int countPaidOrdersThisMonth() throws SQLException {
        String sql = ""
                + "SELECT COUNT(*) AS total "
                + "FROM dbo.DAT_CHO "
                + "WHERE trang_thai IN (N'Đã thanh toán', N'Hoàn tiền một phần', N'Hoàn tiền toàn bộ') "
                + "  AND ngay_dat >= DATEFROMPARTS(YEAR(SYSDATETIME()), MONTH(SYSDATETIME()), 1) "
                + "  AND ngay_dat < DATEADD(MONTH, 1, DATEFROMPARTS(YEAR(SYSDATETIME()), MONTH(SYSDATETIME()), 1))";

        return queryInt(sql);
    }

    private int countNewCustomersThisMonth() throws SQLException {
        String sql = ""
                + "SELECT COUNT(*) AS total "
                + "FROM dbo.KHACH_HANG "
                + "WHERE ngay_tao >= DATEFROMPARTS(YEAR(SYSDATETIME()), MONTH(SYSDATETIME()), 1) "
                + "  AND ngay_tao < DATEADD(MONTH, 1, DATEFROMPARTS(YEAR(SYSDATETIME()), MONTH(SYSDATETIME()), 1))";

        return queryInt(sql);
    }

    private int countPendingRefunds() throws SQLException {
        String sql = ""
                + "SELECT COUNT(*) AS total "
                + "FROM dbo.HOAN_TIEN "
                + "WHERE trang_thai = N'Chờ xử lý'";

        return queryInt(sql);
    }

    public List<MonthlyRevenueDTO> getMonthlyRevenue(int numberOfMonths) throws SQLException {
        String sql = ""
                + "WITH Months AS ( "
                + "    SELECT 0 AS n, DATEFROMPARTS(YEAR(SYSDATETIME()), MONTH(SYSDATETIME()), 1) AS month_start "
                + "    UNION ALL "
                + "    SELECT n + 1, DATEADD(MONTH, -1, month_start) "
                + "    FROM Months "
                + "    WHERE n + 1 < ? "
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
                + "    FORMAT(m.month_start, 'MM/yyyy') AS label, "
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

        List<MonthlyRevenueDTO> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, numberOfMonths);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    MonthlyRevenueDTO dto = new MonthlyRevenueDTO();
                    dto.setLabel(rs.getString("label"));
                    dto.setRevenue(rs.getBigDecimal("revenue"));
                    dto.setRefund(rs.getBigDecimal("refund"));
                    dto.setNetRevenue(rs.getBigDecimal("net_revenue"));
                    dto.setTicketCount(rs.getInt("ticket_count"));
                    list.add(dto);
                }
            }
        }

        return list;
    }

    public List<LabelValueDTO> getSeatClassStats() throws SQLException {
        String sql = ""
                + "SELECT "
                + "    COALESCE(toa.loai_toa, N'Không xác định') AS label, "
                + "    COUNT(v.id) AS ticket_count, "
                + "    COALESCE(SUM(v.gia_ve_chi_tiet), 0) AS total_value "
                + "FROM dbo.VE v "
                + "JOIN dbo.GHE g ON g.id = v.ghe_id "
                + "JOIN dbo.TOA_TAU toa ON toa.id = g.toa_tau_id "
                + "WHERE v.trang_thai IN (N'Hợp lệ', N'Đã sử dụng', N'Đã trả') "
                + "GROUP BY toa.loai_toa "
                + "ORDER BY ticket_count DESC";

        return queryLabelValue(sql);
    }

    public List<LabelValueDTO> getPaymentMethodStats() throws SQLException {
        String sql = ""
                + "SELECT "
                + "    COALESCE(phuong_thuc, N'Không xác định') AS label, "
                + "    COUNT(*) AS ticket_count, "
                + "    COALESCE(SUM(so_tien), 0) AS total_value "
                + "FROM dbo.THANH_TOAN "
                + "WHERE trang_thai = N'Thành công' "
                + "GROUP BY phuong_thuc "
                + "ORDER BY total_value DESC";

        return queryLabelValue(sql);
    }

    public List<LabelValueDTO> getTopRoutes(int limit) throws SQLException {
        String sql = ""
                + "SELECT TOP (?) "
                + "    CONCAT(gd.ten_ga, N' → ', ga.ten_ga) AS label, "
                + "    COUNT(v.id) AS ticket_count, "
                + "    COALESCE(SUM(v.gia_ve_chi_tiet), 0) AS total_value "
                + "FROM dbo.VE v "
                + "JOIN dbo.GA gd ON gd.id = v.ga_di_id "
                + "JOIN dbo.GA ga ON ga.id = v.ga_den_id "
                + "WHERE v.trang_thai IN (N'Hợp lệ', N'Đã sử dụng', N'Đã trả') "
                + "GROUP BY gd.ten_ga, ga.ten_ga "
                + "ORDER BY total_value DESC";

        List<LabelValueDTO> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    LabelValueDTO dto = new LabelValueDTO();
                    dto.setLabel(rs.getString("label"));
                    dto.setCount(rs.getInt("ticket_count"));
                    dto.setValue(rs.getBigDecimal("total_value"));
                    list.add(dto);
                }
            }
        }

        return list;
    }

    public List<RecentBookingDTO> getRecentBookings(int limit) throws SQLException {
        String sql = ""
                + "SELECT TOP (?) "
                + "    dc.id, "
                + "    dc.ma_dat_cho, "
                + "    kh.ho_ten AS ten_khach_hang, "
                + "    dc.ngay_dat, "
                + "    dc.tong_thanh_toan, "
                + "    dc.trang_thai "
                + "FROM dbo.DAT_CHO dc "
                + "LEFT JOIN dbo.KHACH_HANG kh ON kh.id = dc.khach_hang_id "
                + "ORDER BY dc.ngay_dat DESC, dc.id DESC";

        List<RecentBookingDTO> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, limit);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RecentBookingDTO dto = new RecentBookingDTO();
                    dto.setId(rs.getInt("id"));
                    dto.setMaDatCho(rs.getString("ma_dat_cho"));
                    dto.setTenKhachHang(rs.getString("ten_khach_hang"));

                    java.sql.Timestamp ngayDat = rs.getTimestamp("ngay_dat");
                    if (ngayDat != null) {
                        dto.setNgayDat(ngayDat.toLocalDateTime());
                    }

                    dto.setTongThanhToan(rs.getBigDecimal("tong_thanh_toan"));
                    dto.setTrangThai(rs.getString("trang_thai"));
                    list.add(dto);
                }
            }
        }

        return list;
    }

    private BigDecimal queryBigDecimal(String sql) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                BigDecimal value = rs.getBigDecimal("total");
                return value == null ? BigDecimal.ZERO : value;
            }

            return BigDecimal.ZERO;
        }
    }

    private int queryInt(String sql) throws SQLException {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            return rs.next() ? rs.getInt("total") : 0;
        }
    }

    private List<LabelValueDTO> queryLabelValue(String sql) throws SQLException {
        List<LabelValueDTO> list = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LabelValueDTO dto = new LabelValueDTO();
                dto.setLabel(rs.getString("label"));
                dto.setCount(rs.getInt("ticket_count"));
                dto.setValue(rs.getBigDecimal("total_value"));
                list.add(dto);
            }
        }

        return list;
    }
}