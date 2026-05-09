/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vetau.controller.admin;

import vetau.dao.BangGiaDAO;
import vetau.dao.GaDAO;
import vetau.model.BangGia;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@WebServlet(name = "AdminBangGiaServlet", urlPatterns = {"/admin/bang-gia"})
public class AdminBangGiaServlet extends HttpServlet {

    private final BangGiaDAO bangGiaDAO = new BangGiaDAO();
    private final GaDAO gaDAO = new GaDAO();

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
                    request.setAttribute("listGa", gaDAO.layDanhSachGaHoatDong());
                    request.getRequestDispatcher("/views/admin/bang-gia-form.jsp").forward(request, response);
                    break;
                case "edit":
                    int id = Integer.parseInt(request.getParameter("id"));
                    request.setAttribute("bg", bangGiaDAO.findById(id));
                    request.setAttribute("listGa", gaDAO.layDanhSachGaHoatDong());
                    request.getRequestDispatcher("/views/admin/bang-gia-form.jsp").forward(request, response);
                    break;
                case "delete":
                    int deleteId = Integer.parseInt(request.getParameter("id"));
                    bangGiaDAO.delete(deleteId); // Xóa mềm: Chuyển sang Tạm dừng
                    response.sendRedirect(request.getContextPath() + "/admin/bang-gia?msg=deleted");
                    break;
                default:
                    request.setAttribute("listBangGia", bangGiaDAO.layDanhSachBangGia());
                    request.getRequestDispatcher("/views/admin/bang-gia-list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception ex) {
            response.getWriter().println("Lỗi hệ thống bảng giá: " + ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        // KIỂM TRA BẢO MẬT POST REQUEST
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentAdmin") == null) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            BangGia bg = new BangGia();
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isBlank()) {
                bg.setId(Integer.parseInt(idParam));
            }
            
            bg.setGaDiId(Integer.parseInt(request.getParameter("gaDiId")));
            bg.setGaDenId(Integer.parseInt(request.getParameter("gaDenId")));
            
            if (bg.getGaDiId() == bg.getGaDenId()) {
                throw new IllegalArgumentException("Ga đi và Ga đến không được trùng nhau!");
            }

            bg.setLoaiToaApDung(request.getParameter("loaiToaApDung"));
            String tang = request.getParameter("tangApDung");
            bg.setTangApDung((tang != null && !tang.isBlank()) ? Integer.parseInt(tang) : null);
            
            bg.setGiaCoSo(new BigDecimal(request.getParameter("giaCoSo")));
            bg.setPhuThuCaoDiemMacDinh(new BigDecimal(request.getParameter("phuThu")));
            bg.setHieuLucTu(LocalDateTime.parse(request.getParameter("hieuLucTu")));
            
            String hieuLucDen = request.getParameter("hieuLucDen");
            if (hieuLucDen != null && !hieuLucDen.isBlank()) {
                bg.setHieuLucDen(LocalDateTime.parse(hieuLucDen));
            }
            bg.setTrangThai(request.getParameter("trangThai"));

            if (bg.getId() == 0) {
                bangGiaDAO.insert(bg);
            } else {
                bangGiaDAO.update(bg);
            }
            response.sendRedirect(request.getContextPath() + "/admin/bang-gia?msg=saved");
        } catch (Exception ex) {
            request.setAttribute("error", "Lỗi lưu dữ liệu: " + ex.getMessage());
            try { request.setAttribute("listGa", gaDAO.layDanhSachGaHoatDong()); } catch (Exception ignored) {}
            request.getRequestDispatcher("/views/admin/bang-gia-form.jsp").forward(request, response);
        }
    }
}