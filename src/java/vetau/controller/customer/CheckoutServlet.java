/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vetau.controller.customer;

import vetau.dao.DatChoDAO;
import vetau.dto.CustomerSessionDTO;
import vetau.model.DatCho;
import vetau.service.GiuChoCleanupService;
import vetau.util.TrangThai;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "CheckoutServlet", urlPatterns = {"/checkout"})
public class CheckoutServlet extends HttpServlet {

    private final DatChoDAO datChoDAO = new DatChoDAO();
    private final GiuChoCleanupService cleanupService = new GiuChoCleanupService();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        CustomerSessionDTO currentCustomer = session == null
                ? null
                : (CustomerSessionDTO) session.getAttribute("currentCustomer");

        if (currentCustomer == null) {
            response.sendRedirect(request.getContextPath() + "/login?message=login-required");
            return;
        }

        try {
            int datChoId = Integer.parseInt(request.getParameter("datChoId"));
            cleanupService.huyDonQuaHan(datChoId);
            DatCho datCho = datChoDAO.findById(datChoId);

            if (datCho == null || datCho.getKhachHangId() != currentCustomer.getId()) {
                throw new IllegalArgumentException("Đơn đặt chỗ không tồn tại hoặc không thuộc quyền sở hữu của bạn.");
            }

            if (TrangThai.DAT_CHO_HET_HAN.equals(datCho.getTrangThai())) {
                throw new IllegalStateException("Đơn đặt chỗ đã hết hạn thanh toán. Vui lòng đặt vé lại.");
            }

            if (!TrangThai.DAT_CHO_CHO_THANH_TOAN.equals(datCho.getTrangThai())) {
                throw new IllegalStateException("Đơn đặt chỗ này không ở trạng thái chờ thanh toán.");
            }

            request.setAttribute("datCho", datCho);
            request.getRequestDispatcher("/views/customer/checkout.jsp").forward(request, response);

        } catch (Exception ex) {
            String msg = ex.getMessage() == null ? "" : ex.getMessage();

            if (msg.contains("hết hạn")) {
                response.sendRedirect(request.getContextPath() + "/my-bookings?error=checkout-expired");
            } else {
                response.sendRedirect(request.getContextPath() + "/my-bookings?error=checkout");
            }
            return;
        }
    }
}