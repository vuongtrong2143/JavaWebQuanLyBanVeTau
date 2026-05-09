package vetau.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ChuyenTauSearchResultDTO {
    private int chuyenTauId;
    private String maChuyen;
    private String maTau;
    private String tenTau;
    private String gaDiTen;
    private String gaDenTen;
    private LocalDateTime thoiGianDi;
    private LocalDateTime thoiGianDen;
    private int thuTuGaDi;
    private int thuTuGaDen;
    private BigDecimal giaThapNhat;
    private int soGheHoatDong;
    private int soGheDaBiChiem;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public int getChuyenTauId() {
        return chuyenTauId;
    }

    public void setChuyenTauId(int chuyenTauId) {
        this.chuyenTauId = chuyenTauId;
    }

    public String getMaChuyen() {
        return maChuyen;
    }

    public void setMaChuyen(String maChuyen) {
        this.maChuyen = maChuyen;
    }

    public String getMaTau() {
        return maTau;
    }

    public void setMaTau(String maTau) {
        this.maTau = maTau;
    }

    public String getTenTau() {
        return tenTau;
    }

    public void setTenTau(String tenTau) {
        this.tenTau = tenTau;
    }

    public String getGaDiTen() {
        return gaDiTen;
    }

    public void setGaDiTen(String gaDiTen) {
        this.gaDiTen = gaDiTen;
    }

    public String getGaDenTen() {
        return gaDenTen;
    }

    public void setGaDenTen(String gaDenTen) {
        this.gaDenTen = gaDenTen;
    }

    public LocalDateTime getThoiGianDi() {
        return thoiGianDi;
    }

    public void setThoiGianDi(LocalDateTime thoiGianDi) {
        this.thoiGianDi = thoiGianDi;
    }

    public LocalDateTime getThoiGianDen() {
        return thoiGianDen;
    }

    public void setThoiGianDen(LocalDateTime thoiGianDen) {
        this.thoiGianDen = thoiGianDen;
    }

    public int getThuTuGaDi() {
        return thuTuGaDi;
    }

    public void setThuTuGaDi(int thuTuGaDi) {
        this.thuTuGaDi = thuTuGaDi;
    }

    public int getThuTuGaDen() {
        return thuTuGaDen;
    }

    public void setThuTuGaDen(int thuTuGaDen) {
        this.thuTuGaDen = thuTuGaDen;
    }

    public BigDecimal getGiaThapNhat() {
        return giaThapNhat;
    }

    public void setGiaThapNhat(BigDecimal giaThapNhat) {
        this.giaThapNhat = giaThapNhat;
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

    public int getSoGheConLai() {
        int conLai = soGheHoatDong - soGheDaBiChiem;
        return Math.max(conLai, 0);
    }

    public String getThoiGianDiText() {
        return thoiGianDi == null ? "" : thoiGianDi.format(DATE_TIME_FORMATTER);
    }

    public String getThoiGianDenText() {
        return thoiGianDen == null ? "" : thoiGianDen.format(DATE_TIME_FORMATTER);
    }

    public String getGiaThapNhatText() {
        if (giaThapNhat == null) {
            return "Chưa cấu hình giá";
        }
        return NumberFormat.getCurrencyInstance(new Locale("vi", "VN")).format(giaThapNhat);
    }
}
