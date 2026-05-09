package vetau.controller.customer;

import vetau.dto.CustomerSessionDTO;
import vetau.model.HanhKhach;
import vetau.model.KhuyenMai;
import vetau.service.DatVeService;
import vetau.service.GiaVeService;
import vetau.service.GiaVeService.KetQuaTinhGia;
import vetau.service.KhuyenMaiService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@WebServlet(name = "CreateBookingServlet", urlPatterns = {"/booking/create"})
public class CreateBookingServlet extends HttpServlet {

    private final GiaVeService giaVeService = new GiaVeService();
    private final KhuyenMaiService khuyenMaiService = new KhuyenMaiService();
    private final DatVeService datVeService = new DatVeService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);
        CustomerSessionDTO currentCustomer = session == null
                ? null
                : (CustomerSessionDTO) session.getAttribute("currentCustomer");

        if (currentCustomer == null) {
            response.sendRedirect(request.getContextPath() + "/login?message=login-required");
            return;
        }

        try {
            int chuyenTauId = Integer.parseInt(request.getParameter("chuyenTauId"));
            int gaDiId = Integer.parseInt(request.getParameter("gaDiId"));
            int gaDenId = Integer.parseInt(request.getParameter("gaDenId"));
            int gheId = Integer.parseInt(request.getParameter("gheId"));

            String loaiToa = request.getParameter("loaiToa");
            String tangRaw = request.getParameter("tang");
            Integer tang = (tangRaw == null || tangRaw.isBlank()) ? null : Integer.parseInt(tangRaw);

            String uuDaiRaw = request.getParameter("doiTuongUuDaiId");
            Integer doiTuongUuDaiId = (uuDaiRaw == null || uuDaiRaw.isBlank()) ? null : Integer.parseInt(uuDaiRaw);

            String maKhuyenMai = request.getParameter("maKhuyenMai");

            LocalDateTime thoiDiemHienTai = LocalDateTime.now();

            /*
             * Tính lại giá chính thức tại server.
             * Không tin bất kỳ giá tiền nào từ HTML/client.
             */
            KetQuaTinhGia ketQuaFinal = giaVeService.tinhGiaVe(
                    gaDiId,
                    gaDenId,
                    loaiToa,
                    tang,
                    doiTuongUuDaiId,
                    maKhuyenMai,
                    null,
                    thoiDiemHienTai
            );

            Integer khuyenMaiId = null;
            if (maKhuyenMai != null && !maKhuyenMai.isBlank()) {
                KhuyenMai km = khuyenMaiService.timKhuyenMaiHopLe(maKhuyenMai, null, thoiDiemHienTai);
                if (km != null) {
                    khuyenMaiId = km.getId();
                }
            }

            HanhKhach hanhKhach = new HanhKhach();
            hanhKhach.setHoTen(request.getParameter("hoTen"));
            hanhKhach.setLoaiGiayTo(request.getParameter("loaiGiayTo"));
            hanhKhach.setSoGiayTo(request.getParameter("soGiayTo"));

            String ngaySinhRaw = request.getParameter("ngaySinh");
            if (ngaySinhRaw != null && !ngaySinhRaw.isBlank()) {
                hanhKhach.setNgaySinh(LocalDate.parse(ngaySinhRaw));
            }

            hanhKhach.setQuocTich("Việt Nam");

            int newDatChoId = datVeService.taoDonGiuChoVaVeChoThanhToan(
                    currentCustomer.getId(),
                    khuyenMaiId,
                    chuyenTauId,
                    gheId,
                    gaDiId,
                    gaDenId,
                    hanhKhach,
                    doiTuongUuDaiId,
                    ketQuaFinal,
                    30
            );

            response.sendRedirect(request.getContextPath() + "/checkout?datChoId=" + newDatChoId);

        } catch (Exception ex) {
            request.setAttribute("error", "Không thể chốt đơn: " + ex.getMessage());
            request.getRequestDispatcher("/views/customer/booking-review.jsp").forward(request, response);
        }
    }
}