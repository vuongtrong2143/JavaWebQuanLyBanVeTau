package vetau.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class BangGia {
    private int id;
    private int gaDiId;
    private int gaDenId;
    private String loaiToaApDung;
    private Integer tangApDung;
    private BigDecimal giaCoSo;
    private BigDecimal phuThuCaoDiemMacDinh;
    private LocalDateTime hieuLucTu;
    private LocalDateTime hieuLucDen;
    private String trangThai;
    
    private String tenGaDi;
    private String tenGaDen;
    
    public BangGia() {
    }

    public BangGia(int id, int gaDiId, int gaDenId, String loaiToaApDung, Integer tangApDung, BigDecimal giaCoSo, BigDecimal phuThuCaoDiemMacDinh, LocalDateTime hieuLucTu, LocalDateTime hieuLucDen, String trangThai) {
        this.id = id;
        this.gaDiId = gaDiId;
        this.gaDenId = gaDenId;
        this.loaiToaApDung = loaiToaApDung;
        this.tangApDung = tangApDung;
        this.giaCoSo = giaCoSo;
        this.phuThuCaoDiemMacDinh = phuThuCaoDiemMacDinh;
        this.hieuLucTu = hieuLucTu;
        this.hieuLucDen = hieuLucDen;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGaDiId() {
        return gaDiId;
    }

    public void setGaDiId(int gaDiId) {
        this.gaDiId = gaDiId;
    }

    public int getGaDenId() {
        return gaDenId;
    }

    public void setGaDenId(int gaDenId) {
        this.gaDenId = gaDenId;
    }

    public String getLoaiToaApDung() {
        return loaiToaApDung;
    }

    public void setLoaiToaApDung(String loaiToaApDung) {
        this.loaiToaApDung = loaiToaApDung;
    }

    public Integer getTangApDung() {
        return tangApDung;
    }

    public void setTangApDung(Integer tangApDung) {
        this.tangApDung = tangApDung;
    }

    public BigDecimal getGiaCoSo() {
        return giaCoSo;
    }

    public void setGiaCoSo(BigDecimal giaCoSo) {
        this.giaCoSo = giaCoSo;
    }

    public BigDecimal getPhuThuCaoDiemMacDinh() {
        return phuThuCaoDiemMacDinh;
    }

    public void setPhuThuCaoDiemMacDinh(BigDecimal phuThuCaoDiemMacDinh) {
        this.phuThuCaoDiemMacDinh = phuThuCaoDiemMacDinh;
    }

    public LocalDateTime getHieuLucTu() {
        return hieuLucTu;
    }

    public void setHieuLucTu(LocalDateTime hieuLucTu) {
        this.hieuLucTu = hieuLucTu;
    }

    public LocalDateTime getHieuLucDen() {
        return hieuLucDen;
    }

    public void setHieuLucDen(LocalDateTime hieuLucDen) {
        this.hieuLucDen = hieuLucDen;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
    public String getTenGaDi() { return tenGaDi; }
    public void setTenGaDi(String tenGaDi) { this.tenGaDi = tenGaDi; }

    public String getTenGaDen() { return tenGaDen; }
    public void setTenGaDen(String tenGaDen) { this.tenGaDen = tenGaDen; }
}
