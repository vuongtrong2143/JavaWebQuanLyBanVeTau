package vetau.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;
import vetau.dao.DoiTuongUuDaiDAO;
import vetau.model.DoiTuongUuDai;

public class UuDaiService {

    private final DoiTuongUuDaiDAO doiTuongUuDaiDAO = new DoiTuongUuDaiDAO();

    public List<DoiTuongUuDai> layUuDaiDangHoatDong() throws SQLException {
        return doiTuongUuDaiDAO.findAllActive();
    }

    public BigDecimal tinhTienGiam(BigDecimal giaCoSo, Integer doiTuongUuDaiId) throws SQLException {
        if (giaCoSo == null || doiTuongUuDaiId == null) {
            return BigDecimal.ZERO;
        }
        DoiTuongUuDai uuDai = doiTuongUuDaiDAO.findById(doiTuongUuDaiId);
        if (uuDai == null || !"Hoạt động".equalsIgnoreCase(uuDai.getTrangThai())) {
            return BigDecimal.ZERO;
        }
        return giaCoSo.multiply(uuDai.getPhanTramGiam())
                .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
    }
}
