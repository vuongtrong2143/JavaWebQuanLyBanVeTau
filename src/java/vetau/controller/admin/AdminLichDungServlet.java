/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vetau.controller.admin;

import vetau.dao.ChuyenTauDAO;
import vetau.dao.GaDAO;
import vetau.dao.LichDungDAO;
import vetau.model.LichDung;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "AdminLichDungServlet", urlPatterns = {"/admin/lich-dung"})
public class AdminLichDungServlet extends HttpServlet {

    private final LichDungDAO lichDungDAO = new LichDungDAO();
    private final ChuyenTauDAO chuyenTauDAO = new ChuyenTauDAO();
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
                    request.setAttribute("listChuyen", chuyenTauDAO.layDanhSachChuyenTau());
                    request.setAttribute("listGa", gaDAO.layDanhSachGaHoatDong());
                    request.getRequestDispatcher("/views/admin/lich-dung-form.jsp").forward(request, response);
                    break;
                case "edit":
                    int id = Integer.parseInt(request.getParameter("id"));
                    request.setAttribute("ld", lichDungDAO.findById(id));
                    request.setAttribute("listChuyen", chuyenTauDAO.layDanhSachChuyenTau());
                    request.setAttribute("listGa", gaDAO.layDanhSachGaHoatDong());
                    request.getRequestDispatcher("/views/admin/lich-dung-form.jsp").forward(request, response);
                    break;
                case "delete":
                    int deleteId = Integer.parseInt(request.getParameter("id"));
                    lichDungDAO.delete(deleteId);
                    response.sendRedirect(request.getContextPath() + "/admin/lich-dung?msg=deleted");
                    break;
                default:
                    request.setAttribute("listLichDung", lichDungDAO.layDanhSachLichDung());
                    request.getRequestDispatcher("/views/admin/lich-dung-list.jsp").forward(request, response);
                    break;
            }
        } catch (Exception ex) {
            response.getWriter().println("Lỗi hệ thống: " + ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        // KIỂM TRA BẢO MẬT ADMIN
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentAdmin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }

        try {
            LichDung ld = new LichDung();
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isBlank()) {
                ld.setId(Integer.parseInt(idParam));
            }
            
            ld.setChuyenTauId(Integer.parseInt(request.getParameter("chuyenTauId")));
            ld.setGaId(Integer.parseInt(request.getParameter("gaId")));
            ld.setThuTuDung(Integer.parseInt(request.getParameter("thuTuDung")));
            
            String tDen = request.getParameter("thoiGianDen");
            if (tDen != null && !tDen.isBlank()) ld.setThoiGianDen(LocalDateTime.parse(tDen));
            
            String tDi = request.getParameter("thoiGianDi");
            if (tDi != null && !tDi.isBlank()) ld.setThoiGianDi(LocalDateTime.parse(tDi));

            if (ld.getId() == 0) {
                lichDungDAO.insert(ld);
            } else {
                lichDungDAO.update(ld);
            }
            response.sendRedirect(request.getContextPath() + "/admin/lich-dung?msg=saved");
        } catch (Exception ex) {
            request.setAttribute("error", "Lỗi lưu dữ liệu (Có thể trùng ga trên cùng một chuyến): " + ex.getMessage());
            try { 
                request.setAttribute("listChuyen", chuyenTauDAO.layDanhSachChuyenTau());
                request.setAttribute("listGa", gaDAO.layDanhSachGaHoatDong());
            } catch (Exception ignored) {}
            request.getRequestDispatcher("/views/admin/lich-dung-form.jsp").forward(request, response);
        }
    }
}