package vetau.model;

public class Ghe {
    private int id;
    private int toaTauId;
    private String soGhe;
    private Integer tang;
    private String loaiCho;
    private String trangThai;

    public Ghe() {
    }

    public Ghe(int id, int toaTauId, String soGhe, Integer tang, String loaiCho, String trangThai) {
        this.id = id;
        this.toaTauId = toaTauId;
        this.soGhe = soGhe;
        this.tang = tang;
        this.loaiCho = loaiCho;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getToaTauId() {
        return toaTauId;
    }

    public void setToaTauId(int toaTauId) {
        this.toaTauId = toaTauId;
    }

    public String getSoGhe() {
        return soGhe;
    }

    public void setSoGhe(String soGhe) {
        this.soGhe = soGhe;
    }

    public Integer getTang() {
        return tang;
    }

    public void setTang(Integer tang) {
        this.tang = tang;
    }

    public String getLoaiCho() {
        return loaiCho;
    }

    public void setLoaiCho(String loaiCho) {
        this.loaiCho = loaiCho;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}
