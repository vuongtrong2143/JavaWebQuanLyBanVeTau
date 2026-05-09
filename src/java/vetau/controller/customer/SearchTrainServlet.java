package vetau.controller.customer;

import vetau.dto.ChuyenTauSearchResultDTO;
import vetau.service.GaService;
import vetau.service.TimKiemChuyenTauService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet(name = "SearchTrainServlet", urlPatterns = {"/search-train"})
public class SearchTrainServlet extends HttpServlet {
    private final TimKiemChuyenTauService timKiemChuyenTauService = new TimKiemChuyenTauService();
    private final GaService gaService = new GaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String gaDiIdRaw = request.getParameter("gaDiId");
        String gaDenIdRaw = request.getParameter("gaDenId");
        String ngayDiRaw = request.getParameter("ngayDi");

        // Trường hợp 1: Nếu KHÔNG CÓ tham số -> Khách bấm vào menu "Tìm chuyến" -> Hiển thị form trắng
        if (gaDiIdRaw == null && gaDenIdRaw == null) {
            loadSearchFormData(request);
            request.getRequestDispatcher("/views/customer/search-train.jsp").forward(request, response);
            return;
        }

        // Trường hợp 2: Nếu CÓ tham số -> Khách tìm từ Trang chủ (phương thức GET) -> Gọi hàm xử lý tìm
        processSearch(request, response, gaDiIdRaw, gaDenIdRaw, ngayDiRaw);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String gaDiIdRaw = request.getParameter("gaDiId");
        String gaDenIdRaw = request.getParameter("gaDenId");
        String ngayDiRaw = request.getParameter("ngayDi");
        
        // Khách tìm từ form trang search-train.jsp (phương thức POST) -> Vẫn dùng chung 1 hàm xử lý
        processSearch(request, response, gaDiIdRaw, gaDenIdRaw, ngayDiRaw);
    }

    // --- HÀM XỬ LÝ TÌM KIẾM CHUNG (Gộp logic để không lặp code) ---
    private void processSearch(HttpServletRequest request, HttpServletResponse response, 
                               String gaDiIdRaw, String gaDenIdRaw, String ngayDiRaw) 
            throws ServletException, IOException {
        
        request.setAttribute("gaDiId", gaDiIdRaw);
        request.setAttribute("gaDenId", gaDenIdRaw);
        request.setAttribute("ngayDi", ngayDiRaw);

        try {
            int gaDiId = parseRequiredInt(gaDiIdRaw, "Vui lòng chọn ga đi.");
            int gaDenId = parseRequiredInt(gaDenIdRaw, "Vui lòng chọn ga đến.");
            LocalDate ngayDi = parseRequiredDate(ngayDiRaw);

            // Dùng Service xịn của bạn để lấy dữ liệu
            List<ChuyenTauSearchResultDTO> results = timKiemChuyenTauService.timChuyenTau(gaDiId, gaDenId, ngayDi);
            
            // Bơm dữ liệu cho giao diện Kết quả Tìm kiếm (UI mới)
            request.setAttribute("results", results);
            request.setAttribute("totalResults", results.size());
            request.setAttribute("gaList", gaService.layTatCaGa()); // Load lại danh sách ga cho thanh Sticky Search
            
            request.getRequestDispatcher("/views/customer/search-result.jsp").forward(request, response);
            
        } catch (IllegalArgumentException ex) {
            // Lỗi Validate dữ liệu -> Giữ nguyên cách xử lý cũ của bạn
            forwardBackToSearchFormWithError(request, response, ex.getMessage(), gaDiIdRaw, gaDenIdRaw, ngayDiRaw);
        } catch (Exception ex) {
            // Lỗi hệ thống
            request.setAttribute("message", "Có lỗi khi tìm chuyến tàu: " + ex.getMessage());
            
            // Dùng khối try-catch an toàn hoặc gọi lại hàm loadSearchFormData đã viết sẵn
            try {
                request.setAttribute("gaList", gaService.layTatCaGa());
            } catch (Exception dbEx) {
                // Nếu lỗi DB, bỏ qua (giao diện sẽ tự ẩn thanh tìm kiếm nhanh)
            }
            
            request.getRequestDispatcher("/views/customer/search-result.jsp").forward(request, response);
        }
    }

    // =========================================================================
    // CÁC HÀM TIỆN ÍCH GIỮ NGUYÊN BẢN CỦA BẠN (Không thay đổi gì)
    // =========================================================================

    private void loadSearchFormData(HttpServletRequest request) {
        try {
            request.setAttribute("gaList", gaService.layTatCaGa());
        } catch (Exception ex) {
            request.setAttribute("dbWarning", "Chưa lấy được danh sách ga. Hãy kiểm tra SQL Server, database và DBConnection.java.");
        }

        if (request.getAttribute("ngayDiValue") == null) {
            request.setAttribute("ngayDiValue", LocalDate.now().plusDays(1).toString());
        }
    }

    private int parseRequiredInt(String rawValue, String errorMessage) {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }
        try {
            return Integer.parseInt(rawValue);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private LocalDate parseRequiredDate(String rawValue) {
        if (rawValue == null || rawValue.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng chọn ngày đi.");
        }
        try {
            return LocalDate.parse(rawValue);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Ngày đi không hợp lệ.");
        }
    }

    private void forwardBackToSearchFormWithError(HttpServletRequest request, HttpServletResponse response,
                                                  String errorMessage, String gaDiIdRaw, String gaDenIdRaw, String ngayDiRaw)
            throws ServletException, IOException {
        loadSearchFormData(request);
        request.setAttribute("searchError", errorMessage);
        request.setAttribute("gaDiIdValue", gaDiIdRaw);
        request.setAttribute("gaDenIdValue", gaDenIdRaw);
        request.setAttribute("ngayDiValue", ngayDiRaw == null || ngayDiRaw.trim().isEmpty()
                ? LocalDate.now().plusDays(1).toString()
                : ngayDiRaw);
        request.getRequestDispatcher("/views/customer/search-train.jsp").forward(request, response);
    }
}