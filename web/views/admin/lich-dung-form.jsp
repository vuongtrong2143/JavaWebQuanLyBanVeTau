<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty ld ? 'Thêm' : 'Sửa'} Lịch Dừng - Vé Tàu VN</title>
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
            <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/ga">🚉 Quản lý Nhà Ga</a></li>
            <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/chuyen-tau">🗓️ Quản lý Lịch trình</a></li>
            <li class="nav-item active"><a href="${pageContext.request.contextPath}/admin/lich-dung">📍 Quản lý Lịch Dừng</a></li>
        </ul>
    </aside>

    <main class="main-content">
        <div class="form-card">
            <h2 class="form-title">${empty ld ? '📍 Thêm Ga Dừng vào Hành trình' : '✏️ Chỉnh sửa Giờ Dừng Đỗ'}</h2>

            <c:if test="${not empty error}">
                <div style="background: #fef2f2; color: #dc2626; padding: 12px; border-radius: 8px; margin-bottom: 20px; font-weight: bold; border: 1px solid #fecaca;">❌ ${error}</div>
            </c:if>

            <form action="${pageContext.request.contextPath}/admin/lich-dung" method="post">
                <input type="hidden" name="id" value="${ld.id}">
            
                <div class="form-grid">
                    <div class="form-group full-width">
                        <label>Thuộc Chuyến Tàu</label>
                        <select name="chuyenTauId" class="form-control" required>
                            <option value="">-- Chọn chuyến tàu vận hành --</option>
                            <c:forEach var="ct" items="${listChuyen}">
                                <option value="${ct.id}" ${ld.chuyenTauId == ct.id ? 'selected' : ''}>${ct.maChuyen} - ${ct.tenTau}</option>
                            </c:forEach>
                        </select>
                    </div>
                    
                    <div class="form-group">
                        <label>Ga Dừng</label>
                        <select name="gaId" class="form-control" required>
                            <option value="">-- Chọn nhà ga --</option>
                            <c:forEach var="ga" items="${listGa}">
                                <option value="${ga.id}" ${ld.gaId == ga.id ? 'selected' : ''}>${ga.tenGa} (${ga.maGa})</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Thứ Tự Dừng (1, 2, 3...)</label>
                        <input type="number" name="thuTuDung" value="${empty ld ? 1 : ld.thuTuDung}" min="1" class="form-control" required>
                    </div>

                    <div class="form-group">
                        <label>Giờ Đến (Bỏ trống nếu là Ga xuất phát)</label>
                        <input type="datetime-local" name="thoiGianDen" value="${ld.thoiGianDen}" class="form-control">
                    </div>

                    <div class="form-group">
                        <label>Giờ Đi (Bỏ trống nếu là Ga cuối)</label>
                        <input type="datetime-local" name="thoiGianDi" value="${ld.thoiGianDi}" class="form-control">
                    </div>
                </div>
                
                <div class="btn-group">
                    <a href="${pageContext.request.contextPath}/admin/lich-dung" class="btn btn-cancel">Hủy bỏ</a>
                    <button type="submit" class="btn btn-save">💾 Lưu Lịch dừng</button>
                </div>
            </form>
        </div>
    </main>

</body>
</html>