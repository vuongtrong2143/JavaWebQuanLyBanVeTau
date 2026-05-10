/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vetau.util;

public final class TrangThai {

    private TrangThai() {
    }

    // Trạng thái dùng chung
    public static final String HOAT_DONG = "Hoạt động";
    public static final String TAM_DUNG = "Tạm dừng";

    // DAT_CHO
    public static final String DAT_CHO_CHO_THANH_TOAN = "Chờ thanh toán";
    public static final String DAT_CHO_DA_THANH_TOAN = "Đã thanh toán";
    public static final String DAT_CHO_DA_HUY = "Đã hủy";
    public static final String DAT_CHO_HET_HAN = "Hết hạn";
    public static final String DAT_CHO_DANG_THANH_TOAN = "Đang thanh toán";
    public static final String DAT_CHO_HOAN_TIEN_MOT_PHAN = "Hoàn tiền một phần";
    public static final String DAT_CHO_HOAN_TIEN_TOAN_BO = "Hoàn tiền toàn bộ";

    // GIU_CHO
    public static final String GIU_CHO_DANG_GIU = "Đang giữ";
    public static final String GIU_CHO_DA_CHUYEN_VE = "Đã chuyển vé";
    public static final String GIU_CHO_DA_HUY = "Đã hủy";
    public static final String GIU_CHO_HET_HAN = "Hết hạn";

    // THANH_TOAN
    public static final String THANH_TOAN_PENDING = "Pending";
    public static final String THANH_TOAN_THANH_CONG = "Thành công";
    public static final String THANH_TOAN_THAT_BAI = "Thất bại";
    public static final String THANH_TOAN_HUY = "Hủy";
    public static final String THANH_TOAN_DANG_HOAN_TIEN = "Đang hoàn tiền";
    public static final String THANH_TOAN_DA_HOAN_TIEN = "Đã hoàn tiền";

    // VE
    public static final String VE_CHO_THANH_TOAN = "Chờ thanh toán";
    public static final String VE_HOP_LE = "Hợp lệ";
    public static final String VE_DA_TRA = "Đã trả";
    public static final String VE_DA_SU_DUNG = "Đã sử dụng";
    public static final String VE_DA_HUY = "Đã hủy";

    // HOAN_TIEN
    public static final String HOAN_TIEN_CHO_XU_LY = "Chờ xử lý";
    public static final String HOAN_TIEN_HOAN_TAT = "Hoàn tất";
    public static final String HOAN_TIEN_TU_CHOI = "Từ chối";
}