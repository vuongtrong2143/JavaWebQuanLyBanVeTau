package vetau.controller.admin;

import vetau.dto.DashboardStatsDTO;
import vetau.dto.RecentBookingDTO;
import vetau.service.BaoCaoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminDashboardServlet", urlPatterns = {"/admin", "/admin/dashboard"})
public class AdminDashboardServlet extends HttpServlet {

    private final BaoCaoService baoCaoService = new BaoCaoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("currentAdmin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        try {
            DashboardStatsDTO stats = baoCaoService.getDashboardStats();
            List<RecentBookingDTO> recentBookings = baoCaoService.getRecentBookings(8);

            request.setAttribute("stats", stats);
            request.setAttribute("recentBookings", recentBookings);

            /*
             * Giữ lại attribute cũ để dashboard.jsp hiện tại không vỡ.
             */
            request.setAttribute("totalRevenue", stats.getNetRevenueThisMonthText());
            request.setAttribute("totalTickets", stats.getTicketsThisMonth());
            request.setAttribute("newUsers", stats.getNewCustomersThisMonth());
            request.setAttribute("pendingRefunds", stats.getPendingRefunds());

        } catch (Exception ex) {
            request.setAttribute("dashboardWarning",
                    "Không tải được dữ liệu dashboard từ database: " + ex.getMessage());

            /*
             * Fallback để JSP không lỗi null.
             */
            request.setAttribute("totalRevenue", "0 đ");
            request.setAttribute("totalTickets", 0);
            request.setAttribute("newUsers", 0);
            request.setAttribute("pendingRefunds", 0);
        }

        request.getRequestDispatcher("/views/admin/dashboard.jsp").forward(request, response);
    }
}