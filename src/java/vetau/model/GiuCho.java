package vetau.model;
import java.time.LocalDateTime;
public class GiuCho {
    private int id;
    private int datChoId;
    private int chuyenTauId;
    private int gheId;
    private int gaDiId;
    private int gaDenId;
    private LocalDateTime thoiGianGiu;
    private LocalDateTime thoiGianHetHan;
    private String trangThai;

    public GiuCho() {
    }

    public GiuCho(int id, int datChoId, int chuyenTauId, int gheId, int gaDiId, int gaDenId, LocalDateTime thoiGianGiu, LocalDateTime thoiGianHetHan, String trangThai) {
        this.id = id;
        this.datChoId = datChoId;
        this.chuyenTauId = chuyenTauId;
        this.gheId = gheId;
        this.gaDiId = gaDiId;
        this.gaDenId = gaDenId;
        this.thoiGianGiu = thoiGianGiu;
        this.thoiGianHetHan = thoiGianHetHan;
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

    public LocalDateTime getThoiGianGiu() {
        return thoiGianGiu;
    }

    public void setThoiGianGiu(LocalDateTime thoiGianGiu) {
        this.thoiGianGiu = thoiGianGiu;
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
