<%-- 
    Document   : passenger-info
    Created on : May 6, 2026, 4:38:48 PM
    Author     : trong
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thông tin hành khách - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <style>
        body { background: #f1f5f9; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding-bottom: 50px; }
        
        /* Header Navigation */
        .main-nav { display: flex; gap: 15px; align-items: center; flex-wrap: wrap; }
        .main-nav a { text-decoration: none; color: #475569; font-weight: 600; font-size: 15px; transition: 0.2s; }
        .main-nav a:hover { color: #1d4ed8; }
        .nav-auth { border-left: 2px solid #e2e8f0; padding-left: 15px; display: flex; gap: 15px; }
        .nav-auth a { color: #1d4ed8; }
        .nav-auth a.logout-btn { color: #dc2626; }

        /* Bố cục 2 cột Checkout */
        .checkout-layout { max-width: 1100px; margin: 40px auto; padding: 0 20px; display: flex; gap: 30px; align-items: flex-start; }
        .checkout-main { flex: 7; display: flex; flex-direction: column; gap: 24px; }
        .checkout-sidebar { flex: 3; position: sticky; top: 90px; }

        /* Card chung */
        .box-card { background: white; border-radius: 16px; padding: 24px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); }
        .box-title { font-size: 20px; color: #0f172a; font-weight: 800; margin-top: 0; margin-bottom: 20px; display: flex; align-items: center; gap: 10px; border-bottom: 2px solid #f1f5f9; padding-bottom: 12px; }

        /* Cảnh báo khách vãng lai */
        .guest-alert { background: #fffbeb; border: 1px solid #fde68a; border-left: 4px solid #f59e0b; padding: 16px 20px; border-radius: 8px; color: #92400e; display: flex; align-items: center; gap: 12px; }
        .guest-alert a { color: #d97706; font-weight: bold; text-decoration: underline; }

        /* Form UI */
        .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; }
        .form-group { display: flex; flex-direction: column; gap: 8px; }
        .form-group.full-width { grid-column: span 2; }
        .form-label { font-size: 14px; font-weight: 600; color: #334155; }
        .form-label .required { color: #ef4444; }
        .form-control { padding: 12px 16px; border: 1px solid #cbd5e1; border-radius: 8px; font-size: 15px; font-family: inherit; transition: 0.2s; background: #f8fafc; }
        .form-control:focus { outline: none; border-color: #3b82f6; background: white; box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1); }
        .form-hint { font-size: 13px; color: #64748b; margin-top: -4px; }

        /* Nút Submit */
        .btn-submit { background: #ea580c; color: white; border: none; padding: 16px; border-radius: 12px; font-weight: 800; font-size: 16px; cursor: pointer; transition: 0.2s; width: 100%; margin-top: 24px; box-shadow: 0 4px 12px rgba(234, 88, 12, 0.3); display: flex; justify-content: center; align-items: center; gap: 8px; }
        .btn-submit:hover { background: #c2410c; transform: translateY(-2px); box-shadow: 0 6px 16px rgba(234, 88, 12, 0.4); }

        /* Sidebar - Tóm tắt đơn hàng */
        .summary-card { background: #1e293b; color: white; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.1); }
        .summary-header { background: #0f172a; padding: 20px; font-weight: bold; font-size: 18px; text-align: center; }
        .summary-body { padding: 24px; }
        .summary-item { margin-bottom: 16px; padding-bottom: 16px; border-bottom: 1px dashed #334155; }
        .summary-item:last-child { border-bottom: none; margin-bottom: 0; padding-bottom: 0; }
        .s-label { font-size: 13px; color: #94a3b8; display: block; margin-bottom: 4px; }
        .s-value { font-size: 16px; font-weight: 600; color: #f8fafc; }
        .s-highlight { color: #38bdf8; font-size: 18px; font-weight: 800; }

        @media (max-width: 768px) {
            .checkout-layout { flex-direction: column; }
            .checkout-sidebar { position: static; width: 100%; order: -1; /* Đưa tóm tắt lên trên form trên mobile */ }
            .form-grid { grid-template-columns: 1fr; }
            .form-group.full-width { grid-column: span 1; }
        }
    </style>
</head>
<body>

<header style="background: white; padding: 16px 24px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 1px 3px rgba(0,0,0,0.1); position: sticky; top: 0; z-index: 1000;">
    <div style="font-size: 24px; font-weight: 800; color: #1d4ed8;">🚂 VETAU VN</div>
    <nav class="main-nav">
        <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
        <a href="${pageContext.request.contextPath}/search-train">Tìm chuyến</a>
        <a href="${pageContext.request.contextPath}/ticket-check">Tra cứu vé</a>
        <div class="nav-auth">
            <c:choose>
                <c:when test="${not empty sessionScope.currentCustomer}">
                    <a href="${pageContext.request.contextPath}/profile">Tài khoản</a>
                    <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                </c:otherwise>
            </c:choose>
        </div>
    </nav>
</header>

<div class="checkout-layout">
    
    <main class="checkout-main">
        
        <c:if test="${empty sessionScope.currentCustomer}">
            <div class="guest-alert">
                <span>⚠️ Bạn đang đặt vé với tư cách khách vãng lai. Hệ thống khuyến khích bạn <a href="${pageContext.request.contextPath}/login">đăng nhập</a> để dễ dàng quản lý vé và tích điểm sau này.</span>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/booking-review" method="post" id="passengerForm">
            <input type="hidden" name="chuyenTauId" value="${chuyenTauId}">
            <input type="hidden" name="gaDiId" value="${gaDiId}">
            <input type="hidden" name="gaDenId" value="${gaDenId}">
            <input type="hidden" name="toaTauId" value="${toaTauId}">
            <input type="hidden" name="gheId" value="${gheId}">
            <input type="hidden" name="loaiToa" value="${pageData.selectedToa.loaiToa}">
            <input type="hidden" name="tang" value="${selectedSeat.tang}">

            <div class="box-card">
                <h2 class="box-title">👤 Thông tin Hành khách</h2>
                
                <div class="form-grid">
                    <div class="form-group full-width">
                        <label class="form-label" for="hoTen">Họ và tên hành khách <span class="required">*</span></label>
                        <input class="form-control" id="hoTen" type="text" name="hoTen" placeholder="VD: NGUYEN VAN A" required>
                        <div class="form-hint">Nhập đúng họ tên trên giấy tờ tùy thân.</div>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="loaiGiayTo">Loại giấy tờ <span class="required">*</span></label>
                        <select class="form-control" id="loaiGiayTo" name="loaiGiayTo" required>
                            <option value="CCCD">Căn cước công dân</option>
                            <option value="Hộ chiếu">Hộ chiếu</option>
                            <option value="Giấy khai sinh">Giấy khai sinh (Trẻ em)</option>
                            <option value="VNeID">Tài khoản VNeID</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="soGiayTo">Số giấy tờ <span class="required">*</span></label>
                        <input class="form-control" id="soGiayTo" type="text" name="soGiayTo" placeholder="Nhập số CCCD/Passport" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="ngaySinh">Ngày sinh <span class="required">*</span></label>
                        <input class="form-control" id="ngaySinh" type="date" name="ngaySinh" required>
                    </div>

                    <div class="form-group">
                        <label class="form-label" for="doiTuongUuDaiId">Đối tượng ưu đãi</label>
                        <select class="form-control" id="doiTuongUuDaiId" name="doiTuongUuDaiId">
                            <option value="">Không có (Người lớn)</option>
                            <c:forEach var="uuDai" items="${uuDaiList}">
                                <option value="${uuDai.id}">${uuDai.tenDoiTuong} (Giảm ${uuDai.phanTramGiam}%)</option>
                            </c:forEach>
                        </select>
                        <div class="form-hint" style="color: #ea580c;">Phải xuất trình giấy tờ khi lên tàu.</div>
                    </div>
                </div>

                <button type="submit" class="btn-submit">
                    Tính giá và Xem lại đơn ➔
                </button>
            </div>
        </form>
    </main>

    <aside class="checkout-sidebar">
        <div class="summary-card">
            <div class="summary-header">
                TÓM TẮT HÀNH TRÌNH
            </div>
            <div class="summary-body">
                <div class="summary-item">
                    <span class="s-label">Tuyến đường</span>
                    <span class="s-value">${pageData.trip.gaDiTen} ➔ ${pageData.trip.gaDenTen}</span>
                </div>
                
                <div class="summary-item">
                    <span class="s-label">Thông tin tàu</span>
                    <span class="s-highlight">Tàu ${pageData.trip.tenTau}</span>
                    <div style="font-size: 14px; color: #cbd5e1; margin-top: 4px;">Mã chuyến: ${pageData.trip.maChuyen}</div>
                </div>

                <div class="summary-item">
                    <span class="s-label">Thời gian xuất phát</span>
                    <span class="s-value">🕒 ${pageData.trip.thoiGianDiText}</span>
                </div>

                <div class="summary-item" style="background: rgba(255,255,255,0.1); padding: 16px; border-radius: 8px;">
                    <span class="s-label" style="color: #bae6fd;">Vị trí chỗ ngồi</span>
                    <span class="s-value" style="font-size: 18px;">Toa ${pageData.selectedToa.soToa} - Ghế ${selectedSeat.soGhe}</span>
                    <div style="font-size: 13px; color: #94a3b8; margin-top: 4px;">(${pageData.selectedToa.loaiToa} | ${selectedSeat.moTaTang})</div>
                </div>
            </div>
        </div>
    </aside>

</div>

</body>
</html>