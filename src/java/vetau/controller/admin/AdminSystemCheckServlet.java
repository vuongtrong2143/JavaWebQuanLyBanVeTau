package vetau.controller.admin;

import vetau.dao.SystemCheckDAO;
import vetau.dto.SystemCheckItemDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminSystemCheckServlet", urlPatterns = {"/admin/system-check"})
public class AdminSystemCheckServlet extends HttpServlet {

    private final SystemCheckDAO systemCheckDAO = new SystemCheckDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("currentAdmin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        try {
            List<SystemCheckItemDTO> checks = systemCheckDAO.runAllChecks();
            Map<String, Integer> summaryCounts = systemCheckDAO.getSummaryCounts();

            int okCount = 0;
            int warningCount = 0;
            int errorCount = 0;

            for (SystemCheckItemDTO item : checks) {
                if (item.isOk()) {
                    okCount++;
                } else if (item.isWarning()) {
                    warningCount++;
                } else if (item.isError()) {
                    errorCount++;
                }
            }

            request.setAttribute("checks", checks);
            request.setAttribute("summaryCounts", summaryCounts);
            request.setAttribute("okCount", okCount);
            request.setAttribute("warningCount", warningCount);
            request.setAttribute("errorCount", errorCount);

        } catch (Exception ex) {
            request.setAttribute("error", "Không thể kiểm tra hệ thống: " + ex.getMessage());
        }

        request.getRequestDispatcher("/views/admin/system-check.jsp").forward(request, response);
    }
}