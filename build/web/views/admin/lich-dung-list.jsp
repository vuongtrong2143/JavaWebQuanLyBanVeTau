<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Lịch Dừng - Vé Tàu VN</title>
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
        
        .page-header { display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 24px; }
        .page-title h1 { margin: 0 0 4px 0; font-size: 24px; font-weight: 800; color: #0f172a; }
        .page-title p { margin: 0; color: #64748b; font-size: 14px; }

        .btn-add { background: var(--accent-blue); color: white; border: none; padding: 10px 20px; border-radius: 8px; font-weight: 600; text-decoration: none; transition: 0.2s; box-shadow: 0 4px 6px rgba(59, 130, 246, 0.3); }
        .btn-add:hover { background: #2563eb; transform: translateY(-2px); }

        .content-card { background: white; border-radius: 16px; box-shadow: 0 4px 10px rgba(0,0,0,0.03); border: 1px solid #e2e8f0; overflow: hidden; }
        .admin-table { width: 100%; border-collapse: collapse; text-align: left; }
        .admin-table th { background: #f8fafc; padding: 16px 24px; font-size: 13px; font-weight: 700; color: #64748b; text-transform: uppercase; border-bottom: 1px solid #e2e8f0; }
        .admin-table td { padding: 16px 24px; border-bottom: 1px solid #f1f5f9; font-size: 14px; color: #1e293b; vertical-align: middle; }
        .admin-table tr:hover { background: #fcfdfe; }

        .btn-action { padding: 6px 12px; border-radius: 6px; font-size: 13px; font-weight: 600; text-decoration: none; transition: 0.2s; display: inline-block; }
        .btn-edit { background: #eff6ff; color: #1d4ed8; border: 1px solid #bfdbfe; }
        .btn-edit:hover { background: #dbeafe; }
        .btn-delete { background: #fef2f2; color: #dc2626; border: 1px solid #fecaca; margin-left: 8px; }
        .btn-delete:hover { background: #fee2e2; }

        .badge-order { background: #f1f5f9; color: #475569; padding: 4px 10px; border-radius: 12px; font-weight: bold; font-size: 12px; }
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
            <li class="nav-item" style="margin-top: 40px;"><a href="${pageContext.request.contextPath}/admin/logout" style="color: #fca5a5;">🚪 Đăng xuất</a></li>
        </ul>
    </aside>

    <main class="main-content">
        <div class="page-header">
            <div class="page-title">
                <h1>Lịch dừng tại các Ga</h1>
                <p>Thiết lập danh sách các ga mà tàu sẽ đi qua trong từng chuyến cụ thể.</p>
            </div>
            <a href="${pageContext.request.contextPath}/admin/lich-dung?action=add" class="btn-add">➕ Thêm Ga Dừng</a>
        </div>

        <c:if test="${param.msg == 'saved'}">
            <div style="background: #dcfce7; color: #166534; padding: 12px 16px; border-radius: 8px; margin-bottom: 20px; font-weight: bold; border: 1px solid #bbf7d0;">✅ Lưu Lịch dừng thành công!</div>
        </c:if>
        <c:if test="${param.msg == 'deleted'}">
            <div style="background: #fef2f2; color: #dc2626; padding: 12px 16px; border-radius: 8px; margin-bottom: 20px; font-weight: bold; border: 1px solid #fecaca;">🗑️ Đã xóa Ga dừng khỏi hành trình!</div>
        </c:if>

        <div class="content-card">
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>Chuyến tàu</th>
                        <th>Thứ tự</th>
                        <th>Ga dừng</th>
                        <th>Giờ đến</th>
                        <th>Giờ đi</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${listLichDung}">
                        <tr>
                            <td><b style="color: #0f172a;">${item.maChuyen}</b></td>
                            <td><span class="badge-order">${item.thuTuDung}</span></td>
                            <td style="color: #1d4ed8; font-weight: bold;">${item.tenGa}</td>
                            <td>${empty item.thoiGianDen ? '<span style="color:#94a3b8; font-style: italic;">-- Điểm xuất phát --</span>' : item.thoiGianDen}</td>
                            <td>${empty item.thoiGianDi ? '<span style="color:#94a3b8; font-style: italic;">-- Điểm kết thúc --</span>' : item.thoiGianDi}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/lich-dung?action=edit&id=${item.id}" class="btn-action btn-edit">Sửa</a>
                                <a href="${pageContext.request.contextPath}/admin/lich-dung?action=delete&id=${item.id}" onclick="return confirm('Xóa ga dừng này khỏi hành trình?')" class="btn-action btn-delete">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty listLichDung}">
                        <tr><td colspan="6" style="text-align: center; padding: 40px; color: #64748b;">Chưa có dữ liệu lịch dừng.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </main>
</body>
</html>