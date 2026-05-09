package vetau.model;
import java.time.LocalDate;
public class HanhKhach {
    private int id;
    private String hoTen;
    private String loaiGiayTo;
    private String soGiayTo;
    private LocalDate ngaySinh;
    private String quocTich;

    public HanhKhach() {
    }

    public HanhKhach(int id, String hoTen, String loaiGiayTo, String soGiayTo, LocalDate ngaySinh, String quocTich) {
        this.id = id;
        this.hoTen = hoTen;
        this.loaiGiayTo = loaiGiayTo;
        this.soGiayTo = soGiayTo;
        this.ngaySinh = ngaySinh;
        this.quocTich = quocTich;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getLoaiGiayTo() {
        return loaiGiayTo;
    }

    public void setLoaiGiayTo(String loaiGiayTo) {
        this.loaiGiayTo = loaiGiayTo;
    }

    public String getSoGiayTo() {
        return soGiayTo;
    }

    public void setSoGiayTo(String soGiayTo) {
        this.soGiayTo = soGiayTo;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public String getQuocTich() {
        return quocTich;
    }

    public void setQuocTich(String quocTich) {
        this.quocTich = quocTich;
    }

}
