package vetau.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import vetau.util.DBConnection;

public class AdminService {

    public int demBanGhi(String tenBang) throws SQLException {
        String table = chuanHoaTenBang(tenBang);
        String sql = "SELECT COUNT(*) FROM dbo." + table;
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private String chuanHoaTenBang(String tenBang) {
        String table = tenBang == null ? "" : tenBang.trim().toUpperCase();
        switch (table) {
            case "GA": case "TAU": case "TOA_TAU": case "GHE": case "CHUYEN_TAU": case "LICH_DUNG":
            case "BANG_GIA": case "KHUYEN_MAI": case "DOI_TUONG_UU_DAI": case "CHINH_SACH_DOI_TRA":
            case "KHACH_HANG": case "DAT_CHO": case "GIU_CHO": case "VE": case "THANH_TOAN": case "HOAN_TIEN":
                return table;
            default:
                throw new IllegalArgumentException("Bảng không được phép thống kê: " + tenBang);
        }
    }
}
