package vetau.controller.customer;

import vetau.dao.DatChoDAO;
import vetau.dao.ThanhToanDAO;
import vetau.dto.CustomerSessionDTO;
import vetau.model.DatCho;
import vetau.model.ThanhToan;
import vetau.service.ThanhToanService;
import vetau.util.TrangThai;
import vetau.service.GiuChoCleanupService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "PaymentServlet", urlPatterns = {"/payment"})
public class PaymentServlet extends HttpServlet {

    private final ThanhToanService thanhToanService = new ThanhToanService();
    private final ThanhToanDAO thanhToanDAO = new ThanhToanDAO();
    private final DatChoDAO datChoDAO = new DatChoDAO();
    private final GiuChoCleanupService cleanupService = new GiuChoCleanupService();
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

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
            String phuongThuc = request.getParameter("phuongThuc");
            cleanupService.huyDonQuaHan(datChoId);
            
            DatCho datCho = datChoDAO.findById(datChoId);
            if (datCho == null || datCho.getKhachHangId() != currentCustomer.getId()) {
                throw new IllegalArgumentException("Đơn đặt chỗ không tồn tại hoặc không thuộc quyền sở hữu của bạn.");
            }
            if (TrangThai.DAT_CHO_HET_HAN.equals(datCho.getTrangThai())) {
                throw new IllegalStateException("Đơn đặt chỗ đã hết hạn thanh toán. Vui lòng đặt vé lại.");
            }

            if (!TrangThai.DAT_CHO_CHO_THANH_TOAN.equals(datCho.getTrangThai())) {
                throw new IllegalStateException("Đơn đặt chỗ không ở trạng thái chờ thanh toán.");
            }

            if (datCho.getThoiGianHetHan() != null
                    && datCho.getThoiGianHetHan().isBefore(LocalDateTime.now())) {
                throw new IllegalStateException("Đơn đặt chỗ đã hết hạn thanh toán.");
            }

            int thanhToanId = thanhToanService.taoThanhToanPending(
                    datChoId,
                    phuongThuc,
                    datCho.getTongThanhToan()
            );

            ThanhToan tt = thanhToanDAO.findById(thanhToanId);

            /*
             * Demo giả lập callback thành công.
             * Khi tích hợp VNPay/MoMo thật thì thay đoạn này bằng redirect tới URL thanh toán.
             */
            String callbackUrl = request.getContextPath()
                    + "/payment-callback?requestId="
                    + tt.getRequestId()
                    + "&status=success";

            response.sendRedirect(callbackUrl);

        } catch (Exception ex) {
            request.setAttribute("error", "Lỗi khởi tạo thanh toán: " + ex.getMessage());
            request.getRequestDispatcher("/views/customer/my-bookings.jsp").forward(request, response);
        }
    }
}