package vetau.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TicketCheckResultDTO {
    private boolean found;
    private String message;

    private String maVe;
    private String trangThai;
    private String tenHanhKhach;
    private String loaiGiayTo;
    private String soGiayTo;

    private String maChuyen;
    private String maTau;
    private String tenTau;
    private String tenGaDi;
    private String tenGaDen;
    private LocalDateTime thoiGianDi;
    private LocalDateTime thoiGianDen;

    private Integer soToa;
    private String soGhe;
    private BigDecimal giaVe;

    public static TicketCheckResultDTO notFound(String message) {
        TicketCheckResultDTO dto = new TicketCheckResultDTO();
        dto.setFound(false);
        dto.setMessage(message);
        return dto;
    }

    public boolean isFound() {
        return found;
    }

    public void setFound(boolean found) {
        this.found = found;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMaVe() {
        return maVe;
    }

    public void setMaVe(String maVe) {
        this.maVe = maVe;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getTenHanhKhach() {
        return tenHanhKhach;
    }

    public void setTenHanhKhach(String tenHanhKhach) {
        this.tenHanhKhach = tenHanhKhach;
    }

    public String getLoaiGiayTo() {
        return loaiGiayTo;
    }

    public void setLoaiGiayTo(String loaiGiayTo) {
        this.loaiGiayTo = loaiGiayTo;
    }

    public String getSoGiayTo() {
        return soGiayTo;
    }

    public void setSoGiayTo(String soGiayTo) {
        this.soGiayTo = soGiayTo;
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

    public String getTenGaDi() {
        return tenGaDi;
    }

    public void setTenGaDi(String tenGaDi) {
        this.tenGaDi = tenGaDi;
    }

    public String getTenGaDen() {
        return tenGaDen;
    }

    public void setTenGaDen(String tenGaDen) {
        this.tenGaDen = tenGaDen;
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

    public Integer getSoToa() {
        return soToa;
    }

    public void setSoToa(Integer soToa) {
        this.soToa = soToa;
    }

    public String getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(String soGhe) {
        this.soGhe = soGhe;
    }

    public BigDecimal getGiaVe() {
        return giaVe;
    }

    public void setGiaVe(BigDecimal giaVe) {
        this.giaVe = giaVe;
    }

    public String getThoiGianDiText() {
        return formatDateTime(thoiGianDi);
    }

    public String getThoiGianDenText() {
        return formatDateTime(thoiGianDen);
    }

    public String getGiaVeText() {
        if (giaVe == null) {
            return "Chưa có";
        }
        return String.format("%,.0f đ", giaVe);
    }

    private String formatDateTime(LocalDateTime value) {
        if (value == null) {
            return "Chưa rõ";
        }
        return value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
