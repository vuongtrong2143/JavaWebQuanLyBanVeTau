/*
package vetau.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class PasswordUtil {

    private PasswordUtil() {
    }

    public static String hashPassword(String plainPassword) {
        if (plainPassword == null) {
            return null;
        }

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(plainPassword.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Không hỗ trợ thuật toán SHA-256", ex);
        }
    }

    public static boolean matches(String plainPassword, String storedPasswordHash) {
        if (plainPassword == null || storedPasswordHash == null) {
            return false;
        }

        String plain = plainPassword.trim();
        String stored = storedPasswordHash.trim();

        
         //* 1) Cách đúng từ Giai đoạn 5 trở đi: so sánh SHA-256.
         //* 2) Hỗ trợ dữ liệu mẫu cũ trong VeTauDB.sql: 123456_hash_demo.
         //* 3) Hỗ trợ tạm trường hợp dữ liệu demo lưu plain text.
         
        return stored.equals(hashPassword(plain))
                || stored.equals(plain + "_hash_demo")
                || stored.equals(plain);
    }
}

*/
        
package vetau.util;

public final class PasswordUtil {

    private PasswordUtil() {
    }

    /*
     * Giai đoạn học/demo:
     * Không mã hóa mật khẩu, lưu nguyên mật khẩu người dùng nhập.
     *
     * Ví dụ:
     * Người dùng nhập: 987456
     * Database lưu:     987456
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null) {
            return null;
        }

        return plainPassword.trim();
    }

    public static boolean matches(String plainPassword, String storedPassword) {
        if (plainPassword == null || storedPassword == null) {
            return false;
        }

        String plain = plainPassword.trim();
        String stored = storedPassword.trim();

        /*
         * So sánh mật khẩu nhập với mật khẩu đang lưu.
         *
         * Dòng thứ hai giữ lại để tài khoản demo cũ vẫn đăng nhập được:
         * 123456 nhập vào vẫn khớp với 123456_hash_demo trong database mẫu.
         */
        return stored.equals(plain)
                || stored.equals(plain + "_hash_demo");
    }
}