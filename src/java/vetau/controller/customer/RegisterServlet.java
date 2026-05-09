package vetau.controller.customer;

import vetau.dto.CustomerSessionDTO;
import vetau.service.AuthService;
import vetau.util.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/customer/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String hoTen = request.getParameter("hoTen");
        String email = request.getParameter("email");
        String soDienThoai = request.getParameter("soDienThoai");
        String matKhau = request.getParameter("matKhau");
        String xacNhanMatKhau = request.getParameter("xacNhanMatKhau");

        request.setAttribute("hoTenValue", hoTen);
        request.setAttribute("emailValue", email);
        request.setAttribute("soDienThoaiValue", soDienThoai);

        try {
            CustomerSessionDTO customer = authService.dangKyKhachHang(
                    hoTen, email, soDienThoai, matKhau, xacNhanMatKhau
            );

            HttpSession session = SecurityUtil.regenerateSession(request);
            session.setAttribute("currentCustomer", customer);
            session.setAttribute("flashMessage", "Đăng ký tài khoản thành công.");

            response.sendRedirect(request.getContextPath() + "/profile");
        } catch (Exception ex) {
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("/views/customer/register.jsp").forward(request, response);
        }
    }
}
