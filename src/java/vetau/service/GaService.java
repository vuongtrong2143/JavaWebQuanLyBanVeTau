package vetau.service;

import vetau.dao.GaDAO;
import vetau.model.Ga;

import java.sql.SQLException;
import java.util.List;

/**
 * Service là nơi đặt nghiệp vụ. Với Giai đoạn 1, service này còn đơn giản.
 * Sau này có thể bổ sung kiểm tra ga hoạt động, lọc tuyến, tìm kiếm theo tên...
 */
public class GaService {

    private final GaDAO gaDAO = new GaDAO();

    public List<Ga> layTatCaGa() throws SQLException {
        return gaDAO.layDanhSachGaHoatDong();
    }
}
