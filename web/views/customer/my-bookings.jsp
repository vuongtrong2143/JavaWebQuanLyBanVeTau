<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lịch sử đặt vé - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/status.css">
    <style>
        body {
            background: #f8fafc;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding-bottom: 60px;
        }

        /* Header Đồng Bộ */
        .top-header {
            background: white;
            padding: 12px 24px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            box-shadow: 0 2px 10px rgba(0,0,0,0.05);
            position: sticky;
            top: 0;
            z-index: 1000;
        }

        .main-nav {
            display: flex;
            gap: 18px;
            align-items: center;
            flex-wrap: wrap;
        }

        .main-nav a {
            text-decoration: none;
            color: #475569;
            font-weight: 600;
            font-size: 14px;
            transition: 0.2s;
        }

        .main-nav a:hover {
            color: #1d4ed8;
        }

        .nav-auth {
            border-left: 2px solid #e2e8f0;
            padding-left: 18px;
            display: flex;
            gap: 18px;
            align-items: center;
            flex-wrap: wrap;
        }

        .nav-auth a {
            color: #1d4ed8;
            font-weight: 600;
            text-decoration: none;
            font-size: 14px;
        }

        .nav-auth a.logout-btn {
            color: #ef4444;
        }

        .container-history {
            max-width: 980px;
            margin: 40px auto;
            padding: 0 20px;
        }

        .page-title {
            font-size: 24px;
            font-weight: 800;
            color: #0f172a;
            margin-bottom: 24px;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .notice-box {
            margin-bottom: 16px;
            padding: 14px 16px;
            border-radius: 12px;
            font-weight: 600;
        }

        .notice-info {
            background: #f8fafc;
            border: 1px solid #cbd5e1;
            color: #475569;
        }

        .notice-warning {
            background: #fffbeb;
            border: 1px solid #fde68a;
            color: #92400e;
        }

        .notice-danger {
            background: #fef2f2;
            border: 1px solid #fecaca;
            color: #991b1b;
        }

        /* Card Lịch Sử */
        .booking-card {
            background: white;
            border-radius: 16px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.05);
            margin-bottom: 20px;
            overflow: hidden;
            border: 1px solid #e2e8f0;
            transition: 0.2s;
        }

        .booking-card:hover {
            border-color: #93c5fd;
            box-shadow: 0 10px 25px rgba(0,0,0,0.08);
            transform: translateY(-2px);
        }

        .card-header {
            background: #f8fafc;
            padding: 16px 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 1px solid #e2e8f0;
            gap: 16px;
        }

        .booking-code {
            font-family: 'Courier New', monospace;
            font-size: 18px;
            font-weight: 800;
            color: #1d4ed8;
        }

        .booking-date {
            font-size: 13px;
            color: #64748b;
            margin-top: 4px;
        }

        .badges-group {
            display: flex;
            gap: 8px;
            flex-wrap: wrap;
            justify-content: flex-end;
        }

        .status-badge {
            display: inline-block;
            padding: 6px 12px;
            border-radius: 999px;
            font-size: 12px;
            font-weight: 800;
            white-space: nowrap;
            text-transform: uppercase;
        }

        .status-paid {
            background: #dcfce7;
            color: #166534;
            border: 1px solid #bbf7d0;
        }

        .status-pending {
            background: #fef3c7;
            color: #92400e;
            border: 1px solid #fde68a;
        }

        .status-expired {
            background: #e5e7eb;
            color: #374151;
            border: 1px solid #cbd5e1;
        }

        .status-cancelled {
            background: #fee2e2;
            color: #991b1b;
            border: 1px solid #fecaca;
        }

        .status-refund {
            background: #ffedd5;
            color: #9a3412;
            border: 1px solid #fed7aa;
        }

        .card-body {
            padding: 20px;
            display: grid;
            grid-template-columns: minmax(0, 2fr) minmax(240px, 1fr);
            gap: 20px;
            align-items: center;
        }

        .route-info {
            display: flex;
            flex-direction: column;
            gap: 8px;
        }

        .route-title {
            font-size: 16px;
            font-weight: 700;
            color: #1e293b;
        }

        .route-meta {
            font-size: 14px;
            color: #475569;
            display: flex;
            gap: 16px;
            flex-wrap: wrap;
        }

        .meta-item {
            display: flex;
            align-items: center;
            gap: 4px;
        }

        .price-section {
            text-align: right;
            border-left: 1px dashed #cbd5e1;
            padding-left: 20px;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: flex-end;
        }

        .total-price {
            font-size: 20px;
            font-weight: 900;
            color: #ea580c;
            margin-bottom: 12px;
        }

        .action-group {
            display: flex;
            flex-wrap: wrap;
            justify-content: flex-end;
            gap: 8px;
        }

        .btn-action {
            display: inline-block;
            text-align: center;
            padding: 9px 14px;
            border-radius: 8px;
            font-weight: 800;
            font-size: 13px;
            text-decoration: none;
            transition: 0.2s;
            cursor: pointer;
            border: 1px solid transparent;
        }

        .btn-pay {
            background: #1d4ed8;
            color: white;
            border-color: #1d4ed8;
            box-shadow: 0 4px 10px rgba(29, 78, 216, 0.22);
        }

        .btn-pay:hover {
            background: #1e40af;
            transform: translateY(-1px);
        }

        .btn-view {
            background: #ecfdf5;
            color: #166534;
            border-color: #bbf7d0;
        }

        .btn-view:hover {
            background: #dcfce7;
        }

        .btn-disabled {
            display: inline-block;
            padding: 9px 14px;
            border-radius: 8px;
            font-size: 13px;
            font-weight: 800;
            background: #f1f5f9;
            color: #64748b;
            border: 1px solid #cbd5e1;
        }

        .empty-card {
            background: white;
            border-radius: 16px;
            padding: 60px 20px;
            text-align: center;
            box-shadow: 0 4px 6px rgba(0,0,0,0.05);
            border: 1px solid #e2e8f0;
        }

        @media (max-width: 768px) {
            .top-header {
                align-items: flex-start;
                flex-direction: column;
                gap: 12px;
            }

            .nav-auth {
                border-left: none;
                padding-left: 0;
            }

            .card-body {
                grid-template-columns: 1fr;
            }

            .price-section {
                text-align: left;
                border-left: none;
                border-top: 1px dashed #cbd5e1;
                padding-left: 0;
                padding-top: 16px;
                align-items: flex-start;
            }

            .badges-group,
            .action-group {
                justify-content: flex-start;
            }

            .card-header {
                flex-direction: column;
                align-items: flex-start;
            }
        }
    </style>
