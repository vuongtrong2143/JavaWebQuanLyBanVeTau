package vetau.service;

import vetau.dao.PublicInfoDAO;
import vetau.dto.PromotionViewDTO;

import java.sql.SQLException;
import java.util.List;

public class PublicInfoService {
    private final PublicInfoDAO publicInfoDAO = new PublicInfoDAO();

    public List<PromotionViewDTO> layKhuyenMaiDangHoatDong() throws SQLException {
        return publicInfoDAO.findActivePromotions();
    }
}
