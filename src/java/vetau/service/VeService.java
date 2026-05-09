package vetau.service;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import vetau.dao.VeDAO;
import vetau.model.Ve;
import vetau.util.TrangThai;

public class VeService {

    private final VeDAO veDAO = new VeDAO();

    public int taoVe(Ve ve) throws SQLException {
        if (ve.getMaVe() == null || ve.getMaVe().trim().isEmpty()) {
            ve.setMaVe("VE" + UUID.randomUUID().toString().replace("-", "").substring(0, 14).toUpperCase());
        }
        if (ve.getTrangThai() == null) {
            ve.setTrangThai(TrangThai.VE_HOP_LE);
        }
        return veDAO.insert(ve);
    }
    public int taoVeChoThanhToan(Ve ve) throws SQLException {
        if (ve.getMaVe() == null || ve.getMaVe().trim().isEmpty()) {
            ve.setMaVe("VE" + UUID.randomUUID().toString().replace("-", "").substring(0, 14).toUpperCase());
        }

        ve.setTrangThai(TrangThai.VE_CHO_THANH_TOAN);
        return veDAO.insert(ve);
    }
    
    public Ve traCuuBangMaVe(String maVe) throws SQLException {
        return veDAO.findByMaVe(maVe);
    }

    public List<Ve> layVeTheoDatCho(int datChoId) throws SQLException {
        return veDAO.findByDatChoId(datChoId);
    }
    
    public void capNhatTrangThai(int veId, String trangThai) throws SQLException {
        veDAO.updateTrangThai(veId, trangThai);
    }
}
