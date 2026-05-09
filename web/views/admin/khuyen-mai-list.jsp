<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Khuyến Mãi - Vé Tàu VN</title>
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

        .btn-add { background: var(--accent-blue); color: white; padding: 10px 20px; border-radius: 8px; font-weight: 600; text-decoration: none; transition: 0.2s; box-shadow: 0 4px 6px rgba(59, 130, 246, 0.3); }
        .btn-add:hover { background: #2563eb; transform: translateY(-2px); }

        .content-card { background: white; border-radius: 16px; box-shadow: 0 4px 10px rgba(0,0,0,0.03); border: 1px solid #e2e8f0; overflow: hidden; }
        .admin-table { width: 100%; border-collapse: collapse; text-align: left; }
        .admin-table th { background: #f8fafc; padding: 12px; border-bottom: 1px solid #e2e8f0; font-size: 13px; font-weight: 700; color: #64748b; text-transform: uppercase; }
        .admin-table td { padding: 14px 12px; border-bottom: 1px solid #f1f5f9; font-size: 13px; color: #1e293b; vertical-align: middle; }
        
        .btn-action { padding: 6px 12px; border-radius: 6px; font-size: 12px; font-weight: 700; text-decoration: none; display: inline-block; transition: 0.2s; }
        .btn-edit { background: #eff6ff; color: #1d4ed8; border: 1px solid #bfdbfe; }
        .btn-delete { background: #fef2f2; color: #dc2626; border: 1px solid #fecaca; margin-left: 4px; }

        .status-pill { padding: 4px 10px; border-radius: 20px; font-size: 11px; font-weight: bold; }
        .pill-active { background: #dcfce7; color: #16a34a; }
        .pill-expired { background: #fee2e2; color: #dc2626; }
    </style>
</head>
<body>

    <aside class="sidebar">
        <div class="sidebar-brand">🚂 VETAU<span>ADMIN</span></div>
        <ul class="nav-menu">
            <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/dashboard">📊 Bảng điều khiển</a></li>
            <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/tau">🚆 Quản lý Tàu</a></li>
            <li class="nav-item active"><a href="${pageContext.request.contextPath}/admin/khuyen-mai">🎁 Khuyến mãi</a></li>
            <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/bang-gia">💰 Bảng giá</a></li>
            <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/bookings">📦 Đơn hàng</a></li>
            <li class="nav-item" style="margin-top: 40px;"><a href="${pageContext.request.contextPath}/admin/logout" style="color: #fca5a5;">🚪 Đăng xuất</a></li>
        </ul>
    </aside>

    <main class="main-content">
        <div class="page-header">
            <div class="page-title">
                <h1>Chương trình Khuyến mãi</h1>
                <p>Thiết lập các mã giảm giá và chiến dịch ưu đãi cho hành khách.</p>
            </div>
            <a href="${pageContext.request.contextPath}/admin/khuyen-mai?action=add" class="btn-add">➕ Tạo Khuyến Mãi</a>
        </div>

        <c:if test="${param.msg == 'saved'}">
            <div style="background: #dcfce7; color: #166534; padding: 12px; border-radius: 8px; margin-bottom: 20px; font-weight: bold; border: 1px solid #bbf7d0;">✅ Đã cập nhật chương trình khuyến mãi!</div>
        </c:if>

        <div class="content-card">
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>Mã / Tên chương trình</th>
                        <th>Mức giảm</th>
                        <th>Đơn tối thiểu</th>
                        <th>Áp dụng</th>
                        <th>Hiệu lực đến</th>
                        <th>Trạng thái</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${listKhuyenMai}">
                        <tr>
                            <td>
                                <b style="color: #1d4ed8; font-family: monospace; font-size: 15px;">${item.maKhuyenMai}</b><br>
                                <span style="font-size: 12px; color: #64748b;">${item.tenChuongTrinh}</span>
                            </td>
                            <td>
                                <span style="color: #ea580c; font-weight: 800;">${item.phanTramGiam}%</span><br>
                                <span style="font-size: 11px; color: #94a3b8;">Max: <fmt:formatNumber value="${item.giamToiDa}" pattern="#,###"/> đ</span>
                            </td>
                            <td><fmt:formatNumber value="${item.giaTriDonToiThieu}" pattern="#,###"/> đ</td>
                            <td><span style="font-size: 12px;">${empty item.phuongThucTtApDung ? 'Tất cả' : item.phuongThucTtApDung}</span></td>
                            <td><span style="font-size: 12px;">${item.ngayKetThuc}</span></td>
                            <td>
                                <span class="status-pill ${item.trangThai == 'Hoạt động' ? 'pill-active' : 'pill-expired'}">${item.trangThai}</span>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/khuyen-mai?action=edit&id=${item.id}" class="btn-action btn-edit">Sửa</a>
                                <a href="${pageContext.request.contextPath}/admin/khuyen-mai?action=delete&id=${item.id}" onclick="return confirm('Ngưng kích hoạt khuyến mãi này?')" class="btn-action btn-delete">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty listKhuyenMai}">
                        <tr><td colspan="7" style="text-align: center; padding: 40px; color: #64748b;">Chưa có chương trình khuyến mãi nào được tạo.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </main>
</body>
</html>