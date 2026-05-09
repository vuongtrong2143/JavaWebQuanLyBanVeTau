package vetau.controller.admin;

import vetau.dto.LabelValueDTO;
import vetau.dto.MonthlyRevenueDTO;
import vetau.service.BaoCaoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@WebServlet(name = "AdminReportServlet", urlPatterns = {"/admin/reports"})
public class AdminReportServlet extends HttpServlet {

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
            List<MonthlyRevenueDTO> monthlyRevenue = baoCaoService.getMonthlyRevenue(6);
            List<LabelValueDTO> seatClassStats = baoCaoService.getSeatClassStats();
            List<LabelValueDTO> paymentMethodStats = baoCaoService.getPaymentMethodStats();
            List<LabelValueDTO> topRoutes = baoCaoService.getTopRoutes(5);

            request.setAttribute("monthlyRevenue", monthlyRevenue);
            request.setAttribute("seatClassStats", seatClassStats);
            request.setAttribute("paymentMethodStats", paymentMethodStats);
            request.setAttribute("topRoutes", topRoutes);

            /*
             * Giữ attribute cũ để report-revenue.jsp hiện tại không vỡ.
             */
            request.setAttribute("chartMonths", toJsStringArrayFromMonthly(monthlyRevenue));
            request.setAttribute("chartRevenues", toJsNumberArrayFromMonthlyRevenue(monthlyRevenue));
            request.setAttribute("chartSeatClasses", toJsStringArrayFromLabel(seatClassStats));
            request.setAttribute("chartTicketCounts", toJsNumberArrayFromCount(seatClassStats));

            BigDecimal totalRevenue = BigDecimal.ZERO;
            int totalTickets = 0;

            for (MonthlyRevenueDTO item : monthlyRevenue) {
                if (item.getNetRevenue() != null) {
                    totalRevenue = totalRevenue.add(item.getNetRevenue());
                }

                totalTickets += item.getTicketCount();
            }

            request.setAttribute("totalYearRevenue", totalRevenue);
            request.setAttribute("totalYearTickets", totalTickets);

        } catch (Exception ex) {
            request.setAttribute("reportWarning",
                    "Không tải được dữ liệu báo cáo từ database: " + ex.getMessage());

            request.setAttribute("chartMonths", "[]");
            request.setAttribute("chartRevenues", "[]");
            request.setAttribute("chartSeatClasses", "[]");
            request.setAttribute("chartTicketCounts", "[]");
            request.setAttribute("totalYearRevenue", BigDecimal.ZERO);
            request.setAttribute("totalYearTickets", 0);
        }

        request.getRequestDispatcher("/views/admin/report-revenue.jsp").forward(request, response);
    }

    private String toJsStringArrayFromMonthly(List<MonthlyRevenueDTO> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append("'").append(escapeJs(list.get(i).getLabel())).append("'");
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private String toJsNumberArrayFromMonthlyRevenue(List<MonthlyRevenueDTO> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            BigDecimal value = list.get(i).getNetRevenue();
            sb.append(value == null ? "0" : value.toPlainString());
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private String toJsStringArrayFromLabel(List<LabelValueDTO> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append("'").append(escapeJs(list.get(i).getLabel())).append("'");
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private String toJsNumberArrayFromCount(List<LabelValueDTO> list) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            sb.append(list.get(i).getCount());
            if (i < list.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    private String escapeJs(String input) {
        if (input == null) {
            return "";
        }

        return input.replace("\\", "\\\\").replace("'", "\\'");
    }
}