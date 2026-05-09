package vetau.dto;

import java.util.ArrayList;
import java.util.List;

public class SeatSelectionPageDTO {
    private ChuyenTauSearchResultDTO trip;
    private List<ToaOptionDTO> toaList = new ArrayList<>();
    private List<GheTrangThaiDTO> gheList = new ArrayList<>();
    private int selectedToaTauId;
    private int gaDiId;
    private int gaDenId;

    public ChuyenTauSearchResultDTO getTrip() {
        return trip;
    }

    public void setTrip(ChuyenTauSearchResultDTO trip) {
        this.trip = trip;
    }

    public List<ToaOptionDTO> getToaList() {
        return toaList;
    }

    public void setToaList(List<ToaOptionDTO> toaList) {
        this.toaList = toaList;
    }

    public List<GheTrangThaiDTO> getGheList() {
        return gheList;
    }

    public void setGheList(List<GheTrangThaiDTO> gheList) {
        this.gheList = gheList;
    }

    public int getSelectedToaTauId() {
        return selectedToaTauId;
    }

    public void setSelectedToaTauId(int selectedToaTauId) {
        this.selectedToaTauId = selectedToaTauId;
    }

    public int getGaDiId() {
        return gaDiId;
    }

    public void setGaDiId(int gaDiId) {
        this.gaDiId = gaDiId;
    }

    public int getGaDenId() {
        return gaDenId;
    }

    public void setGaDenId(int gaDenId) {
        this.gaDenId = gaDenId;
    }

    public ToaOptionDTO getSelectedToa() {
        if (toaList == null) {
            return null;
        }
        for (ToaOptionDTO toa : toaList) {
            if (toa.getToaTauId() == selectedToaTauId) {
                return toa;
            }
        }
        return null;
    }
}
