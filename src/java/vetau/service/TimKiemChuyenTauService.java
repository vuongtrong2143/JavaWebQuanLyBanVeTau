package vetau.service;

import vetau.dao.ChuyenTauDAO;
import vetau.dto.ChuyenTauSearchResultDTO;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class TimKiemChuyenTauService {
    private final ChuyenTauDAO chuyenTauDAO = new ChuyenTauDAO();
    private final GiuChoCleanupService cleanupService = new GiuChoCleanupService();

    public List<ChuyenTauSearchResultDTO> timChuyenTau(int gaDiId, int gaDenId, LocalDate ngayDi) throws SQLException {
        cleanupService.huyCacDonQuaHan();

        validateInput(gaDiId, gaDenId, ngayDi);

        return chuyenTauDAO.searchByRouteAndDate(gaDiId, gaDenId, ngayDi);
    }

    private void validateInput(int gaDiId, int gaDenId, LocalDate ngayDi) {
        if (gaDiId <= 0) {
            throw new IllegalArgumentException("Vui lòng chọn ga đi hợp lệ.");
        }

        if (gaDenId <= 0) {
            throw new IllegalArgumentException("Vui lòng chọn ga đến hợp lệ.");
        }

        if (gaDiId == gaDenId) {
            throw new IllegalArgumentException("Ga đi và ga đến không được trùng nhau.");
        }

        if (ngayDi == null) {
            throw new IllegalArgumentException("Vui lòng chọn ngày đi.");
        }

        if (ngayDi.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Ngày đi không được nhỏ hơn ngày hiện tại.");
        }
    }
}