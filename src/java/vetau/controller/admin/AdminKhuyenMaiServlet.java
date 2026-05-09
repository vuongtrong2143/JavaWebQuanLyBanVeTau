/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vetau.controller.admin;

import vetau.dao.KhuyenMaiDAO;
import vetau.model.KhuyenMai;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet(name = "AdminKhuyenMaiServlet", urlPatterns = {"/admin/khuyen-mai"})
public class AdminKhuyenMaiServlet extends HttpServlet {

    private final KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // KIỂM TRA BẢO MẬT ADMIN
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentAdmin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "add":
                    request.getRequestDispatcher("/views/admin/khuyen-mai-form.jsp").forward(request, response);
                    break;
                case "edit":
                    int id = Integer.parseInt(request.getParameter("id"));
                    KhuyenMai km = khuyenMaiDAO.findById(id);
                    request.setAttribute("km", km);
                    request.getRequestDispatcher("/views/admin/khuyen-mai-form.jsp").forward(request, response);
                    break;
                case "delete":
                    int deleteId = Integer.parseInt(request.getParameter("id"));
                    // Xóa mềm bằng cách cập nhật trạng thái
                    khuyenMaiDAO.delete(deleteId);
                    response.sendRedirect(request.getContextPath() + "/admin/khuyen-mai?msg=deleted");
                    break;
                default:
                    List<KhuyenMai> listKhuyenMai = khuyenMaiDAO.findAll();
                    request.setAttribute("listKhuyenMai", listKhuyenMai);
                    request.getRequestDispatcher("/views/admin/khuyen-mai-list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception ex) {
            response.getWriter().println("Lỗi hệ thống Khuyến mãi: " + ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentAdmin") == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            KhuyenMai km = new KhuyenMai();
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isBlank()) {
                km.setId(Integer.parseInt(idParam));
            }
            
            km.setMaKhuyenMai(request.getParameter("maKhuyenMai").toUpperCase());
            km.setTenChuongTrinh(request.getParameter("tenChuongTrinh"));
            km.setPhanTramGiam(new BigDecimal(request.getParameter("phanTramGiam")));
            
            String giamToiDa = request.getParameter("giamToiDa");
            km.setGiamToiDa((giamToiDa != null && !giamToiDa.isBlank()) ? new BigDecimal(giamToiDa) : null);

            km.setGiaTriDonToiThieu(new BigDecimal(request.getParameter("giaTriDonToiThieu")));
            
            String phuongThuc = request.getParameter("phuongThucTtApDung");
            km.setPhuongThucTtApDung((phuongThuc != null && !phuongThuc.isBlank()) ? phuongThuc : null);

            km.setNgayBatDau(LocalDateTime.parse(request.getParameter("ngayBatDau")));
            km.setNgayKetThuc(LocalDateTime.parse(request.getParameter("ngayKetThuc")));

            String soLuong = request.getParameter("soLuongToiDa");
            km.setSoLuongToiDa((soLuong != null && !soLuong.isBlank()) ? Integer.parseInt(soLuong) : null);

            km.setTrangThai(request.getParameter("trangThai"));

            if (km.getId() == 0) {
                khuyenMaiDAO.insert(km);
            } else {
                khuyenMaiDAO.update(km);
            }
            response.sendRedirect(request.getContextPath() + "/admin/khuyen-mai?msg=saved");
        } catch (Exception ex) {
            request.setAttribute("error", "Lỗi dữ liệu: " + ex.getMessage());
            request.getRequestDispatcher("/views/admin/khuyen-mai-form.jsp").forward(request, response);
        }
    }
}