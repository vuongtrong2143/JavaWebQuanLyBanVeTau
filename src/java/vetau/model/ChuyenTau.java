package vetau.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ChuyenTau {
    private int id;
    private int tauId;
    private String maChuyen;
    private LocalDate ngayChay;
    private LocalDateTime gioKhoiHanh;
    private LocalDateTime gioDenDuKien;
    private String trangThai;
    private String tenTau;
    
    public ChuyenTau() {
    }

    public ChuyenTau(int id, int tauId, String maChuyen, LocalDate ngayChay,
                     LocalDateTime gioKhoiHanh, LocalDateTime gioDenDuKien, String trangThai) {
        this.id = id;
        this.tauId = tauId;
        this.maChuyen = maChuyen;
        this.ngayChay = ngayChay;
        this.gioKhoiHanh = gioKhoiHanh;
        this.gioDenDuKien = gioDenDuKien;
        this.trangThai = trangThai;
    }
public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getTauId() { return tauId; }
    public void setTauId(int tauId) { this.tauId = tauId; }

    public String getMaChuyen() { return maChuyen; }
    public void setMaChuyen(String maChuyen) { this.maChuyen = maChuyen; }

    public LocalDate getNgayChay() { return ngayChay; }
    public void setNgayChay(LocalDate ngayChay) { this.ngayChay = ngayChay; }

    public LocalDateTime getGioKhoiHanh() { return gioKhoiHanh; }
    public void setGioKhoiHanh(LocalDateTime gioKhoiHanh) { this.gioKhoiHanh = gioKhoiHanh; }

    public LocalDateTime getGioDenDuKien() { return gioDenDuKien; }
    public void setGioDenDuKien(LocalDateTime gioDenDuKien) { this.gioDenDuKien = gioDenDuKien; }

    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }

    public String getTenTau() { return tenTau; }
    public void setTenTau(String tenTau) { this.tenTau = tenTau; }
}
