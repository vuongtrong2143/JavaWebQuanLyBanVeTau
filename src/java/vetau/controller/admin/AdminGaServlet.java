/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package vetau.controller.admin;

import vetau.dao.GaDAO;
import vetau.model.Ga;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminGaServlet", urlPatterns = {"/admin/ga"})
public class AdminGaServlet extends HttpServlet {

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
                    request.getRequestDispatcher("/views/admin/ga-form.jsp").forward(request, response);
                    break;
                case "edit":
                    int id = Integer.parseInt(request.getParameter("id"));
                    Ga ga = gaDAO.findById(id);
                    request.setAttribute("ga", ga);
                    request.getRequestDispatcher("/views/admin/ga-form.jsp").forward(request, response);
                    break;
                case "delete":
                    int deleteId = Integer.parseInt(request.getParameter("id"));
                    gaDAO.delete(deleteId);
                    response.sendRedirect(request.getContextPath() + "/admin/ga?msg=deleted");
                    break;
                default:
                    List<Ga> listGa = gaDAO.layDanhSachGa();
                    request.setAttribute("listGa", listGa);
                    request.getRequestDispatcher("/views/admin/ga-list.jsp").forward(request, response);
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
        
        // KIỂM TRA BẢO MẬT ADMIN CHO POST REQUEST
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("currentAdmin") == null) {
            response.sendRedirect(request.getContextPath() + "/admin/login");
            return;
        }
        
        try {
            Ga ga = new Ga();
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isBlank()) {
                ga.setId(Integer.parseInt(idParam));
            }
            ga.setMaGa(request.getParameter("maGa"));
            ga.setTenGa(request.getParameter("tenGa"));
            ga.setTinhThanh(request.getParameter("tinhThanh"));
            ga.setLyTrinhKm(Integer.parseInt(request.getParameter("lyTrinhKm")));
            ga.setTrangThai(request.getParameter("trangThai"));

            if (ga.getId() == 0) {
                gaDAO.insert(ga);
            } else {
                gaDAO.update(ga);
            }
            response.sendRedirect(request.getContextPath() + "/admin/ga?msg=saved");
        } catch (Exception ex) {
            request.setAttribute("error", "Lỗi lưu dữ liệu: " + ex.getMessage());
            request.getRequestDispatcher("/views/admin/ga-form.jsp").forward(request, response);
        }
    }
}