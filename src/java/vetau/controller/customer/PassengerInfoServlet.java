/*
package vetau.controller.customer;

import vetau.dto.GheTrangThaiDTO;
import vetau.dto.SeatSelectionPageDTO;
import vetau.service.ChonGheService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PassengerInfoServlet", urlPatterns = {"/passenger-info"})
public class PassengerInfoServlet extends HttpServlet {
    private final ChonGheService chonGheService = new ChonGheService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            int chuyenTauId = parseRequiredInt(request.getParameter("chuyenTauId"), "Thiếu mã chuyến tàu.");
            int gaDiId = parseRequiredInt(request.getParameter("gaDiId"), "Thiếu ga đi.");
            int gaDenId = parseRequiredInt(request.getParameter("gaDenId"), "Thiếu ga đến.");
            int toaTauId = parseRequiredInt(request.getParameter("toaTauId"), "Thiếu toa tàu.");
            int gheId = parseRequiredInt(request.getParameter("gheId"), "Vui lòng chọn ghế.");

            GheTrangThaiDTO selectedSeat = chonGheService.validateSelectedSeat(chuyenTauId, gaDiId, gaDenId, toaTauId, gheId);
            SeatSelectionPageDTO pageDTO = chonGheService.loadSeatSelectionPage(chuyenTauId, gaDiId, gaDenId, toaTauId);

            request.setAttribute("pageData", pageDTO);
            request.setAttribute("selectedSeat", selectedSeat);
            request.setAttribute("chuyenTauId", chuyenTauId);
            request.setAttribute("gaDiId", gaDiId);
            request.setAttribute("gaDenId", gaDenId);
            request.setAttribute("toaTauId", toaTauId);
            request.setAttribute("gheId", gheId);

            request.getRequestDispatcher("/views/customer/passenger-info.jsp").forward(request, response);
        } catch (Exception ex) {
            request.setAttribute("message", "Không thể chuyển sang bước nhập hành khách: " + ex.getMessage());
            request.getRequestDispatcher("/views/customer/seat-selection.jsp").forward(request, response);
        }
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
}
*/
package vetau.controller.customer;

import vetau.dto.GheTrangThaiDTO;
import vetau.dto.SeatSelectionPageDTO;
import vetau.service.ChonGheService;
import vetau.service.UuDaiService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PassengerInfoServlet", urlPatterns = {"/passenger-info"})
public class PassengerInfoServlet extends HttpServlet {
    private final ChonGheService chonGheService = new ChonGheService();
    private final UuDaiService uuDaiService = new UuDaiService(); // Bổ sung service ưu đãi

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            int chuyenTauId = parseRequiredInt(request.getParameter("chuyenTauId"), "Thiếu mã chuyến tàu.");
            int gaDiId = parseRequiredInt(request.getParameter("gaDiId"), "Thiếu ga đi.");
            int gaDenId = parseRequiredInt(request.getParameter("gaDenId"), "Thiếu ga đến.");
            int toaTauId = parseRequiredInt(request.getParameter("toaTauId"), "Thiếu toa tàu.");
            int gheId = parseRequiredInt(request.getParameter("gheId"), "Vui lòng chọn ghế.");

            GheTrangThaiDTO selectedSeat = chonGheService.validateSelectedSeat(chuyenTauId, gaDiId, gaDenId, toaTauId, gheId);
            SeatSelectionPageDTO pageDTO = chonGheService.loadSeatSelectionPage(chuyenTauId, gaDiId, gaDenId, toaTauId);

            request.setAttribute("pageData", pageDTO);
            request.setAttribute("selectedSeat", selectedSeat);
            request.setAttribute("chuyenTauId", chuyenTauId);
            request.setAttribute("gaDiId", gaDiId);
            request.setAttribute("gaDenId", gaDenId);
            request.setAttribute("toaTauId", toaTauId);
            request.setAttribute("gheId", gheId);
            
            // Bổ sung: Nạp danh sách ưu đãi đang hoạt động
            request.setAttribute("uuDaiList", uuDaiService.layUuDaiDangHoatDong());

            request.getRequestDispatcher("/views/customer/passenger-info.jsp").forward(request, response);
        } catch (Exception ex) {
            request.setAttribute("message", "Không thể chuyển sang bước nhập hành khách: " + ex.getMessage());
            request.getRequestDispatcher("/views/customer/seat-selection.jsp").forward(request, response);
        }
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
}