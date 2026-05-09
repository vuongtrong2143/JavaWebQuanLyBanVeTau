package vetau.filter;

import vetau.util.SecurityUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AdminAuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Không cần cấu hình thêm.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        SecurityUtil.setNoCache(res);

        String uri = req.getRequestURI();

        if (uri.endsWith("/admin/login") || uri.endsWith("/admin/logout")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        boolean loggedIn = session != null && session.getAttribute("currentAdmin") != null;

        if (loggedIn) {
            chain.doFilter(request, response);
            return;
        }

        String returnUrl = req.getRequestURI();

        if (req.getQueryString() != null) {
            returnUrl += "?" + req.getQueryString();
        }

        String encoded = URLEncoder.encode(returnUrl, StandardCharsets.UTF_8.name());

        res.sendRedirect(req.getContextPath() + "/admin/login?returnUrl=" + encoded);
    }

    @Override
    public void destroy() {
        // Không cần giải phóng tài nguyên.
    }
}