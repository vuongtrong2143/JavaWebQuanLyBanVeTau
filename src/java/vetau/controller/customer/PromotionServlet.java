package vetau.controller.customer;

import vetau.service.PublicInfoService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "PromotionServlet", urlPatterns = {"/promotion"})
public class PromotionServlet extends HttpServlet {
    private final PublicInfoService publicInfoService = new PublicInfoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setAttribute("promotions", publicInfoService.layKhuyenMaiDangHoatDong());
        } catch (Exception ex) {
            request.setAttribute("message", "Chưa tải được danh sách khuyến mãi: " + ex.getMessage());
        }

        request.getRequestDispatcher("/views/customer/promotion.jsp").forward(request, response);
    }
}
