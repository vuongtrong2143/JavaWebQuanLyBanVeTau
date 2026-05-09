package vetau.controller.admin;

import vetau.dao.ChuyenTauDAO;
import vetau.dao.TauDAO;
import vetau.model.ChuyenTau;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

@WebServlet(name = "AdminChuyenTauServlet", urlPatterns = {"/admin/chuyen-tau"})
public class AdminChuyenTauServlet extends HttpServlet {

    private final ChuyenTauDAO chuyenTauDAO = new ChuyenTauDAO();
    private final TauDAO tauDAO = new TauDAO(); // Dùng để load danh sách Tàu

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
                    request.setAttribute("listTau", tauDAO.layDanhSachTauHoatDong());
                    request.getRequestDispatcher("/views/admin/chuyen-tau-form.jsp").forward(request, response);
                    break;
                case "edit":
                    int id = Integer.parseInt(request.getParameter("id"));
                    request.setAttribute("ct", chuyenTauDAO.findById(id));
                    request.setAttribute("listTau", tauDAO.layDanhSachTauHoatDong());
                    request.getRequestDispatcher("/views/admin/chuyen-tau-form.jsp").forward(request, response);
                    break;
                case "delete":
                    int deleteId = Integer.parseInt(request.getParameter("id"));
                    chuyenTauDAO.delete(deleteId);
                    response.sendRedirect(request.getContextPath() + "/admin/chuyen-tau?msg=deleted");
                    break;
                default:
                    request.setAttribute("listChuyen", chuyenTauDAO.layDanhSachChuyenTau());
                    request.getRequestDispatcher("/views/admin/chuyen-tau-list.jsp").forward(request, response);
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
            ChuyenTau ct = new ChuyenTau();
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isBlank()) {
                ct.setId(Integer.parseInt(idParam));
            }
            
            ct.setTauId(Integer.parseInt(request.getParameter("tauId")));
            ct.setMaChuyen(request.getParameter("maChuyen"));
            ct.setNgayChay(LocalDate.parse(request.getParameter("ngayChay")));
            ct.setGioKhoiHanh(LocalDateTime.parse(request.getParameter("gioKhoiHanh")));
            
            String gioDenStr = request.getParameter("gioDenDuKien");
            if (gioDenStr != null && !gioDenStr.isBlank()) {
                ct.setGioDenDuKien(LocalDateTime.parse(gioDenStr));
            }
            
            ct.setTrangThai(request.getParameter("trangThai"));

            if (ct.getId() == 0) {
                chuyenTauDAO.insert(ct);
            } else {
                chuyenTauDAO.update(ct);
            }
            response.sendRedirect(request.getContextPath() + "/admin/chuyen-tau?msg=saved");
        } catch (Exception ex) {
            request.setAttribute("error", "Lỗi lưu dữ liệu: " + ex.getMessage());
            try { request.setAttribute("listTau", tauDAO.layDanhSachTauHoatDong()); } catch (Exception ignored) {}
            request.getRequestDispatcher("/views/admin/chuyen-tau-form.jsp").forward(request, response);
        }
    }
}