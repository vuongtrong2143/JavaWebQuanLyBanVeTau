<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác nhận đơn đặt vé - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <style>
        body { background: #f1f5f9; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding-bottom: 50px; }
        
        /* Header Đồng Bộ */
        .top-header { background: white; padding: 12px 24px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.05); position: sticky; top: 0; z-index: 1000; }
        .main-nav { display: flex; gap: 18px; align-items: center; }
        .main-nav a { text-decoration: none; color: #475569; font-weight: 600; font-size: 14px; transition: 0.2s; }
        .main-nav a:hover { color: #1d4ed8; }
        .nav-auth { border-left: 2px solid #e2e8f0; padding-left: 18px; display: flex; gap: 18px; align-items: center; }
        .nav-auth a { color: #1d4ed8; font-weight: 600; text-decoration: none; font-size: 14px;}
        .nav-auth a.logout-btn { color: #ef4444; }

        /* Layout 2 cột */
        .review-layout { max-width: 1100px; margin: 40px auto; padding: 0 20px; display: flex; gap: 30px; align-items: flex-start; }
        .review-main { flex: 6; display: flex; flex-direction: column; gap: 24px; }
        .review-sidebar { flex: 4; position: sticky; top: 80px; }

        /* Card chung */
        .box-card { background: white; border-radius: 16px; padding: 24px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); border: 1px solid #e2e8f0; }
        .box-title { font-size: 18px; color: #0f172a; font-weight: 800; margin-top: 0; margin-bottom: 20px; display: flex; align-items: center; gap: 10px; border-bottom: 2px solid #f1f5f9; padding-bottom: 12px; }

        /* Bảng thông tin */
        .info-grid { display: grid; grid-template-columns: 1fr 2fr; gap: 12px 20px; font-size: 15px; }
        .info-label { color: #64748b; font-weight: 500; }
        .info-value { color: #0f172a; font-weight: 600; }

        /* Hóa đơn (Invoice) */
        .invoice-box { background: #f8fafc; border-radius: 12px; padding: 20px; border: 1px dashed #cbd5e1; }
        .price-row { display: flex; justify-content: space-between; margin-bottom: 12px; font-size: 15px; color: #475569; }
        .price-row.discount { color: #16a34a; font-weight: 600; }
        .price-total { display: flex; justify-content: space-between; margin-top: 16px; padding-top: 16px; border-top: 2px dashed #cbd5e1; font-weight: 900; font-size: 20px; color: #ea580c; }

        /* Input Khuyến mãi */
        .promo-input-group { display: flex; gap: 10px; margin-top: 15px; }
        .promo-input-group input { flex: 1; padding: 10px 16px; border: 1px solid #cbd5e1; border-radius: 8px; font-size: 14px; outline: none; }
        .promo-input-group input:focus { border-color: #3b82f6; }

        /* Buttons */
        .btn-group { display: flex; gap: 16px; margin-top: 24px; }
        .btn-back { flex: 1; background: #f1f5f9; color: #475569; border: 1px solid #cbd5e1; padding: 14px; border-radius: 12px; font-weight: bold; cursor: pointer; transition: 0.2s; font-size: 15px; }
        .btn-back:hover { background: #e2e8f0; }
        .btn-confirm { flex: 2; background: #1d4ed8; color: white; border: none; padding: 14px; border-radius: 12px; font-weight: 800; cursor: pointer; transition: 0.2s; font-size: 16px; box-shadow: 0 4px 12px rgba(29, 78, 216, 0.3); }
        .btn-confirm:hover { background: #1e40af; transform: translateY(-2px); }

        @media (max-width: 768px) {
            .review-layout { flex-direction: column; }
            .review-sidebar { position: static; width: 100%; order: -1; }
            .info-grid { grid-template-columns: 1fr; gap: 4px; margin-bottom: 16px; }
            .info-label { margin-top: 8px; }
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
                    <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                </c:otherwise>
            </c:choose>
        </div>
    </nav>
</header>

<div class="review-layout">
    <main class="review-main">
        <jsp:include page="/views/common/flash-message.jsp"/>
        <c:if test="${not empty error}">
            <div style="background: #fef2f2; color: #dc2626; padding: 16px; border-radius: 8px; font-weight: bold; border-left: 4px solid #ef4444; margin-bottom: 20px;">
                ❌ ${error}
            </div>
        </c:if>

        <div class="box-card">
            <h2 class="box-title">👤 Thông tin Hành khách</h2>
            <div class="info-grid">
                <div class="info-label">Họ và tên:</div>
                <div class="info-value" style="text-transform: uppercase;">${hoTen}</div>
                
                <div class="info-label">Loại giấy tờ:</div>
                <div class="info-value">${loaiGiayTo}</div>
                
                <div class="info-label">Số giấy tờ:</div>
                <div class="info-value">${soGiayTo}</div>
                
                <div class="info-label">Ngày sinh:</div>
                <div class="info-value">${ngaySinh}</div>
                
                <div class="info-label">Mã nhóm ưu đãi:</div>
                <div class="info-value">
                    <c:choose>
                        <c:when test="${empty doiTuongUuDaiId}"><span style="color: #94a3b8; font-weight: normal;">Không áp dụng</span></c:when>
                        <c:otherwise><span style="color: #16a34a;">${doiTuongUuDaiId}</span></c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <div class="box-card">
            <h2 class="box-title">🎟️ Mã Khuyến Mãi (Nếu có)</h2>
            <p style="margin: 0; color: #64748b; font-size: 14px;">Hệ thống sẽ tự động kiểm tra và giảm giá ở bước Thanh toán nếu mã của bạn hợp lệ.</p>
            <div class="promo-input-group">
                <input type="text" form="bookingForm" name="maKhuyenMai" placeholder="Nhập mã KM (VD: TET2026)...">
            </div>
        </div>
    </main>

    <aside class="review-sidebar">
        <div class="box-card" style="border-top: 4px solid #1d4ed8;">
            <h2 class="box-title" style="border-bottom: none; padding-bottom: 0;">🧾 Chi tiết thanh toán</h2>
            
            <div class="invoice-box">
                <div class="price-row">
                    <span>Giá vé cơ sở:</span>
                    <span><fmt:formatNumber value="${ketQuaGia.giaCoSo}" pattern="#,###"/> đ</span>
                </div>
                <div class="price-row">
                    <span>Phụ thu cao điểm:</span>
                    <span><fmt:formatNumber value="${ketQuaGia.phuThuCaoDiem}" pattern="#,###"/> đ</span>
                </div>
                <div class="price-row discount">
                    <span>Giảm giá đối tượng:</span>
                    <span>- <fmt:formatNumber value="${ketQuaGia.giamDoiTuong}" pattern="#,###"/> đ</span>
                </div>
                
                <div class="price-total">
                    <span>Thành tiền:</span>
                    <span><fmt:formatNumber value="${ketQuaGia.thanhTien}" pattern="#,###"/> đ</span>
                </div>
            </div>

            <form action="${pageContext.request.contextPath}/booking/create" method="post" id="bookingForm">
                <input type="hidden" name="chuyenTauId" value="${chuyenTauId}">
                <input type="hidden" name="gaDiId" value="${gaDiId}">
                <input type="hidden" name="gaDenId" value="${gaDenId}">
                <input type="hidden" name="toaTauId" value="${toaTauId}">
                <input type="hidden" name="gheId" value="${gheId}">
                <input type="hidden" name="loaiToa" value="${param.loaiToa}">
                <input type="hidden" name="tang" value="${param.tang}">
                <input type="hidden" name="hoTen" value="${hoTen}">
                <input type="hidden" name="loaiGiayTo" value="${loaiGiayTo}">
                <input type="hidden" name="soGiayTo" value="${soGiayTo}">
                <input type="hidden" name="ngaySinh" value="${ngaySinh}">
                <input type="hidden" name="doiTuongUuDaiId" value="${doiTuongUuDaiId}">

                <div class="btn-group">
                    <button type="button" class="btn-back" onclick="history.back()">Quay lại sửa</button>
                    <button type="submit" class="btn-confirm">Tạo Đặt Chỗ ➔</button>
                </div>
            </form>
        </div>
    </aside>
</div>

</body>
</html>