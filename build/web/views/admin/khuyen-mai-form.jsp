<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty km ? 'Thêm mới' : 'Chỉnh sửa'} Khuyến Mãi - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <style>
        :root { --sidebar-width: 260px; --primary-dark: #0f172a; --accent-blue: #3b82f6; }
        body { background: #f8fafc; font-family: 'Segoe UI', sans-serif; margin: 0; display: flex; }
        .sidebar { width: var(--sidebar-width); background: var(--primary-dark); height: 100vh; position: fixed; color: white; padding: 20px 0; }
        .main-content { margin-left: var(--sidebar-width); flex: 1; padding: 30px; }
        
        .form-card { background: white; border-radius: 16px; padding: 30px; max-width: 850px; box-shadow: 0 4px 10px rgba(0,0,0,0.03); border: 1px solid #e2e8f0; }
        .form-title { margin-top: 0; font-size: 22px; color: #0f172a; margin-bottom: 24px; padding-bottom: 16px; border-bottom: 2px solid #f1f5f9; }

        .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-bottom: 20px; }
        .form-group label { display: block; font-size: 14px; font-weight: bold; color: #334155; margin-bottom: 8px; }
        .form-control { width: 100%; padding: 12px 16px; border: 1px solid #cbd5e1; border-radius: 8px; font-size: 14px; font-family: inherit; box-sizing: border-box; transition: 0.2s; outline: none; }
        .form-control:focus { border-color: #3b82f6; box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1); }

        .btn-group { display: flex; gap: 16px; margin-top: 24px; padding-top: 24px; border-top: 1px dashed #e2e8f0; justify-content: flex-end; }
        .btn { padding: 12px 24px; border-radius: 8px; font-weight: bold; font-size: 15px; text-decoration: none; cursor: pointer; transition: 0.2s; border: none; }
        .btn-save { background: #1d4ed8; color: white; box-shadow: 0 4px 6px rgba(29, 78, 216, 0.3); }
        .btn-save:hover { background: #1e40af; transform: translateY(-2px); }
    </style>
</head>
<body>

    <aside class="sidebar">
        <div class="sidebar-brand" style="padding: 0 24px 30px; font-size: 22px; font-weight: 900;">🚂 VETAU<span>ADMIN</span></div>
        <ul style="list-style: none; padding: 0;">
            <li style="padding: 14px 24px;"><a href="${pageContext.request.contextPath}/admin/khuyen-mai" style="color: white; text-decoration: none; font-weight: 500;">← Quay lại danh sách</a></li>
        </ul>
    </aside>

    <main class="main-content">
        <div class="form-card">
            <h2 class="form-title">${empty km ? '🎁 Thiết lập Khuyến Mãi Mới' : '✏️ Cập nhật Chương trình'}</h2>

            <c:if test="${not empty error}">
                <div style="background: #fef2f2; color: #dc2626; padding: 12px; border-radius: 8px; margin-bottom: 20px; font-weight: bold;">❌ ${error}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/admin/khuyen-mai" method="post">
                <input type="hidden" name="id" value="${km.id}">
                
                <div class="form-grid">
                    <div class="form-group">
                        <label>Mã Khuyến Mãi (VD: TET2027, SINHVIEN)</label>
                        <input type="text" name="maKhuyenMai" value="${km.maKhuyenMai}" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label>Tên Chương Trình Ưu Đãi</label>
                        <input type="text" name="tenChuongTrinh" value="${km.tenChuongTrinh}" class="form-control" placeholder="VD: Ưu đãi đồng hành cùng Sinh viên" required>
                    </div>
                </div>

                <div class="form-grid">
                    <div class="form-group">
                        <label>Phần trăm giảm (%)</label>
                        <input type="number" name="phanTramGiam" value="${km.phanTramGiam}" min="1" max="100" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label>Số tiền giảm tối đa (Để trống nếu không giới hạn)</label>
                        <input type="number" name="giamToiDa" value="${km.giamToiDa}" class="form-control">
                    </div>
                </div>

                <div class="form-grid">
                    <div class="form-group">
                        <label>Giá trị đơn hàng tối thiểu (VNĐ)</label>
                        <input type="number" name="giaTriDonToiThieu" value="${km.giaTriDonToiThieu}" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label>Phương thức thanh toán áp dụng</label>
                        <select name="phuongThucTtApDung" class="form-control">
                            <option value="" ${empty km.phuongThucTtApDung ? 'selected' : ''}>Tất cả phương thức</option>
                            <option value="VNPay" ${km.phuongThucTtApDung == 'VNPay' ? 'selected' : ''}>Chỉ VNPay</option>
                            <option value="MoMo" ${km.phuongThucTtApDung == 'MoMo' ? 'selected' : ''}>Chỉ MoMo</option>
                        </select>
                    </div>
                </div>

                <div class="form-grid">
                    <div class="form-group">
                        <label>Ngày Bắt Đầu</label>
                        <input type="datetime-local" name="ngayBatDau" value="${km.ngayBatDau}" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label>Ngày Kết Thúc</label>
                        <input type="datetime-local" name="ngayKetThuc" value="${km.ngayKetThuc}" class="form-control" required>
                    </div>
                </div>

                <div class="form-grid">
                    <div class="form-group">
                        <label>Số lượng tối đa (Lượt dùng)</label>
                        <input type="number" name="soLuongToiDa" value="${km.soLuongToiDa}" class="form-control" placeholder="Để trống nếu không giới hạn">
                    </div>
                    <div class="form-group">
                        <label>Trạng thái</label>
                        <select name="trangThai" class="form-control">
                            <option value="Hoạt động" ${km.trangThai == 'Hoạt động' ? 'selected' : ''}>🟢 Kích hoạt</option>
                            <option value="Tạm dừng" ${km.trangThai == 'Tạm dừng' ? 'selected' : ''}>⚪ Tạm dừng</option>
                            <option value="Hết hạn" ${km.trangThai == 'Hết hạn' ? 'selected' : ''}>🔴 Hết hiệu lực</option>
                        </select>
                    </div>
                </div>

                <div class="btn-group">
                    <button type="submit" class="btn btn-save">💾 Lưu Khuyến Mãi</button>
                </div>
            </form>
        </div>
    </main>
</body>
</html>