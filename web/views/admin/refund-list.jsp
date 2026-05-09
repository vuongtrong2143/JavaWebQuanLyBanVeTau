<%--
    Document   : refund-list
    Updated for RefundAdminDTO-based refund management
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Duyệt Hoàn Tiền - Vé Tàu VN</title>
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
            min-width: 0;
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

        .filter-form {
            display: flex;
            gap: 8px;
            align-items: center;
            flex-wrap: wrap;
            background: white;
            border: 1px solid #e2e8f0;
            border-radius: 12px;
            padding: 10px 12px;
        }

        .filter-form label {
            color: #475569;
            font-weight: 800;
            font-size: 14px;
        }

        .filter-form select {
            padding: 8px 10px;
            border: 1px solid #cbd5e1;
            border-radius: 8px;
            outline: none;
            color: #0f172a;
            font-weight: 600;
            background: white;
        }

        .filter-form button {
            padding: 8px 14px;
            border: none;
            background: #1d4ed8;
            color: white;
            border-radius: 8px;
            font-weight: 800;
            cursor: pointer;
        }

        .filter-form button:hover {
            background: #1e40af;
        }

        .alert {
            padding: 12px 16px;
            border-radius: 10px;
            margin-bottom: 18px;
            font-weight: 700;
        }

        .alert-success {
            background: #dcfce7;
            color: #166534;
            border: 1px solid #bbf7d0;
        }

        .alert-warning {
            background: #fffbeb;
            color: #92400e;
            border: 1px solid #fde68a;
        }

        .alert-danger {
            background: #fee2e2;
            color: #991b1b;
            border: 1px solid #fecaca;
        }

        .content-card {
            background: white;
            border-radius: 16px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.03);
            border: 1px solid #e2e8f0;
            overflow: hidden;
        }

        .table-wrapper {
            overflow-x: auto;
        }

        .admin-table {
            width: 100%;
            border-collapse: collapse;
            text-align: left;
            min-width: 1050px;
        }

        .admin-table th {
            background: #f8fafc;
            padding: 16px 20px;
            font-size: 13px;
            font-weight: 800;
            color: #64748b;
            text-transform: uppercase;
            border-bottom: 1px solid #e2e8f0;
            white-space: nowrap;
        }

        .admin-table td {
            padding: 16px 20px;
            border-bottom: 1px solid #f1f5f9;
            font-size: 14px;
            color: #1e293b;
            vertical-align: middle;
        }

        .admin-table tr:hover {
            background: #fcfdfe;
        }

        .muted {
            color: #64748b;
            font-size: 13px;
        }

        .money {
            font-weight: 900;
            color: #16a34a;
            white-space: nowrap;
        }

        .btn-action {
            padding: 8px 12px;
            border-radius: 8px;
            font-size: 13px;
            font-weight: 800;
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
            padding: 6px 12px;
            border-radius: 999px;
            font-size: 12px;
            font-weight: 900;
            display: inline-flex;
            align-items: center;
            gap: 4px;
            white-space: nowrap;
        }

        .pill-pending {
            background: #ffedd5;
            color: #9a3412;
        }

        .pill-success {
            background: #dcfce7;
            color: #166534;
        }

        .pill-danger {
            background: #fee2e2;
            color: #991b1b;
        }

        .pill-muted {
            background: #e5e7eb;
            color: #374151;
            border: 1px solid #d1d5db;
        }

        .empty-row {
            text-align: center;
            padding: 44px !important;
            color: #64748b !important;
            font-weight: 600;
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
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/bookings">📦 Quản lý Đơn hàng</a></li>
        <li class="nav-item active"><a href="${pageContext.request.contextPath}/admin/refunds">💸 Duyệt Hoàn tiền</a></li>
        <li class="nav-item" style="margin-top: 40px;"><a href="${pageContext.request.contextPath}/admin/logout" style="color: #fca5a5;">🚪 Đăng xuất</a></li>
    </ul>
</aside>

<main class="main-content">
    <jsp:include page="/views/common/flash-message.jsp"/>
    <div class="page-header">
        <div class="page-title">
            <h1>Duyệt hoàn tiền</h1>
            <p>Quản lý các yêu cầu trả vé và hoàn tiền</p>
            
            <a href="${pageContext.request.contextPath}/admin/export?type=refunds"
                style="background:#ea580c;color:white;padding:10px 16px;border-radius:8px;text-decoration:none;font-weight:800;display:inline-block;">
                 📥 Xuất hoàn tiền CSV
            </a>
        </div>

        <form method="get" action="${pageContext.request.contextPath}/admin/refunds" class="filter-form">
            <label for="status">Trạng thái</label>
            <select id="status" name="status">
                <option value="">Tất cả</option>
                <option value="Chờ xử lý" ${statusValue eq 'Chờ xử lý' ? 'selected' : ''}>Chờ xử lý</option>
                <option value="Hoàn tất" ${statusValue eq 'Hoàn tất' ? 'selected' : ''}>Hoàn tất</option>
                <option value="Từ chối" ${statusValue eq 'Từ chối' ? 'selected' : ''}>Từ chối</option>
                <option value="Pending" ${statusValue eq 'Pending' ? 'selected' : ''}>Pending cũ</option>
            </select>
            <button type="submit">Lọc</button>
        </form>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>


    <div class="content-card">
        <div class="table-wrapper">
            <table class="admin-table">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Mã vé</th>
                    <th>Mã đặt chỗ</th>
                    <th>Khách hàng</th>
                    <th>Hành khách</th>
                    <th>Số tiền hoàn</th>
                    <th>Thời gian yêu cầu</th>
                    <th>Trạng thái</th>
                    <th>Thao tác</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="r" items="${refunds}">
                    <tr>
                        <td><strong>#${r.hoanTienId}</strong></td>

                        <td>
                            <b style="color:#0f172a;font-family:monospace;">${r.maVe}</b>
                        </td>

                        <td>
                            <span style="font-family:monospace;color:#1d4ed8;font-weight:800;">${r.maDatCho}</span>
                        </td>

                        <td>
                            <strong>${r.tenKhachHang}</strong><br>
                            <span class="muted">${r.emailKhachHang}</span><br>
                            <span class="muted">${r.soDienThoaiKhachHang}</span>
                        </td>

                        <td>
                            <strong>${r.tenHanhKhach}</strong><br>
                            <span class="muted">${r.soGiayTo}</span>
                        </td>

                        <td class="money">${r.soTienHoanText}</td>

                        <td>${r.thoiGianYeuCauText}</td>

                        <td>
                            <c:choose>
                                <c:when test="${r.trangThai eq 'Chờ xử lý'}">
                                    <span class="status-pill pill-pending">⏳ Chờ xử lý</span>
                                </c:when>

                                <c:when test="${r.trangThai eq 'Hoàn tất'}">
                                    <span class="status-pill pill-success">✅ Hoàn tất</span>
                                </c:when>

                                <c:when test="${r.trangThai eq 'Từ chối'}">
                                    <span class="status-pill pill-danger">❌ Từ chối</span>
                                </c:when>

                                <c:otherwise>
                                    <span class="status-pill pill-muted">${r.trangThai}</span>
                                </c:otherwise>
                            </c:choose>
                        </td>

                        <td>
                            <a href="${pageContext.request.contextPath}/admin/refunds?action=detail&id=${r.hoanTienId}"
                               class="btn-action btn-view">
                                Chi tiết
                            </a>
                        </td>
                    </tr>
                </c:forEach>

                <c:if test="${empty refunds}">
                    <tr>
                        <td colspan="9" class="empty-row">Chưa có yêu cầu hoàn tiền nào.</td>
                    </tr>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</main>

</body>
</html>
