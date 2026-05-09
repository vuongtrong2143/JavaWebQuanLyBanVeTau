package vetau.service;

import vetau.dao.BaoCaoDAO;
import vetau.dto.DashboardStatsDTO;
import vetau.dto.LabelValueDTO;
import vetau.dto.MonthlyRevenueDTO;
import vetau.dto.RecentBookingDTO;

import java.sql.SQLException;
import java.util.List;

public class BaoCaoService {

    private final BaoCaoDAO baoCaoDAO = new BaoCaoDAO();

    public DashboardStatsDTO getDashboardStats() throws SQLException {
        return baoCaoDAO.getDashboardStats();
    }

    public List<MonthlyRevenueDTO> getMonthlyRevenue(int months) throws SQLException {
        return baoCaoDAO.getMonthlyRevenue(months);
    }

    public List<LabelValueDTO> getSeatClassStats() throws SQLException {
        return baoCaoDAO.getSeatClassStats();
    }

    public List<LabelValueDTO> getPaymentMethodStats() throws SQLException {
        return baoCaoDAO.getPaymentMethodStats();
    }

    public List<LabelValueDTO> getTopRoutes(int limit) throws SQLException {
        return baoCaoDAO.getTopRoutes(limit);
    }

    public List<RecentBookingDTO> getRecentBookings(int limit) throws SQLException {
        return baoCaoDAO.getRecentBookings(limit);
    }
}