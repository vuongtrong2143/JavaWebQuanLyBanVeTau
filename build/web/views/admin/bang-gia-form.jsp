<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty bg ? 'Thêm mới' : 'Chỉnh sửa'} Bảng Giá - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <style>
        :root { --sidebar-width: 260px; --primary-dark: #0f172a; --accent-blue: #3b82f6; }
        body { background: #f8fafc; font-family: 'Segoe UI', sans-serif; margin: 0; display: flex; }
        .sidebar { width: var(--sidebar-width); background: var(--primary-dark); height: 100vh; position: fixed; color: white; padding: 20px 0; }
        .main-content { margin-left: var(--sidebar-width); flex: 1; padding: 30px; }
        
        .form-card { background: white; border-radius: 16px; padding: 30px; max-width: 800px; box-shadow: 0 4px 10px rgba(0,0,0,0.03); border: 1px solid #e2e8f0; }
        .form-title { margin-top: 0; font-size: 22px; color: #0f172a; margin-bottom: 24px; padding-bottom: 16px; border-bottom: 2px solid #f1f5f9; }

        .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-bottom: 20px; }
        .form-group label { display: block; font-size: 14px; font-weight: bold; color: #334155; margin-bottom: 8px; }
        .form-control { width: 100%; padding: 12px 16px; border: 1px solid #cbd5e1; border-radius: 8px; font-size: 15px; font-family: inherit; box-sizing: border-box; transition: 0.2s; outline: none; }
        .form-control:focus { border-color: #3b82f6; box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1); }

        .btn-group { display: flex; gap: 16px; margin-top: 24px; padding-top: 24px; border-top: 1px dashed #e2e8f0; justify-content: flex-end; }
        .btn { padding: 12px 24px; border-radius: 8px; font-weight: bold; font-size: 15px; text-decoration: none; cursor: pointer; transition: 0.2s; border: none; }
        .btn-save { background: #1d4ed8; color: white; }
        .btn-save:hover { background: #1e40af; transform: translateY(-2px); }
    </style>
</head>
<body>

    <aside class="sidebar">
        <div class="sidebar-brand" style="padding: 0 24px 30px; font-size: 22px; font-weight: 900;">🚂 VETAU<span>ADMIN</span></div>
        <ul style="list-style: none; padding: 0;">
            <li style="padding: 14px 24px;"><a href="${pageContext.request.contextPath}/admin/bang-gia" style="color: white; text-decoration: none; font-weight: 500;">← Quay lại danh sách</a></li>
        </ul>
    </aside>

    <main class="main-content">
        <div class="form-card">
            <h2 class="form-title">${empty bg ? '➕ Thiết lập Giá Vé Mới' : '✏️ Chỉnh sửa Cấu hình Giá'}</h2>

            <c:if test="${not empty error}">
                <div style="background: #fef2f2; color: #dc2626; padding: 12px; border-radius: 8px; margin-bottom: 20px; font-weight: bold;">❌ ${error}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/admin/bang-gia" method="post">
                <input type="hidden" name="id" value="${bg.id}">
                
                <div class="form-grid">
                    <div class="form-group">
                        <label>Ga Đi</label>
                        <select name="gaDiId" class="form-control" required>
                            <option value="">-- Chọn Ga khởi hành --</option>
                            <c:forEach var="ga" items="${listGa}">
                                <option value="${ga.id}" ${bg.gaDiId == ga.id ? 'selected' : ''}>${ga.tenGa} (${ga.maGa})</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Ga Đến</label>
                        <select name="gaDenId" class="form-control" required>
                            <option value="">-- Chọn Ga kết thúc --</option>
                            <c:forEach var="ga" items="${listGa}">
                                <option value="${ga.id}" ${bg.gaDenId == ga.id ? 'selected' : ''}>${ga.tenGa} (${ga.maGa})</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>

                <div class="form-grid">
                    <div class="form-group">
                        <label>Loại Toa Áp Dụng</label>
                        <select name="loaiToaApDung" class="form-control" required>
                            <option value="Ngồi mềm" ${bg.loaiToaApDung == 'Ngồi mềm' ? 'selected' : ''}>Ngồi mềm điều hòa</option>
                            <option value="Nằm khoang 6" ${bg.loaiToaApDung == 'Nằm khoang 6' ? 'selected' : ''}>Nằm khoang 6 điều hòa</option>
                            <option value="Nằm khoang 4" ${bg.loaiToaApDung == 'Nằm khoang 4' ? 'selected' : ''}>Nằm khoang 4 điều hòa</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Tầng (Bỏ trống nếu áp dụng cho cả toa)</label>
                        <input type="number" name="tangApDung" value="${bg.tangApDung}" min="1" max="3" class="form-control">
                    </div>
                </div>

                <div class="form-grid">
                    <div class="form-group">
                        <label>Giá Cơ Sở (VNĐ)</label>
                        <input type="number" name="giaCoSo" value="${bg.giaCoSo}" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label>Phụ Thu Cao Điểm Mặc Định</label>
                        <input type="number" name="phuThu" value="${bg.phuThuCaoDiemMacDinh}" class="form-control" required>
                    </div>
                </div>

                <div class="form-grid">
                    <div class="form-group">
                        <label>Ngày Bắt Đầu Áp Dụng</label>
                        <input type="datetime-local" name="hieuLucTu" value="${bg.hieuLucTu}" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label>Trạng thái</label>
                        <select name="trangThai" class="form-control">
                            <option value="Hoạt động" ${bg.trangThai == 'Hoạt động' ? 'selected' : ''}>🟢 Hoạt động</option>
                            <option value="Tạm dừng" ${bg.trangThai == 'Tạm dừng' ? 'selected' : ''}>⚪ Tạm dừng</option>
                        </select>
                    </div>
                </div>

                <div class="btn-group">
                    <button type="submit" class="btn btn-save">💾 Lưu Bảng Giá</button>
                </div>
            </form>
        </div>
    </main>
</body>
</html>