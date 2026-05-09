package vetau.service;

import vetau.dao.ChuyenTauDAO;
import vetau.dao.GheDAO;
import vetau.dao.ToaTauDAO;
import vetau.dto.ChuyenTauSearchResultDTO;
import vetau.dto.GheTrangThaiDTO;
import vetau.dto.SeatSelectionPageDTO;
import vetau.dto.ToaOptionDTO;

import java.sql.SQLException;
import java.util.List;

public class ChonGheService {
    private final ChuyenTauDAO chuyenTauDAO = new ChuyenTauDAO();
    private final ToaTauDAO toaTauDAO = new ToaTauDAO();
    private final GheDAO gheDAO = new GheDAO();
    private final GiuChoCleanupService cleanupService = new GiuChoCleanupService();
    
    public SeatSelectionPageDTO loadSeatSelectionPage(int chuyenTauId, int gaDiId, int gaDenId, Integer requestedToaTauId) throws SQLException {
        cleanupService.huyCacDonQuaHan();
        validateRoute(chuyenTauId, gaDiId, gaDenId);

        ChuyenTauSearchResultDTO trip = chuyenTauDAO.findTripDetailByIdAndRoute(chuyenTauId, gaDiId, gaDenId);
        if (trip == null) {
            throw new IllegalArgumentException("Không tìm thấy chuyến tàu hoặc chặng đã chọn không hợp lệ.");
        }

        List<ToaOptionDTO> toaList = toaTauDAO.findToaOptionsByChuyenAndRoute(chuyenTauId, gaDiId, gaDenId);
        if (toaList.isEmpty()) {
            throw new IllegalArgumentException("Chuyến tàu chưa có toa/ghế để hiển thị.");
        }

        int selectedToaTauId = chooseToaTauId(toaList, requestedToaTauId);
        for (ToaOptionDTO toa : toaList) {
            toa.setSelected(toa.getToaTauId() == selectedToaTauId);
        }

        List<GheTrangThaiDTO> gheList = gheDAO.findSeatsByToaAndRoute(chuyenTauId, selectedToaTauId, gaDiId, gaDenId);

        SeatSelectionPageDTO pageDTO = new SeatSelectionPageDTO();
        pageDTO.setTrip(trip);
        pageDTO.setToaList(toaList);
        pageDTO.setGheList(gheList);
        pageDTO.setSelectedToaTauId(selectedToaTauId);
        pageDTO.setGaDiId(gaDiId);
        pageDTO.setGaDenId(gaDenId);
        return pageDTO;
    }

    public GheTrangThaiDTO validateSelectedSeat(int chuyenTauId, int gaDiId, int gaDenId, int toaTauId, int gheId) throws SQLException {
        SeatSelectionPageDTO pageDTO = loadSeatSelectionPage(chuyenTauId, gaDiId, gaDenId, toaTauId);
        for (GheTrangThaiDTO ghe : pageDTO.getGheList()) {
            if (ghe.getGheId() == gheId) {
                if (!ghe.isChonDuoc()) {
                    throw new IllegalArgumentException("Ghế " + ghe.getSoGhe() + " không thể chọn: " + ghe.getTrangThaiHienThi());
                }
                return ghe;
            }
        }
        throw new IllegalArgumentException("Ghế đã chọn không thuộc toa/chuyến hiện tại.");
    }

    private void validateRoute(int chuyenTauId, int gaDiId, int gaDenId) {
        if (chuyenTauId <= 0) {
            throw new IllegalArgumentException("Thiếu mã chuyến tàu.");
        }
        if (gaDiId <= 0) {
            throw new IllegalArgumentException("Thiếu ga đi.");
        }
        if (gaDenId <= 0) {
            throw new IllegalArgumentException("Thiếu ga đến.");
        }
        if (gaDiId == gaDenId) {
            throw new IllegalArgumentException("Ga đi và ga đến không được trùng nhau.");
        }
    }

    private int chooseToaTauId(List<ToaOptionDTO> toaList, Integer requestedToaTauId) {
        if (requestedToaTauId != null && requestedToaTauId > 0) {
            for (ToaOptionDTO toa : toaList) {
                if (toa.getToaTauId() == requestedToaTauId) {
                    return requestedToaTauId;
                }
            }
        }
        return toaList.get(0).getToaTauId();
    }
}
