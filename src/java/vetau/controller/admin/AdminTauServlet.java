package vetau.controller.admin;

import vetau.dao.TauDAO;
import vetau.model.Tau;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminTauServlet", urlPatterns = {"/admin/tau"})
public class AdminTauServlet extends HttpServlet {

    private final TauDAO tauDAO = new TauDAO();

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
                    request.getRequestDispatcher("/views/admin/tau-form.jsp").forward(request, response);
                    break;
                case "edit":
                    int id = Integer.parseInt(request.getParameter("id"));
                    Tau tau = tauDAO.findById(id);
                    request.setAttribute("tau", tau);
                    request.getRequestDispatcher("/views/admin/tau-form.jsp").forward(request, response);
                    break;
                case "delete":
                    int deleteId = Integer.parseInt(request.getParameter("id"));
                    tauDAO.delete(deleteId);
                    response.sendRedirect(request.getContextPath() + "/admin/tau?msg=deleted");
                    break;
                default:
                    List<Tau> listTau = tauDAO.layDanhSachTau();
                    request.setAttribute("listTau", listTau);
                    request.getRequestDispatcher("/views/admin/tau-list.jsp").forward(request, response);
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
            Tau tau = new Tau();
            String idParam = request.getParameter("id");
            if (idParam != null && !idParam.isBlank()) {
                tau.setId(Integer.parseInt(idParam));
            }
            tau.setMaTau(request.getParameter("maTau"));
            tau.setTenTau(request.getParameter("tenTau"));
            tau.setChieuDi(request.getParameter("chieuDi"));
            
            // Xử lý checkbox (nếu check thì có value "on")
            String tuyenThongNhat = request.getParameter("thuocTuyenThongNhat");
            tau.setThuocTuyenThongNhat(tuyenThongNhat != null && tuyenThongNhat.equals("on"));
            
            tau.setMoTa(request.getParameter("moTa"));
            tau.setTrangThai(request.getParameter("trangThai"));

            if (tau.getId() == 0) {
                tauDAO.insert(tau);
            } else {
                tauDAO.update(tau);
            }
            response.sendRedirect(request.getContextPath() + "/admin/tau?msg=saved");
        } catch (Exception ex) {
            request.setAttribute("error", "Lỗi lưu dữ liệu: " + ex.getMessage());
            request.getRequestDispatcher("/views/admin/tau-form.jsp").forward(request, response);
        }
    }
}