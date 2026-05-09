package vetau.controller.customer;

import vetau.dto.CustomerSessionDTO;
import vetau.service.AuthService;
import vetau.util.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SecurityUtil.setNoCache(response);

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("currentCustomer") != null) {
            response.sendRedirect(request.getContextPath() + "/profile");
            return;
        }

        String message = request.getParameter("message");

        if ("login-required".equals(message)) {
            request.setAttribute("message", "Vui lòng đăng nhập để tiếp tục.");
        }

        request.setAttribute("returnUrl", request.getParameter("returnUrl"));
        request.getRequestDispatcher("/views/customer/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        SecurityUtil.setNoCache(response);

        String email = request.getParameter("email");
        String matKhau = request.getParameter("matKhau");
        String returnUrl = request.getParameter("returnUrl");

        request.setAttribute("emailValue", email);
        request.setAttribute("returnUrl", returnUrl);

        try {
            CustomerSessionDTO customer = authService.dangNhapKhachHang(email, matKhau);

            HttpSession session = SecurityUtil.regenerateSession(request);
            session.setAttribute("currentCustomer", customer);

            String safeUrl = SecurityUtil.safeReturnUrl(request, returnUrl, "/profile");
            response.sendRedirect(safeUrl);

        } catch (Exception ex) {
            request.setAttribute("error", ex.getMessage());
            request.getRequestDispatcher("/views/customer/login.jsp").forward(request, response);
        }
    }
}