package vetau.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PromotionViewDTO {
    private String maKhuyenMai;
    private String tenChuongTrinh;
    private BigDecimal phanTramGiam;
    private BigDecimal giamToiDa;
    private BigDecimal giaTriDonToiThieu;
    private String phuongThucThanhToanApDung;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private Integer soLuongToiDa;

    public String getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(String maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }

    public String getTenChuongTrinh() {
        return tenChuongTrinh;
    }

    public void setTenChuongTrinh(String tenChuongTrinh) {
        this.tenChuongTrinh = tenChuongTrinh;
    }

    public BigDecimal getPhanTramGiam() {
        return phanTramGiam;
    }

    public void setPhanTramGiam(BigDecimal phanTramGiam) {
        this.phanTramGiam = phanTramGiam;
    }

    public BigDecimal getGiamToiDa() {
        return giamToiDa;
    }

    public void setGiamToiDa(BigDecimal giamToiDa) {
        this.giamToiDa = giamToiDa;
    }

    public BigDecimal getGiaTriDonToiThieu() {
        return giaTriDonToiThieu;
    }

    public void setGiaTriDonToiThieu(BigDecimal giaTriDonToiThieu) {
        this.giaTriDonToiThieu = giaTriDonToiThieu;
    }

    public String getPhuongThucThanhToanApDung() {
        return phuongThucThanhToanApDung;
    }

    public void setPhuongThucThanhToanApDung(String phuongThucThanhToanApDung) {
        this.phuongThucThanhToanApDung = phuongThucThanhToanApDung;
    }

    public LocalDateTime getNgayBatDau() {
        return ngayBatDau;
    }

    public void setNgayBatDau(LocalDateTime ngayBatDau) {
        this.ngayBatDau = ngayBatDau;
    }

    public LocalDateTime getNgayKetThuc() {
        return ngayKetThuc;
    }

    public void setNgayKetThuc(LocalDateTime ngayKetThuc) {
        this.ngayKetThuc = ngayKetThuc;
    }

    public Integer getSoLuongToiDa() {
        return soLuongToiDa;
    }

    public void setSoLuongToiDa(Integer soLuongToiDa) {
        this.soLuongToiDa = soLuongToiDa;
    }

    public String getPhanTramGiamText() {
        if (phanTramGiam == null) {
            return "0%";
        }
        return phanTramGiam.stripTrailingZeros().toPlainString() + "%";
    }

    public String getGiamToiDaText() {
        if (giamToiDa == null) {
            return "Không giới hạn";
        }
        return String.format("%,.0f đ", giamToiDa);
    }

    public String getGiaTriDonToiThieuText() {
        if (giaTriDonToiThieu == null || BigDecimal.ZERO.compareTo(giaTriDonToiThieu) == 0) {
            return "Không yêu cầu";
        }
        return String.format("%,.0f đ", giaTriDonToiThieu);
    }

    public String getThoiGianApDungText() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String tu = ngayBatDau == null ? "Không rõ" : ngayBatDau.format(formatter);
        String den = ngayKetThuc == null ? "Không rõ" : ngayKetThuc.format(formatter);
        return tu + " - " + den;
    }
}
