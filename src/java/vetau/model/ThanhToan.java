package vetau.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class ThanhToan {
    private int id;
    private int datChoId;
    private String maGiaoDich;
    private String requestId;
    private String phuongThuc;
    private BigDecimal soTien;
    private LocalDateTime ngayTao;
    private LocalDateTime ngayThanhToan;
    private String trangThai;

    public ThanhToan() {
    }

    public ThanhToan(int id, int datChoId, String maGiaoDich, String requestId, String phuongThuc, BigDecimal soTien, LocalDateTime ngayTao, LocalDateTime ngayThanhToan, String trangThai) {
        this.id = id;
        this.datChoId = datChoId;
        this.maGiaoDich = maGiaoDich;
        this.requestId = requestId;
        this.phuongThuc = phuongThuc;
        this.soTien = soTien;
        this.ngayTao = ngayTao;
        this.ngayThanhToan = ngayThanhToan;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDatChoId() {
        return datChoId;
    }

    public void setDatChoId(int datChoId) {
        this.datChoId = datChoId;
    }

    public String getMaGiaoDich() {
        return maGiaoDich;
    }

    public void setMaGiaoDich(String maGiaoDich) {
        this.maGiaoDich = maGiaoDich;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getPhuongThuc() {
        return phuongThuc;
    }

    public void setPhuongThuc(String phuongThuc) {
        this.phuongThuc = phuongThuc;
    }

    public BigDecimal getSoTien() {
        return soTien;
    }

    public void setSoTien(BigDecimal soTien) {
        this.soTien = soTien;
    }

    public LocalDateTime getNgayTao() {
        return ngayTao;
    }

    public void setNgayTao(LocalDateTime ngayTao) {
        this.ngayTao = ngayTao;
    }

    public LocalDateTime getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(LocalDateTime ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

}
