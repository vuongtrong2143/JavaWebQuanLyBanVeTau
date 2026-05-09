<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Tàu - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <style>
        :root { --sidebar-width: 260px; --primary-dark: #0f172a; --accent-blue: #3b82f6; }
        body { background: #f8fafc; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; display: flex; }
        
        /* Sidebar */
        .sidebar { width: var(--sidebar-width); background: var(--primary-dark); height: 100vh; position: fixed; color: white; padding: 20px 0; z-index: 100;}
        .sidebar-brand { padding: 0 24px 30px; font-size: 22px; font-weight: 900; letter-spacing: -1px; }
        .sidebar-brand span { color: var(--accent-blue); }
        .nav-menu { list-style: none; padding: 0; margin: 0; }
        .nav-item a { display: flex; align-items: center; padding: 14px 24px; color: #94a3b8; text-decoration: none; transition: 0.2s; font-size: 15px; font-weight: 500; }
        .nav-item a:hover, .nav-item.active a { background: rgba(255,255,255,0.05); color: white; border-left: 4px solid var(--accent-blue); }

        /* Main Content */
        .main-content { margin-left: var(--sidebar-width); flex: 1; padding: 30px; }
        
        .page-header { display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 24px; }
        .page-title h1 { margin: 0 0 4px 0; font-size: 24px; font-weight: 800; color: #0f172a; }
        .page-title p { margin: 0; color: #64748b; font-size: 14px; }

        .btn-add { background: var(--accent-blue); color: white; border: none; padding: 10px 20px; border-radius: 8px; font-weight: 600; text-decoration: none; transition: 0.2s; box-shadow: 0 4px 6px rgba(59, 130, 246, 0.3); }
        .btn-add:hover { background: #2563eb; transform: translateY(-2px); }

        /* Bảng dữ liệu */
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

        .status-pill { padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: bold; }
        .pill-active { background: #dcfce7; color: #16a34a; }
        .pill-inactive { background: #fee2e2; color: #dc2626; }
        .pill-maintenance { background: #fef3c7; color: #d97706; }
    </style>
</head>
<body>

    <aside class="sidebar">
        <div class="sidebar-brand">🚂 VETAU<span>ADMIN</span></div>
        <ul class="nav-menu">
            <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/dashboard">📊 Bảng điều khiển</a></li>
            <li class="nav-item active"><a href="${pageContext.request.contextPath}/admin/tau">🚆 Quản lý Tàu</a></li>
            <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/schedules">🕒 Quản lý Lịch trình</a></li>
            <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/bookings">📦 Quản lý Đơn hàng</a></li>
            <li class="nav-item" style="margin-top: 40px;"><a href="${pageContext.request.contextPath}/admin/logout" style="color: #fca5a5;">🚪 Đăng xuất</a></li>
        </ul>
    </aside>

    <main class="main-content">
        <div class="page-header">
            <div class="page-title">
                <h1>Danh sách Đoàn Tàu</h1>
                <p>Quản lý thông tin và trạng thái của các đoàn tàu vật lý trong hệ thống.</p>
            </div>
            <a href="${pageContext.request.contextPath}/admin/tau?action=add" class="btn-add">➕ Thêm Tàu Mới</a>
        </div>

        <c:if test="${param.msg == 'saved'}">
            <div style="background: #dcfce7; color: #166534; padding: 12px 16px; border-radius: 8px; margin-bottom: 20px; font-weight: bold; border: 1px solid #bbf7d0;">✅ Lưu thông tin tàu thành công!</div>
        </c:if>
        <c:if test="${param.msg == 'deleted'}">
            <div style="background: #fef2f2; color: #dc2626; padding: 12px 16px; border-radius: 8px; margin-bottom: 20px; font-weight: bold; border: 1px solid #fecaca;">🗑️ Đã xóa tàu khỏi hệ thống!</div>
        </c:if>

        <div class="content-card">
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>Mã Tàu</th>
                        <th>Tên Tàu</th>
                        <th>Chiều đi</th>
                        <th>Tuyến Thống Nhất</th>
                        <th>Trạng thái</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${listTau}">
                        <tr>
                            <td><b style="color: #1d4ed8;">${item.maTau}</b></td>
                            <td>${item.tenTau}</td>
                            <td>${item.chieuDi}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${item.thuocTuyenThongNhat}"><span style="color: #0ea5e9; font-weight: bold;">Có</span></c:when>
                                    <c:otherwise><span style="color: #94a3b8;">Không</span></c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${item.trangThai == 'Hoạt động'}">
                                        <span class="status-pill pill-active">Hoạt động</span>
                                    </c:when>
                                    <c:when test="${item.trangThai == 'Bảo trì'}">
                                        <span class="status-pill pill-maintenance">Bảo trì</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="status-pill pill-inactive">${item.trangThai}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/tau?action=edit&id=${item.id}" class="btn-action btn-edit">Sửa</a>
                                <a href="${pageContext.request.contextPath}/admin/tau?action=delete&id=${item.id}" onclick="return confirm('Bạn có chắc chắn muốn xóa tàu ${item.maTau} này không?')" class="btn-action btn-delete">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty listTau}">
                        <tr><td colspan="6" style="text-align: center; padding: 40px; color: #64748b;">Chưa có dữ liệu tàu trong hệ thống.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </main>

</body>
</html>