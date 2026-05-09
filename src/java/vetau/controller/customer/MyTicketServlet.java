/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vetau.controller.customer;

import vetau.dao.DatChoDAO;
import vetau.dto.CustomerSessionDTO;
import vetau.dto.TicketCheckResultDTO;
import vetau.model.DatCho;
import vetau.model.Ve;
import vetau.service.TicketCheckService;
import vetau.service.VeService;
import vetau.service.GiuChoCleanupService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Đã được bảo vệ bởi AuthFilter vì nằm trong url pattern /my-tickets/*
@WebServlet(name = "MyTicketServlet", urlPatterns = {"/my-tickets"})
public class MyTicketServlet extends HttpServlet {

    private final DatChoDAO datChoDAO = new DatChoDAO();
    private final VeService veService = new VeService();
    private final TicketCheckService ticketCheckService = new TicketCheckService();
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

            // Kiểm tra bảo mật: Không cho phép xem vé của người khác
            if (datCho == null || datCho.getKhachHangId() != currentCustomer.getId()) {
                throw new IllegalArgumentException("Không tìm thấy đơn đặt chỗ hoặc bạn không có quyền truy cập.");
            }

            // Lấy danh sách Vé thuộc đơn này
            List<Ve> veList = veService.layVeTheoDatCho(datChoId);
            List<TicketCheckResultDTO> ticketDetails = new ArrayList<>();

            // Dùng TicketCheckService để lấy chi tiết full thông tin (Ga, Tàu, Ngày giờ)
            for (Ve ve : veList) {
                // Truyền null cho soGiayTo vì người dùng đang đăng nhập chính chủ, không cần check CCCD
                TicketCheckResultDTO detail = ticketCheckService.traCuuVe(ve.getMaVe(), null);
                if (detail != null && detail.isFound()) {
                    ticketDetails.add(detail);
                }
            }

            request.setAttribute("datCho", datCho);
            request.setAttribute("ticketDetails", ticketDetails);
            request.getRequestDispatcher("/views/customer/my-tickets.jsp").forward(request, response);

        } catch (Exception ex) {
            response.sendRedirect(request.getContextPath() + "/my-bookings?error=checkout");
        }
    }
}