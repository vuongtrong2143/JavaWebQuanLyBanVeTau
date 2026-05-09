/*
package vetau.dao;

import vetau.dto.ToaOptionDTO;
import vetau.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ToaTauDAO {
    public List<ToaOptionDTO> findToaOptionsByChuyenAndRoute(int chuyenTauId, int gaDiId, int gaDenId) throws SQLException {
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
                    toa.id AS toa_tau_id,
                    toa.so_toa,
                    toa.loai_toa,
                    toa.suc_chua,
                    COUNT(CASE WHEN ghe.trang_thai = N'Hoạt động' THEN 1 END) AS so_ghe_hoat_dong,
                    SUM(
                        CASE
                            WHEN ghe.id IS NULL THEN 0
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
                        END
                    ) AS so_ghe_da_bi_chiem
                FROM Chang c
                JOIN dbo.TOA_TAU toa ON toa.tau_id = c.tau_id
                LEFT JOIN dbo.GHE ghe ON ghe.toa_tau_id = toa.id
                GROUP BY toa.id, toa.so_toa, toa.loai_toa, toa.suc_chua
                ORDER BY toa.so_toa
                """;

        List<ToaOptionDTO> list = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, gaDiId);
            statement.setInt(2, gaDenId);
            statement.setInt(3, chuyenTauId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ToaOptionDTO dto = new ToaOptionDTO();
                    dto.setToaTauId(resultSet.getInt("toa_tau_id"));
                    dto.setSoToa(resultSet.getInt("so_toa"));
                    dto.setLoaiToa(resultSet.getString("loai_toa"));
                    dto.setSucChua(resultSet.getInt("suc_chua"));
                    dto.setSoGheHoatDong(resultSet.getInt("so_ghe_hoat_dong"));
                    dto.setSoGheDaBiChiem(resultSet.getInt("so_ghe_da_bi_chiem"));
                    list.add(dto);
                }
            }
        }
        return list;
    }
}
*/
package vetau.dao;

import vetau.dto.ToaOptionDTO;
import vetau.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ToaTauDAO {
    public List<ToaOptionDTO> findToaOptionsByChuyenAndRoute(int chuyenTauId, int gaDiId, int gaDenId) throws SQLException {
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
                ),
                TrangThaiGhe AS (
                    SELECT
                        toa.id AS toa_tau_id,
                        toa.so_toa,
                        toa.loai_toa,
                        toa.suc_chua,
                        ghe.id AS ghe_id,
                        ghe.trang_thai AS trang_thai_ghe,
                        CASE
                            WHEN ghe.id IS NULL THEN 0
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
                        END AS bi_chiem
                    FROM Chang c
                    JOIN dbo.TOA_TAU toa ON toa.tau_id = c.tau_id
                    LEFT JOIN dbo.GHE ghe ON ghe.toa_tau_id = toa.id
                )
                SELECT
                    toa_tau_id,
                    so_toa,
                    loai_toa,
                    suc_chua,
                    COUNT(CASE WHEN trang_thai_ghe = N'Hoạt động' THEN 1 END) AS so_ghe_hoat_dong,
                    SUM(bi_chiem) AS so_ghe_da_bi_chiem
                FROM TrangThaiGhe
                GROUP BY toa_tau_id, so_toa, loai_toa, suc_chua
                ORDER BY so_toa
                """;

        List<ToaOptionDTO> list = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, gaDiId);
            statement.setInt(2, gaDenId);
            statement.setInt(3, chuyenTauId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    ToaOptionDTO dto = new ToaOptionDTO();
                    dto.setToaTauId(resultSet.getInt("toa_tau_id"));
                    dto.setSoToa(resultSet.getInt("so_toa"));
                    dto.setLoaiToa(resultSet.getString("loai_toa"));
                    dto.setSucChua(resultSet.getInt("suc_chua"));
                    dto.setSoGheHoatDong(resultSet.getInt("so_ghe_hoat_dong"));
                    dto.setSoGheDaBiChiem(resultSet.getInt("so_ghe_da_bi_chiem"));
                    list.add(dto);
                }
            }
        }
        return list;
    }
}