<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Báo Cáo Doanh Thu - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/status.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
            font-weight: 900;
            color: #0f172a;
        }

        .page-title p {
            margin: 0;
            color: #64748b;
            font-size: 14px;
        }

        .btn-filter {
            background: #1d4ed8;
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 8px;
            font-weight: 800;
            cursor: pointer;
            transition: 0.2s;
            text-decoration: none;
            display: inline-block;
        }

        .btn-filter:hover {
            background: #1e40af;
        }

        .info-bar {
            background: white;
            padding: 16px 24px;
            border-radius: 12px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.02);
            border: 1px solid #e2e8f0;
            margin-bottom: 24px;
            display: flex;
            justify-content: space-between;
            gap: 16px;
            align-items: center;
            flex-wrap: wrap;
            color: #475569;
            font-weight: 700;
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

        .kpi-grid {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 20px;
            margin-bottom: 30px;
        }

        .kpi-card {
            background: linear-gradient(135deg, #1d4ed8, #3b82f6);
            color: white;
            padding: 24px;
            border-radius: 16px;
            box-shadow: 0 10px 15px -3px rgba(59, 130, 246, 0.3);
            position: relative;
            overflow: hidden;
        }

        .kpi-card.orange {
            background: linear-gradient(135deg, #ea580c, #f97316);
            box-shadow: 0 10px 15px -3px rgba(234, 88, 12, 0.3);
        }

        .kpi-card.green {
            background: linear-gradient(135deg, #047857, #10b981);
            box-shadow: 0 10px 15px -3px rgba(16, 185, 129, 0.3);
        }

        .kpi-label {
            font-size: 14px;
            font-weight: 700;
            opacity: 0.9;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-bottom: 8px;
        }

        .kpi-value {
            font-size: 32px;
            font-weight: 900;
        }

        .charts-grid {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 24px;
            margin-bottom: 30px;
        }

        .chart-box,
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
            padding-bottom: 12px;
            border-bottom: 2px solid #f1f5f9;
        }

        .table-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 24px;
            margin-bottom: 30px;
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

        .money {
            font-weight: 900;
            color: #16a34a;
            white-space: nowrap;
        }

        .empty-note {
            color: #64748b;
            font-weight: 600;
            text-align: center;
            padding: 20px 0;
        }

        @media (max-width: 1024px) {
            .kpi-grid,
            .charts-grid,
            .table-grid {
                grid-template-columns: 1fr;
            }
        }

        @media print {
            .sidebar,
            .page-header button,
            .info-bar a {
                display: none !important;
            }

            .main-content {
                margin-left: 0;
            }

            body {
                background: white;
                display: block;
            }

            .chart-box,
            .content-card,
            .kpi-card {
                box-shadow: none;
                page-break-inside: avoid;
            }
        }
    </style>
</head>
<body>

<aside class="sidebar">
    <div class="sidebar-brand">🚂 VETAU<span>ADMIN</span></div>
    <ul class="nav-menu">
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/dashboard">📊 Bảng điều khiển</a></li>
        <li class="nav-item active"><a href="${pageContext.request.contextPath}/admin/reports">📈 Báo cáo Doanh thu</a></li>
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/tau">🚆 Quản lý Tàu</a></li>
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/ga">🚉 Quản lý Nhà Ga</a></li>
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/chuyen-tau">🗓️ Quản lý Lịch trình</a></li>
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/bookings">📦 Quản lý Đơn hàng</a></li>
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/refunds">💸 Duyệt Hoàn tiền</a></li>
        <li class="nav-item" style="margin-top: 40px;"><a href="${pageContext.request.contextPath}/admin/logout" style="color: #fca5a5;">🚪 Đăng xuất</a></li>
    </ul>
</aside>

<main class="main-content">
    <jsp:include page="/views/common/flash-message.jsp"/>
    <div class="page-header">
        <div class="page-title">
            <h1>Báo cáo Phân tích Chuyên sâu</h1>
            <p>Theo dõi doanh thu thật từ THANH_TOAN, hoàn tiền từ HOAN_TIEN và hiệu quả bán vé từ VE.</p>
        </div>
        <div style="display: flex; gap: 10px; flex-wrap: wrap;">
            <button class="btn-filter" onclick="window.print()">🖨️ Xuất PDF</button>

            <a class="btn-filter"
               href="${pageContext.request.contextPath}/admin/export?type=revenue"
               style="text-decoration: none; background: #16a34a;">
                📥 CSV Doanh thu
            </a>

            <a class="btn-filter"
               href="${pageContext.request.contextPath}/admin/export?type=bookings"
               style="text-decoration: none; background: #0f766e;">
                📦 CSV Đơn hàng
            </a>
        </div>
    </div>

    <c:if test="${not empty reportWarning}">
        <div class="alert-warning">
            ${reportWarning}
        </div>
    </c:if>

    <div class="info-bar">
        <span>Khoảng dữ liệu mặc định: 6 tháng gần nhất.</span>
        <a href="${pageContext.request.contextPath}/admin/reports" class="btn-filter">Làm mới dữ liệu</a>
    </div>

    <div class="kpi-grid">
        <div class="kpi-card">
            <div class="kpi-label">Tổng doanh thu thuần 6 tháng</div>
            <div class="kpi-value">
                <fmt:formatNumber value="${totalYearRevenue}" pattern="#,###"/> đ
            </div>
        </div>

        <div class="kpi-card green">
            <div class="kpi-label">Tổng số vé đã bán</div>
            <div class="kpi-value">
                <fmt:formatNumber value="${totalYearTickets}" pattern="#,###"/> vé
            </div>
        </div>

        <div class="kpi-card orange">
            <div class="kpi-label">Số tháng phân tích</div>
            <div class="kpi-value">
                <c:choose>
                    <c:when test="${not empty monthlyRevenue}">
                        ${monthlyRevenue.size()}
                    </c:when>
                    <c:otherwise>0</c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>

    <div class="charts-grid">
        <div class="chart-box">
            <h3 class="chart-title">📊 Xu hướng doanh thu thuần theo tháng</h3>
            <canvas id="revenueBarChart" height="130"></canvas>
        </div>

        <div class="chart-box">
            <h3 class="chart-title">💺 Cơ cấu hạng ghế bán ra</h3>
            <canvas id="seatClassChart"></canvas>
        </div>
    </div>

    <c:if test="${not empty monthlyRevenue}">
        <div class="content-card" style="margin-bottom:30px;">
            <h3 class="section-title">📅 Chi tiết doanh thu theo tháng</h3>

            <table class="admin-table">
                <thead>
                <tr>
                    <th>Tháng</th>
                    <th>Doanh thu gốc</th>
                    <th>Hoàn tiền</th>
                    <th>Doanh thu thuần</th>
                    <th>Số vé</th>
                </tr>
                </thead>

                <tbody>
                <c:forEach var="m" items="${monthlyRevenue}">
                    <tr>
                        <td><strong>${m.label}</strong></td>
                        <td class="money"><fmt:formatNumber value="${m.revenue}" pattern="#,###"/> đ</td>
                        <td style="color:#dc2626;font-weight:800;"><fmt:formatNumber value="${m.refund}" pattern="#,###"/> đ</td>
                        <td class="money"><fmt:formatNumber value="${m.netRevenue}" pattern="#,###"/> đ</td>
                        <td><strong>${m.ticketCount}</strong></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>

    <div class="table-grid">
        <div class="content-card">
            <h3 class="section-title">🚄 Top tuyến doanh thu cao</h3>

            <c:choose>
                <c:when test="${empty topRoutes}">
                    <div class="empty-note">Chưa có dữ liệu tuyến.</div>
                </c:when>

                <c:otherwise>
                    <table class="admin-table">
                        <thead>
                        <tr>
                            <th>Tuyến</th>
                            <th>Số vé</th>
                            <th>Doanh thu</th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach var="r" items="${topRoutes}">
                            <tr>
                                <td><strong>${r.label}</strong></td>
                                <td>${r.count}</td>
                                <td class="money"><fmt:formatNumber value="${r.value}" pattern="#,###"/> đ</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>

        <div class="content-card">
            <h3 class="section-title">💳 Cơ cấu phương thức thanh toán</h3>

            <c:choose>
                <c:when test="${empty paymentMethodStats}">
                    <div class="empty-note">Chưa có dữ liệu thanh toán.</div>
                </c:when>

                <c:otherwise>
                    <table class="admin-table">
                        <thead>
                        <tr>
                            <th>Phương thức</th>
                            <th>Số giao dịch</th>
                            <th>Doanh thu</th>
                        </tr>
                        </thead>

                        <tbody>
                        <c:forEach var="p" items="${paymentMethodStats}">
                            <tr>
                                <td><strong>${p.label}</strong></td>
                                <td>${p.count}</td>
                                <td class="money"><fmt:formatNumber value="${p.value}" pattern="#,###"/> đ</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</main>

<c:set var="safeChartMonths" value="${empty chartMonths ? '[]' : chartMonths}" />
<c:set var="safeChartRevenues" value="${empty chartRevenues ? '[]' : chartRevenues}" />
<c:set var="safeChartSeatClasses" value="${empty chartSeatClasses ? '[]' : chartSeatClasses}" />
<c:set var="safeChartTicketCounts" value="${empty chartTicketCounts ? '[]' : chartTicketCounts}" />

<script>
    const monthsData = ${safeChartMonths};
    const revenuesData = ${safeChartRevenues};
    const seatClassesData = ${safeChartSeatClasses};
    const ticketCountsData = ${safeChartTicketCounts};

    const ctxRev = document.getElementById('revenueBarChart').getContext('2d');
    new Chart(ctxRev, {
        type: 'bar',
        data: {
            labels: monthsData,
            datasets: [{
                label: 'Doanh thu thuần (VNĐ)',
                data: revenuesData,
                backgroundColor: 'rgba(59, 130, 246, 0.8)',
                borderColor: '#1d4ed8',
                borderWidth: 1,
                borderRadius: 6
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
                            return (value / 1000000) + ' Tr';
                        }
                    }
                }
            }
        }
    });

    const ctxSeat = document.getElementById('seatClassChart').getContext('2d');
    new Chart(ctxSeat, {
        type: 'doughnut',
        data: {
            labels: seatClassesData,
            datasets: [{
                data: ticketCountsData,
                backgroundColor: [
                    '#1d4ed8',
                    '#10b981',
                    '#f59e0b',
                    '#64748b',
                    '#8b5cf6',
                    '#ef4444'
                ],
                borderWidth: 2,
                borderColor: '#ffffff'
            }]
        },
        options: {
            responsive: true,
            cutout: '65%',
            plugins: {
                legend: { position: 'bottom' }
            }
        }
    });
</script>
</body>
</html>
