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

public class RecentBookingDTO {

    private int id;
    private String maDatCho;
    private String tenKhachHang;
    private LocalDateTime ngayDat;
    private BigDecimal tongThanhToan;
    private String trangThai;

    public int getId() {
        return id;
    }

    public String getMaDatCho() {
        return maDatCho;
    }

    public String getTenKhachHang() {
        return tenKhachHang;
    }

    public LocalDateTime getNgayDat() {
        return ngayDat;
    }

    public BigDecimal getTongThanhToan() {
        return tongThanhToan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMaDatCho(String maDatCho) {
        this.maDatCho = maDatCho;
    }

    public void setTenKhachHang(String tenKhachHang) {
        this.tenKhachHang = tenKhachHang;
    }

    public void setNgayDat(LocalDateTime ngayDat) {
        this.ngayDat = ngayDat;
    }

    public void setTongThanhToan(BigDecimal tongThanhToan) {
        this.tongThanhToan = tongThanhToan;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getNgayDatText() {
        if (ngayDat == null) {
            return "-";
        }

        return ngayDat.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getTongThanhToanText() {
        if (tongThanhToan == null) {
            return "0 đ";
        }

        NumberFormat nf = NumberFormat.getInstance(new Locale("vi", "VN"));
        return nf.format(tongThanhToan) + " đ";
    }
}