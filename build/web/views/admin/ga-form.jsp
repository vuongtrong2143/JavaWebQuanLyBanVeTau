<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty ga ? 'Thêm mới' : 'Chỉnh sửa'} Nhà Ga - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <style>
        :root { --sidebar-width: 260px; --primary-dark: #0f172a; --accent-blue: #3b82f6; }
        body { background: #f8fafc; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; display: flex; }
        
        .sidebar { width: var(--sidebar-width); background: var(--primary-dark); height: 100vh; position: fixed; color: white; padding: 20px 0; z-index: 100;}
        .sidebar-brand { padding: 0 24px 30px; font-size: 22px; font-weight: 900; letter-spacing: -1px; }
        .sidebar-brand span { color: var(--accent-blue); }
        .nav-menu { list-style: none; padding: 0; margin: 0; }
        .nav-item a { display: flex; align-items: center; padding: 14px 24px; color: #94a3b8; text-decoration: none; transition: 0.2s; font-size: 15px; font-weight: 500; }
        .nav-item a:hover, .nav-item.active a { background: rgba(255,255,255,0.05); color: white; border-left: 4px solid var(--accent-blue); }

        .main-content { margin-left: var(--sidebar-width); flex: 1; padding: 30px; }
        
        .form-card { background: white; border-radius: 16px; padding: 30px; max-width: 800px; box-shadow: 0 4px 10px rgba(0,0,0,0.03); border: 1px solid #e2e8f0; }
        .form-title { margin-top: 0; font-size: 22px; color: #0f172a; margin-bottom: 24px; padding-bottom: 16px; border-bottom: 2px solid #f1f5f9; }

        .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }
        .form-group { margin-bottom: 20px; }
        .form-group.full-width { grid-column: span 2; }
        
        .form-group label { display: block; font-size: 14px; font-weight: bold; color: #334155; margin-bottom: 8px; }
        .form-control { width: 100%; padding: 12px 16px; border: 1px solid #cbd5e1; border-radius: 8px; font-size: 15px; font-family: inherit; box-sizing: border-box; transition: 0.2s; outline: none; }
        .form-control:focus { border-color: #3b82f6; box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1); }

        .btn-group { display: flex; gap: 16px; margin-top: 24px; padding-top: 24px; border-top: 1px dashed #e2e8f0; justify-content: flex-end; }
        .btn { padding: 12px 24px; border-radius: 8px; font-weight: bold; font-size: 15px; text-decoration: none; cursor: pointer; transition: 0.2s; border: none; }
        .btn-cancel { background: white; color: #64748b; border: 1px solid #cbd5e1; }
        .btn-cancel:hover { background: #f1f5f9; }
        .btn-save { background: #1d4ed8; color: white; box-shadow: 0 4px 10px rgba(29, 78, 216, 0.3); }
        .btn-save:hover { background: #1e40af; transform: translateY(-2px); }
    </style>
</head>
<body>

    <aside class="sidebar">
        <div class="sidebar-brand">🚂 VETAU<span>ADMIN</span></div>
        <ul class="nav-menu">
            <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/dashboard">📊 Bảng điều khiển</a></li>
            <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/tau">🚆 Quản lý Tàu</a></li>
            <li class="nav-item active"><a href="${pageContext.request.contextPath}/admin/ga">🚉 Quản lý Nhà Ga</a></li>
            <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/chuyen-tau">🗓️ Quản lý Lịch trình</a></li>
            <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/lich-dung">📍 Quản lý Lịch Dừng</a></li>
        </ul>
    </aside>

    <main class="main-content">
        <div class="form-card">
            <h2 class="form-title">${empty ga ? '➕ Thêm mới Nhà Ga' : '✏️ Chỉnh sửa Nhà Ga'}</h2>

            <c:if test="${not empty error}">
                <div style="background: #fef2f2; color: #dc2626; padding: 12px; border-radius: 8px; margin-bottom: 20px; font-weight: bold; border: 1px solid #fecaca;">❌ ${error}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/admin/ga" method="post">
                <input type="hidden" name="id" value="${ga.id}">
                
                <div class="form-grid">
                    <div class="form-group">
                        <label>Mã Ga (Viết tắt, VD: SGN, HNO)</label>
                        <input type="text" name="maGa" value="${ga.maGa}" class="form-control" placeholder="Mã định danh duy nhất" required>
                    </div>

                    <div class="form-group">
                        <label>Tên Nhà Ga</label>
                        <input type="text" name="tenGa" value="${ga.tenGa}" class="form-control" placeholder="VD: Ga Sài Gòn, Ga Hà Nội" required>
                    </div>

                    <div class="form-group">
                        <label>Tỉnh/Thành phố trực thuộc</label>
                        <input type="text" name="tinhThanh" value="${ga.tinhThanh}" class="form-control" placeholder="VD: TP. Hồ Chí Minh" required>
                    </div>

                    <div class="form-group">
                        <label>Lý trình tuyến Thống Nhất (km)</label>
                        <input type="number" name="lyTrinhKm" value="${empty ga ? 0 : ga.lyTrinhKm}" min="0" class="form-control" required>
                    </div>

                    <div class="form-group full-width">
                        <label>Trạng thái hoạt động</label>
                        <select name="trangThai" class="form-control">
                            <option value="Hoạt động" ${ga.trangThai == 'Hoạt động' ? 'selected' : ''}>🟢 Đang hoạt động (Mở bán vé)</option>
                            <option value="Tạm ngưng" ${ga.trangThai == 'Tạm ngưng' ? 'selected' : ''}>🔴 Tạm ngưng hoạt động</option>
                        </select>
                    </div>
                </div>
                
                <div class="btn-group">
                    <a href="${pageContext.request.contextPath}/admin/ga" class="btn btn-cancel">Quay lại</a>
                    <button type="submit" class="btn btn-save">💾 Lưu dữ liệu</button>
                </div>
            </form>
        </div>
    </main>

</body>
</html>