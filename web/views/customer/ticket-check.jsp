<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tra cứu vé - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/status.css">
    <style>
        body {
            background: #f1f5f9;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            padding-bottom: 50px;
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

        .container-check {
            max-width: 800px;
            margin: 40px auto;
            padding: 0 20px;
        }

        /* Form Card */
        .search-card {
            background: white;
            border-radius: 16px;
            padding: 30px;
            box-shadow: 0 10px 25px rgba(0,0,0,0.05);
            margin-bottom: 30px;
            border-top: 5px solid #1d4ed8;
        }

        .form-title {
            font-size: 22px;
            font-weight: 800;
            color: #0f172a;
            margin-bottom: 20px;
            text-align: center;
        }

        .form-grid {
            display: grid;
            grid-template-columns: 1fr 1fr auto;
            gap: 15px;
            align-items: flex-end;
        }

        .form-group label {
            display: block;
            font-size: 13px;
            font-weight: 700;
            color: #64748b;
            margin-bottom: 6px;
        }

        .form-control {
            width: 100%;
            padding: 12px;
            border: 1px solid #cbd5e1;
            border-radius: 8px;
            font-size: 15px;
            box-sizing: border-box;
            outline: none;
            transition: 0.2s;
        }

        .form-control:focus {
            border-color: #3b82f6;
            box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1);
        }

        .btn-check {
            background: #1d4ed8;
            color: white;
            border: none;
            padding: 12px 24px;
            border-radius: 8px;
            font-weight: bold;
            cursor: pointer;
            transition: 0.2s;
            height: 45px;
        }

        .btn-check:hover {
            background: #1e40af;
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(29, 78, 216, 0.3);
        }

        /* Kết quả vé */
        .ticket-result {
            margin-top: 40px;
            animation: fadeInUp 0.5s ease;
        }

        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(20px);
            }

            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .ticket {
            background: white;
            border-radius: 20px;
            overflow: hidden;
            display: flex;
            flex-direction: column;
            box-shadow: 0 15px 35px rgba(0,0,0,0.1);
            position: relative;
        }

        .ticket-header {
            background: #1e293b;
            color: white;
            padding: 20px 30px;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .ticket-body {
            padding: 30px;
            display: grid;
            grid-template-columns: 2.5fr 1fr;
            gap: 20px;
        }

        .t-label {
            font-size: 12px;
            color: #94a3b8;
            text-transform: uppercase;
            font-weight: bold;
        }

        .t-value {
            font-size: 17px;
            font-weight: 700;
            color: #1e293b;
            margin-top: 4px;
        }

        .ticket-qr {
            border-left: 2px dashed #e2e8f0;
            padding-left: 20px;
            text-align: center;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            min-height: 180px;
        }

        .status-badge {
            display: inline-block;
            padding: 6px 16px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: bold;
            margin-top: 15px;
            text-transform: uppercase;
        }

        .status-success {
            background: #dcfce7;
            color: #166534;
            border: 1px solid #bbf7d0;
        }

        .status-warning {
            background: #fef3c7;
            color: #92400e;
            border: 1px solid #fde68a;
        }

        .status-danger {
            background: #fee2e2;
            color: #b91c1c;
            border: 1px solid #fecaca;
        }

        .status-muted {
            background: #e5e7eb;
            color: #374151;
            border: 1px solid #d1d5db;
        }

        .ticket-note {
            margin: 24px 30px 30px;
            padding: 14px 16px;
            border-radius: 12px;
            font-size: 15px;
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

        .qr-placeholder {
            width: 130px;
            height: 130px;
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

            .nav-auth {
                border-left: none;
                padding-left: 0;
                flex-wrap: wrap;
            }

            .form-grid {
                grid-template-columns: 1fr;
            }

            .ticket-body {
                grid-template-columns: 1fr;
            }

            .ticket-qr {
                border-left: none;
                border-top: 2px dashed #e2e8f0;
                padding-left: 0;
                padding-top: 20px;
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
        <a href="${pageContext.request.contextPath}/ticket-check" style="color: #1d4ed8;">Tra cứu vé</a>
        <a href="${pageContext.request.contextPath}/promotion">Khuyến mãi</a>
        <a href="${pageContext.request.contextPath}/guide">Hướng dẫn</a>

        <div class="nav-auth">
            <c:choose>
                <c:when test="${not empty sessionScope.currentCustomer}">
                    <a href="${pageContext.request.contextPath}/profile">Tài khoản</a>
                    <a href="${pageContext.request.contextPath}/my-bookings">Lịch sử đặt vé</a>
                    <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                    <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
                </c:otherwise>
            </c:choose>
        </div>
    </nav>
</header>

<main class="container-check">
    <div class="search-card">
        <h1 class="form-title">Tra cứu thông tin vé</h1>

        <c:if test="${not empty message}">
            <div style="margin-bottom: 20px; padding: 12px; background: #fffbeb; color: #b45309; border-radius: 8px; border-left: 4px solid #f59e0b; font-weight: 500;">
                ⚠️ ${message}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/ticket-check" method="post" class="form-grid">
            <div class="form-group">
                <label for="maVe">Mã vé</label>
                <input id="maVe"
                       type="text"
                       name="maVe"
                       class="form-control"
                       value="${maVeValue}"
                       placeholder="Ví dụ: VE123ABC..."
                       required>
            </div>

            <div class="form-group">
                <label for="soGiayTo">Số giấy tờ hành khách</label>
                <input id="soGiayTo"
                       type="text"
                       name="soGiayTo"
                       class="form-control"
                       value="${soGiayToValue}"
                       placeholder="Có thể bỏ trống khi demo">
            </div>

            <button type="submit" class="btn-check">🔍 Tra cứu ngay</button>
        </form>
    </div>

    <c:if test="${not empty ticketResult}">
        <div class="ticket-result">
            <c:choose>
                <c:when test="${ticketResult.found}">
                    <div class="ticket">
                        <div class="ticket-header">
                            <div style="font-weight: 800;">THÔNG TIN VÉ ĐIỆN TỬ</div>
                            <div style="font-family: 'Courier New', monospace; font-size: 18px; font-weight: bold; background: rgba(255,255,255,0.2); padding: 4px 12px; border-radius: 6px;">
                                #${ticketResult.maVe}
                            </div>
                        </div>

                        <div class="ticket-body">
                            <div class="ticket-info">
                                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-bottom: 20px;">
                                    <div>
                                        <div class="t-label">Hành khách</div>
                                        <div class="t-value" style="text-transform: uppercase;">${ticketResult.tenHanhKhach}</div>
                                    </div>

                                    <div>
                                        <div class="t-label">Giấy tờ (${ticketResult.loaiGiayTo})</div>
                                        <div class="t-value">${ticketResult.soGiayTo}</div>
                                    </div>
                                </div>

                                <div style="margin-bottom: 20px;">
                                    <div class="t-label">Chuyến tàu / Hành trình</div>
                                    <div class="t-value">Tàu ${ticketResult.tenTau} (${ticketResult.maChuyen})</div>
                                    <div style="font-size: 14px; color: #475569; margin-top: 4px; font-weight: 500;">
                                        📍 ${ticketResult.tenGaDi} ➔ ${ticketResult.tenGaDen}
                                    </div>
                                </div>

                                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 20px;">
                                    <div>
                                        <div class="t-label">Vị trí chỗ ngồi</div>
                                        <div class="t-value" style="color: #1d4ed8;">
                                            Toa ${ticketResult.soToa} - Ghế ${ticketResult.soGhe}
                                        </div>
                                    </div>

                                    <div>
                                        <div class="t-label">Giờ khởi hành</div>
                                        <div class="t-value">${ticketResult.thoiGianDiText}</div>
                                    </div>
                                </div>
                            </div>

                            <div class="ticket-qr">
                                <c:choose>
                                    <c:when test="${ticketResult.trangThai eq 'Hợp lệ'}">
                                        <img src="https://chart.googleapis.com/chart?chs=130x130&cht=qr&chl=${ticketResult.maVe}&choe=UTF-8"
                                             alt="QR Code"
                                             style="border: 1px solid #e2e8f0; border-radius: 8px; padding: 4px;">
                                    </c:when>

                                    <c:otherwise>
                                        <div class="qr-placeholder">
                                            QR chỉ hiển thị khi vé hợp lệ
                                        </div>
                                    </c:otherwise>
                                </c:choose>

                                <c:choose>
                                    <c:when test="${ticketResult.trangThai eq 'Hợp lệ'}">
                                        <div class="status-badge status-success">✅ Hợp lệ</div>
                                    </c:when>

                                    <c:when test="${ticketResult.trangThai eq 'Chờ thanh toán'}">
                                        <div class="status-badge status-warning">⏳ Chờ thanh toán</div>
                                    </c:when>

                                    <c:when test="${ticketResult.trangThai eq 'Đã trả'}">
                                        <div class="status-badge status-muted">↩ Đã trả</div>
                                    </c:when>

                                    <c:when test="${ticketResult.trangThai eq 'Đã hủy'}">
                                        <div class="status-badge status-danger">❌ Đã hủy</div>
                                    </c:when>

                                    <c:when test="${ticketResult.trangThai eq 'Đã sử dụng'}">
                                        <div class="status-badge status-muted">✓ Đã sử dụng</div>
                                    </c:when>

                                    <c:otherwise>
                                        <div class="status-badge status-muted">${ticketResult.trangThai}</div>
                                    </c:otherwise>
                                </c:choose>

                                <div style="font-weight: 800; font-size: 18px; color: #ea580c; margin-top: 15px;">
                                    ${ticketResult.giaVeText}
                                </div>
                            </div>
                        </div>

                        <c:choose>
                            <c:when test="${ticketResult.trangThai eq 'Hợp lệ'}">
                                <div class="ticket-note note-success">
                                    ✅ Vé hợp lệ. Hành khách có thể sử dụng vé này để đi tàu theo đúng hành trình đã đặt.
                                </div>
                            </c:when>

                            <c:when test="${ticketResult.trangThai eq 'Chờ thanh toán'}">
                                <div class="ticket-note note-warning">
                                    ⏳ Vé đã được tạo nhưng chưa có hiệu lực vì đơn đặt chỗ chưa thanh toán thành công.
                                    Vui lòng thanh toán trong thời hạn giữ chỗ để kích hoạt vé.
                                </div>
                            </c:when>

                            <c:when test="${ticketResult.trangThai eq 'Đã trả'}">
                                <div class="ticket-note note-muted">
                                    ↩ Vé này đã được trả. Nếu có hoàn tiền, vui lòng theo dõi trạng thái hoàn tiền trong lịch sử đặt vé.
                                </div>
                            </c:when>

                            <c:when test="${ticketResult.trangThai eq 'Đã hủy'}">
                                <div class="ticket-note note-danger">
                                    ❌ Vé này đã bị hủy và không còn hiệu lực sử dụng.
                                </div>
                            </c:when>

                            <c:when test="${ticketResult.trangThai eq 'Đã sử dụng'}">
                                <div class="ticket-note note-muted">
                                    ✓ Vé này đã được sử dụng.
                                </div>
                            </c:when>

                            <c:otherwise>
                                <div class="ticket-note note-muted">
                                    Trạng thái hiện tại của vé: ${ticketResult.trangThai}
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <c:if test="${ticketResult.trangThai eq 'Hợp lệ'}">
                        <div style="text-align: center; margin-top: 24px;">
                            <button class="btn-check"
                                    style="background: white; color: #1d4ed8; border: 2px solid #1d4ed8; padding: 12px 30px; font-size: 16px;"
                                    onclick="window.print()">
                                🖨️ In vé điện tử / Lưu PDF
                            </button>
                        </div>
                    </c:if>
                </c:when>

                <c:otherwise>
                    <div style="background: white; border-radius: 16px; padding: 40px 20px; text-align: center; box-shadow: 0 10px 25px rgba(0,0,0,0.05); border: 1px solid #e2e8f0;">
                        <img src="https://cdn-icons-png.flaticon.com/512/6134/6134065.png"
                             alt="Not Found"
                             style="width: 80px; opacity: 0.5; margin-bottom: 16px;">
                        <h2 style="color: #0f172a; margin-top: 0; margin-bottom: 8px;">Không tìm thấy vé</h2>
                        <p style="color: #64748b; font-size: 15px;">${ticketResult.message}</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </c:if>
</main>

</body>
</html>