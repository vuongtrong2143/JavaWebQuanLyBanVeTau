/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vetau.controller.admin;

import vetau.dao.DatChoDAO;
import vetau.model.DatCho;
import vetau.service.VeService;
import vetau.service.GiuChoCleanupService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminBookingServlet", urlPatterns = {"/admin/bookings"})
public class AdminBookingServlet extends HttpServlet {

    private final DatChoDAO datChoDAO = new DatChoDAO();
    private final VeService veService = new VeService();
    private final GiuChoCleanupService cleanupService = new GiuChoCleanupService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // KIỂM TRA BẢO MẬT ADMIN
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentAdmin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }
        try {
            cleanupService.huyCacDonQuaHan();
        } catch (Exception ex) {
            request.setAttribute("cleanupWarning", "Không thể tự động cập nhật đơn quá hạn: " + ex.getMessage());
        }
        
        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            if ("detail".equals(action)) {
                // Xem chi tiết đơn hàng và danh sách vé
                int id = Integer.parseInt(request.getParameter("id"));
                DatCho datCho = datChoDAO.findById(id);
                request.setAttribute("datCho", datCho);
                
                // Lấy các vé thuộc đơn hàng này thông qua VeService
                request.setAttribute("listVe", veService.layVeTheoDatCho(id));
                
                request.getRequestDispatcher("/views/admin/booking-detail.jsp").forward(request, response);
            } else {
                // Hiển thị danh sách tất cả đơn đặt chỗ bằng hàm findAll() chuẩn
                List<DatCho> listBookings = datChoDAO.findAll();
                request.setAttribute("listBookings", listBookings);
                request.getRequestDispatcher("/views/admin/booking-list.jsp").forward(request, response);
            }
        } catch (Exception ex) {
            response.getWriter().println("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        // KIỂM TRA BẢO MẬT ADMIN
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentAdmin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        String action = request.getParameter("action");
        try {
            if ("cancel".equals(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                // Cập nhật trạng thái thành 'Đã hủy' bằng hàm thực tế trong DatChoDAO
                datChoDAO.updateTrangThai(id, "Đã hủy");
                response.sendRedirect(request.getContextPath() + "/admin/bookings?msg=cancelled");
            }
        } catch (Exception ex) {
            response.getWriter().println("Lỗi xử lý: " + ex.getMessage());
        }
    }
}