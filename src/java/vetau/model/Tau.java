package vetau.model;

public class Tau {
    private int id;
    private String maTau;
    private String tenTau;
    private String chieuDi;
    private boolean thuocTuyenThongNhat;
    private String moTa;
    private String trangThai;

    public Tau() {
    }

    public Tau(int id, String maTau, String tenTau, String chieuDi, boolean thuocTuyenThongNhat, String moTa, String trangThai) {
        this.id = id;
        this.maTau = maTau;
        this.tenTau = tenTau;
        this.chieuDi = chieuDi;
        this.thuocTuyenThongNhat = thuocTuyenThongNhat;
        this.moTa = moTa;
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaTau() {
        return maTau;
    }

    public void setMaTau(String maTau) {
        this.maTau = maTau;
    }

    public String getTenTau() {
        return tenTau;
    }

    public void setTenTau(String tenTau) {
        this.tenTau = tenTau;
    }

    public String getChieuDi() {
        return chieuDi;
    }

    public void setChieuDi(String chieuDi) {
        this.chieuDi = chieuDi;
    }

    public boolean isThuocTuyenThongNhat() {
        return thuocTuyenThongNhat;
    }

    public void setThuocTuyenThongNhat(boolean thuocTuyenThongNhat) {
        this.thuocTuyenThongNhat = thuocTuyenThongNhat;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }
    public String getTrangThai() { return trangThai; }
    public void setTrangThai(String trangThai) { this.trangThai = trangThai; }
}
