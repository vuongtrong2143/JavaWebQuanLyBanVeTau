<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty cs ? 'Thêm mới' : 'Chỉnh sửa'} Chính Sách - Vé Tàu VN</title>
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

        .checkbox-group { display: flex; gap: 30px; background: #f8fafc; padding: 15px; border-radius: 8px; border: 1px solid #e2e8f0; margin-bottom: 20px; }
        .checkbox-item { display: flex; align-items: center; gap: 10px; cursor: pointer; }
        .checkbox-item input { width: 18px; height: 18px; }

        .btn-group { display: flex; gap: 16px; margin-top: 24px; padding-top: 24px; border-top: 1px dashed #e2e8f0; justify-content: flex-end; }
        .btn { padding: 12px 24px; border-radius: 8px; font-weight: bold; font-size: 15px; text-decoration: none; cursor: pointer; transition: 0.2s; border: none; }
        .btn-save { background: #1d4ed8; color: white; box-shadow: 0 4px 6px rgba(29, 78, 216, 0.3); }
        .btn-save:hover { background: #1e40af; transform: translateY(-2px); }
    </style>
</head>
<body>

    <aside class="sidebar">
        <div class="sidebar-brand" style="padding: 0 24px 30px; font-size: 22px; font-weight: 900;">VETAU<span>ADMIN</span></div>
        <ul style="list-style: none; padding: 0;">
            <li style="padding: 14px 24px;"><a href="${pageContext.request.contextPath}/admin/chinh-sach-doi-tra" style="color: white; text-decoration: none; font-weight: 500;">← Quay lại danh sách</a></li>
        </ul>
    </aside>

    <main class="main-content">
        <div class="form-card">
            <h2 class="form-title">${empty cs ? '➕ Thiết lập Quy định Đổi Trả' : '✏️ Cập nhật Chính Sách'}</h2>

            <c:if test="${not empty error}">
                <div style="background: #fef2f2; color: #dc2626; padding: 12px; border-radius: 8px; margin-bottom: 20px; font-weight: bold;">❌ ${error}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/admin/chinh-sach-doi-tra" method="post">
                <input type="hidden" name="id" value="${cs.id}">
                
                <div class="form-group">
                    <label>Tên Chính Sách (Mô tả ngắn gọn)</label>
                    <input type="text" name="tenChinhSach" value="${cs.tenChinhSach}" class="form-control" placeholder="VD: Trả vé trước 24h - Tết" required>
                </div>

                <div class="form-grid">
                    <div class="form-group">
                        <label>Loại Đơn Hàng Áp Dụng</label>
                        <select name="loaiDonHangApDung" class="form-control">
                            <option value="Tất cả" ${cs.loaiDonHangApDung == 'Tất cả' ? 'selected' : ''}>Tất cả các đơn</option>
                            <option value="Cá nhân" ${cs.loaiDonHangApDung == 'Cá nhân' ? 'selected' : ''}>Đơn cá nhân</option>
                            <option value="Tập thể" ${cs.loaiDonHangApDung == 'Tập thể' ? 'selected' : ''}>Đơn tập thể</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Chiều Tàu Áp Dụng</label>
                        <select name="chieuTauApDung" class="form-control">
                            <option value="Tất cả" ${cs.chieuTauApDung == 'Tất cả' ? 'selected' : ''}>Cả hai chiều</option>
                            <option value="Chẵn" ${cs.chieuTauApDung == 'Chẵn' ? 'selected' : ''}>Chiều Chẵn (Nam -> Bắc)</option>
                            <option value="Lẻ" ${cs.chieuTauApDung == 'Lẻ' ? 'selected' : ''}>Chiều Lẻ (Bắc -> Nam)</option>
                        </select>
                    </div>
                </div>

                <div class="form-grid">
                    <div class="form-group">
                        <label>Trước Khởi Hành Từ (Giờ)</label>
                        <input type="number" name="truocKhoiHanhTuGio" value="${cs.truocKhoiHanhTuGio}" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label>Trước Khởi Hành Đến (Giờ - Bỏ trống nếu không giới hạn)</label>
                        <input type="number" name="truocKhoiHanhDenGio" value="${cs.truocKhoiHanhDenGio}" class="form-control">
                    </div>
                </div>

                <div class="form-grid">
                    <div class="form-group">
                        <label>Tỷ Lệ Khấu Trừ (%)</label>
                        <input type="number" name="tyLeKhauTru" value="${cs.tyLeKhauTru}" min="0" max="100" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label>Phí Đổi Cố Định (VNĐ)</label>
                        <input type="number" name="phiDoiCoDinh" value="${cs.phiDoiCoDinh}" class="form-control" required>
                    </div>
                </div>

                <div class="checkbox-group">
                    <label class="checkbox-item">
                        <input type="checkbox" name="choPhepDoi" ${cs.choPhepDoi ? 'checked' : ''}>
                        <span>Cho phép Đổi vé</span>
                    </label>
                    <label class="checkbox-item">
                        <input type="checkbox" name="choPhepTra" ${cs.choPhepTra ? 'checked' : ''}>
                        <span>Cho phép Trả vé</span>
                    </label>
                </div>

                <div class="form-grid">
                    <div class="form-group">
                        <label>Ngày Bắt Đầu Hiệu Lực</label>
                        <input type="datetime-local" name="hieuLucTu" value="${cs.hieuLucTu}" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label>Trạng thái</label>
                        <select name="trangThai" class="form-control">
                            <option value="Hoạt động" ${cs.trangThai == 'Hoạt động' ? 'selected' : ''}>🟢 Hoạt động</option>
                            <option value="Tạm dừng" ${cs.trangThai == 'Tạm dừng' ? 'selected' : ''}>⚪ Tạm dừng</option>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label>Độ Ưu Tiên (Càng lớn càng được áp dụng trước)</label>
                    <input type="number" name="doUuTien" value="${cs.doUuTien}" class="form-control" required>
                </div>

                <div class="btn-group">
                    <button type="submit" class="btn btn-save">💾 Lưu Chính Sách</button>
                </div>
            </form>
        </div>
    </main>
</body>
</html>