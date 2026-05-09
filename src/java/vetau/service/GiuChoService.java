package vetau.service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import vetau.dao.GiuChoDAO;
import vetau.model.GiuCho;
import vetau.util.TrangThai;

public class GiuChoService {

    private final GiuChoDAO giuChoDAO = new GiuChoDAO();
    private final GheTrongService gheTrongService = new GheTrongService();

    public int taoGiuCho(int datChoId, int chuyenTauId, int gheId, int gaDiId, int gaDenId, int soPhutGiu) throws SQLException {
        if (!gheTrongService.laGheTrongTheoChang(chuyenTauId, gheId, gaDiId, gaDenId)) {
            throw new IllegalStateException("Ghế đã bị giữ hoặc đã bán trên đoạn chặng này.");
        }
        LocalDateTime now = LocalDateTime.now();
        GiuCho gc = new GiuCho();
        gc.setDatChoId(datChoId);
        gc.setChuyenTauId(chuyenTauId);
        gc.setGheId(gheId);
        gc.setGaDiId(gaDiId);
        gc.setGaDenId(gaDenId);
        gc.setThoiGianGiu(now);
        gc.setThoiGianHetHan(now.plusMinutes(soPhutGiu));
        gc.setTrangThai(TrangThai.GIU_CHO_DANG_GIU);
        return giuChoDAO.insert(gc);
    }

    public List<GiuCho> layTheoDatCho(int datChoId) throws SQLException {
        return giuChoDAO.findByDatChoId(datChoId);
    }

    public void chuyenSangVe(int datChoId) throws SQLException {
        giuChoDAO.updateTrangThaiByDatChoId(datChoId, TrangThai.GIU_CHO_DA_CHUYEN_VE);
    }
}
