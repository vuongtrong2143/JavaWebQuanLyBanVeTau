package vetau.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import vetau.dao.HoanTienDAO;
import vetau.model.HoanTien;
import vetau.util.TrangThai;

public class HoanTienService {

    private final HoanTienDAO hoanTienDAO = new HoanTienDAO();

    public int taoYeuCauHoanTien(int thanhToanId, int veId, BigDecimal soTienHoan) throws SQLException {
        HoanTien ht = new HoanTien();
        ht.setThanhToanId(thanhToanId);
        ht.setVeId(veId);
        ht.setSoTienHoan(soTienHoan);
        ht.setMaGiaoDichHoan(null);
        ht.setThoiGianYeuCau(LocalDateTime.now());
        ht.setThoiGianHoanTat(null);
        ht.setTrangThai(TrangThai.HOAN_TIEN_CHO_XU_LY);

        return hoanTienDAO.insert(ht);
    }

    public void danhDauHoanTat(int hoanTienId, String maGiaoDichHoan) throws SQLException {
        hoanTienDAO.markCompleted(hoanTienId, maGiaoDichHoan);
    }

    public void tuChoiHoanTien(int hoanTienId) throws SQLException {
        hoanTienDAO.markRejected(hoanTienId);
    }
}