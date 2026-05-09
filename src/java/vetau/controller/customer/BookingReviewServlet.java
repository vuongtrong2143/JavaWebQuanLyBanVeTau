/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vetau.controller.customer;

import vetau.service.GiaVeService;
import vetau.service.GiaVeService.KetQuaTinhGia;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "BookingReviewServlet", urlPatterns = {"/booking-review"})
public class BookingReviewServlet extends HttpServlet {
    
    private final GiaVeService giaVeService = new GiaVeService();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            // Nhận dữ liệu chuyến và chặng
            int chuyenTauId = Integer.parseInt(request.getParameter("chuyenTauId"));
            int gaDiId = Integer.parseInt(request.getParameter("gaDiId"));
            int gaDenId = Integer.parseInt(request.getParameter("gaDenId"));
            int toaTauId = Integer.parseInt(request.getParameter("toaTauId"));
            int gheId = Integer.parseInt(request.getParameter("gheId"));
            
            String loaiToa = request.getParameter("loaiToa");
            String tangRaw = request.getParameter("tang");
            Integer tang = (tangRaw == null || tangRaw.isBlank()) ? null : Integer.parseInt(tangRaw);

            // Nhận dữ liệu hành khách
            String hoTen = request.getParameter("hoTen");
            String loaiGiayTo = request.getParameter("loaiGiayTo");
            String soGiayTo = request.getParameter("soGiayTo");
            String ngaySinh = request.getParameter("ngaySinh");
            
            String uuDaiRaw = request.getParameter("doiTuongUuDaiId");
            Integer doiTuongUuDaiId = (uuDaiRaw == null || uuDaiRaw.isBlank()) ? null : Integer.parseInt(uuDaiRaw);
            
            // Tính giá vé tạm thời (chưa có khuyến mãi ở bước này)
            KetQuaTinhGia ketQuaGia = giaVeService.tinhGiaVe(
                    gaDiId, gaDenId, loaiToa, tang, doiTuongUuDaiId, null, null, null
            );

            // Gửi toàn bộ dữ liệu sang trang xác nhận (booking-review.jsp)
            request.setAttribute("chuyenTauId", chuyenTauId);
            request.setAttribute("gaDiId", gaDiId);
            request.setAttribute("gaDenId", gaDenId);
            request.setAttribute("toaTauId", toaTauId);
            request.setAttribute("gheId", gheId);
            
            request.setAttribute("hoTen", hoTen);
            request.setAttribute("loaiGiayTo", loaiGiayTo);
            request.setAttribute("soGiayTo", soGiayTo);
            request.setAttribute("ngaySinh", ngaySinh);
            request.setAttribute("doiTuongUuDaiId", doiTuongUuDaiId);
            
            request.setAttribute("ketQuaGia", ketQuaGia);

            request.getRequestDispatcher("/views/customer/booking-review.jsp").forward(request, response);
            
        } catch (Exception ex) {
            request.setAttribute("error", "Lỗi tính giá vé: " + ex.getMessage());
            request.getRequestDispatcher("/views/customer/passenger-info.jsp").forward(request, response);
        }
    }
}