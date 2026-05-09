<%-- 
    Document   : booking-list
    Updated    : Giai đoạn 3 cleanup giữ chỗ quá hạn
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Đơn hàng - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/status.css">
    <style>
        :root {
            --sidebar-width: 260px;
            --primary-dark: #0f172a;
            --accent-blue: #3b82f6;
        }

        body {
            background: #f8fafc;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            display: flex;
        }

        .sidebar {
            width: var(--sidebar-width);
            background: var(--primary-dark);
            height: 100vh;
            position: fixed;
            color: white;
            padding: 20px 0;
            z-index: 100;
        }

        .sidebar-brand {
            padding: 0 24px 30px;
            font-size: 22px;
            font-weight: 900;
            letter-spacing: -1px;
        }

        .sidebar-brand span {
            color: var(--accent-blue);
        }

        .nav-menu {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .nav-item a {
            display: flex;
            align-items: center;
            padding: 14px 24px;
            color: #94a3b8;
            text-decoration: none;
            transition: 0.2s;
            font-size: 15px;
            font-weight: 500;
        }

        .nav-item a:hover,
        .nav-item.active a {
            background: rgba(255,255,255,0.05);
            color: white;
            border-left: 4px solid var(--accent-blue);
        }

        .main-content {
            margin-left: var(--sidebar-width);
            flex: 1;
            padding: 30px;
        }

        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-end;
            margin-bottom: 24px;
            gap: 16px;
            flex-wrap: wrap;
        }

        .page-title h1 {
            margin: 0 0 4px 0;
            font-size: 24px;
            font-weight: 800;
            color: #0f172a;
        }

        .page-title p {
            margin: 0;
            color: #64748b;
            font-size: 14px;
        }

        .content-card {
            background: white;
            border-radius: 16px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.03);
            border: 1px solid #e2e8f0;
            overflow: hidden;
        }

        .admin-table {
            width: 100%;
            border-collapse: collapse;
            text-align: left;
        }

        .admin-table th {
            background: #f8fafc;
            padding: 16px 24px;
            font-size: 13px;
            font-weight: 700;
            color: #64748b;
            text-transform: uppercase;
            border-bottom: 1px solid #e2e8f0;
        }

        .admin-table td {
            padding: 16px 24px;
            border-bottom: 1px solid #f1f5f9;
            font-size: 14px;
            color: #1e293b;
            vertical-align: middle;
        }

        .admin-table tr:hover {
            background: #fcfdfe;
        }

        .btn-action {
            padding: 6px 12px;
            border-radius: 6px;
            font-size: 13px;
            font-weight: 600;
            text-decoration: none;
            transition: 0.2s;
            display: inline-block;
            border: 1px solid transparent;
            white-space: nowrap;
        }

        .btn-view {
            background: #eff6ff;
            color: #1d4ed8;
            border-color: #bfdbfe;
        }

        .btn-view:hover {
            background: #dbeafe;
        }

        .status-pill {
            display: inline-block;
            padding: 5px 12px;
            border-radius: 999px;
            font-size: 12px;
            font-weight: 800;
            white-space: nowrap;
            text-transform: uppercase;
        }

        .pill-success {
            background: #dcfce7;
            color: #166534;
            border: 1px solid #bbf7d0;
        }

        .pill-warning {
            background: #fef3c7;
            color: #92400e;
            border: 1px solid #fde68a;
        }

        .pill-danger {
            background: #fee2e2;
            color: #991b1b;
            border: 1px solid #fecaca;
        }

        .pill-neutral {
            background: #e5e7eb;
            color: #374151;
            border: 1px solid #cbd5e1;
        }

        .pill-refund {
            background: #ffedd5;
            color: #9a3412;
            border: 1px solid #fed7aa;
        }

        .alert-box {
            padding: 12px 16px;
            border-radius: 10px;
            margin-bottom: 16px;
            font-weight: 700;
        }

        .alert-danger {
            background: #fef2f2;
            color: #dc2626;
            border: 1px solid #fecaca;
        }

        .alert-warning {
            background: #fffbeb;
            color: #92400e;
            border: 1px solid #fde68a;
        }

        .table-wrapper {
            overflow-x: auto;
        }
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
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/lich-dung">📍 Quản lý Lịch Dừng</a></li>
        <li class="nav-item active"><a href="${pageContext.request.contextPath}/admin/bookings">📦 Quản lý Đơn hàng</a></li>
        <li class="nav-item" style="margin-top: 40px;"><a href="${pageContext.request.contextPath}/admin/logout" style="color: #fca5a5;">🚪 Đăng xuất</a></li>
    </ul>
</aside>

<main class="main-content">
    <div class="page-header">
        <div class="page-title">
            <h1>Quản lý Đơn hàng (Bookings)</h1>
            <p>Theo dõi giao dịch, trạng thái đơn và thông tin đặt chỗ của khách hàng.</p>
            
            <a href="${pageContext.request.contextPath}/admin/export?type=bookings"
                style="background:#0f766e;color:white;padding:10px 16px;border-radius:8px;text-decoration:none;font-weight:800;display:inline-block;">
                 📥 Xuất đơn hàng CSV
            </a>
        </div>
    </div>

    <c:if test="${param.msg == 'cancelled'}">
        <div class="alert-box alert-danger">⚠️ Đã hủy đơn hàng thành công!</div>
    </c:if>

    <c:if test="${not empty cleanupWarning}">
        <div class="alert-box alert-warning">
            ${cleanupWarning}
        </div>
    </c:if>

    <div class="content-card">
        <div class="table-wrapper">
            <table class="admin-table">
                <thead>
                <tr>
                    <th>Mã Đặt Chỗ</th>
                    <th>Thời gian đặt</th>
                    <th>Loại đơn</th>
                    <th>Tổng tiền</th>
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="item" items="${listBookings}">
                    <tr>
                        <td>
                            <b style="color: #0f172a; font-family: monospace; font-size: 16px;">#${item.maDatCho}</b>
                        </td>
                        <td>${item.ngayDat}</td>
                        <td>${item.loaiDonHang}</td>
                        <td style="font-weight: 800; color: #ea580c;">
                            <fmt:formatNumber value="${item.tongThanhToan}" pattern="#,###"/> đ
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${item.trangThai eq 'Đã thanh toán'}">
                                    <span class="status-pill pill-success">Đã thanh toán</span>
                                </c:when>

                                <c:when test="${item.trangThai eq 'Chờ thanh toán'}">
                                    <span class="status-pill pill-warning">Chờ thanh toán</span>
                                </c:when>

                                <c:when test="${item.trangThai eq 'Hết hạn'}">
                                    <span class="status-pill pill-neutral">Hết hạn</span>
                                </c:when>

                                <c:when test="${item.trangThai eq 'Đã hủy'}">
                                    <span class="status-pill pill-danger">Đã hủy</span>
                                </c:when>

                                <c:when test="${item.trangThai eq 'Hoàn tiền một phần' || item.trangThai eq 'Hoàn tiền toàn bộ'}">
                                    <span class="status-pill pill-refund">${item.trangThai}</span>
                                </c:when>

                                <c:otherwise>
                                    <span class="status-pill pill-neutral">${item.trangThai}</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <a href="${pageContext.request.contextPath}/admin/bookings?action=detail&id=${item.id}" class="btn-action btn-view">
                                👁️ Xem chi tiết
                            </a>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty listBookings}">
                    <tr>
                        <td colspan="6" style="text-align: center; padding: 40px; color: #64748b;">
                            Chưa có đơn hàng nào phát sinh.
                        </td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</main>

</body>
</html>
