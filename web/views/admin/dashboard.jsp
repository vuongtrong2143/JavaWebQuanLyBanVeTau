<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Bảng điều khiển - Quản trị Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/status.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        :root {
            --sidebar-width: 260px;
            --primary-dark: #0f172a;
            --accent-blue: #3b82f6;
            --text-gray: #64748b;
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
            display: flex;
            align-items: center;
            gap: 10px;
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
            gap: 12px;
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

        .top-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 24px;
            background: white;
            padding: 16px 24px;
            border-radius: 12px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.02);
            border: 1px solid #e2e8f0;
        }

        .top-bar h2 {
            margin: 0;
            font-size: 20px;
            color: #0f172a;
        }

        .top-bar p {
            margin: 4px 0 0;
            color: #64748b;
            font-size: 13px;
        }

        .admin-user {
            display: flex;
            align-items: center;
            gap: 12px;
            font-weight: 600;
        }

        .alert-warning {
            padding: 12px 14px;
            background: #fffbeb;
            color: #92400e;
            border: 1px solid #fde68a;
            border-radius: 10px;
            margin-bottom: 16px;
            font-weight: 700;
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(4, 1fr);
            gap: 20px;
            margin-bottom: 30px;
        }

        .stat-card {
            background: white;
            padding: 24px;
            border-radius: 16px;
            box-shadow: 0 4px 6px -1px rgba(0,0,0,0.02);
            border: 1px solid #e2e8f0;
            position: relative;
            overflow: hidden;
        }

        .stat-label {
            color: var(--text-gray);
            font-size: 13px;
            font-weight: 800;
            text-transform: uppercase;
            margin-bottom: 8px;
            letter-spacing: 0.5px;
        }

        .stat-value {
            font-size: 26px;
            font-weight: 900;
            color: #1e293b;
        }

        .stat-sub {
            font-size: 13px;
            margin-top: 10px;
            font-weight: 700;
            color: #64748b;
        }

        .stat-trend {
            font-size: 13px;
            margin-top: 10px;
            font-weight: 700;
            display: inline-block;
            padding: 4px 8px;
            border-radius: 6px;
        }

        .trend-up {
            background: #dcfce7;
            color: #166534;
        }

        .trend-warn {
            background: #ffedd5;
            color: #9a3412;
        }

        .charts-grid {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 24px;
            margin-bottom: 30px;
        }

        .chart-container,
        .content-card {
            background: white;
            padding: 24px;
            border-radius: 16px;
            border: 1px solid #e2e8f0;
            box-shadow: 0 4px 6px -1px rgba(0,0,0,0.02);
        }

        .chart-title,
        .section-title {
            font-size: 16px;
            font-weight: 900;
            color: #0f172a;
            margin-top: 0;
            margin-bottom: 20px;
            border-bottom: 2px solid #f1f5f9;
            padding-bottom: 12px;
        }

        .empty-note {
            color: #64748b;
            font-weight: 600;
            text-align: center;
            padding: 20px 0;
        }

        .admin-table {
            width: 100%;
            border-collapse: collapse;
            text-align: left;
        }

        .admin-table th {
            background: #f8fafc;
            padding: 12px 14px;
            font-size: 13px;
            font-weight: 900;
            color: #64748b;
            text-transform: uppercase;
            border-bottom: 1px solid #e2e8f0;
            white-space: nowrap;
        }

        .admin-table td {
            padding: 13px 14px;
            border-bottom: 1px solid #f1f5f9;
            font-size: 14px;
            color: #1e293b;
            vertical-align: middle;
        }

        .status-pill {
            display: inline-block;
            padding: 5px 10px;
            border-radius: 999px;
            font-size: 12px;
            font-weight: 900;
            white-space: nowrap;
        }

        .status-paid {
            background: #dcfce7;
            color: #166534;
        }

        .status-pending {
            background: #fef3c7;
            color: #92400e;
        }

        .status-expired {
            background: #e5e7eb;
            color: #374151;
        }

        .status-cancelled {
            background: #fee2e2;
            color: #991b1b;
        }

        .status-refund {
            background: #ffedd5;
            color: #9a3412;
        }

        @media (max-width: 1024px) {
            .stats-grid {
                grid-template-columns: 1fr 1fr;
            }

            .charts-grid {
                grid-template-columns: 1fr;
            }
        }

        @media (max-width: 720px) {
            .sidebar {
                position: static;
                width: 100%;
                height: auto;
            }

            body {
                display: block;
            }

            .main-content {
                margin-left: 0;
                padding: 18px;
            }

            .stats-grid {
                grid-template-columns: 1fr;
            }

            .top-bar {
                flex-direction: column;
                align-items: flex-start;
            }
        }
    </style>
</head>
<body>

