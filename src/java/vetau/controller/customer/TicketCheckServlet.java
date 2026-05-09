package vetau.controller.customer;

import vetau.dto.TicketCheckResultDTO;
import vetau.service.TicketCheckService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "TicketCheckServlet", urlPatterns = {"/ticket-check"})
public class TicketCheckServlet extends HttpServlet {
    private final TicketCheckService ticketCheckService = new TicketCheckService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/customer/ticket-check.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String maVe = request.getParameter("maVe");
        String soGiayTo = request.getParameter("soGiayTo");

        request.setAttribute("maVeValue", maVe);
        request.setAttribute("soGiayToValue", soGiayTo);

        try {
            TicketCheckResultDTO result = ticketCheckService.traCuuVe(maVe, soGiayTo);
            request.setAttribute("ticketResult", result);
        } catch (IllegalArgumentException ex) {
            request.setAttribute("message", ex.getMessage());
        } catch (Exception ex) {
            request.setAttribute("message", "Có lỗi khi tra cứu vé: " + ex.getMessage());
        }

        request.getRequestDispatcher("/views/customer/ticket-check.jsp").forward(request, response);
    }
}
