package vetau.controller.admin;

import vetau.dao.AdminExportDAO;
import vetau.dao.AdminExportDAO.ExportData;
import vetau.util.CsvUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(name = "AdminExportServlet", urlPatterns = {"/admin/export"})
public class AdminExportServlet extends HttpServlet {

    private final AdminExportDAO adminExportDAO = new AdminExportDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("currentAdmin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        String type = request.getParameter("type");

        if (type == null || type.trim().isEmpty()) {
            type = "revenue";
        }

        try {
            ExportData data;
            String filePrefix;

            switch (type) {
                case "bookings":
                    data = adminExportDAO.exportBookings();
                    filePrefix = "don_dat_cho";
                    break;

                case "refunds":
                    data = adminExportDAO.exportRefunds();
                    filePrefix = "hoan_tien";
                    break;

                case "tickets":
                    data = adminExportDAO.exportTickets();
                    filePrefix = "ve";
                    break;

                case "revenue":
                default:
                    data = adminExportDAO.exportMonthlyRevenue();
                    filePrefix = "bao_cao_doanh_thu";
                    break;
            }

            String csv = CsvUtil.toCsv(data.getHeaders(), data.getRows());
            String fileName = CsvUtil.safeFileName(
                    filePrefix + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv"
            );

            writeCsv(response, fileName, csv);

        } catch (Exception ex) {
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write("Không thể xuất file: " + ex.getMessage());
        }
    }

    private void writeCsv(HttpServletResponse response, String fileName, String csv)
            throws IOException {
        byte[] bytes = csv.getBytes(StandardCharsets.UTF_8);

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentLength(bytes.length);

        response.getOutputStream().write(bytes);
        response.getOutputStream().flush();
    }
}