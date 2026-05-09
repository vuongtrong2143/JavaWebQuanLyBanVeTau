package vetau.model;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public class DatCho {
    private int id;
    private int khachHangId;
    private Integer khuyenMaiId;
    private String maDatCho;
    private String loaiDonHang;
    private String loaiHanhTrinh;
    private LocalDateTime ngayDat;
    private BigDecimal tongTienVeGoc;
    private BigDecimal thueVat;
    private BigDecimal phiThanhToan;
    private BigDecimal tongGiamKhuyenMai;
    private BigDecimal giamGiaKhuHoi;
    private BigDecimal tongThanhToan;
    private LocalDateTime thoiGianHetHan;
    private String trangThai;

    public DatCho() {
    }

    public DatCho(int id, int khachHangId, Integer khuyenMaiId, String maDatCho, String loaiDonHang, String loaiHanhTrinh, LocalDateTime ngayDat, BigDecimal tongTienVeGoc, BigDecimal thueVat, BigDecimal phiThanhToan, BigDecimal tongGiamKhuyenMai, BigDecimal giamGiaKhuHoi, BigDecimal tongThanhToan, LocalDateTime thoiGianHetHan, String trangThai) {
        this.id = id;
        this.khachHangId = khachHangId;
        this.khuyenMaiId = khuyenMaiId;
        this.maDatCho = maDatCho;
        this.loaiDonHang = loaiDonHang;
        this.loaiHanhTrinh = loaiHanhTrinh;
        this.ngayDat = ngayDat;
        this.tongTienVeGoc = tongTienVeGoc;
        this.thueVat = thueVat;
        this.phiThanhToan = phiThanhToan;
        this.tongGiamKhuyenMai = tongGiamKhuyenMai;
        this.giamGiaKhuHoi = giamGiaKhuHoi;
        this.tongThanhToan = tongThanhToan;
        this.thoiGianHetHan = thoiGianHetHan;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKhachHangId() {
        return khachHangId;
    }

    public void setKhachHangId(int khachHangId) {
        this.khachHangId = khachHangId;
    }

    public Integer getKhuyenMaiId() {
        return khuyenMaiId;
    }

    public void setKhuyenMaiId(Integer khuyenMaiId) {
        this.khuyenMaiId = khuyenMaiId;
    }

    public String getMaDatCho() {
        return maDatCho;
    }

    public void setMaDatCho(String maDatCho) {
        this.maDatCho = maDatCho;
    }

    public String getLoaiDonHang() {
        return loaiDonHang;
    }

    public void setLoaiDonHang(String loaiDonHang) {
        this.loaiDonHang = loaiDonHang;
    }

    public String getLoaiHanhTrinh() {
        return loaiHanhTrinh;
    }

    public void setLoaiHanhTrinh(String loaiHanhTrinh) {
        this.loaiHanhTrinh = loaiHanhTrinh;
    }

    public LocalDateTime getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(LocalDateTime ngayDat) {
        this.ngayDat = ngayDat;
    }

    public BigDecimal getTongTienVeGoc() {
        return tongTienVeGoc;
    }

    public void setTongTienVeGoc(BigDecimal tongTienVeGoc) {
        this.tongTienVeGoc = tongTienVeGoc;
    }

    public BigDecimal getThueVat() {
        return thueVat;
    }

    public void setThueVat(BigDecimal thueVat) {
        this.thueVat = thueVat;
    }

    public BigDecimal getPhiThanhToan() {
        return phiThanhToan;
    }

    public void setPhiThanhToan(BigDecimal phiThanhToan) {
        this.phiThanhToan = phiThanhToan;
    }

    public BigDecimal getTongGiamKhuyenMai() {
        return tongGiamKhuyenMai;
    }

    public void setTongGiamKhuyenMai(BigDecimal tongGiamKhuyenMai) {
        this.tongGiamKhuyenMai = tongGiamKhuyenMai;
    }

    public BigDecimal getGiamGiaKhuHoi() {
        return giamGiaKhuHoi;
    }

    public void setGiamGiaKhuHoi(BigDecimal giamGiaKhuHoi) {
        this.giamGiaKhuHoi = giamGiaKhuHoi;
    }

    public BigDecimal getTongThanhToan() {
        return tongThanhToan;
    }

    public void setTongThanhToan(BigDecimal tongThanhToan) {
        this.tongThanhToan = tongThanhToan;
    }

    public LocalDateTime getThoiGianHetHan() {
        return thoiGianHetHan;
    }

    public void setThoiGianHetHan(LocalDateTime thoiGianHetHan) {
        this.thoiGianHetHan = thoiGianHetHan;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

}
