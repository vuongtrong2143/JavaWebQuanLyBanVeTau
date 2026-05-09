package vetau.model;

public class Ga {
    private int id;
    private String maGa;
    private String tenGa;
    private String tinhThanh;
    private Integer lyTrinhKm;
    private String diaChi;
    private String trangThai;
    public Ga() {
    }

    public Ga(int id, String maGa, String tenGa, String tinhThanh, Integer lyTrinhKm, String diaChi, String trangThai) {
        this.id = id;
        this.maGa = maGa;
        this.tenGa = tenGa;
        this.tinhThanh = tinhThanh;
        this.lyTrinhKm = lyTrinhKm;
        this.diaChi = diaChi;
        this.trangThai = trangThai;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMaGa() { return maGa; }
    public void setMaGa(String maGa) { this.maGa = maGa; }

    public String getTenGa() { return tenGa; }
    public void setTenGa(String tenGa) { this.tenGa = tenGa; }

    public String getTinhThanh() { return tinhThanh; }
    public void setTinhThanh(String tinhThanh) { this.tinhThanh = tinhThanh; }

    public Integer getLyTrinhKm() { return lyTrinhKm; }
    public void setLyTrinhKm(Integer lyTrinhKm) { this.lyTrinhKm = lyTrinhKm; }

    public String getDiaChi() { return diaChi; }
    public void setDiaChi(String diaChi) { this.diaChi = diaChi; }
    
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
