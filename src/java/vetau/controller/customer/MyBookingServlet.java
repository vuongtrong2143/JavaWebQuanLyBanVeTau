package vetau.controller.customer;

import vetau.dto.CustomerSessionDTO;
import vetau.service.MyBookingService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "MyBookingServlet", urlPatterns = {"/my-bookings"})
public class MyBookingServlet extends HttpServlet {
    private final MyBookingService myBookingService = new MyBookingService();

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
            request.setAttribute("bookings", myBookingService.layLichSuDatVe(currentCustomer.getId()));
        } catch (Exception ex) {
            request.setAttribute("error", "Không tải được lịch sử đặt vé: " + ex.getMessage());
        }

        request.getRequestDispatcher("/views/customer/my-bookings.jsp").forward(request, response);
    }
}
