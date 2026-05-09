<%-- 
    Document   : seat-selection
    Created on : May 6, 2026, 4:09:53 PM
    Author     : trong
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chọn toa và ghế</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <style>
        /* Header Navigation - Thống nhất toàn hệ thống */
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
            white-space: nowrap;
        }

        .main-nav a:hover {
            color: #1d4ed8;
        }

        .main-nav a.active {
            color: #1d4ed8;
        }

        /* Khối Đăng nhập / Tài khoản */
        .nav-auth {
            border-left: 2px solid #e2e8f0;
            padding-left: 18px;
            display: flex;
            gap: 18px;
            align-items: center;
        }

        .nav-auth a {
            color: #1d4ed8;
        }

        .nav-auth a.logout-btn {
            color: #ef4444; /* Màu đỏ cho Đăng xuất */
        }

        @media (max-width: 992px) {
            .main-nav { display: none; } /* Ẩn menu chính trên mobile để tránh vỡ giao diện */
        }
        .trip-info {
            background: #eff6ff;
            border: 1px solid #bfdbfe;
            border-radius: 12px;
            padding: 16px;
            margin-bottom: 20px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 16px;
        }

        .toa-list {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
            margin-bottom: 24px;
        }

        .toa-item {
            padding: 12px 16px;
            border: 1px solid #cbd5e1;
            border-radius: 10px;
            text-decoration: none;
            color: #0f172a;
            background: #f8fafc;
            text-align: center;
            min-width: 120px;
            transition: all 0.2s;
        }

        .toa-item.active {
            background: #1d4ed8;
            color: #ffffff;
            border-color: #1d4ed8;
            box-shadow: 0 4px 12px rgba(29, 78, 216, 0.2);
        }

        .toa-item:hover:not(.active) {
            background: #e2e8f0;
        }

        .seat-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(80px, 1fr));
            gap: 14px;
            margin-top: 16px;
        }

        .seat {
            padding: 16px 8px;
            border: 2px solid #cbd5e1;
            border-radius: 10px;
            text-align: center;
            background: #ffffff;
            user-select: none;
            transition: all 0.2s;
            position: relative;
        }

        .seat .seat-number {
            font-weight: 800;
            font-size: 16px;
            display: block;
        }

        .seat .seat-type {
            font-size: 12px;
            display: block;
            margin-top: 4px;
        }

        /* Các trạng thái ghế lấy từ DTO */
        .seat.available {
            cursor: pointer;
        }
        .seat.available:hover {
            border-color: #1d4ed8;
            background: #eff6ff;
            transform: translateY(-2px);
        }
        .seat.occupied {
            background: #f1f5f9;
            border-color: #94a3b8;
            color: #64748b;
            cursor: not-allowed;
        }
        .seat.blocked {
            background: #fef2f2;
            border-color: #fca5a5;
            color: #ef4444;
            cursor: not-allowed;
        }
        .seat.selected {
            background: #16a34a;
            border-color: #16a34a;
            color: white;
        }
        .seat.selected .seat-type {
            color: #dcfce7;
        }

        .legend {
            display: flex;
            gap: 24px;
            margin-top: 24px;
            padding-top: 16px;
            border-top: 1px solid #e2e8f0;
            font-size: 14px;
            font-weight: 600;
            flex-wrap: wrap;
        }

        .legend-item {
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .legend-box {
            width: 24px;
            height: 24px;
            border-radius: 6px;
            border: 2px solid;
        }
        
        .box-available { background: #fff; border-color: #cbd5e1; }
        .box-occupied { background: #c93636; border-color: #94a3b8; }
        .box-blocked { background: #abcc3d; border-color: #fca5a5; }
        .box-selected { background: #16a34a; border-color: #16a34a; }

        .submit-section {
            margin-top: 32px;
            text-align: right;
            padding: 16px;
            background: #f8fafc;
            border-radius: 12px;
            border: 1px solid #e2e8f0;
        }
        
        #btnSubmitSeat:disabled {
            background: #94a3b8;
            border-color: #94a3b8;
            cursor: not-allowed;
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
        <a href="${pageContext.request.contextPath}/search-train" class="active">Tìm chuyến</a>
        <a href="${pageContext.request.contextPath}/ticket-check">Tra cứu vé</a>
        <a href="${pageContext.request.contextPath}/promotion">Khuyến mãi</a>
        <a href="${pageContext.request.contextPath}/guide">Hướng dẫn</a>
        <a href="${pageContext.request.contextPath}/news">Tin tức</a>

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

<main class="container">
    <section class="card">
        <h1>Chọn toa và ghế</h1>
        
        <c:if test="${not empty message}">
            <div class="alert">${message}</div>
        </c:if>

        <c:if test="${not empty pageData}">
            <!-- 1. Thông tin chuyến tàu -->
            <div class="trip-info">
                <div>
                    <h2 style="margin: 0; color: #1d4ed8;">
                        ${pageData.trip.tenTau} (${pageData.trip.maChuyen})
                    </h2>
                    <p style="margin: 4px 0 0;">
                        <b>Chặng:</b> ${pageData.trip.gaDiTen} → ${pageData.trip.gaDenTen}
                    </p>
                </div>
                <div style="text-align: right;">
                    <p style="margin: 0;"><b>Giờ đi:</b> ${pageData.trip.thoiGianDiText}</p>
                    <p style="margin: 4px 0 0; color: #ea580c; font-weight: bold;">
                        Giá từ: ${pageData.trip.giaThapNhatText}
                    </p>
                </div>
            </div>

            <!-- 2. Danh sách Toa tàu -->
            <h3>Danh sách Toa tàu</h3>
            <div class="toa-list">
                <c:forEach var="toa" items="${pageData.toaList}">
                    <a href="${pageContext.request.contextPath}/seat-selection?chuyenTauId=${pageData.trip.chuyenTauId}&gaDiId=${pageData.gaDiId}&gaDenId=${pageData.gaDenId}&toaTauId=${toa.toaTauId}" 
                       class="toa-item ${toa.selected ? 'active' : ''}">
                        <div style="font-weight: bold; font-size: 18px;">Toa ${toa.soToa}</div>
                        <div style="font-size: 13px; margin: 4px 0;">${toa.loaiToa}</div>
                        <div style="font-size: 12px; opacity: 0.9;">
                            Còn ${toa.soGheConLai}/${toa.soGheHoatDong} chỗ
                        </div>
                    </a>
                </c:forEach>
            </div>

            <!-- 3. Sơ đồ ghế của toa đang chọn -->
            <h3>Sơ đồ ghế: <span style="color: #1d4ed8;">${pageData.selectedToa.tenHienThi}</span></h3>
            
            <div class="seat-grid">
                <c:forEach var="ghe" items="${pageData.gheList}">
                    <!-- Lấy cssClass từ GheTrangThaiDTO (available, occupied, blocked) -->
                    <div class="${ghe.cssClass}" 
                         data-id="${ghe.gheId}" 
                         data-chonduoc="${ghe.chonDuoc}"
                         title="${ghe.trangThaiHienThi}">
                        <span class="seat-number">${ghe.soGhe}</span>
                        <span class="seat-type">${ghe.moTaTang}</span>
                    </div>
                </c:forEach>
            </div>

            <!-- Chú thích sơ đồ ghế -->
            <div class="legend">
                <div class="legend-item">
                    <div class="legend-box box-available"></div> Còn trống
                </div>
                <div class="legend-item">
                    <div class="legend-box box-selected"></div> Đang chọn
                </div>
                <div class="legend-item">
                    <div class="legend-box box-occupied"></div> Đã bán / Đang giữ
                </div>
                <div class="legend-item">
                    <div class="legend-box box-blocked"></div> Bảo trì
                </div>
            </div>

            <!-- 4. Form Submit sang bước Nhập thông tin hành khách -->
            <div class="submit-section">
                <form action="${pageContext.request.contextPath}/passenger-info" method="post" id="seatForm">
                    <input type="hidden" name="chuyenTauId" value="${pageData.trip.chuyenTauId}">
                    <input type="hidden" name="gaDiId" value="${pageData.gaDiId}">
                    <input type="hidden" name="gaDenId" value="${pageData.gaDenId}">
                    <input type="hidden" name="toaTauId" value="${pageData.selectedToaTauId}">
                    
                    <!-- ID ghế sẽ được gán bằng JavaScript khi click -->
                    <input type="hidden" name="gheId" id="selectedGheId" value="">
                    
                    <span id="seatSelectionText" style="margin-right: 16px; font-weight: bold; color: #16a34a;">
                        Chưa chọn ghế nào
                    </span>
                    <button type="submit" id="btnSubmitSeat" disabled>Tiếp tục nhập thông tin</button>
                </form>
            </div>
        </c:if>
    </section>
</main>

<script>
    document.addEventListener("DOMContentLoaded", function () {
        const seats = document.querySelectorAll('.seat.available');
        const hiddenInput = document.getElementById('selectedGheId');
        const submitBtn = document.getElementById('btnSubmitSeat');
        const selectionText = document.getElementById('seatSelectionText');

        seats.forEach(function (seat) {
            seat.addEventListener('click', function () {
                // Nếu ghế này không thể chọn (phòng hờ logic từ DTO)
                if (this.getAttribute('data-chonduoc') !== 'true') return;

                // Reset tất cả các ghế đang chọn
                seats.forEach(s => s.classList.remove('selected'));
                
                // Active ghế được click
                this.classList.add('selected');
                
                // Lấy thông tin ID và số ghế
                const gheId = this.getAttribute('data-id');
                const soGhe = this.querySelector('.seat-number').innerText;

                // Gán vào form và mở khóa nút Submit
                hiddenInput.value = gheId;
                selectionText.innerText = "Đang chọn: Ghế " + soGhe;
                submitBtn.disabled = false;
            });
        });
    });
</script>
</body>
</html>
