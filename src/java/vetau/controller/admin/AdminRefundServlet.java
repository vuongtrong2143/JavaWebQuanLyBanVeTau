package vetau.controller.admin;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import vetau.dao.HoanTienDAO;
import vetau.dto.RefundAdminDTO;
import vetau.service.AdminRefundService;
import vetau.util.FlashMessageUtil;

@WebServlet(name = "AdminRefundServlet", urlPatterns = {"/admin/refunds"})
public class AdminRefundServlet extends HttpServlet {

    private final HoanTienDAO hoanTienDAO = new HoanTienDAO();
    private final AdminRefundService adminRefundService = new AdminRefundService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("currentAdmin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null || action.trim().isEmpty()) {
            action = "list";
        }

        try {
            if ("detail".equals(action)) {
                showDetail(request, response);
                return;
            }

            showList(request, response);

        } catch (Exception ex) {
            request.setAttribute("error", "Lỗi trang hoàn tiền: " + ex.getMessage());
            request.getRequestDispatcher("/views/admin/refund-list.jsp").forward(request, response);
        }
    }

    private void showList(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String status = request.getParameter("status");

        List<RefundAdminDTO> refunds = hoanTienDAO.findAdminRefunds(status);

        request.setAttribute("refunds", refunds);
        request.setAttribute("statusValue", status);
        request.getRequestDispatcher("/views/admin/refund-list.jsp").forward(request, response);
    }

    private void showDetail(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        int id = Integer.parseInt(request.getParameter("id"));

        RefundAdminDTO refund = hoanTienDAO.findAdminRefundById(id);

        if (refund == null) {
            throw new IllegalArgumentException("Không tìm thấy yêu cầu hoàn tiền.");
        }

        request.setAttribute("refund", refund);
        request.getRequestDispatcher("/views/admin/refund-detail.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("currentAdmin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        try {
            String action = request.getParameter("action");
            int id = Integer.parseInt(request.getParameter("id"));

            if ("approve".equals(action)) {
                adminRefundService.duyetHoanTien(id);

                FlashMessageUtil.success(request, "Đã duyệt hoàn tiền thành công.");

                response.sendRedirect(request.getContextPath() + "/admin/refunds");
                return;
            }

            if ("reject".equals(action)) {
                adminRefundService.tuChoiHoanTien(id);

                FlashMessageUtil.warning(request, "Đã từ chối yêu cầu hoàn tiền.");

                response.sendRedirect(request.getContextPath() + "/admin/refunds");
                return;
            }

            throw new IllegalArgumentException("Hành động không hợp lệ.");

        } catch (Exception ex) {
            FlashMessageUtil.error(request, "Lỗi xử lý hoàn tiền: " + ex.getMessage());

            response.sendRedirect(request.getContextPath() + "/admin/refunds");
        }
    }
}