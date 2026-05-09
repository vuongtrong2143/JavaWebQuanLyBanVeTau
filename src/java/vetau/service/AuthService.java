package vetau.service;

import vetau.dao.KhachHangDAO;
import vetau.dto.CustomerSessionDTO;
import vetau.model.KhachHang;
import vetau.util.PasswordUtil;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.regex.Pattern;

public class AuthService {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[0-9]{9,15}$");

    private final KhachHangDAO khachHangDAO = new KhachHangDAO();

    public CustomerSessionDTO dangNhapKhachHang(String email, String matKhau) throws SQLException {
        if (isBlank(email) || isBlank(matKhau)) {
            throw new IllegalArgumentException("Vui lòng nhập email và mật khẩu.");
        }

        KhachHang khachHang = khachHangDAO.findByEmail(email.trim());

        if (khachHang == null || !"Hoạt động".equalsIgnoreCase(khachHang.getTrangThai())) {
            throw new IllegalArgumentException("Email hoặc mật khẩu không đúng.");
        }

        if (!PasswordUtil.matches(matKhau, khachHang.getMatKhauHash())) {
            throw new IllegalArgumentException("Email hoặc mật khẩu không đúng.");
        }

        return toSessionDTO(khachHang);
    }

    public CustomerSessionDTO dangKyKhachHang(String hoTen, String email, String soDienThoai,
                                              String matKhau, String xacNhanMatKhau)
            throws SQLException {
        validateRegisterInput(hoTen, email, soDienThoai, matKhau, xacNhanMatKhau);

        KhachHang khachHang = new KhachHang();
        khachHang.setHoTen(hoTen.trim());
        khachHang.setEmail(email.trim().toLowerCase());
        khachHang.setSoDienThoai(blankToNull(soDienThoai));
        khachHang.setMatKhauHash(PasswordUtil.hashPassword(matKhau));
        khachHang.setTrangThai("Hoạt động");

        int newId = khachHangDAO.insert(khachHang);
        KhachHang saved = khachHangDAO.findById(newId);

        return toSessionDTO(saved);
    }

    public KhachHang layThongTinKhachHang(int khachHangId) throws SQLException {
        return khachHangDAO.findById(khachHangId);
    }

    public CustomerSessionDTO capNhatHoSo(int khachHangId, String hoTen, String soDienThoai,
                                          String ngaySinhRaw, String gioiTinh, String diaChi)
            throws SQLException {
        if (isBlank(hoTen)) {
            throw new IllegalArgumentException("Họ tên không được để trống.");
        }

        String phone = blankToNull(soDienThoai);
        if (phone != null && !PHONE_PATTERN.matcher(phone).matches()) {
            throw new IllegalArgumentException("Số điện thoại chỉ gồm 9 đến 15 chữ số.");
        }

        KhachHang current = khachHangDAO.findById(khachHangId);
        if (current == null) {
            throw new IllegalArgumentException("Không tìm thấy tài khoản khách hàng.");
        }

        if (phone != null) {
            KhachHang byPhone = khachHangDAO.findBySoDienThoai(phone);
            if (byPhone != null && byPhone.getId() != khachHangId) {
                throw new IllegalArgumentException("Số điện thoại đã được dùng bởi tài khoản khác.");
            }
        }

        current.setHoTen(hoTen.trim());
        current.setSoDienThoai(phone);
        current.setNgaySinh(parseNullableDate(ngaySinhRaw));
        current.setGioiTinh(blankToNull(gioiTinh));
        current.setDiaChi(blankToNull(diaChi));

        khachHangDAO.updateProfile(current);

        KhachHang updated = khachHangDAO.findById(khachHangId);
        return toSessionDTO(updated);
    }

    public void doiMatKhau(int khachHangId, String matKhauCu, String matKhauMoi, String xacNhanMatKhau)
            throws SQLException {
        if (isBlank(matKhauCu) || isBlank(matKhauMoi) || isBlank(xacNhanMatKhau)) {
            throw new IllegalArgumentException("Vui lòng nhập đầy đủ mật khẩu cũ, mật khẩu mới và xác nhận mật khẩu.");
        }

        if (matKhauMoi.length() < 6) {
            throw new IllegalArgumentException("Mật khẩu mới phải có ít nhất 6 ký tự.");
        }

        if (!matKhauMoi.equals(xacNhanMatKhau)) {
            throw new IllegalArgumentException("Mật khẩu mới và xác nhận mật khẩu không khớp.");
        }

        KhachHang current = khachHangDAO.findById(khachHangId);
        if (current == null) {
            throw new IllegalArgumentException("Không tìm thấy tài khoản khách hàng.");
        }

        if (!PasswordUtil.matches(matKhauCu, current.getMatKhauHash())) {
            throw new IllegalArgumentException("Mật khẩu cũ không đúng.");
        }

        khachHangDAO.updatePasswordHash(khachHangId, PasswordUtil.hashPassword(matKhauMoi));
    }

    public boolean emailKhachHangDaTonTai(String email) throws SQLException {
        return !isBlank(email) && khachHangDAO.findByEmail(email.trim()) != null;
    }

    private void validateRegisterInput(String hoTen, String email, String soDienThoai,
                                       String matKhau, String xacNhanMatKhau)
            throws SQLException {
        if (isBlank(hoTen)) {
            throw new IllegalArgumentException("Vui lòng nhập họ tên.");
        }

        if (isBlank(email) || !EMAIL_PATTERN.matcher(email.trim()).matches()) {
            throw new IllegalArgumentException("Email không hợp lệ.");
        }

        if (khachHangDAO.findByEmail(email.trim().toLowerCase()) != null) {
            throw new IllegalArgumentException("Email đã được đăng ký.");
        }

        String phone = blankToNull(soDienThoai);
        if (phone != null) {
            if (!PHONE_PATTERN.matcher(phone).matches()) {
                throw new IllegalArgumentException("Số điện thoại chỉ gồm 9 đến 15 chữ số.");
            }

            if (khachHangDAO.findBySoDienThoai(phone) != null) {
                throw new IllegalArgumentException("Số điện thoại đã được đăng ký.");
            }
        }

        if (isBlank(matKhau) || matKhau.length() < 6) {
            throw new IllegalArgumentException("Mật khẩu phải có ít nhất 6 ký tự.");
        }

        if (!matKhau.equals(xacNhanMatKhau)) {
            throw new IllegalArgumentException("Mật khẩu xác nhận không khớp.");
        }
    }

    private CustomerSessionDTO toSessionDTO(KhachHang khachHang) {
        if (khachHang == null) {
            return null;
        }

        return new CustomerSessionDTO(
                khachHang.getId(),
                khachHang.getHoTen(),
                khachHang.getEmail(),
                khachHang.getSoDienThoai()
        );
    }

    private LocalDate parseNullableDate(String raw) {
        if (isBlank(raw)) {
            return null;
        }
        return LocalDate.parse(raw.trim());
    }

    private String blankToNull(String value) {
        return isBlank(value) ? null : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
