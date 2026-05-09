package vetau.dao;

import vetau.dto.GheTrangThaiDTO;
import vetau.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GheDAO {
    public List<GheTrangThaiDTO> findSeatsByToaAndRoute(int chuyenTauId, int toaTauId, int gaDiId, int gaDenId) throws SQLException {
        String sql = """
                WITH Chang AS (
                    SELECT
                        ct.id AS chuyen_tau_id,
                        ct.tau_id,
                        ld_di.thu_tu_dung AS thu_tu_di,
                        ld_den.thu_tu_dung AS thu_tu_den
                    FROM dbo.CHUYEN_TAU ct
                    JOIN dbo.LICH_DUNG ld_di
                        ON ld_di.chuyen_tau_id = ct.id
                       AND ld_di.ga_id = ?
                    JOIN dbo.LICH_DUNG ld_den
                        ON ld_den.chuyen_tau_id = ct.id
                       AND ld_den.ga_id = ?
                    WHERE ct.id = ?
                      AND ct.trang_thai = N'Hoạt động'
                      AND ld_di.thu_tu_dung < ld_den.thu_tu_dung
                )
                SELECT
                    ghe.id AS ghe_id,
                    ghe.toa_tau_id,
                    ghe.so_ghe,
                    ghe.tang,
                    ghe.loai_cho,
                    ghe.trang_thai,
                    CASE
                        WHEN ghe.trang_thai <> N'Hoạt động' THEN 1
                        WHEN EXISTS (
                            SELECT 1
                            FROM dbo.VE v
                            JOIN dbo.LICH_DUNG v_di
                                ON v_di.chuyen_tau_id = v.chuyen_tau_id
                               AND v_di.ga_id = v.ga_di_id
                            JOIN dbo.LICH_DUNG v_den
                                ON v_den.chuyen_tau_id = v.chuyen_tau_id
                               AND v_den.ga_id = v.ga_den_id
                            CROSS JOIN Chang c2
                            WHERE v.chuyen_tau_id = c2.chuyen_tau_id
                              AND v.ghe_id = ghe.id
                              AND v.trang_thai IN (N'Hợp lệ', N'Đã sử dụng')
                              AND c2.thu_tu_di < v_den.thu_tu_dung
                              AND v_di.thu_tu_dung < c2.thu_tu_den
                        ) THEN 1
                        WHEN EXISTS (
                            SELECT 1
                            FROM dbo.GIU_CHO gc
                            JOIN dbo.LICH_DUNG gc_di
                                ON gc_di.chuyen_tau_id = gc.chuyen_tau_id
                               AND gc_di.ga_id = gc.ga_di_id
                            JOIN dbo.LICH_DUNG gc_den
                                ON gc_den.chuyen_tau_id = gc.chuyen_tau_id
                               AND gc_den.ga_id = gc.ga_den_id
                            CROSS JOIN Chang c3
                            WHERE gc.chuyen_tau_id = c3.chuyen_tau_id
                              AND gc.ghe_id = ghe.id
                              AND gc.trang_thai = N'Đang giữ'
                              AND gc.thoi_gian_het_han > SYSDATETIME()
                              AND c3.thu_tu_di < gc_den.thu_tu_dung
                              AND gc_di.thu_tu_dung < c3.thu_tu_den
                        ) THEN 1
                        ELSE 0
                    END AS bi_chiem,
                    CASE
                        WHEN ghe.trang_thai <> N'Hoạt động' THEN ghe.trang_thai
                        WHEN EXISTS (
                            SELECT 1
                            FROM dbo.VE v
                            JOIN dbo.LICH_DUNG v_di
                                ON v_di.chuyen_tau_id = v.chuyen_tau_id
                               AND v_di.ga_id = v.ga_di_id
                            JOIN dbo.LICH_DUNG v_den
                                ON v_den.chuyen_tau_id = v.chuyen_tau_id
                               AND v_den.ga_id = v.ga_den_id
                            CROSS JOIN Chang c4
                            WHERE v.chuyen_tau_id = c4.chuyen_tau_id
                              AND v.ghe_id = ghe.id
                              AND v.trang_thai IN (N'Hợp lệ', N'Đã sử dụng')
                              AND c4.thu_tu_di < v_den.thu_tu_dung
                              AND v_di.thu_tu_dung < c4.thu_tu_den
                        ) THEN N'Đã bán'
                        WHEN EXISTS (
                            SELECT 1
                            FROM dbo.GIU_CHO gc
                            JOIN dbo.LICH_DUNG gc_di
                                ON gc_di.chuyen_tau_id = gc.chuyen_tau_id
                               AND gc_di.ga_id = gc.ga_di_id
                            JOIN dbo.LICH_DUNG gc_den
                                ON gc_den.chuyen_tau_id = gc.chuyen_tau_id
                               AND gc_den.ga_id = gc.ga_den_id
                            CROSS JOIN Chang c5
                            WHERE gc.chuyen_tau_id = c5.chuyen_tau_id
                              AND gc.ghe_id = ghe.id
                              AND gc.trang_thai = N'Đang giữ'
                              AND gc.thoi_gian_het_han > SYSDATETIME()
                              AND c5.thu_tu_di < gc_den.thu_tu_dung
                              AND gc_di.thu_tu_dung < c5.thu_tu_den
                        ) THEN N'Đang giữ'
                        ELSE N'Còn trống'
                    END AS ly_do
                FROM Chang c
                JOIN dbo.TOA_TAU toa ON toa.tau_id = c.tau_id
                JOIN dbo.GHE ghe ON ghe.toa_tau_id = toa.id
                WHERE toa.id = ?
                ORDER BY
                    CASE WHEN ghe.tang IS NULL THEN 0 ELSE ghe.tang END,
                    TRY_CONVERT(INT, SUBSTRING(ghe.so_ghe, 2, LEN(ghe.so_ghe))),
                    ghe.so_ghe
                """;

        List<GheTrangThaiDTO> list = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, gaDiId);
            statement.setInt(2, gaDenId);
            statement.setInt(3, chuyenTauId);
            statement.setInt(4, toaTauId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(mapRow(resultSet));
                }
            }
        }
        return list;
    }

    private GheTrangThaiDTO mapRow(ResultSet resultSet) throws SQLException {
        GheTrangThaiDTO dto = new GheTrangThaiDTO();
        dto.setGheId(resultSet.getInt("ghe_id"));
        dto.setToaTauId(resultSet.getInt("toa_tau_id"));
        dto.setSoGhe(resultSet.getString("so_ghe"));
        int tang = resultSet.getInt("tang");
        dto.setTang(resultSet.wasNull() ? null : tang);
        dto.setLoaiCho(resultSet.getString("loai_cho"));
        dto.setTrangThaiGhe(resultSet.getString("trang_thai"));
        dto.setBiChiem(resultSet.getInt("bi_chiem") == 1);
        dto.setLyDoKhongChonDuoc(resultSet.getString("ly_do"));
        return dto;
    }
}
