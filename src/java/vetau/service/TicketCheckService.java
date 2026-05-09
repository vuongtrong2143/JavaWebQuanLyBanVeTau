package vetau.service;

import vetau.dao.TicketCheckDAO;
import vetau.dto.TicketCheckResultDTO;

import java.sql.SQLException;

public class TicketCheckService {
    private final TicketCheckDAO ticketCheckDAO = new TicketCheckDAO();
    private final GiuChoCleanupService cleanupService = new GiuChoCleanupService();

    public TicketCheckResultDTO traCuuVe(String maVe, String soGiayTo) throws SQLException {
        if (maVe == null || maVe.trim().isEmpty()) {
            throw new IllegalArgumentException("Vui lòng nhập mã vé.");
        }

        cleanupService.huyCacDonQuaHan();

        TicketCheckResultDTO result = ticketCheckDAO.findByMaVeAndSoGiayTo(maVe.trim(), soGiayTo);

        if (result == null) {
            return TicketCheckResultDTO.notFound("Không tìm thấy vé phù hợp. Hãy kiểm tra lại mã vé hoặc số giấy tờ.");
        }

        return result;
    }
}