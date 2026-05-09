package vetau.controller.customer;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import vetau.dto.CustomerSessionDTO;
import vetau.service.ReturnTicketService;
import vetau.service.ReturnTicketService.ReturnPreview;
import vetau.util.FlashMessageUtil;

@WebServlet(name = "ReturnTicketServlet", urlPatterns = {"/return-ticket"})
public class ReturnTicketServlet extends HttpServlet {

    private final ReturnTicketService returnTicketService = new ReturnTicketService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CustomerSessionDTO currentCustomer = getCurrentCustomer(request, response);
        if (currentCustomer == null) {
            return;
        }

        try {
            String maVe = request.getParameter("maVe");
            ReturnPreview preview = returnTicketService.xemTruocTraVe(currentCustomer.getId(), maVe);

            request.setAttribute("ticketResult", preview.getTicket());
            request.setAttribute("ve", preview.getTicket());
            request.setAttribute("chinhSach", preview.getChinhSach());
            request.setAttribute("soTienHoan", preview.getSoTienHoan());
            request.setAttribute("phiKhauTru", preview.getPhiKhauTru());
            request.setAttribute("soTienHoanText", formatMoney(preview.getSoTienHoan()));
            request.setAttribute("phiKhauTruText", formatMoney(preview.getPhiKhauTru()));

            request.getRequestDispatcher("/views/customer/return-ticket.jsp").forward(request, response);

        } catch (Exception ex) {
            request.setAttribute("error", "Không thể trả vé: " + ex.getMessage());
            request.getRequestDispatcher("/views/customer/ticket-check.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        CustomerSessionDTO currentCustomer = getCurrentCustomer(request, response);
        if (currentCustomer == null) {
            return;
        }

        try {
            String maVe = request.getParameter("maVe");
            String lyDo = request.getParameter("lyDo");

            returnTicketService.xacNhanTraVe(currentCustomer.getId(), maVe, lyDo);
            FlashMessageUtil.success(
                request,
                "Yêu cầu trả vé đã được ghi nhận. Admin sẽ xử lý hoàn tiền theo quy định."
        );
            response.sendRedirect(request.getContextPath() + "/my-bookings");

        } catch (Exception ex) {
            request.setAttribute("error", "Không thể xác nhận trả vé: " + ex.getMessage());
            request.getRequestDispatcher("/views/customer/ticket-check.jsp").forward(request, response);
        }
    }

    private CustomerSessionDTO getCurrentCustomer(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        CustomerSessionDTO currentCustomer = session == null
                ? null
                : (CustomerSessionDTO) session.getAttribute("currentCustomer");

        if (currentCustomer == null) {
            response.sendRedirect(request.getContextPath() + "/login?message=login-required");
            return null;
        }

        return currentCustomer;
    }

    private String formatMoney(java.math.BigDecimal value) {
        if (value == null) {
            return "0 đ";
        }
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        return nf.format(value) + " đ";
    }
}