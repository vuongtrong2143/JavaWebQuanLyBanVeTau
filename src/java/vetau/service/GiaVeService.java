package vetau.service;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;
import vetau.dao.BangGiaDAO;
import vetau.model.BangGia;

public class GiaVeService {

    private final BangGiaDAO bangGiaDAO = new BangGiaDAO();
    private final UuDaiService uuDaiService = new UuDaiService();
    private final KhuyenMaiService khuyenMaiService = new KhuyenMaiService();

    public KetQuaTinhGia tinhGiaVe(int gaDiId, int gaDenId, String loaiToa, Integer tang,
                                  Integer doiTuongUuDaiId, String maKhuyenMai,
                                  String phuongThucThanhToan, LocalDateTime thoiDiem) throws SQLException {
        if (thoiDiem == null) {
            thoiDiem = LocalDateTime.now();
        }
        BangGia bangGia = bangGiaDAO.findGiaHienHanh(gaDiId, gaDenId, loaiToa, tang, thoiDiem);
        if (bangGia == null) {
            throw new IllegalArgumentException("Không tìm thấy bảng giá phù hợp cho chặng đã chọn.");
        }
        BigDecimal giaCoSo = safe(bangGia.getGiaCoSo());
        BigDecimal phuThu = safe(bangGia.getPhuThuCaoDiemMacDinh());
        BigDecimal giamDoiTuong = uuDaiService.tinhTienGiam(giaCoSo, doiTuongUuDaiId);
        BigDecimal tamTinh = giaCoSo.add(phuThu).subtract(giamDoiTuong);
        if (tamTinh.compareTo(BigDecimal.ZERO) < 0) {
            tamTinh = BigDecimal.ZERO;
        }
        BigDecimal giamKhuyenMai = khuyenMaiService.tinhTienGiam(tamTinh, maKhuyenMai, phuongThucThanhToan, thoiDiem);
        BigDecimal thanhTien = tamTinh.subtract(giamKhuyenMai);
        if (thanhTien.compareTo(BigDecimal.ZERO) < 0) {
            thanhTien = BigDecimal.ZERO;
        }
        return new KetQuaTinhGia(giaCoSo, phuThu, giamDoiTuong, giamKhuyenMai, thanhTien, bangGia);
    }

    private BigDecimal safe(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    public static class KetQuaTinhGia {
        private final BigDecimal giaCoSo;
        private final BigDecimal phuThuCaoDiem;
        private final BigDecimal giamDoiTuong;
        private final BigDecimal giamKhuyenMai;
        private final BigDecimal thanhTien;
        private final BangGia bangGia;

        public KetQuaTinhGia(BigDecimal giaCoSo, BigDecimal phuThuCaoDiem, BigDecimal giamDoiTuong,
                             BigDecimal giamKhuyenMai, BigDecimal thanhTien, BangGia bangGia) {
            this.giaCoSo = giaCoSo;
            this.phuThuCaoDiem = phuThuCaoDiem;
            this.giamDoiTuong = giamDoiTuong;
            this.giamKhuyenMai = giamKhuyenMai;
            this.thanhTien = thanhTien;
            this.bangGia = bangGia;
        }

        public BigDecimal getGiaCoSo() { return giaCoSo; }
        public BigDecimal getPhuThuCaoDiem() { return phuThuCaoDiem; }
        public BigDecimal getGiamDoiTuong() { return giamDoiTuong; }
        public BigDecimal getGiamKhuyenMai() { return giamKhuyenMai; }
        public BigDecimal getThanhTien() { return thanhTien; }
        public BangGia getBangGia() { return bangGia; }
    }
}
