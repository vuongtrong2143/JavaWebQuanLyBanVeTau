package vetau.dto;

import java.io.Serializable;

public class CustomerSessionDTO implements Serializable {
    private int id;
    private String hoTen;
    private String email;
    private String soDienThoai;

    public CustomerSessionDTO() {
    }

    public CustomerSessionDTO(int id, String hoTen, String email, String soDienThoai) {
        this.id = id;
        this.hoTen = hoTen;
        this.email = email;
        this.soDienThoai = soDienThoai;
    }

    public int getId() {
        return id;
    }

    public String getHoTen() {
        return hoTen;
    }

    public String getEmail() {
        return email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
}
