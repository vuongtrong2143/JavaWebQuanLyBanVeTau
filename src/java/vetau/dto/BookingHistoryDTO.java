package vetau.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BookingHistoryDTO {
    private int id;
    private String maDatCho;
    private String loaiDonHang;
    private String loaiHanhTrinh;
    private LocalDateTime ngayDat;
    private BigDecimal tongThanhToan;
    private String trangThaiDatCho;
    private String trangThaiThanhToan;
    private int soVe;

    public int getId() {
        return id;
    }

    public String getMaDatCho() {
        return maDatCho;
    }

    public String getLoaiDonHang() {
        return loaiDonHang;
    }

    public String getLoaiHanhTrinh() {
        return loaiHanhTrinh;
    }

    public LocalDateTime getNgayDat() {
        return ngayDat;
    }

    public BigDecimal getTongThanhToan() {
        return tongThanhToan;
    }

    public String getTrangThaiDatCho() {
        return trangThaiDatCho;
    }

    public String getTrangThaiThanhToan() {
        return trangThaiThanhToan;
    }

    public int getSoVe() {
        return soVe;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMaDatCho(String maDatCho) {
        this.maDatCho = maDatCho;
    }

    public void setLoaiDonHang(String loaiDonHang) {
        this.loaiDonHang = loaiDonHang;
    }

    public void setLoaiHanhTrinh(String loaiHanhTrinh) {
        this.loaiHanhTrinh = loaiHanhTrinh;
    }

    public void setNgayDat(LocalDateTime ngayDat) {
        this.ngayDat = ngayDat;
    }

    public void setTongThanhToan(BigDecimal tongThanhToan) {
        this.tongThanhToan = tongThanhToan;
    }

    public void setTrangThaiDatCho(String trangThaiDatCho) {
        this.trangThaiDatCho = trangThaiDatCho;
    }

    public void setTrangThaiThanhToan(String trangThaiThanhToan) {
        this.trangThaiThanhToan = trangThaiThanhToan;
    }

    public void setSoVe(int soVe) {
        this.soVe = soVe;
    }

    public String getNgayDatText() {
        if (ngayDat == null) {
            return "Chưa rõ";
        }
        return ngayDat.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    public String getTongThanhToanText() {
        if (tongThanhToan == null) {
            return "0 đ";
        }
        return String.format("%,.0f đ", tongThanhToan);
    }
}
