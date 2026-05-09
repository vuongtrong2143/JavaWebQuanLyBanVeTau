package vetau.model;
import java.math.BigDecimal;
public class Ve {
    private int id;
    private int datChoId;
    private int chuyenTauId;
    private int gheId;
    private int hanhKhachId;
    private Integer doiTuongUuDaiId;
    private int gaDiId;
    private int gaDenId;
    private String maVe;
    private BigDecimal giaCoSo;
    private BigDecimal giamDoiTuong;
    private BigDecimal phuThuCaoDiem;
    private BigDecimal giaVeChiTiet;
    private String trangThai;

    public Ve() {
    }

    public Ve(int id, int datChoId, int chuyenTauId, int gheId, int hanhKhachId, Integer doiTuongUuDaiId, int gaDiId, int gaDenId, String maVe, BigDecimal giaCoSo, BigDecimal giamDoiTuong, BigDecimal phuThuCaoDiem, BigDecimal giaVeChiTiet, String trangThai) {
        this.id = id;
        this.datChoId = datChoId;
        this.chuyenTauId = chuyenTauId;
        this.gheId = gheId;
        this.hanhKhachId = hanhKhachId;
        this.doiTuongUuDaiId = doiTuongUuDaiId;
        this.gaDiId = gaDiId;
        this.gaDenId = gaDenId;
        this.maVe = maVe;
        this.giaCoSo = giaCoSo;
        this.giamDoiTuong = giamDoiTuong;
        this.phuThuCaoDiem = phuThuCaoDiem;
        this.giaVeChiTiet = giaVeChiTiet;
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

    public int getChuyenTauId() {
        return chuyenTauId;
    }

    public void setChuyenTauId(int chuyenTauId) {
        this.chuyenTauId = chuyenTauId;
    }

    public int getGheId() {
        return gheId;
    }

    public void setGheId(int gheId) {
        this.gheId = gheId;
    }

    public int getHanhKhachId() {
        return hanhKhachId;
    }

    public void setHanhKhachId(int hanhKhachId) {
        this.hanhKhachId = hanhKhachId;
    }

    public Integer getDoiTuongUuDaiId() {
        return doiTuongUuDaiId;
    }

    public void setDoiTuongUuDaiId(Integer doiTuongUuDaiId) {
        this.doiTuongUuDaiId = doiTuongUuDaiId;
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

    public String getMaVe() {
        return maVe;
    }

    public void setMaVe(String maVe) {
        this.maVe = maVe;
    }

    public BigDecimal getGiaCoSo() {
        return giaCoSo;
    }

    public void setGiaCoSo(BigDecimal giaCoSo) {
        this.giaCoSo = giaCoSo;
    }

    public BigDecimal getGiamDoiTuong() {
        return giamDoiTuong;
    }

    public void setGiamDoiTuong(BigDecimal giamDoiTuong) {
        this.giamDoiTuong = giamDoiTuong;
    }

    public BigDecimal getPhuThuCaoDiem() {
        return phuThuCaoDiem;
    }

    public void setPhuThuCaoDiem(BigDecimal phuThuCaoDiem) {
        this.phuThuCaoDiem = phuThuCaoDiem;
    }

    public BigDecimal getGiaVeChiTiet() {
        return giaVeChiTiet;
    }

    public void setGiaVeChiTiet(BigDecimal giaVeChiTiet) {
        this.giaVeChiTiet = giaVeChiTiet;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

}