</head>
<body>

<header class="top-header">
    <div style="font-size: 22px; font-weight: 800; color: #1d4ed8; letter-spacing: -0.5px;">
        <a href="${pageContext.request.contextPath}/home" style="text-decoration: none; color: inherit;">🚂 VETAU VN</a>
    </div>
    <nav class="main-nav">
        <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
        <a href="${pageContext.request.contextPath}/search-train">Tìm chuyến</a>
        <a href="${pageContext.request.contextPath}/ticket-check">Tra cứu vé</a>
        <div class="nav-auth">
            <c:choose>
                <c:when test="${not empty sessionScope.currentCustomer}">
                    <a href="${pageContext.request.contextPath}/profile">Tài khoản</a>
                    <a href="${pageContext.request.contextPath}/my-bookings" style="color: #1d4ed8;">Lịch sử đặt vé</a>
                    <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                </c:otherwise>
            </c:choose>
        </div>
    </nav>
</header>

<main class="container-history">
    <jsp:include page="/views/common/flash-message.jsp"/>
    <div class="page-title">
        🎫 Lịch sử đặt vé của bạn
    </div>

    <c:if test="${param.error eq 'checkout-expired'}">
        <div class="notice-box notice-warning">
            ⏳ Đơn đặt chỗ đã hết hạn thanh toán. Ghế đã được giải phóng, vui lòng đặt vé lại nếu bạn vẫn có nhu cầu.
        </div>
    </c:if>

    <c:if test="${param.error eq 'checkout'}">
        <div class="notice-box notice-warning">
            Không thể mở trang thanh toán cho đơn này. Vui lòng kiểm tra lại trạng thái đơn đặt chỗ.
        </div>
    </c:if>

    <c:if test="${not empty error}">
        <div class="notice-box notice-danger">
            ⚠️ ${error}
        </div>
    </c:if>

    <c:choose>
        <c:when test="${empty bookings}">
            <div class="empty-card">
                <img src="https://cdn-icons-png.flaticon.com/512/6134/6134065.png" alt="No bookings" style="width: 100px; opacity: 0.5; margin-bottom: 20px;">
                <h2 style="color: #0f172a; margin-top: 0;">Bạn chưa có chuyến đi nào</h2>
                <p style="color: #64748b;">Hãy bắt đầu lên kế hoạch cho hành trình khám phá Việt Nam của bạn ngay hôm nay!</p>
                <a href="${pageContext.request.contextPath}/search-train" class="btn-action btn-pay" style="margin-top: 16px; display: inline-block;">
                    🔍 Tìm chuyến tàu ngay
                </a>
            </div>
        </c:when>

        <c:otherwise>
            <c:forEach var="item" items="${bookings}">
                <div class="booking-card">
                    <div class="card-header">
                        <div>
                            <div class="booking-code">#${item.maDatCho}</div>
                            <div class="booking-date">Ngày đặt: ${item.ngayDatText}</div>
                        </div>

                        <div class="badges-group">
                            <c:choose>
                                <c:when test="${item.trangThaiDatCho eq 'Đã thanh toán'}">
                                    <span class="status-badge status-paid">Đã thanh toán</span>
                                </c:when>
                                <c:when test="${item.trangThaiDatCho eq 'Chờ thanh toán'}">
                                    <span class="status-badge status-pending">Chờ thanh toán</span>
                                </c:when>
                                <c:when test="${item.trangThaiDatCho eq 'Hết hạn'}">
                                    <span class="status-badge status-expired">Hết hạn</span>
                                </c:when>
                                <c:when test="${item.trangThaiDatCho eq 'Đã hủy'}">
                                    <span class="status-badge status-cancelled">Đã hủy</span>
                                </c:when>
                                <c:when test="${item.trangThaiDatCho eq 'Hoàn tiền một phần' || item.trangThaiDatCho eq 'Hoàn tiền toàn bộ'}">
                                    <span class="status-badge status-refund">${item.trangThaiDatCho}</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-badge status-expired">${item.trangThaiDatCho}</span>
                                </c:otherwise>
                            </c:choose>

                            <c:choose>
                                <c:when test="${item.trangThaiThanhToan eq 'Thành công'}">
                                    <span class="status-badge status-paid">Thanh toán thành công</span>
                                </c:when>
                                <c:when test="${item.trangThaiThanhToan eq 'Pending'}">
                                    <span class="status-badge status-pending">Thanh toán pending</span>
                                </c:when>
                                <c:when test="${item.trangThaiThanhToan eq 'Chưa thanh toán'}">
                                    <span class="status-badge status-pending">Chưa thanh toán</span>
                                </c:when>
                                <c:when test="${item.trangThaiThanhToan eq 'Thất bại'}">
                                    <span class="status-badge status-cancelled">Thanh toán thất bại</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-badge status-expired">${item.trangThaiThanhToan}</span>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <div class="card-body">
                        <div class="route-info">
                            <div class="route-title">📍 Hành trình: ${item.loaiHanhTrinh}</div>
                            <div class="route-meta">
                                <span class="meta-item">🚆 Loại đơn: <b>${item.loaiDonHang}</b></span>
                                <span class="meta-item">🎟️ Số lượng: <b>${item.soVe} vé</b></span>
                            </div>
                        </div>

                        <div class="price-section">
                            <div class="total-price">${item.tongThanhToanText}</div>

                            <div class="action-group">
                                <c:choose>
                                    <c:when test="${item.trangThaiDatCho eq 'Chờ thanh toán'}">
                                        <a class="btn-action btn-pay"
                                           href="${pageContext.request.contextPath}/checkout?datChoId=${item.id}">
                                            Thanh toán ngay ➔
                                        </a>
                                    </c:when>

                                    <c:when test="${item.trangThaiDatCho eq 'Đã thanh toán'}">
                                        <a href="${pageContext.request.contextPath}/my-tickets?datChoId=${item.id}" class="btn-action btn-view">
                                            🎫 Xem chi tiết vé
                                        </a>
                                    </c:when>

                                    <c:when test="${item.trangThaiDatCho eq 'Hết hạn'}">
                                        <span class="btn-disabled">Đơn đã hết hạn</span>
                                    </c:when>

                                    <c:when test="${item.trangThaiDatCho eq 'Đã hủy'}">
                                        <span class="btn-disabled">Đơn đã hủy</span>
                                    </c:when>

                                    <c:when test="${item.trangThaiDatCho eq 'Hoàn tiền một phần' || item.trangThaiDatCho eq 'Hoàn tiền toàn bộ'}">
                                        <a href="${pageContext.request.contextPath}/my-tickets?datChoId=${item.id}" class="btn-action btn-view">
                                            🎫 Xem vé / hoàn tiền
                                        </a>
                                    </c:when>

                                    <c:otherwise>
                                        <span class="btn-disabled">Không khả dụng</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </c:otherwise>
    </c:choose>
</main>

</body>
</html>
