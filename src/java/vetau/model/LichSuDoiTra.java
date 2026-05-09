package vetau.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class LichSuDoiTra {
    private int id;
    private int veId;
    private Integer nhanVienId;
    private Integer chinhSachId;
    private String loaiGiaoDich;
    private String lyDo;
    private BigDecimal phiDoi;
    private BigDecimal tyLeKhauTru;
    private BigDecimal soTienHoan;
    private LocalDateTime thoiGianXuLy;
    private String ghiChu;

    public LichSuDoiTra() {
    }

    public LichSuDoiTra(int id, int veId, Integer nhanVienId, Integer chinhSachId, String loaiGiaoDich, String lyDo, BigDecimal phiDoi, BigDecimal tyLeKhauTru, BigDecimal soTienHoan, LocalDateTime thoiGianXuLy, String ghiChu) {
        this.id = id;
        this.veId = veId;
        this.nhanVienId = nhanVienId;
        this.chinhSachId = chinhSachId;
        this.loaiGiaoDich = loaiGiaoDich;
        this.lyDo = lyDo;
        this.phiDoi = phiDoi;
        this.tyLeKhauTru = tyLeKhauTru;
        this.soTienHoan = soTienHoan;
        this.thoiGianXuLy = thoiGianXuLy;
        this.ghiChu = ghiChu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getVeId() {
        return veId;
    }

    public void setVeId(int veId) {
        this.veId = veId;
    }

    public Integer getNhanVienId() {
        return nhanVienId;
    }

    public void setNhanVienId(Integer nhanVienId) {
        this.nhanVienId = nhanVienId;
    }

    public Integer getChinhSachId() {
        return chinhSachId;
    }

    public void setChinhSachId(Integer chinhSachId) {
        this.chinhSachId = chinhSachId;
    }

    public String getLoaiGiaoDich() {
        return loaiGiaoDich;
    }

    public void setLoaiGiaoDich(String loaiGiaoDich) {
        this.loaiGiaoDich = loaiGiaoDich;
    }

    public String getLyDo() {
        return lyDo;
    }

    public void setLyDo(String lyDo) {
        this.lyDo = lyDo;
    }

    public BigDecimal getPhiDoi() {
        return phiDoi;
    }

    public void setPhiDoi(BigDecimal phiDoi) {
        this.phiDoi = phiDoi;
    }

    public BigDecimal getTyLeKhauTru() {
        return tyLeKhauTru;
    }

    public void setTyLeKhauTru(BigDecimal tyLeKhauTru) {
        this.tyLeKhauTru = tyLeKhauTru;
    }

    public BigDecimal getSoTienHoan() {
        return soTienHoan;
    }

    public void setSoTienHoan(BigDecimal soTienHoan) {
        this.soTienHoan = soTienHoan;
    }

    public LocalDateTime getThoiGianXuLy() {
        return thoiGianXuLy;
    }

    public void setThoiGianXuLy(LocalDateTime thoiGianXuLy) {
        this.thoiGianXuLy = thoiGianXuLy;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

}
