/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vetau.controller.admin;

import vetau.dao.ChinhSachDoiTraDAO;
import vetau.model.ChinhSachDoiTra;

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

@WebServlet(name = "AdminChinhSachDoiTraServlet", urlPatterns = {"/admin/chinh-sach-doi-tra"})
public class AdminChinhSachDoiTraServlet extends HttpServlet {

    private final ChinhSachDoiTraDAO dao = new ChinhSachDoiTraDAO();

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
                    request.getRequestDispatcher("/views/admin/chinh-sach-doi-tra-form.jsp").forward(request, response);
                    break;
                case "edit":
                    int id = Integer.parseInt(request.getParameter("id"));
                    request.setAttribute("cs", dao.findById(id));
                    request.getRequestDispatcher("/views/admin/chinh-sach-doi-tra-form.jsp").forward(request, response);
                    break;
                case "delete":
                    int deleteId = Integer.parseInt(request.getParameter("id"));
                    dao.delete(deleteId);
                    response.sendRedirect(request.getContextPath() + "/admin/chinh-sach-doi-tra?msg=deleted");
                    break;
                default:
                    List<ChinhSachDoiTra> list = dao.findAll();
                    request.setAttribute("listChinhSach", list);
                    request.getRequestDispatcher("/views/admin/chinh-sach-doi-tra-list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception ex) {
            response.getWriter().println("Lỗi hệ thống chính sách: " + ex.getMessage());
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
            ChinhSachDoiTra cs = new ChinhSachDoiTra();
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isBlank()) {
                cs.setId(Integer.parseInt(idParam));
            }
            
            cs.setTenChinhSach(request.getParameter("tenChinhSach"));
            cs.setLoaiDonHangApDung(request.getParameter("loaiDonHangApDung"));
            cs.setChieuTauApDung(request.getParameter("chieuTauApDung"));

            String tuGio = request.getParameter("truocKhoiHanhTuGio");
            cs.setTruocKhoiHanhTuGio((tuGio != null && !tuGio.isBlank()) ? Integer.parseInt(tuGio) : null);
            
            String denGio = request.getParameter("truocKhoiHanhDenGio");
            cs.setTruocKhoiHanhDenGio((denGio != null && !denGio.isBlank()) ? Integer.parseInt(denGio) : null);

            cs.setTyLeKhauTru(new BigDecimal(request.getParameter("tyLeKhauTru")));
            cs.setPhiDoiCoDinh(new BigDecimal(request.getParameter("phiDoiCoDinh")));

            cs.setChoPhepDoi(request.getParameter("choPhepDoi") != null);
            cs.setChoPhepTra(request.getParameter("choPhepTra") != null);

            cs.setHieuLucTu(LocalDateTime.parse(request.getParameter("hieuLucTu")));
            String hieuLucDen = request.getParameter("hieuLucDen");
            if (hieuLucDen != null && !hieuLucDen.isBlank()) {
                cs.setHieuLucDen(LocalDateTime.parse(hieuLucDen));
            }

            cs.setDoUuTien(Integer.parseInt(request.getParameter("doUuTien")));
            cs.setTrangThai(request.getParameter("trangThai"));

            if (cs.getId() == 0) {
                dao.insert(cs);
            } else {
                dao.update(cs);
            }
            response.sendRedirect(request.getContextPath() + "/admin/chinh-sach-doi-tra?msg=saved");
        } catch (Exception ex) {
            request.setAttribute("error", "Lỗi lưu dữ liệu: " + ex.getMessage());
            request.getRequestDispatcher("/views/admin/chinh-sach-doi-tra-form.jsp").forward(request, response);
        }
    }
}