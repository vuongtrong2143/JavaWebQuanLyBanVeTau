package vetau.dto;

public class GheTrangThaiDTO {
    private int gheId;
    private int toaTauId;
    private String soGhe;
    private Integer tang;
    private String loaiCho;
    private String trangThaiGhe;
    private boolean biChiem;
    private String lyDoKhongChonDuoc;

    public int getGheId() {
        return gheId;
    }

    public void setGheId(int gheId) {
        this.gheId = gheId;
    }

    public int getToaTauId() {
        return toaTauId;
    }

    public void setToaTauId(int toaTauId) {
        this.toaTauId = toaTauId;
    }

    public String getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(String soGhe) {
        this.soGhe = soGhe;
    }

    public Integer getTang() {
        return tang;
    }

    public void setTang(Integer tang) {
        this.tang = tang;
    }

    public String getLoaiCho() {
        return loaiCho;
    }

    public void setLoaiCho(String loaiCho) {
        this.loaiCho = loaiCho;
    }

    public String getTrangThaiGhe() {
        return trangThaiGhe;
    }

    public void setTrangThaiGhe(String trangThaiGhe) {
        this.trangThaiGhe = trangThaiGhe;
    }

    public boolean isBiChiem() {
        return biChiem;
    }

    public void setBiChiem(boolean biChiem) {
        this.biChiem = biChiem;
    }

    public String getLyDoKhongChonDuoc() {
        return lyDoKhongChonDuoc;
    }

    public void setLyDoKhongChonDuoc(String lyDoKhongChonDuoc) {
        this.lyDoKhongChonDuoc = lyDoKhongChonDuoc;
    }

    public boolean isChonDuoc() {
        return "Hoạt động".equals(trangThaiGhe) && !biChiem;
    }

    public String getCssClass() {
        if (!"Hoạt động".equals(trangThaiGhe)) {
            return "seat blocked";
        }
        if (biChiem) {
            return "seat occupied";
        }
        return "seat available";
    }

    public String getTrangThaiHienThi() {
        if (!"Hoạt động".equals(trangThaiGhe)) {
            return trangThaiGhe;
        }
        if (biChiem) {
            return lyDoKhongChonDuoc == null || lyDoKhongChonDuoc.isBlank()
                    ? "Đã có khách"
                    : lyDoKhongChonDuoc;
        }
        return "Còn trống";
    }

    public String getMoTaTang() {
        return tang == null ? "" : "Tầng " + tang;
    }
}
