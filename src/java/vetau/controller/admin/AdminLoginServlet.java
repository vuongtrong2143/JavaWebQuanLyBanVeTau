package vetau.controller.admin;

import vetau.util.SecurityUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet(name = "AdminLoginServlet", urlPatterns = {"/admin/login", "/admin/logout"})
public class AdminLoginServlet extends HttpServlet {

    private static final String ADMIN_EMAIL = "admin@vetau.vn";
    private static final String ADMIN_PASSWORD = "admin123";
    private static final String ADMIN_DISPLAY_NAME = "Super Admin";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        SecurityUtil.setNoCache(response);

        String path = request.getServletPath();

        if ("/admin/logout".equals(path)) {
            HttpSession session = request.getSession(false);

            if (session != null) {
                session.invalidate();
            }

            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("currentAdmin") != null) {
            response.sendRedirect(request.getContextPath() + "/admin/dashboard");
            return;
        }

        request.setAttribute("returnUrl", request.getParameter("returnUrl"));
        request.getRequestDispatcher("/views/admin/admin-login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        SecurityUtil.setNoCache(response);

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String returnUrl = request.getParameter("returnUrl");

        request.setAttribute("emailValue", email);
        request.setAttribute("returnUrl", returnUrl);

        if (ADMIN_EMAIL.equalsIgnoreCase(email == null ? "" : email.trim())
                && ADMIN_PASSWORD.equals(password == null ? "" : password.trim())) {

            HttpSession session = SecurityUtil.regenerateSession(request);
            session.setAttribute("currentAdmin", ADMIN_DISPLAY_NAME);

            String safeUrl = SecurityUtil.safeReturnUrl(request, returnUrl, "/admin/dashboard");
            response.sendRedirect(safeUrl);
            return;
        }

        request.setAttribute("error", "Email hoặc mật khẩu quản trị không đúng!");
        request.getRequestDispatcher("/views/admin/admin-login.jsp").forward(request, response);
    }
}