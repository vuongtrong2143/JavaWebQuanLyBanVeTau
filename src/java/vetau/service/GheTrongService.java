package vetau.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import vetau.util.DBConnection;

public class GheTrongService {

    public boolean laGheTrongTheoChang(int chuyenTauId, int gheId, int gaDiId, int gaDenId) throws SQLException {
        String sql =
                "WITH ChangMoi AS ( "
              + "    SELECT ld_di.thu_tu_dung AS tu, ld_den.thu_tu_dung AS den "
              + "    FROM dbo.LICH_DUNG ld_di "
              + "    JOIN dbo.LICH_DUNG ld_den ON ld_den.chuyen_tau_id = ld_di.chuyen_tau_id "
              + "    WHERE ld_di.chuyen_tau_id = ? AND ld_di.ga_id = ? AND ld_den.ga_id = ? "
              + "), ChangVe AS ( "
              + "    SELECT ld_di.thu_tu_dung AS tu, ld_den.thu_tu_dung AS den "
              + "    FROM dbo.VE v "
              + "    JOIN dbo.LICH_DUNG ld_di ON ld_di.chuyen_tau_id = v.chuyen_tau_id AND ld_di.ga_id = v.ga_di_id "
              + "    JOIN dbo.LICH_DUNG ld_den ON ld_den.chuyen_tau_id = v.chuyen_tau_id AND ld_den.ga_id = v.ga_den_id "
              + "    WHERE v.chuyen_tau_id = ? AND v.ghe_id = ? AND v.trang_thai IN (N'Hợp lệ', N'Đã đổi') "
              + "), ChangGiu AS ( "
              + "    SELECT ld_di.thu_tu_dung AS tu, ld_den.thu_tu_dung AS den "
              + "    FROM dbo.GIU_CHO gc "
              + "    JOIN dbo.LICH_DUNG ld_di ON ld_di.chuyen_tau_id = gc.chuyen_tau_id AND ld_di.ga_id = gc.ga_di_id "
              + "    JOIN dbo.LICH_DUNG ld_den ON ld_den.chuyen_tau_id = gc.chuyen_tau_id AND ld_den.ga_id = gc.ga_den_id "
              + "    WHERE gc.chuyen_tau_id = ? AND gc.ghe_id = ? AND gc.trang_thai = N'Đang giữ' AND gc.thoi_gian_het_han > SYSDATETIME() "
              + ") "
              + "SELECT CASE WHEN EXISTS (SELECT 1 FROM ChangMoi WHERE tu < den) "
              + "AND NOT EXISTS (SELECT 1 FROM ChangMoi m JOIN ChangVe x ON m.tu < x.den AND x.tu < m.den) "
              + "AND NOT EXISTS (SELECT 1 FROM ChangMoi m JOIN ChangGiu x ON m.tu < x.den AND x.tu < m.den) "
              + "THEN 1 ELSE 0 END AS ok";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, chuyenTauId);
            ps.setInt(2, gaDiId);
            ps.setInt(3, gaDenId);
            ps.setInt(4, chuyenTauId);
            ps.setInt(5, gheId);
            ps.setInt(6, chuyenTauId);
            ps.setInt(7, gheId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt("ok") == 1;
            }
        }
    }
}
