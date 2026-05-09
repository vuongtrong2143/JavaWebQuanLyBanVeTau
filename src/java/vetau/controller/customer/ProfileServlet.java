package vetau.controller.customer;

import vetau.dto.CustomerSessionDTO;
import vetau.model.KhachHang;
import vetau.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "ProfileServlet", urlPatterns = {"/profile"})
public class ProfileServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        loadProfile(request);
        request.getRequestDispatcher("/views/customer/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        CustomerSessionDTO currentCustomer = getCurrentCustomer(request);

        String hoTen = request.getParameter("hoTen");
        String soDienThoai = request.getParameter("soDienThoai");
        String ngaySinh = request.getParameter("ngaySinh");
        String gioiTinh = request.getParameter("gioiTinh");
        String diaChi = request.getParameter("diaChi");

        try {
            CustomerSessionDTO updatedSession = authService.capNhatHoSo(
                    currentCustomer.getId(), hoTen, soDienThoai, ngaySinh, gioiTinh, diaChi
            );

            request.getSession().setAttribute("currentCustomer", updatedSession);
            request.setAttribute("success", "Cập nhật hồ sơ thành công.");
        } catch (Exception ex) {
            request.setAttribute("error", ex.getMessage());
        }

        loadProfile(request);
        request.getRequestDispatcher("/views/customer/profile.jsp").forward(request, response);
    }

    private void loadProfile(HttpServletRequest request) throws ServletException {
        CustomerSessionDTO currentCustomer = getCurrentCustomer(request);

        try {
            KhachHang khachHang = authService.layThongTinKhachHang(currentCustomer.getId());
            request.setAttribute("khachHang", khachHang);

            HttpSession session = request.getSession(false);
            if (session != null) {
                Object flash = session.getAttribute("flashMessage");
                if (flash != null) {
                    request.setAttribute("success", flash.toString());
                    session.removeAttribute("flashMessage");
                }
            }
        } catch (Exception ex) {
            request.setAttribute("error", "Không tải được hồ sơ: " + ex.getMessage());
        }
    }

    private CustomerSessionDTO getCurrentCustomer(HttpServletRequest request) throws ServletException {
        HttpSession session = request.getSession(false);
        CustomerSessionDTO currentCustomer = session == null
                ? null
                : (CustomerSessionDTO) session.getAttribute("currentCustomer");

        if (currentCustomer == null) {
            throw new ServletException("Chưa đăng nhập.");
        }

        return currentCustomer;
    }
}
