/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vetau.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class FlashMessageUtil {

    public static final String FLASH_TYPE = "flashType";
    public static final String FLASH_MESSAGE = "flashMessage";

    private FlashMessageUtil() {
    }

    public static void success(HttpServletRequest request, String message) {
        set(request, "success", message);
    }

    public static void error(HttpServletRequest request, String message) {
        set(request, "error", message);
    }

    public static void warning(HttpServletRequest request, String message) {
        set(request, "warning", message);
    }

    public static void info(HttpServletRequest request, String message) {
        set(request, "info", message);
    }

    public static void set(HttpServletRequest request, String type, String message) {
        if (request == null || message == null || message.trim().isEmpty()) {
            return;
        }

        HttpSession session = request.getSession(true);
        session.setAttribute(FLASH_TYPE, type);
        session.setAttribute(FLASH_MESSAGE, message);
    }

    public static void clear(HttpServletRequest request) {
        if (request == null) {
            return;
        }

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.removeAttribute(FLASH_TYPE);
            session.removeAttribute(FLASH_MESSAGE);
        }
    }
}