<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thanh toán đơn hàng - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <style>
        body { background: #f8fafc; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding-bottom: 50px; }
        
        /* Header Đồng Bộ */
        .top-header { background: white; padding: 12px 24px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.05); position: sticky; top: 0; z-index: 1000; }
        .main-nav { display: flex; gap: 18px; align-items: center; }
        .main-nav a { text-decoration: none; color: #475569; font-weight: 600; font-size: 14px; transition: 0.2s; }
        .main-nav a:hover { color: #1d4ed8; }

        /* Container Thanh toán */
        .checkout-container { max-width: 600px; margin: 40px auto; padding: 0 20px; }
        
        /* Box Báo Thành Công */
        .success-banner { background: #dcfce7; border: 1px solid #86efac; padding: 20px; border-radius: 16px; text-align: center; margin-bottom: 24px; }
        .success-banner h2 { color: #166534; margin-top: 0; margin-bottom: 8px; font-size: 22px; }
        .success-banner p { color: #15803d; margin: 0; font-size: 15px; }

        /* Box Tổng Tiền */
        .amount-box { background: white; border-radius: 16px; padding: 30px; text-align: center; box-shadow: 0 4px 10px rgba(0,0,0,0.05); margin-bottom: 24px; border-top: 6px solid #ea580c; }
        .amount-label { color: #64748b; font-size: 16px; font-weight: 600; text-transform: uppercase; letter-spacing: 1px; }
        .amount-value { color: #ea580c; font-size: 36px; font-weight: 900; margin-top: 10px; }

        /* Phương thức thanh toán (Radio Button nâng cao) */
        .payment-methods { display: flex; flex-direction: column; gap: 12px; margin-bottom: 24px; }
        .method-card { background: white; border: 2px solid #e2e8f0; border-radius: 12px; padding: 16px 20px; display: flex; align-items: center; gap: 16px; cursor: pointer; transition: 0.2s; position: relative; }
        .method-card:hover { border-color: #93c5fd; background: #f8fafc; }
        
        /* Ẩn radio mặc định */
        .method-card input[type="radio"] { position: absolute; opacity: 0; cursor: pointer; height: 0; width: 0; }
        
        /* Nút radio giả */
        .radio-custom { width: 20px; height: 20px; border: 2px solid #cbd5e1; border-radius: 50%; display: flex; align-items: center; justify-content: center; }
        .radio-custom::after { content: ''; width: 10px; height: 10px; background: #1d4ed8; border-radius: 50%; opacity: 0; transition: 0.2s; }
        
        /* Hiệu ứng khi được chọn */
        .method-card input:checked ~ .radio-custom { border-color: #1d4ed8; }
        .method-card input:checked ~ .radio-custom::after { opacity: 1; }
        .method-card:has(input:checked) { border-color: #1d4ed8; background: #eff6ff; }
        
        .method-info { flex: 1; }
        .method-name { font-weight: bold; color: #0f172a; font-size: 16px; }
        .method-desc { font-size: 13px; color: #64748b; margin-top: 2px; }
        
        /* Icon ngân hàng giả lập bằng CSS */
        .method-icon { width: 40px; height: 40px; border-radius: 8px; display: flex; align-items: center; justify-content: center; font-weight: bold; font-size: 12px; color: white; }
        .icon-vnpay { background: linear-gradient(135deg, #00509E, #00A3E0); }
        .icon-momo { background: #A50064; }

        .btn-pay { background: #1d4ed8; color: white; border: none; padding: 16px; border-radius: 12px; font-weight: 800; font-size: 18px; cursor: pointer; transition: 0.2s; width: 100%; box-shadow: 0 4px 15px rgba(29, 78, 216, 0.3); }
        .btn-pay:hover { background: #1e40af; transform: translateY(-2px); }
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

<main class="checkout-container">
    <jsp:include page="/views/common/flash-message.jsp"/>
    <div class="success-banner">
        <h2>✅ Tạo Đặt Chỗ Thành Công!</h2>
        <p>Mã đặt chỗ của bạn: <b style="font-size: 18px;">${datCho.maDatCho}</b></p>
        <p style="margin-top: 8px; font-size: 13px; color: #166534;">
            <fmt:parseDate value="${datCho.thoiGianHetHan}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both" />
            Vui lòng thanh toán trước <b><fmt:formatDate pattern="HH:mm - dd/MM/yyyy" value="${parsedDateTime}" /></b>
        </p>
    </div>

    <div class="amount-box">
        <div class="amount-label">Tổng thanh toán</div>
        <div class="amount-value"><fmt:formatNumber value="${datCho.tongThanhToan}" pattern="#,###"/> <span style="font-size: 20px; color: #64748b;">VNĐ</span></div>
    </div>

    <form action="${pageContext.request.contextPath}/payment" method="post">
        <input type="hidden" name="datChoId" value="${datCho.id}">
        
        <h3 style="color: #334155; margin-bottom: 12px; font-size: 16px;">Chọn phương thức thanh toán:</h3>
        
        <div class="payment-methods">
            <label class="method-card">
                <input type="radio" name="phuongThuc" value="VNPay" checked>
                <div class="radio-custom"></div>
                <div class="method-icon icon-vnpay">VNP</div>
                <div class="method-info">
                    <div class="method-name">Cổng thanh toán VNPay</div>
                    <div class="method-desc">Quét mã QR, Thẻ ATM nội địa, Visa/Mastercard</div>
                </div>
            </label>

            <label class="method-card">
                <input type="radio" name="phuongThuc" value="MoMo">
                <div class="radio-custom"></div>
                <div class="method-icon icon-momo">MoMo</div>
                <div class="method-info">
                    <div class="method-name">Ví điện tử MoMo</div>
                    <div class="method-desc">Mở ứng dụng MoMo để quét mã</div>
                </div>
            </label>
        </div>

        <button type="submit" class="btn-pay">🔒 THANH TOÁN AN TOÀN</button>
    </form>

</main>

</body>
</html>