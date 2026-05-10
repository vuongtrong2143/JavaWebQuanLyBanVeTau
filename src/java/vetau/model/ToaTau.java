package vetau.model;

public class ToaTau {
    private int id;
    private int tauId;
    private int soToa;
    private String loaiToa;
    private int sucChua;
    private String trangThai;
    
    public ToaTau() {
    }

    public ToaTau(int id, int tauId, int soToa, String loaiToa, int sucChua, String trangThai) {
        this.id = id;
        this.tauId = tauId;
        this.soToa = soToa;
        this.loaiToa = loaiToa;
        this.sucChua = sucChua;
        this.trangThai = trangThai ;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTauId() {
        return tauId;
    }

    public void setTauId(int tauId) {
        this.tauId = tauId;
    }

    public int getSoToa() {
        return soToa;
    }

    public void setSoToa(int soToa) {
        this.soToa = soToa;
    }

    public String getLoaiToa() {
        return loaiToa;
    }

    public void setLoaiToa(String loaiToa) {
        this.loaiToa = loaiToa;
    }

    public int getSucChua() {
        return sucChua;
    }

    public void setSucChua(int sucChua) {
        this.sucChua = sucChua;
    }
    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
