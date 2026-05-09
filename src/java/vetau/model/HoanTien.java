package vetau.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class HoanTien {
    private int id;
    private int thanhToanId;
    private int veId;
    private BigDecimal soTienHoan;
    private String maGiaoDichHoan;
    private LocalDateTime thoiGianYeuCau;
    private LocalDateTime thoiGianHoanTat;
    private String trangThai;

    public HoanTien() {
    }

    public HoanTien(int id, int thanhToanId, int veId, BigDecimal soTienHoan, String maGiaoDichHoan, LocalDateTime thoiGianYeuCau, LocalDateTime thoiGianHoanTat, String trangThai) {
        this.id = id;
        this.thanhToanId = thanhToanId;
        this.veId = veId;
        this.soTienHoan = soTienHoan;
        this.maGiaoDichHoan = maGiaoDichHoan;
        this.thoiGianYeuCau = thoiGianYeuCau;
        this.thoiGianHoanTat = thoiGianHoanTat;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getThanhToanId() {
        return thanhToanId;
    }

    public void setThanhToanId(int thanhToanId) {
        this.thanhToanId = thanhToanId;
    }

    public int getVeId() {
        return veId;
    }

    public void setVeId(int veId) {
        this.veId = veId;
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

}
