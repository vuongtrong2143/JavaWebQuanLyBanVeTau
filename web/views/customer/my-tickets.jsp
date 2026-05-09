<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vé điện tử của tôi - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/status.css">
    <style>
        body {
            background: #f1f5f9;
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

        .container-tickets {
            max-width: 900px;
            margin: 40px auto;
            padding: 0 20px;
        }

        /* Tiêu đề & Thông báo */
        .header-section {
            display: flex;
            justify-content: space-between;
            align-items: flex-end;
            margin-bottom: 24px;
            flex-wrap: wrap;
            gap: 16px;
        }

        .page-title {
            font-size: 24px;
            font-weight: 800;
            color: #0f172a;
            margin: 0 0 8px 0;
        }

        .booking-info {
            color: #475569;
            font-size: 15px;
        }

        .btn-back {
            background: white;
            color: #475569;
            border: 1px solid #cbd5e1;
            padding: 10px 20px;
            border-radius: 8px;
            font-weight: bold;
            text-decoration: none;
            transition: 0.2s;
            display: inline-flex;
            align-items: center;
            gap: 8px;
        }

        .btn-back:hover {
            background: #f8fafc;
            border-color: #94a3b8;
        }

        .alert-success {
            background: #dcfce7;
            color: #166534;
            border: 1px solid #bbf7d0;
            padding: 16px;
            border-radius: 12px;
            margin-bottom: 24px;
            font-weight: 500;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .alert-warning {
            background: #fffbeb;
            color: #92400e;
            border: 1px solid #fde68a;
            padding: 16px;
            border-radius: 12px;
            margin-bottom: 24px;
            font-weight: 500;
        }

        /* Vé điện tử */
        .ticket {
            background: white;
            border-radius: 20px;
            overflow: hidden;
            display: flex;
            flex-direction: column;
            box-shadow: 0 10px 25px rgba(0,0,0,0.08);
            margin-bottom: 24px;
            position: relative;
            border: 1px solid #e2e8f0;
        }

        .ticket-header {
            color: white;
            padding: 16px 24px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .header-valid {
            background: #1d4ed8;
        }

        .header-pending {
            background: #d97706;
        }

        .header-refunded {
            background: #64748b;
        }

        .header-cancelled {
            background: #991b1b;
        }

        .header-used {
            background: #334155;
        }

        .ticket-title {
            font-size: 18px;
            font-weight: 800;
            margin: 0;
        }

        .ticket-badge {
            background: rgba(255,255,255,0.2);
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: bold;
            text-transform: uppercase;
        }

        .ticket-body {
            padding: 24px;
            display: grid;
            grid-template-columns: 2.5fr 1fr;
            gap: 20px;
        }

        .t-label {
            font-size: 12px;
            color: #94a3b8;
            text-transform: uppercase;
            font-weight: bold;
            margin-bottom: 4px;
        }

        .t-value {
            font-size: 16px;
            font-weight: 700;
            color: #1e293b;
        }

        .route-info {
            display: flex;
            align-items: center;
            gap: 16px;
            margin-bottom: 20px;
            background: #f8fafc;
            padding: 12px 16px;
            border-radius: 12px;
            border: 1px solid #f1f5f9;
        }

        .station-name {
            font-size: 18px;
            font-weight: 800;
            color: #0f172a;
        }

        .route-arrow {
            color: #94a3b8;
            font-size: 18px;
        }

        /* QR Code & Actions */
        .ticket-qr-section {
            border-left: 2px dashed #e2e8f0;
            padding-left: 24px;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            text-align: center;
        }

        .qr-code-img {
            border: 1px solid #e2e8f0;
            border-radius: 8px;
            padding: 4px;
            margin-bottom: 12px;
        }

        .qr-placeholder {
            width: 120px;
            height: 120px;
            border: 1px dashed #cbd5e1;
            border-radius: 12px;
            background: #f8fafc;
            color: #64748b;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 12px;
            font-size: 13px;
            font-weight: 700;
            text-align: center;
            margin-bottom: 12px;
            box-sizing: border-box;
        }

        .btn-refund {
            background: white;
            color: #dc2626;
            border: 1px solid #fca5a5;
            padding: 8px 16px;
            border-radius: 8px;
            font-weight: bold;
            font-size: 13px;
            text-decoration: none;
            transition: 0.2s;
            margin-top: 10px;
            width: 100%;
            box-sizing: border-box;
        }

        .btn-refund:hover {
            background: #fef2f2;
            border-color: #ef4444;
        }

        .btn-pay {
            background: #1d4ed8;
            color: white;
            border: 1px solid #1d4ed8;
            padding: 8px 16px;
            border-radius: 8px;
            font-weight: bold;
            font-size: 13px;
            text-decoration: none;
            transition: 0.2s;
            margin-top: 10px;
            width: 100%;
            box-sizing: border-box;
        }

        .btn-pay:hover {
            background: #1e40af;
        }

        .state-box {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 8px;
            color: #475569;
        }

        .state-icon {
            font-size: 40px;
        }

        .ticket-note {
            margin: 0 24px 24px;
            padding: 12px 14px;
            border-radius: 12px;
            font-size: 14px;
            font-weight: 600;
        }

        .note-success {
            background: #ecfdf5;
            color: #166534;
            border: 1px solid #bbf7d0;
        }

        .note-warning {
            background: #fffbeb;
            color: #92400e;
            border: 1px solid #fde68a;
        }

        .note-danger {
            background: #fef2f2;
            color: #991b1b;
            border: 1px solid #fecaca;
        }

        .note-muted {
            background: #f8fafc;
            color: #475569;
            border: 1px solid #e2e8f0;
        }

        /* Nút in ở cuối trang */
        .print-section {
            text-align: center;
            margin-top: 40px;
            padding-top: 24px;
            border-top: 1px dashed #cbd5e1;
        }

        .btn-print {
            background: #1d4ed8;
            color: white;
            border: none;
            padding: 14px 32px;
            border-radius: 12px;
            font-weight: 800;
            font-size: 16px;
            cursor: pointer;
            transition: 0.2s;
            box-shadow: 0 4px 12px rgba(29, 78, 216, 0.3);
        }

        .btn-print:hover {
            background: #1e40af;
            transform: translateY(-2px);
        }

        /* Ẩn các nút không cần thiết khi in */
        @media print {
            .top-header,
            .header-section,
            .btn-refund,
            .btn-pay,
            .print-section,
            .ticket-note {
                display: none !important;
            }

            body {
                background: white;
                padding: 0;
            }

            .container-tickets {
                margin: 0;
                max-width: 100%;
            }

            .ticket {
                box-shadow: none;
                border: 2px solid #000;
                page-break-inside: avoid;
            }
        }

        @media (max-width: 768px) {
            .top-header {
                align-items: flex-start;
                gap: 12px;
                flex-direction: column;
            }

            .main-nav {
                flex-wrap: wrap;
            }

            .ticket-body {
                grid-template-columns: 1fr;
            }

            .ticket-qr-section {
                border-left: none;
                border-top: 2px dashed #e2e8f0;
                padding-left: 0;
                padding-top: 24px;
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
        <a href="${pageContext.request.contextPath}/profile">Tài khoản</a>
        <a href="${pageContext.request.contextPath}/my-bookings" style="color: #1d4ed8;">Lịch sử đặt vé</a>
    </nav>
</header>

<main class="container-tickets">
    <jsp:include page="/views/common/flash-message.jsp"/>
    <div class="header-section">
        <div>
            <h1 class="page-title">🎫 Danh sách vé điện tử</h1>
            <div class="booking-info">
                Mã đơn hàng:
                <b style="color: #0f172a;">${datCho.maDatCho}</b>
                |
                Trạng thái đơn:
                <b>
                    <c:choose>
                        <c:when test="${datCho.trangThai eq 'Đã thanh toán'}">
                            <span style="color: #16a34a;">${datCho.trangThai}</span>
                        </c:when>
                        <c:when test="${datCho.trangThai eq 'Chờ thanh toán'}">
                            <span style="color: #d97706;">${datCho.trangThai}</span>
                        </c:when>
                        <c:otherwise>
                            <span style="color: #475569;">${datCho.trangThai}</span>
                        </c:otherwise>
                    </c:choose>
                </b>
            </div>
        </div>

        <a href="${pageContext.request.contextPath}/my-bookings" class="btn-back">← Quay lại lịch sử</a>
    </div>

    <c:if test="${param.refundSuccess == 'true'}">
        <div class="alert-success">
            <span style="font-size: 20px;">✅</span>
            Yêu cầu trả vé thành công. Hệ thống đã ghi nhận yêu cầu hoàn tiền và đang chờ xử lý.
        </div>
    </c:if>

    <c:if test="${datCho.trangThai eq 'Chờ thanh toán'}">
        <div class="alert-warning">
            ⏳ Đơn hàng này đang chờ thanh toán. Các vé bên dưới chỉ có hiệu lực sau khi thanh toán thành công.
        </div>
    </c:if>

    <c:choose>
        <c:when test="${empty ticketDetails}">
            <div style="background: white; border-radius: 16px; padding: 60px 20px; text-align: center; border: 1px solid #e2e8f0;">
                <h3 style="color: #475569; margin: 0;">Đơn hàng này chưa có vé nào được xuất.</h3>

                <c:if test="${datCho.trangThai eq 'Chờ thanh toán'}">
                    <div style="margin-top: 20px;">
                        <a href="${pageContext.request.contextPath}/checkout?datChoId=${datCho.id}" class="btn-pay" style="display: inline-block; width: auto;">
                            Tiếp tục thanh toán
                        </a>
                    </div>
                </c:if>
            </div>
        </c:when>

        <c:otherwise>
            <c:forEach var="ticket" items="${ticketDetails}">

                <c:set var="headerClass" value="header-valid" />
                <c:if test="${ticket.trangThai eq 'Chờ thanh toán'}">
                    <c:set var="headerClass" value="header-pending" />
                </c:if>
                <c:if test="${ticket.trangThai eq 'Đã trả'}">
                    <c:set var="headerClass" value="header-refunded" />
                </c:if>
                <c:if test="${ticket.trangThai eq 'Đã hủy'}">
                    <c:set var="headerClass" value="header-cancelled" />
                </c:if>
                <c:if test="${ticket.trangThai eq 'Đã sử dụng'}">
                    <c:set var="headerClass" value="header-used" />
                </c:if>

                <div class="ticket">
                    <div class="ticket-header ${headerClass}">
                        <h2 class="ticket-title">Tàu ${ticket.tenTau} (${ticket.maTau})</h2>
                        <span class="ticket-badge">${ticket.trangThai}</span>
                    </div>

                    <div class="ticket-body">
                        <div class="ticket-info">
                            <div class="route-info">
                                <div class="station-name">${ticket.tenGaDi}</div>
                                <div class="route-arrow">➔</div>
                                <div class="station-name">${ticket.tenGaDen}</div>
                            </div>

                            <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 16px;">
                                <div>
                                    <div class="t-label">Mã vé</div>
                                    <div class="t-value" style="font-family: 'Courier New', monospace;">${ticket.maVe}</div>
                                </div>

                                <div>
                                    <div class="t-label">Hành khách</div>
                                    <div class="t-value" style="text-transform: uppercase;">${ticket.tenHanhKhach}</div>
                                </div>

                                <div>
                                    <div class="t-label">Giấy tờ (${ticket.loaiGiayTo})</div>
                                    <div class="t-value">${ticket.soGiayTo}</div>
                                </div>

                                <div>
                                    <div class="t-label">Giá vé</div>
                                    <div class="t-value" style="color: #ea580c;">${ticket.giaVeText}</div>
                                </div>

                                <div>
                                    <div class="t-label">Khởi hành</div>
                                    <div class="t-value" style="color: #ea580c;">${ticket.thoiGianDiText}</div>
                                </div>

                                <div>
                                    <div class="t-label">Vị trí chỗ ngồi</div>
                                    <div class="t-value" style="color: #1d4ed8;">Toa ${ticket.soToa} - Ghế ${ticket.soGhe}</div>
                                </div>
                            </div>
                        </div>

                        <div class="ticket-qr-section">
                            <c:choose>
                                <c:when test="${ticket.trangThai eq 'Hợp lệ'}">
                                    <div class="t-label">Mã vé điện tử</div>
                                    <div style="font-family: 'Courier New', monospace; font-size: 20px; font-weight: bold; color: #0f172a; margin-bottom: 8px;">
                                        ${ticket.maVe}
                                    </div>

                                    <img src="https://chart.googleapis.com/chart?chs=120x120&cht=qr&chl=${ticket.maVe}&choe=UTF-8"
                                         alt="QR Code"
                                         class="qr-code-img">

                                    <a href="${pageContext.request.contextPath}/return-ticket?maVe=${ticket.maVe}" class="btn-refund">
                                        ↪ Yêu cầu trả vé
                                    </a>
                                </c:when>

                                <c:when test="${ticket.trangThai eq 'Chờ thanh toán'}">
                                    <div class="qr-placeholder">
                                        QR chỉ hiển thị khi vé hợp lệ
                                    </div>

                                    <div class="state-box">
                                        <div class="state-icon">⏳</div>
                                        <div style="font-weight: 800; font-size: 17px; color: #92400e;">Chờ thanh toán</div>
                                        <div style="font-size: 13px; color: #64748b;">
                                            Vé chưa có hiệu lực
                                        </div>
                                    </div>

                                    <c:if test="${datCho.trangThai eq 'Chờ thanh toán'}">
                                        <a href="${pageContext.request.contextPath}/checkout?datChoId=${datCho.id}" class="btn-pay">
                                            Tiếp tục thanh toán
                                        </a>
                                    </c:if>
                                </c:when>

                                <c:when test="${ticket.trangThai eq 'Đã trả'}">
                                    <div class="state-box">
                                        <div class="state-icon">↩️</div>
                                        <div style="font-weight: 800; font-size: 17px; color: #ea580c;">Vé đã được trả</div>
                                        <div style="font-size: 13px; color: #64748b;">
                                            Hệ thống đang xử lý hoàn tiền nếu đủ điều kiện.
                                        </div>
                                    </div>
                                </c:when>

                                <c:when test="${ticket.trangThai eq 'Đã hủy'}">
                                    <div class="state-box">
                                        <div class="state-icon">❌</div>
                                        <div style="font-weight: 800; font-size: 17px; color: #991b1b;">Vé đã hủy</div>
                                        <div style="font-size: 13px; color: #64748b;">
                                            Vé không còn hiệu lực sử dụng.
                                        </div>
                                    </div>
                                </c:when>

                                <c:when test="${ticket.trangThai eq 'Đã sử dụng'}">
                                    <div class="state-box">
                                        <div class="state-icon">✓</div>
                                        <div style="font-weight: 800; font-size: 17px; color: #334155;">Đã sử dụng</div>
                                        <div style="font-size: 13px; color: #64748b;">
                                            Vé đã được sử dụng.
                                        </div>
                                    </div>
                                </c:when>

                                <c:otherwise>
                                    <div class="state-box">
                                        <div class="state-icon">ℹ️</div>
                                        <div style="font-weight: 800; font-size: 17px; color: #475569;">${ticket.trangThai}</div>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>

                    <c:choose>
                        <c:when test="${ticket.trangThai eq 'Hợp lệ'}">
                            <div class="ticket-note note-success">
                                ✅ Vé đã thanh toán thành công và có hiệu lực sử dụng.
                            </div>
                        </c:when>

                        <c:when test="${ticket.trangThai eq 'Chờ thanh toán'}">
                            <div class="ticket-note note-warning">
                                ⏳ Vé này đang chờ thanh toán. Vé chỉ có hiệu lực sau khi thanh toán thành công.
                            </div>
                        </c:when>

                        <c:when test="${ticket.trangThai eq 'Đã trả'}">
                            <div class="ticket-note note-muted">
                                ↩ Vé này đã được trả. Vui lòng theo dõi trạng thái hoàn tiền trong lịch sử đặt vé hoặc mục hoàn tiền.
                            </div>
                        </c:when>

                        <c:when test="${ticket.trangThai eq 'Đã hủy'}">
                            <div class="ticket-note note-danger">
                                ❌ Vé này đã bị hủy và không còn hiệu lực.
                            </div>
                        </c:when>

                        <c:when test="${ticket.trangThai eq 'Đã sử dụng'}">
                            <div class="ticket-note note-muted">
                                ✓ Vé này đã được sử dụng.
                            </div>
                        </c:when>

                        <c:otherwise>
                            <div class="ticket-note note-muted">
                                Trạng thái hiện tại của vé: ${ticket.trangThai}
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>

            </c:forEach>
        </c:otherwise>
    </c:choose>

    <c:if test="${not empty ticketDetails}">
        <div class="print-section">
            <button class="btn-print" onclick="window.print()">🖨️ In tất cả vé hợp lệ / Lưu PDF</button>
            <p style="margin-top: 12px; font-size: 13px; color: #64748b;">
                Khi in, hệ thống sẽ ẩn các nút thao tác. Vé chờ thanh toán không được xem là vé hợp lệ để sử dụng.
            </p>
        </div>
    </c:if>

</main>

</body>
</html>