package vetau.dto;

public class ToaOptionDTO {
    private int toaTauId;
    private int soToa;
    private String loaiToa;
    private int sucChua;
    private int soGheHoatDong;
    private int soGheDaBiChiem;
    private boolean selected;

    public int getToaTauId() {
        return toaTauId;
    }

    public void setToaTauId(int toaTauId) {
        this.toaTauId = toaTauId;
    }

    public int getSoToa() {
        return soToa;
    }

    public void setSoToa(int soToa) {
        this.soToa = soToa;
    }

    public String getLoaiToa() {
        return loaiToa;
    }

    public void setLoaiToa(String loaiToa) {
        this.loaiToa = loaiToa;
    }

    public int getSucChua() {
        return sucChua;
    }

    public void setSucChua(int sucChua) {
        this.sucChua = sucChua;
    }

    public int getSoGheHoatDong() {
        return soGheHoatDong;
    }

    public void setSoGheHoatDong(int soGheHoatDong) {
        this.soGheHoatDong = soGheHoatDong;
    }

    public int getSoGheDaBiChiem() {
        return soGheDaBiChiem;
    }

    public void setSoGheDaBiChiem(int soGheDaBiChiem) {
        this.soGheDaBiChiem = soGheDaBiChiem;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getSoGheConLai() {
        int remain = soGheHoatDong - soGheDaBiChiem;
        return Math.max(remain, 0);
    }

    public String getTenHienThi() {
        return "Toa " + soToa + " - " + loaiToa;
    }
}
