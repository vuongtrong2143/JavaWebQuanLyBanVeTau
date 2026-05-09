package vetau.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public final class SecurityUtil {

    private SecurityUtil() {
    }

    public static String safeReturnUrl(HttpServletRequest request, String rawReturnUrl, String defaultPath) {
        String contextPath = request.getContextPath();

        if (isBlank(rawReturnUrl)) {
            return contextPath + defaultPath;
        }

        String returnUrl = rawReturnUrl.trim();
        String lower = returnUrl.toLowerCase();

        /*
         * Chặn redirect ra domain ngoài.
         * Ví dụ:
         * https://evil.com
         * http://evil.com
         * //evil.com
         */
        if (lower.startsWith("http://")
                || lower.startsWith("https://")
                || lower.startsWith("//")) {
            return contextPath + defaultPath;
        }

        /*
         * URL đã có contextPath.
         * Ví dụ: /WEB_QUANLYVETAU/profile
         */
        if (returnUrl.startsWith(contextPath + "/")) {
            return returnUrl;
        }

        /*
         * URL nội bộ.
         * Ví dụ: /profile, /my-bookings
         */
        if (returnUrl.startsWith("/")) {
            return contextPath + returnUrl;
        }

        return contextPath + defaultPath;
    }

    public static HttpSession regenerateSession(HttpServletRequest request) {
        HttpSession oldSession = request.getSession(false);

        if (oldSession != null) {
            oldSession.invalidate();
        }

        return request.getSession(true);
    }

    public static void setNoCache(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }

    public static String clientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");

        if (!isBlank(forwarded)) {
            return forwarded.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}