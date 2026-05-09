package vetau.controller.customer;

import vetau.service.ThanhToanService;
import vetau.service.ThanhToanService.CallbackResult;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PaymentCallbackServlet", urlPatterns = {"/payment-callback"})
public class PaymentCallbackServlet extends HttpServlet {

    private final ThanhToanService thanhToanService = new ThanhToanService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String requestId = request.getParameter("requestId");
        String status = request.getParameter("status");

        try {
            if ("success".equals(status)) {
                String mockMaGiaoDich = "MOCK_TXN_" + System.currentTimeMillis();

                CallbackResult result = thanhToanService.xuLyCallbackThanhCong(requestId, mockMaGiaoDich);

                switch (result) {
                    case NEWLY_SUCCESS:
                        request.setAttribute("message", "Thanh toán thành công! Vé điện tử của bạn đã được kích hoạt.");
                        request.setAttribute("success", true);
                        break;

                    case ALREADY_SUCCESS:
                        request.setAttribute("message", "Giao dịch này đã được xử lý thành công trước đó. Vé của bạn vẫn hợp lệ.");
                        request.setAttribute("success", true);
                        break;

                    case EXPIRED:
                        request.setAttribute("message", "Đơn đặt chỗ đã hết hạn thanh toán. Giao dịch không được kích hoạt và vé đã bị hủy.");
                        request.setAttribute("success", false);
                        break;

                    case NOT_FOUND:
                        request.setAttribute("message", "Không tìm thấy giao dịch thanh toán.");
                        request.setAttribute("success", false);
                        break;

                    case INVALID_STATE:
                    default:
                        request.setAttribute("message", "Giao dịch không ở trạng thái có thể xác nhận thanh toán.");
                        request.setAttribute("success", false);
                        break;
                }
            } else {
                thanhToanService.xuLyCallbackThatBai(requestId);
                request.setAttribute("message", "Thanh toán thất bại hoặc đã bị hủy. Bạn có thể thử thanh toán lại nếu đơn còn hạn.");
                request.setAttribute("success", false);
            }

            request.getRequestDispatcher("/views/customer/payment-result.jsp").forward(request, response);

        } catch (Exception ex) {
            request.setAttribute("message", "Lỗi xử lý giao dịch: " + ex.getMessage());
            request.setAttribute("success", false);
            request.getRequestDispatcher("/views/customer/payment-result.jsp").forward(request, response);
        }
    }
}