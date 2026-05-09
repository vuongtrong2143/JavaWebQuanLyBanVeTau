/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vetau.dto;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class RefundAdminDTO {

    private int hoanTienId;
    private int veId;
    private Integer thanhToanId;

    private String maVe;
    private String maDatCho;
    private String tenKhachHang;
    private String emailKhachHang;
    private String soDienThoaiKhachHang;

    private String tenHanhKhach;
    private String soGiayTo;

    private BigDecimal soTienHoan;
    private String maGiaoDichHoan;
    private LocalDateTime thoiGianYeuCau;
    private LocalDateTime thoiGianHoanTat;
    private String trangThai;

    private String phuongThucThanhToan;
    private String maGiaoDichGoc;

    private String lyDoTraVe;
    private BigDecimal tyLeKhauTru;
    private BigDecimal phiDoiTra;

    public int getHoanTienId() {
        return hoanTienId;
    }

    public void setHoanTienId(int hoanTienId) {
        this.hoanTienId = hoanTienId;
    }

    public int getVeId() {
        return veId;
    }

    public void setVeId(int veId) {
        this.veId = veId;
    }

    public Integer getThanhToanId() {
        return thanhToanId;
    }

    public void setThanhToanId(Integer thanhToanId) {
        this.thanhToanId = thanhToanId;
    }

    public String getMaVe() {
        return maVe;
    }

    public void setMaVe(String maVe) {
        this.maVe = maVe;
    }

    public String getMaDatCho() {
        return maDatCho;
    }

    public void setMaDatCho(String maDatCho) {
        this.maDatCho = maDatCho;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public String getEmailKhachHang() {
        return emailKhachHang;
    }

    public void setEmailKhachHang(String emailKhachHang) {
        this.emailKhachHang = emailKhachHang;
    }

    public String getSoDienThoaiKhachHang() {
        return soDienThoaiKhachHang;
    }

    public void setSoDienThoaiKhachHang(String soDienThoaiKhachHang) {
        this.soDienThoaiKhachHang = soDienThoaiKhachHang;
    }

    public String getTenHanhKhach() {
        return tenHanhKhach;
    }

    public void setTenHanhKhach(String tenHanhKhach) {
        this.tenHanhKhach = tenHanhKhach;
    }

    public String getSoGiayTo() {
        return soGiayTo;
    }

    public void setSoGiayTo(String soGiayTo) {
        this.soGiayTo = soGiayTo;
    }

    public BigDecimal getSoTienHoan() {
        return soTienHoan;
    }

    public void setSoTienHoan(BigDecimal soTienHoan) {
        this.soTienHoan = soTienHoan;
    }

    public String getMaGiaoDichHoan() {
        return maGiaoDichHoan;
    }

    public void setMaGiaoDichHoan(String maGiaoDichHoan) {
        this.maGiaoDichHoan = maGiaoDichHoan;
    }

    public LocalDateTime getThoiGianYeuCau() {
        return thoiGianYeuCau;
    }

    public void setThoiGianYeuCau(LocalDateTime thoiGianYeuCau) {
        this.thoiGianYeuCau = thoiGianYeuCau;
    }

    public LocalDateTime getThoiGianHoanTat() {
        return thoiGianHoanTat;
    }

    public void setThoiGianHoanTat(LocalDateTime thoiGianHoanTat) {
        this.thoiGianHoanTat = thoiGianHoanTat;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getPhuongThucThanhToan() {
        return phuongThucThanhToan;
    }

    public void setPhuongThucThanhToan(String phuongThucThanhToan) {
        this.phuongThucThanhToan = phuongThucThanhToan;
    }

    public String getMaGiaoDichGoc() {
        return maGiaoDichGoc;
    }

    public void setMaGiaoDichGoc(String maGiaoDichGoc) {
        this.maGiaoDichGoc = maGiaoDichGoc;
    }

    public String getLyDoTraVe() {
        return lyDoTraVe;
    }

    public void setLyDoTraVe(String lyDoTraVe) {
        this.lyDoTraVe = lyDoTraVe;
    }

    public BigDecimal getTyLeKhauTru() {
        return tyLeKhauTru;
    }

    public void setTyLeKhauTru(BigDecimal tyLeKhauTru) {
        this.tyLeKhauTru = tyLeKhauTru;
    }

    public BigDecimal getPhiDoiTra() {
        return phiDoiTra;
    }

    public void setPhiDoiTra(BigDecimal phiDoiTra) {
        this.phiDoiTra = phiDoiTra;
    }

    public String getSoTienHoanText() {
        if (soTienHoan == null) {
            return "0 đ";
        }
        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        return nf.format(soTienHoan) + " đ";
    }

    public String getThoiGianYeuCauText() {
        return formatDateTime(thoiGianYeuCau);
    }

    public String getThoiGianHoanTatText() {
        return formatDateTime(thoiGianHoanTat);
    }

    private String formatDateTime(LocalDateTime value) {
        if (value == null) {
            return "-";
        }
        return value.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}