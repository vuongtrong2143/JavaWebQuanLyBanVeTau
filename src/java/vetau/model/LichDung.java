package vetau.model;

import java.time.LocalDateTime;

public class LichDung {
    private int id;
    private int chuyenTauId;
    private int gaId;
    private int thuTuDung;
    private LocalDateTime thoiGianDen;
    private LocalDateTime thoiGianDi;

    // Thuộc tính phụ (JOIN từ DB) để hiển thị
    private String maChuyen;
    private String tenGa;
    
    public LichDung() {
    }

    public LichDung(int id, int chuyenTauId, int gaId, int thuTuDung,
                    LocalDateTime thoiGianDen, LocalDateTime thoiGianDi) {
        this.id = id;
        this.chuyenTauId = chuyenTauId;
        this.gaId = gaId;
        this.thuTuDung = thuTuDung;
        this.thoiGianDen = thoiGianDen;
        this.thoiGianDi = thoiGianDi;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getChuyenTauId() {
        return chuyenTauId;
    }

    public void setChuyenTauId(int chuyenTauId) {
        this.chuyenTauId = chuyenTauId;
    }

    public int getGaId() {
        return gaId;
    }

    public void setGaId(int gaId) {
        this.gaId = gaId;
    }

    public int getThuTuDung() {
        return thuTuDung;
    }

    public void setThuTuDung(int thuTuDung) {
        this.thuTuDung = thuTuDung;
    }

    public LocalDateTime getThoiGianDen() {
        return thoiGianDen;
    }

    public void setThoiGianDen(LocalDateTime thoiGianDen) {
        this.thoiGianDen = thoiGianDen;
    }

    public LocalDateTime getThoiGianDi() {
        return thoiGianDi;
    }

    public void setThoiGianDi(LocalDateTime thoiGianDi) {
        this.thoiGianDi = thoiGianDi;
    }
    public String getMaChuyen() { return maChuyen; }
    public void setMaChuyen(String maChuyen) { this.maChuyen = maChuyen; }

    public String getTenGa() { return tenGa; }
    public void setTenGa(String tenGa) { this.tenGa = tenGa; }
}
