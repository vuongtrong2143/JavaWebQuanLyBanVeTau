package vetau.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class KhuyenMai {
    private int id;
    private String maKhuyenMai;
    private String tenChuongTrinh;
    private BigDecimal phanTramGiam;
    private BigDecimal giamToiDa;
    private BigDecimal giaTriDonToiThieu;
    private String phuongThucTtApDung;
    private LocalDateTime ngayBatDau;
    private LocalDateTime ngayKetThuc;
    private Integer soLuongToiDa;
    private String trangThai;

    public KhuyenMai() {
    }

    public KhuyenMai(int id, String maKhuyenMai, String tenChuongTrinh, BigDecimal phanTramGiam, BigDecimal giamToiDa, BigDecimal giaTriDonToiThieu, String phuongThucTtApDung, LocalDateTime ngayBatDau, LocalDateTime ngayKetThuc, Integer soLuongToiDa, String trangThai) {
        this.id = id;
        this.maKhuyenMai = maKhuyenMai;
        this.tenChuongTrinh = tenChuongTrinh;
        this.phanTramGiam = phanTramGiam;
        this.giamToiDa = giamToiDa;
        this.giaTriDonToiThieu = giaTriDonToiThieu;
        this.phuongThucTtApDung = phuongThucTtApDung;
        this.ngayBatDau = ngayBatDau;
        this.ngayKetThuc = ngayKetThuc;
        this.soLuongToiDa = soLuongToiDa;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getPhuongThucTtApDung() {
        return phuongThucTtApDung;
    }

    public void setPhuongThucTtApDung(String phuongThucTtApDung) {
        this.phuongThucTtApDung = phuongThucTtApDung;
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

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

}
