package vetau.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDateTime;
import vetau.dao.KhuyenMaiDAO;
import vetau.model.KhuyenMai;

public class KhuyenMaiService {

    private final KhuyenMaiDAO khuyenMaiDAO = new KhuyenMaiDAO();

    public KhuyenMai timKhuyenMaiHopLe(String maKhuyenMai, String phuongThucThanhToan, LocalDateTime thoiDiem) throws SQLException {
        if (maKhuyenMai == null || maKhuyenMai.trim().isEmpty()) {
            return null;
        }
        return khuyenMaiDAO.findActiveByCode(maKhuyenMai.trim(), phuongThucThanhToan, thoiDiem);
    }

    public BigDecimal tinhTienGiam(BigDecimal tongTien, String maKhuyenMai, String phuongThucThanhToan, LocalDateTime thoiDiem) throws SQLException {
        if (tongTien == null || maKhuyenMai == null || maKhuyenMai.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        KhuyenMai km = timKhuyenMaiHopLe(maKhuyenMai, phuongThucThanhToan, thoiDiem);
        if (km == null || tongTien.compareTo(km.getGiaTriDonToiThieu()) < 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal giam = tongTien.multiply(km.getPhanTramGiam())
                .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_UP);
        if (km.getGiamToiDa() != null && giam.compareTo(km.getGiamToiDa()) > 0) {
            return km.getGiamToiDa();
        }
        return giam;
    }
}
