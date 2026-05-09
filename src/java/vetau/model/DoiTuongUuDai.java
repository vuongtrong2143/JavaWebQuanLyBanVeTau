package vetau.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class DoiTuongUuDai {
    private int id;
    private String maDoiTuong;
    private String tenDoiTuong;
    private BigDecimal phanTramGiam;
    private Integer tuoiMin;
    private Integer tuoiMax;
    private boolean canGiayToChungMinh;
    private LocalDateTime hieuLucTu;
    private LocalDateTime hieuLucDen;
    private String trangThai;

    public DoiTuongUuDai() {
    }

    public DoiTuongUuDai(int id, String maDoiTuong, String tenDoiTuong, BigDecimal phanTramGiam, Integer tuoiMin, Integer tuoiMax, boolean canGiayToChungMinh, LocalDateTime hieuLucTu, LocalDateTime hieuLucDen, String trangThai) {
        this.id = id;
        this.maDoiTuong = maDoiTuong;
        this.tenDoiTuong = tenDoiTuong;
        this.phanTramGiam = phanTramGiam;
        this.tuoiMin = tuoiMin;
        this.tuoiMax = tuoiMax;
        this.canGiayToChungMinh = canGiayToChungMinh;
        this.hieuLucTu = hieuLucTu;
        this.hieuLucDen = hieuLucDen;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaDoiTuong() {
        return maDoiTuong;
    }

    public void setMaDoiTuong(String maDoiTuong) {
        this.maDoiTuong = maDoiTuong;
    }

    public String getTenDoiTuong() {
        return tenDoiTuong;
    }

    public void setTenDoiTuong(String tenDoiTuong) {
        this.tenDoiTuong = tenDoiTuong;
    }

    public BigDecimal getPhanTramGiam() {
        return phanTramGiam;
    }

    public void setPhanTramGiam(BigDecimal phanTramGiam) {
        this.phanTramGiam = phanTramGiam;
    }

    public Integer getTuoiMin() {
        return tuoiMin;
    }

    public void setTuoiMin(Integer tuoiMin) {
        this.tuoiMin = tuoiMin;
    }

    public Integer getTuoiMax() {
        return tuoiMax;
    }

    public void setTuoiMax(Integer tuoiMax) {
        this.tuoiMax = tuoiMax;
    }

    public boolean isCanGiayToChungMinh() {
        return canGiayToChungMinh;
    }

    public void setCanGiayToChungMinh(boolean canGiayToChungMinh) {
        this.canGiayToChungMinh = canGiayToChungMinh;
    }

    public LocalDateTime getHieuLucTu() {
        return hieuLucTu;
    }

    public void setHieuLucTu(LocalDateTime hieuLucTu) {
        this.hieuLucTu = hieuLucTu;
    }

    public LocalDateTime getHieuLucDen() {
        return hieuLucDen;
    }

    public void setHieuLucDen(LocalDateTime hieuLucDen) {
        this.hieuLucDen = hieuLucDen;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

}
