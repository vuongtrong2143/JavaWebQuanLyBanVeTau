<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Xác nhận trả vé - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <style>
        body { background: #f8fafc; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding-bottom: 60px; }
        
        /* Header Đồng Bộ */
        .top-header { background: white; padding: 12px 24px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.05); position: sticky; top: 0; z-index: 1000; }
        .main-nav { display: flex; gap: 18px; align-items: center; }
        .main-nav a { text-decoration: none; color: #475569; font-weight: 600; font-size: 14px; transition: 0.2s; }
        .main-nav a:hover { color: #1d4ed8; }

        .container-return { max-width: 700px; margin: 40px auto; padding: 0 20px; }
        .page-title { font-size: 24px; font-weight: 900; color: #dc2626; margin: 0 0 24px 0; display: flex; align-items: center; gap: 10px; }

        /* Khối Cảnh báo */
        .alert-danger { background: #fef2f2; border: 1px solid #fca5a5; border-left: 4px solid #ef4444; padding: 16px 20px; border-radius: 12px; margin-bottom: 24px; color: #991b1b; line-height: 1.5; }
        .alert-danger b { color: #7f1d1d; }

        /* Thẻ thông tin vé */
        .info-card { background: white; border-radius: 16px; padding: 24px; box-shadow: 0 4px 6px rgba(0,0,0,0.02); border: 1px solid #e2e8f0; margin-bottom: 24px; }
        .card-header { font-size: 16px; font-weight: 800; color: #0f172a; border-bottom: 2px solid #f1f5f9; padding-bottom: 12px; margin-bottom: 16px; display: flex; justify-content: space-between; }
        
        .grid-info { display: grid; grid-template-columns: 1fr 2fr; gap: 12px; font-size: 15px; }
        .label { color: #64748b; font-weight: 500; }
        .value { color: #1e293b; font-weight: 600; }

        /* Bảng tính toán Hoàn tiền */
        .refund-card { background: #fff7ed; border-radius: 16px; padding: 24px; border: 1px solid #fed7aa; margin-bottom: 24px; }
        .refund-header { color: #ea580c; font-size: 16px; font-weight: 800; margin-bottom: 16px; display: flex; align-items: center; gap: 8px; }
        
        .calc-row { display: flex; justify-content: space-between; margin-bottom: 10px; font-size: 15px; color: #431407; }
        .calc-row.minus { color: #dc2626; }
        
        .calc-total { display: flex; justify-content: space-between; align-items: center; margin-top: 16px; padding-top: 16px; border-top: 2px dashed #fdba74; font-weight: bold; font-size: 18px; }
        .total-amount { color: #16a34a; font-size: 24px; font-weight: 900; }

        /* Form Lý do & Buttons */
        .form-group { margin-bottom: 24px; }
        .form-group label { display: block; font-weight: bold; color: #334155; margin-bottom: 8px; font-size: 14px; }
        .form-control { width: 100%; padding: 12px; border: 1px solid #cbd5e1; border-radius: 8px; font-family: inherit; font-size: 15px; box-sizing: border-box; transition: 0.2s; resize: vertical; }
        .form-control:focus { border-color: #3b82f6; outline: none; box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1); }

        .action-group { display: flex; gap: 16px; justify-content: flex-end; }
        .btn { padding: 14px 24px; border-radius: 10px; font-weight: bold; font-size: 15px; text-decoration: none; cursor: pointer; transition: 0.2s; border: none; display: flex; align-items: center; justify-content: center; }
        
        .btn-cancel { background: white; color: #475569; border: 1px solid #cbd5e1; }
        .btn-cancel:hover { background: #f1f5f9; }
        
        .btn-confirm { background: #dc2626; color: white; box-shadow: 0 4px 12px rgba(220, 38, 38, 0.3); }
        .btn-confirm:hover { background: #b91c1c; transform: translateY(-2px); }

        @media (max-width: 600px) {
            .grid-info { grid-template-columns: 1fr; gap: 4px; margin-bottom: 12px; }
            .label { margin-top: 8px; }
            .action-group { flex-direction: column-reverse; }
            .btn { width: 100%; }
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

<main class="container-return">
    <jsp:include page="/views/common/flash-message.jsp"/>
    <h1 class="page-title">⚠️ Yêu cầu Trả vé Trực tuyến</h1>

    <div class="alert-danger">
        <b>Cảnh báo quan trọng:</b> Thao tác trả vé là <u>không thể hoàn tác</u>. Vé sau khi xác nhận trả sẽ lập tức bị hủy bỏ và chỗ ngồi sẽ được mở lại trên hệ thống cho khách hàng khác mua. Vui lòng kiểm tra kỹ thông tin bên dưới.
    </div>

    <div class="info-card">
        <div class="card-header">
            <span>Chi tiết vé</span>
            <span style="font-family: monospace; color: #1d4ed8;">#${ticketDetail.maVe}</span>
        </div>
        <div class="grid-info">
            <div class="label">Hành khách:</div>
            <div class="value" style="text-transform: uppercase;">${ticketDetail.tenHanhKhach}</div>
            
            <div class="label">Giấy tờ:</div>
            <div class="value">${ticketDetail.loaiGiayTo} - ${ticketDetail.soGiayTo}</div>
            
            <div class="label">Hành trình:</div>
            <div class="value">${ticketDetail.tenGaDi} ➔ ${ticketDetail.tenGaDen}</div>
            
            <div class="label">Khởi hành:</div>
            <div class="value" style="color: #ea580c;">${ticketDetail.thoiGianDiText}</div>
            
            <div class="label">Giá mua ban đầu:</div>
            <div class="value"><fmt:formatNumber value="${ve.giaVeChiTiet}" pattern="#,###"/> đ</div>
        </div>
    </div>

    <div class="refund-card">
        <div class="refund-header">
            🧾 Chính sách & Chi tiết Hoàn tiền
        </div>
        
        <div class="calc-row">
            <span>Giá vé ban đầu:</span>
            <span><fmt:formatNumber value="${ve.giaVeChiTiet}" pattern="#,###"/> đ</span>
        </div>
        
        <div class="calc-row">
            <span>Áp dụng quy định:</span>
            <span style="font-weight: 600;">${chinhSach.tenChinhSach}</span>
        </div>

        <c:if test="${chinhSach.tyLeKhauTru > 0}">
            <div class="calc-row minus">
                <span>Phí khấu trừ (${chinhSach.tyLeKhauTru}%):</span>
                <span>- <fmt:formatNumber value="${ve.giaVeChiTiet * chinhSach.tyLeKhauTru / 100}" pattern="#,###"/> đ</span>
            </div>
        </c:if>

        <c:if test="${chinhSach.phiDoiCoDinh > 0}">
            <div class="calc-row minus">
                <span>Phí trả vé cố định:</span>
                <span>- <fmt:formatNumber value="${chinhSach.phiDoiCoDinh}" pattern="#,###"/> đ</span>
            </div>
        </c:if>

        <div class="calc-total">
            <span>Tổng tiền hoàn lại:</span>
            <span class="total-amount"><fmt:formatNumber value="${tienHoan}" pattern="#,###"/> đ</span>
        </div>
        
        <div style="font-size: 13px; color: #9a3412; margin-top: 12px; background: #ffedd5; padding: 10px; border-radius: 8px;">
            ℹ️ Tiền sẽ được hoàn tự động về phương thức thanh toán gốc (VNPay/MoMo/Thẻ ngân hàng) trong vòng 3-5 ngày làm việc tùy thuộc vào ngân hàng phát hành thẻ.
        </div>
    </div>

    <form action="${pageContext.request.contextPath}/return-ticket" method="post">
        <input type="hidden" name="maVe" value="${ve.maVe}">
        
        <div class="form-group">
            <label for="lyDo">Lý do trả vé (Giúp chúng tôi cải thiện dịch vụ)</label>
            <textarea id="lyDo" name="lyDo" rows="3" class="form-control" placeholder="Ví dụ: Thay đổi kế hoạch cá nhân, bận việc đột xuất..."></textarea>
        </div>

        <div class="action-group">
            <a href="${pageContext.request.contextPath}/my-tickets?datChoId=${ve.datChoId}" class="btn btn-cancel">
                Giữ lại vé
            </a>
            <button type="submit" class="btn btn-confirm" onclick="return confirm('Bạn có chắc chắn muốn HỦY và TRẢ vé này không? Hành động này không thể hoàn tác.');">
                Xác nhận Trả vé ngay
            </button>
        </div>
    </form>

</main>

</body>
</html>