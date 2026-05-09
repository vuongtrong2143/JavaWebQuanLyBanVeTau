package vetau.controller.customer;

import vetau.model.Ga;
import vetau.service.GaService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    private final GaService gaService = new GaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        loadHomeData(request);
        request.getRequestDispatcher("/views/customer/home.jsp").forward(request, response);
    }

    private void loadHomeData(HttpServletRequest request) {
        List<Ga> gaList = new ArrayList<>();

        try {
            // Lưu ý: Ở bước trước, hàm layTatCaGa() trong GaService 
            // đã được sửa để gọi gaDAO.layDanhSachGaHoatDong()
            gaList = gaService.layTatCaGa();

            request.setAttribute("dbMessage", "Đã kết nối database và đọc được " + gaList.size() + " ga.");
        } catch (Exception ex) {
            request.setAttribute("dbWarning",
                    "Chưa lấy được danh sách ga. Hãy kiểm tra SQL Server, database và DBConnection.java. Lỗi: "
                    + ex.getMessage());
        }

        // Tương thích với giao diện JSP mới (dùng listGa)
        request.setAttribute("listGa", gaList);
        
        // Tương thích với code cũ
        request.setAttribute("gaList", gaList);
        request.setAttribute("danhSachGa", gaList);

        // Mặc định ngày đi là ngày mai
        request.setAttribute("ngayDiValue", LocalDate.now().plusDays(1).toString());
    }
}