package vetau.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import vetau.dao.ChinhSachDoiTraDAO;
import vetau.model.ChinhSachDoiTra;

public class DoiTraService {

    private final ChinhSachDoiTraDAO chinhSachDoiTraDAO = new ChinhSachDoiTraDAO();

    public ChinhSachDoiTra timChinhSach(String loaiDonHang, String chieuTau, long soGioTruocKhoiHanh, boolean laDoiVe) throws SQLException {
        return chinhSachDoiTraDAO.findPhuHop(loaiDonHang, chieuTau, soGioTruocKhoiHanh, laDoiVe);
    }

    public BigDecimal tinhTienHoan(BigDecimal giaVe, ChinhSachDoiTra cs) {
        if (giaVe == null || cs == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal tienKhauTru = giaVe.multiply(cs.getTyLeKhauTru())
                .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
        BigDecimal tienHoan = giaVe.subtract(tienKhauTru).subtract(cs.getPhiDoiCoDinh());
        return tienHoan.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : tienHoan;
    }
}
