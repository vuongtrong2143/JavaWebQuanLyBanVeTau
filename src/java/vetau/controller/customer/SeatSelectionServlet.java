package vetau.controller.customer;

import vetau.dto.SeatSelectionPageDTO;
import vetau.service.ChonGheService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SeatSelectionServlet", urlPatterns = {"/seat-selection"})
public class SeatSelectionServlet extends HttpServlet {
    private final ChonGheService chonGheService = new ChonGheService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int chuyenTauId = parseRequiredInt(request.getParameter("chuyenTauId"), "Thiếu mã chuyến tàu.");
            int gaDiId = parseRequiredInt(request.getParameter("gaDiId"), "Thiếu ga đi.");
            int gaDenId = parseRequiredInt(request.getParameter("gaDenId"), "Thiếu ga đến.");
            Integer toaTauId = parseOptionalInt(request.getParameter("toaTauId"));

            SeatSelectionPageDTO pageDTO = chonGheService.loadSeatSelectionPage(chuyenTauId, gaDiId, gaDenId, toaTauId);
            request.setAttribute("pageData", pageDTO);
        } catch (Exception ex) {
            request.setAttribute("message", "Không thể hiển thị sơ đồ ghế: " + ex.getMessage());
        }

        request.getRequestDispatcher("/views/customer/seat-selection.jsp").forward(request, response);
    }

    private int parseRequiredInt(String rawValue, String errorMessage) {
        if (rawValue == null || rawValue.isBlank()) {
            throw new IllegalArgumentException(errorMessage);
        }
        try {
            return Integer.parseInt(rawValue);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private Integer parseOptionalInt(String rawValue) {
        if (rawValue == null || rawValue.isBlank()) {
            return null;
        }
        try {
            return Integer.parseInt(rawValue);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
