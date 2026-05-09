package vetau.controller.customer;

import vetau.dto.CustomerSessionDTO;
import vetau.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "ChangePasswordServlet", urlPatterns = {"/change-password"})
public class ChangePasswordServlet extends HttpServlet {
    private final AuthService authService = new AuthService();

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
            authService.doiMatKhau(
                    currentCustomer.getId(),
                    request.getParameter("matKhauCu"),
                    request.getParameter("matKhauMoi"),
                    request.getParameter("xacNhanMatKhau")
            );

            session.setAttribute("flashMessage", "Đổi mật khẩu thành công.");
        } catch (Exception ex) {
            session.setAttribute("flashMessage", "Đổi mật khẩu thất bại: " + ex.getMessage());
        }

        response.sendRedirect(request.getContextPath() + "/profile");
    }
}