<aside class="sidebar">
    <div class="sidebar-brand">🚂 VETAU<span>ADMIN</span></div>
    <ul class="nav-menu">
        <li class="nav-item active"><a href="${pageContext.request.contextPath}/admin/dashboard">📊 Bảng điều khiển</a></li>
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/tau">🚆 Quản lý Tàu</a></li>
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/ga">🚉 Quản lý Nhà Ga</a></li>
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/chuyen-tau">🗓️ Quản lý Lịch trình</a></li>
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/lich-dung">📍 Quản lý Lịch Dừng</a></li>
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/bookings">📦 Quản lý Đơn hàng</a></li>
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/refunds">💸 Duyệt Hoàn tiền</a></li>
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/reports">📈 Báo cáo Doanh thu</a></li>
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/system-check">🛠️ Kiểm tra hệ thống</a></li>
        <li class="nav-item" style="margin-top: 40px;">
            <a href="${pageContext.request.contextPath}/admin/logout" style="color: #fca5a5; border-left-color: transparent;">🚪 Đăng xuất</a>
        </li>
    </ul>
</aside>

<main class="main-content">
    <jsp:include page="/views/common/flash-message.jsp"/>
    <header class="top-bar">
        <div>
            <h2>Tổng quan Hệ thống</h2>
            <p>Dữ liệu thống kê được lấy trực tiếp từ database qua BaoCaoService.</p>
        </div>

        <div class="admin-user">
            <div style="text-align: right;">
                <div style="font-size: 15px; color: #1d4ed8;">${sessionScope.currentAdmin}</div>
                <div style="font-size: 12px; color: var(--text-gray);">Quản trị viên cấp cao</div>
            </div>
            <div style="width: 44px; height: 44px; background: #e2e8f0; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 20px;">👨‍💼</div>
        </div>
    </header>
        <div style="display: flex; gap: 10px; flex-wrap: wrap; margin-bottom: 20px;">
            <a href="${pageContext.request.contextPath}/admin/export?type=revenue"
               style="background:#16a34a;color:white;padding:10px 16px;border-radius:8px;text-decoration:none;font-weight:800;">
                📥 Xuất doanh thu CSV
            </a>

            <a href="${pageContext.request.contextPath}/admin/export?type=bookings"
               style="background:#0f766e;color:white;padding:10px 16px;border-radius:8px;text-decoration:none;font-weight:800;">
                📦 Xuất đơn hàng CSV
            </a>

            <a href="${pageContext.request.contextPath}/admin/export?type=refunds"
               style="background:#ea580c;color:white;padding:10px 16px;border-radius:8px;text-decoration:none;font-weight:800;">
                💸 Xuất hoàn tiền CSV
            </a>

            <a href="${pageContext.request.contextPath}/admin/export?type=tickets"
               style="background:#1d4ed8;color:white;padding:10px 16px;border-radius:8px;text-decoration:none;font-weight:800;">
                🎫 Xuất vé CSV
            </a>
        </div>
    <c:if test="${not empty dashboardWarning}">
        <div class="alert-warning">
            ${dashboardWarning}
        </div>
    </c:if>

    <div class="stats-grid">
        <div class="stat-card" style="border-bottom: 4px solid #10b981;">
            <div class="stat-label">Doanh thu thuần tháng này</div>
            <div class="stat-value">
                <c:choose>
                    <c:when test="${not empty stats}">${stats.netRevenueThisMonthText}</c:when>
                    <c:otherwise>${totalRevenue}</c:otherwise>
                </c:choose>
            </div>
            <div class="stat-sub">
                Tổng thu:
                <c:choose>
                    <c:when test="${not empty stats}">${stats.revenueThisMonthText}</c:when>
                    <c:otherwise>0 đ</c:otherwise>
                </c:choose>
                · Hoàn:
                <c:choose>
                    <c:when test="${not empty stats}">${stats.refundThisMonthText}</c:when>
                    <c:otherwise>0 đ</c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="stat-card" style="border-bottom: 4px solid #3b82f6;">
            <div class="stat-label">Vé bán tháng này</div>
            <div class="stat-value">
                <c:choose>
                    <c:when test="${not empty stats}">${stats.ticketsThisMonth}</c:when>
                    <c:otherwise>${totalTickets}</c:otherwise>
                </c:choose>
            </div>
            <div class="stat-trend trend-up">Tính từ VE + DAT_CHO</div>
        </div>

        <div class="stat-card" style="border-bottom: 4px solid #8b5cf6;">
            <div class="stat-label">Khách hàng mới tháng này</div>
            <div class="stat-value">
                <c:choose>
                    <c:when test="${not empty stats}">${stats.newCustomersThisMonth}</c:when>
                    <c:otherwise>${newUsers}</c:otherwise>
                </c:choose>
            </div>
            <div class="stat-trend trend-up">Từ bảng KHACH_HANG</div>
        </div>

        <div class="stat-card" style="border-bottom: 4px solid #f97316;">
            <div class="stat-label">Hoàn tiền chờ xử lý</div>
            <div class="stat-value" style="color: #ea580c;">
                <c:choose>
                    <c:when test="${not empty stats}">${stats.pendingRefunds}</c:when>
                    <c:otherwise>${pendingRefunds}</c:otherwise>
                </c:choose>
            </div>
            <div class="stat-trend trend-warn">Từ HOAN_TIEN = Chờ xử lý</div>
        </div>
    </div>

    <div class="charts-grid">
        <div class="chart-container">
            <h3 class="chart-title">📈 Doanh thu thuần 6 tháng gần nhất</h3>
            <canvas id="revenueChart" height="120"></canvas>
            <c:if test="${empty chartMonths}">
                <div class="empty-note">Chưa có dữ liệu biểu đồ trên dashboard. Có thể xem biểu đồ đầy đủ tại mục Báo cáo Doanh thu.</div>
            </c:if>
        </div>

        <div class="chart-container">
            <h3 class="chart-title">🚆 Top tuyến theo doanh thu</h3>
            <canvas id="routeChart"></canvas>
            <c:if test="${empty topRoutes}">
                <div class="empty-note">Chưa có dữ liệu top tuyến trên dashboard.</div>
            </c:if>
        </div>
    </div>

    <c:if test="${not empty recentBookings}">
        <div class="content-card">
            <h3 class="section-title">📦 Đơn đặt chỗ gần đây</h3>

            <table class="admin-table">
                <thead>
                <tr>
                    <th>Mã đơn</th>
                    <th>Khách hàng</th>
                    <th>Ngày đặt</th>
                    <th>Tổng thanh toán</th>
                    <th>Trạng thái</th>
                </tr>
                </thead>

                <tbody>
                <c:forEach var="b" items="${recentBookings}">
                    <tr>
                        <td><strong style="font-family:monospace;color:#1d4ed8;">${b.maDatCho}</strong></td>
                        <td>${b.tenKhachHang}</td>
                        <td>${b.ngayDatText}</td>
                        <td><strong>${b.tongThanhToanText}</strong></td>
                        <td>
                            <c:choose>
                                <c:when test="${b.trangThai eq 'Đã thanh toán'}">
                                    <span class="status-pill status-paid">Đã thanh toán</span>
                                </c:when>
                                <c:when test="${b.trangThai eq 'Chờ thanh toán'}">
                                    <span class="status-pill status-pending">Chờ thanh toán</span>
                                </c:when>
                                <c:when test="${b.trangThai eq 'Hết hạn'}">
                                    <span class="status-pill status-expired">Hết hạn</span>
                                </c:when>
                                <c:when test="${b.trangThai eq 'Đã hủy'}">
                                    <span class="status-pill status-cancelled">Đã hủy</span>
                                </c:when>
                                <c:when test="${b.trangThai eq 'Hoàn tiền một phần' || b.trangThai eq 'Hoàn tiền toàn bộ'}">
                                    <span class="status-pill status-refund">${b.trangThai}</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-pill status-expired">${b.trangThai}</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>
