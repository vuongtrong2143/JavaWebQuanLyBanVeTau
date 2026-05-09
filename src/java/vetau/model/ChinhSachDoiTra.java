package vetau.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class ChinhSachDoiTra {
    private int id;
    private String tenChinhSach;
    private String loaiDonHangApDung;
    private String chieuTauApDung;
    private Integer truocKhoiHanhTuGio;
    private Integer truocKhoiHanhDenGio;
    private BigDecimal tyLeKhauTru;
    private BigDecimal phiDoiCoDinh;
    private boolean choPhepDoi;
    private boolean choPhepTra;
    private LocalDateTime hieuLucTu;
    private LocalDateTime hieuLucDen;
    private int doUuTien;
    private String trangThai;

    public ChinhSachDoiTra() {
    }

    public ChinhSachDoiTra(int id, String tenChinhSach, String loaiDonHangApDung, String chieuTauApDung, Integer truocKhoiHanhTuGio, Integer truocKhoiHanhDenGio, BigDecimal tyLeKhauTru, BigDecimal phiDoiCoDinh, boolean choPhepDoi, boolean choPhepTra, LocalDateTime hieuLucTu, LocalDateTime hieuLucDen, int doUuTien, String trangThai) {
        this.id = id;
        this.tenChinhSach = tenChinhSach;
        this.loaiDonHangApDung = loaiDonHangApDung;
        this.chieuTauApDung = chieuTauApDung;
        this.truocKhoiHanhTuGio = truocKhoiHanhTuGio;
        this.truocKhoiHanhDenGio = truocKhoiHanhDenGio;
        this.tyLeKhauTru = tyLeKhauTru;
        this.phiDoiCoDinh = phiDoiCoDinh;
        this.choPhepDoi = choPhepDoi;
        this.choPhepTra = choPhepTra;
        this.hieuLucTu = hieuLucTu;
        this.hieuLucDen = hieuLucDen;
        this.doUuTien = doUuTien;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenChinhSach() {
        return tenChinhSach;
    }

    public void setTenChinhSach(String tenChinhSach) {
        this.tenChinhSach = tenChinhSach;
    }

    public String getLoaiDonHangApDung() {
        return loaiDonHangApDung;
    }

    public void setLoaiDonHangApDung(String loaiDonHangApDung) {
        this.loaiDonHangApDung = loaiDonHangApDung;
    }

    public String getChieuTauApDung() {
        return chieuTauApDung;
    }

    public void setChieuTauApDung(String chieuTauApDung) {
        this.chieuTauApDung = chieuTauApDung;
    }

    public Integer getTruocKhoiHanhTuGio() {
        return truocKhoiHanhTuGio;
    }

    public void setTruocKhoiHanhTuGio(Integer truocKhoiHanhTuGio) {
        this.truocKhoiHanhTuGio = truocKhoiHanhTuGio;
    }

    public Integer getTruocKhoiHanhDenGio() {
        return truocKhoiHanhDenGio;
    }

    public void setTruocKhoiHanhDenGio(Integer truocKhoiHanhDenGio) {
        this.truocKhoiHanhDenGio = truocKhoiHanhDenGio;
    }

    public BigDecimal getTyLeKhauTru() {
        return tyLeKhauTru;
    }

    public void setTyLeKhauTru(BigDecimal tyLeKhauTru) {
        this.tyLeKhauTru = tyLeKhauTru;
    }

    public BigDecimal getPhiDoiCoDinh() {
        return phiDoiCoDinh;
    }

    public void setPhiDoiCoDinh(BigDecimal phiDoiCoDinh) {
        this.phiDoiCoDinh = phiDoiCoDinh;
    }

    public boolean isChoPhepDoi() {
        return choPhepDoi;
    }

    public void setChoPhepDoi(boolean choPhepDoi) {
        this.choPhepDoi = choPhepDoi;
    }

    public boolean isChoPhepTra() {
        return choPhepTra;
    }

    public void setChoPhepTra(boolean choPhepTra) {
        this.choPhepTra = choPhepTra;
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

    public int getDoUuTien() {
        return doUuTien;
    }

    public void setDoUuTien(int doUuTien) {
        this.doUuTien = doUuTien;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

}
