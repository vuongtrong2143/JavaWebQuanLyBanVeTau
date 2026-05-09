package vetau.service;

import vetau.dao.MyBookingDAO;
import vetau.dto.BookingHistoryDTO;

import java.sql.SQLException;
import java.util.List;

public class MyBookingService {
    private final MyBookingDAO myBookingDAO = new MyBookingDAO();
    private final GiuChoCleanupService cleanupService = new GiuChoCleanupService();

    public List<BookingHistoryDTO> layLichSuDatVe(int khachHangId) throws SQLException {
        cleanupService.huyCacDonQuaHan();
        return myBookingDAO.findByCustomerId(khachHangId);
    }
}