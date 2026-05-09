<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Kiểm tra hệ thống - VETAU ADMIN</title>
    <style>
        :root {
            --sidebar-width: 260px;
            --primary-dark: #0f172a;
            --accent-blue: #3b82f6;
        }

        body {
            background: #f8fafc;
            font-family: 'Segoe UI', Tahoma, sans-serif;
            margin: 0;
            display: flex;
            color: #1e293b;
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
            background: white;
            border: 1px solid #e2e8f0;
            border-radius: 16px;
            padding: 22px 24px;
            margin-bottom: 24px;
            box-shadow: 0 4px 10px rgba(15,23,42,0.04);
        }

        .page-header h1 {
            margin: 0 0 6px;
            font-size: 24px;
            color: #0f172a;
        }

        .page-header p {
            margin: 0;
            color: #64748b;
        }

        .summary-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 18px;
            margin-bottom: 24px;
        }

        .summary-card {
            background: white;
            border: 1px solid #e2e8f0;
            border-radius: 16px;
            padding: 20px;
            box-shadow: 0 4px 10px rgba(15,23,42,0.04);
        }

        .summary-label {
            font-size: 13px;
            text-transform: uppercase;
            color: #64748b;
            font-weight: 800;
            margin-bottom: 6px;
        }

        .summary-value {
            font-size: 30px;
            font-weight: 900;
        }

        .value-ok {
            color: #16a34a;
        }

        .value-warning {
            color: #d97706;
        }

        .value-error {
            color: #dc2626;
        }

        .content-card {
            background: white;
            border: 1px solid #e2e8f0;
            border-radius: 16px;
            padding: 24px;
            margin-bottom: 24px;
            box-shadow: 0 4px 10px rgba(15,23,42,0.04);
        }

        .section-title {
            margin: 0 0 18px;
            font-size: 18px;
            font-weight: 900;
            color: #0f172a;
            border-bottom: 2px solid #f1f5f9;
            padding-bottom: 12px;
        }

        .count-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(210px, 1fr));
            gap: 12px;
        }

        .count-item {
            background: #f8fafc;
            border: 1px solid #e2e8f0;
            border-radius: 12px;
            padding: 14px;
        }

        .count-name {
            color: #64748b;
            font-size: 13px;
            font-weight: 800;
        }

        .count-value {
            font-size: 24px;
            font-weight: 900;
            color: #1d4ed8;
            margin-top: 4px;
        }

        .check-table {
            width: 100%;
            border-collapse: collapse;
        }

        .check-table th {
            text-align: left;
            background: #f8fafc;
            color: #64748b;
            padding: 12px;
            font-size: 13px;
            text-transform: uppercase;
            border-bottom: 1px solid #e2e8f0;
        }

        .check-table td {
            padding: 13px 12px;
            border-bottom: 1px solid #f1f5f9;
            vertical-align: top;
            font-size: 14px;
        }

        .badge {
            display: inline-flex;
            align-items: center;
            gap: 5px;
            padding: 5px 10px;
            border-radius: 999px;
            font-size: 12px;
            font-weight: 900;
            white-space: nowrap;
        }

        .badge-ok {
            background: #dcfce7;
            color: #166534;
            border: 1px solid #bbf7d0;
        }

        .badge-warning {
            background: #fef3c7;
            color: #92400e;
            border: 1px solid #fde68a;
        }

        .badge-error {
            background: #fee2e2;
            color: #991b1b;
            border: 1px solid #fecaca;
        }

        .alert-error {
            background: #fee2e2;
            color: #991b1b;
            border: 1px solid #fecaca;
            padding: 14px;
            border-radius: 12px;
            font-weight: 700;
            margin-bottom: 18px;
        }

        .actions {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
            margin-top: 18px;
        }

        .btn {
            border: none;
            padding: 11px 18px;
            border-radius: 10px;
            font-weight: 800;
            text-decoration: none;
            cursor: pointer;
            display: inline-block;
        }

        .btn-primary {
            background: #1d4ed8;
            color: white;
        }

        .btn-light {
            background: #f1f5f9;
            color: #334155;
            border: 1px solid #cbd5e1;
        }

        @media (max-width: 900px) {
            body {
                display: block;
            }

            .sidebar {
                position: static;
                width: 100%;
                height: auto;
            }

            .main-content {
                margin-left: 0;
                padding: 18px;
            }

            .summary-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>

<aside class="sidebar">
    <div class="sidebar-brand">🚂 VETAU<span>ADMIN</span></div>
    <ul class="nav-menu">
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/dashboard">📊 Bảng điều khiển</a></li>
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/bookings">📦 Quản lý Đơn hàng</a></li>
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/refunds">💸 Duyệt Hoàn tiền</a></li>
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/reports">📈 Báo cáo Doanh thu</a></li>
        <li class="nav-item active"><a href="${pageContext.request.contextPath}/admin/system-check">🧪 Kiểm tra hệ thống</a></li>
        <li class="nav-item" style="margin-top: 40px;">
            <a href="${pageContext.request.contextPath}/admin/logout" style="color: #fca5a5;">🚪 Đăng xuất</a>
        </li>
    </ul>
</aside>

<main class="main-content">

    <div class="page-header">
        <h1>🧪 Kiểm tra toàn vẹn hệ thống</h1>
        <p>Trang này giúp kiểm tra nhanh dữ liệu sau 8 giai đoạn trước khi demo hoặc nộp bài.</p>

        <div class="actions">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/system-check">Chạy lại kiểm tra</a>
            <a class="btn btn-light" href="${pageContext.request.contextPath}/admin/dashboard">Về dashboard</a>
        </div>
    </div>

    <c:if test="${not empty error}">
        <div class="alert-error">
            ❌ ${error}
        </div>
    </c:if>

    <div class="summary-grid">
        <div class="summary-card">
            <div class="summary-label">Kiểm tra đạt</div>
            <div class="summary-value value-ok">${okCount}</div>
        </div>

        <div class="summary-card">
            <div class="summary-label">Cảnh báo</div>
            <div class="summary-value value-warning">${warningCount}</div>
        </div>

        <div class="summary-card">
            <div class="summary-label">Lỗi cần xử lý</div>
            <div class="summary-value value-error">${errorCount}</div>
        </div>
    </div>

    <div class="content-card">
        <h2 class="section-title">📌 Số liệu tổng quan</h2>

        <div class="count-grid">
            <c:forEach var="entry" items="${summaryCounts}">
                <div class="count-item">
                    <div class="count-name">${entry.key}</div>
                    <div class="count-value">${entry.value}</div>
                </div>
            </c:forEach>
        </div>
    </div>

    <div class="content-card">
        <h2 class="section-title">✅ Danh sách kiểm tra</h2>

        <table class="check-table">
            <thead>
            <tr>
                <th>Nhóm</th>
                <th>Hạng mục</th>
                <th>Trạng thái</th>
                <th>Số lượng</th>
                <th>Ghi chú</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="item" items="${checks}">
                <tr>
                    <td>${item.groupName}</td>
                    <td><strong>${item.checkName}</strong></td>
                    <td>
                        <c:choose>
                            <c:when test="${item.status eq 'OK'}">
                                <span class="badge badge-ok">✅ OK</span>
                            </c:when>

                            <c:when test="${item.status eq 'WARNING'}">
                                <span class="badge badge-warning">⚠️ Cảnh báo</span>
                            </c:when>

                            <c:otherwise>
                                <span class="badge badge-error">❌ Lỗi</span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${item.countValue}</td>
                    <td>${item.message}</td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>

</main>

</body>
</html>