</main>

<c:set var="dashboardChartMonths" value="${empty chartMonths ? '[]' : chartMonths}" />
<c:set var="dashboardChartRevenues" value="${empty chartRevenues ? '[]' : chartRevenues}" />
<c:set var="dashboardRouteLabels" value="${empty chartRouteLabels ? '[]' : chartRouteLabels}" />
<c:set var="dashboardRouteValues" value="${empty chartRouteValues ? '[]' : chartRouteValues}" />

<script>
    const revenueLabels = ${dashboardChartMonths};
    const revenueValues = ${dashboardChartRevenues};
    const routeLabels = ${dashboardRouteLabels};
    const routeValues = ${dashboardRouteValues};

    const ctxRevenue = document.getElementById('revenueChart').getContext('2d');
    new Chart(ctxRevenue, {
        type: 'line',
        data: {
            labels: revenueLabels,
            datasets: [{
                label: 'Doanh thu thuần (VNĐ)',
                data: revenueValues,
                borderColor: '#3b82f6',
                backgroundColor: 'rgba(59, 130, 246, 0.15)',
                borderWidth: 3,
                pointBackgroundColor: '#1d4ed8',
                pointRadius: 4,
                fill: true,
                tension: 0.4
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: { display: false },
                tooltip: {
                    callbacks: {
                        label: function(context) {
                            let value = context.raw || 0;
                            return ' ' + new Intl.NumberFormat('vi-VN').format(value) + ' đ';
                        }
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    ticks: {
                        callback: function(value) {
                            return new Intl.NumberFormat('vi-VN').format(value);
                        }
                    }
                }
            }
        }
    });

    const ctxRoute = document.getElementById('routeChart').getContext('2d');
    new Chart(ctxRoute, {
        type: 'doughnut',
        data: {
            labels: routeLabels,
            datasets: [{
                data: routeValues,
                backgroundColor: ['#1e40af', '#3b82f6', '#60a5fa', '#93c5fd', '#bfdbfe'],
                borderWidth: 2,
                borderColor: '#ffffff'
            }]
        },
        options: {
            responsive: true,
            plugins: { legend: { position: 'bottom' } },
            cutout: '68%'
        }
    });
</script>
</body>
</html>
