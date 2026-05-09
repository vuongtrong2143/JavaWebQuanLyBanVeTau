package vetau.filter;

import vetau.util.SecurityUtil;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Không cần cấu hình thêm.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        SecurityUtil.setNoCache(httpResponse);

        HttpSession session = httpRequest.getSession(false);
        boolean loggedIn = session != null && session.getAttribute("currentCustomer") != null;

        if (loggedIn) {
            chain.doFilter(request, response);
            return;
        }

        String fullUrl = httpRequest.getRequestURI();

        if (httpRequest.getQueryString() != null) {
            fullUrl += "?" + httpRequest.getQueryString();
        }

        String returnUrl = URLEncoder.encode(fullUrl, StandardCharsets.UTF_8.name());

        httpResponse.sendRedirect(
                httpRequest.getContextPath()
                        + "/login?message=login-required&returnUrl="
                        + returnUrl
        );
    }

    @Override
    public void destroy() {
        // Không cần giải phóng tài nguyên.
    }
}