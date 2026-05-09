<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hướng dẫn đặt vé - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <style>
        /* GIỮ NGUYÊN CSS HEADER TỪ TRANG KHUYẾN MÃI */
        body { background: #f8fafc; font-family: 'Segoe UI', sans-serif; margin: 0; padding-bottom: 60px; }
        .top-header { background: white; padding: 12px 24px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.05); position: sticky; top: 0; z-index: 1000; }
        .main-nav { display: flex; gap: 18px; align-items: center; }
        .main-nav a { text-decoration: none; color: #475569; font-weight: 600; font-size: 14px; transition: 0.2s; }
        .main-nav a:hover { color: #1d4ed8; }
        .nav-auth { border-left: 2px solid #e2e8f0; padding-left: 18px; display: flex; gap: 18px; align-items: center; }
        .nav-auth a.login-btn { color: #1d4ed8; font-weight: 600; text-decoration: none; font-size: 14px; }
        .nav-auth a.register-btn { background: #1d4ed8; color: white; padding: 8px 16px; border-radius: 8px; font-weight: 600; font-size: 14px; text-decoration: none; }
        .nav-auth a.logout-btn { color: #ef4444; font-weight: 600; font-size: 14px; text-decoration: none; }
        
        .page-banner { background: #1e293b; color: white; text-align: center; padding: 60px 20px; }
        .container { max-width: 800px; margin: -40px auto 0; padding: 0 20px; }
        
        .step-card { background: white; border-radius: 16px; padding: 30px; margin-bottom: 24px; box-shadow: 0 10px 25px rgba(0,0,0,0.05); border-left: 6px solid #1d4ed8; display: flex; gap: 24px; align-items: flex-start; }
        .step-number { width: 50px; height: 50px; background: #eff6ff; color: #1d4ed8; border-radius: 50%; display: flex; justify-content: center; align-items: center; font-size: 24px; font-weight: 900; flex-shrink: 0; }
        .step-content h2 { margin: 0 0 10px 0; color: #0f172a; font-size: 20px; }
        .step-content p { margin: 0; color: #475569; line-height: 1.6; }
    </style>
</head>
<body>
<header class="top-header">
    <div style="font-size: 22px; font-weight: 800; color: #1d4ed8; letter-spacing: -0.5px;">🚂 VETAU VN</div>
    <nav class="main-nav">
        <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
        <a href="${pageContext.request.contextPath}/search-train">Tìm chuyến</a>
        <a href="${pageContext.request.contextPath}/ticket-check">Tra cứu vé</a>
        <a href="${pageContext.request.contextPath}/promotion">Khuyến mãi</a>
        <a href="${pageContext.request.contextPath}/guide" style="color: #1d4ed8;">Hướng dẫn</a>
        <a href="${pageContext.request.contextPath}/news">Tin tức</a>
        <div class="nav-auth">
            <c:choose>
                <c:when test="${not empty sessionScope.currentCustomer}">
                    <a href="${pageContext.request.contextPath}/profile" class="login-btn">Tài khoản</a>
                    <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login" class="login-btn">Đăng nhập</a>
                    <a href="${pageContext.request.contextPath}/register" class="register-btn">Đăng ký</a>
                </c:otherwise>
            </c:choose>
        </div>
    </nav>
</header>

<div class="page-banner">
    <h1 style="margin: 0; font-size: 32px;">Quy trình Đặt vé Dễ dàng</h1>
    <p style="margin-top: 10px; color: #94a3b8;">Chỉ với 4 bước đơn giản để bắt đầu hành trình của bạn</p>
</div>

<main class="container">
    <div class="step-card">
        <div class="step-number">1</div>
        <div class="step-content">
            <h2>Tìm kiếm Chuyến tàu</h2>
            <p>Truy cập mục "Tìm chuyến", chọn Ga khởi hành, Ga đến và Ngày đi mong muốn. Hệ thống sẽ liệt kê các chuyến tàu phù hợp cùng với bảng giá và giờ chạy chính xác.</p>
        </div>
    </div>
    
    <div class="step-card">
        <div class="step-number">2</div>
        <div class="step-content">
            <h2>Chọn Toa và Vị trí ghế</h2>
            <p>Sau khi chọn chuyến, bạn sẽ được chuyển đến sơ đồ toa tàu trực quan. Nhấp vào các vị trí ghế màu trắng (còn trống) để chọn chỗ ngồi ưng ý cho mình và gia đình.</p>
        </div>
    </div>

    <div class="step-card">
        <div class="step-number">3</div>
        <div class="step-content">
            <h2>Nhập thông tin Hành khách</h2>
            <p>Điền đầy đủ và chính xác Họ tên, Loại giấy tờ và Số giấy tờ (CCCD/Passport). Nếu thuộc đối tượng ưu đãi, hãy chọn đúng nhóm để hệ thống giảm giá tự động.</p>
        </div>
    </div>

    <div class="step-card" style="border-color: #16a34a;">
        <div class="step-number" style="background: #dcfce7; color: #16a34a;">4</div>
        <div class="step-content">
            <h2>Thanh toán & Nhận vé điện tử</h2>
            <p>Thực hiện thanh toán an toàn qua cổng VNPay hoặc MoMo. Sau khi giao dịch thành công, vé điện tử kèm mã QR Code sẽ được phát hành ngay lập tức vào Lịch sử đặt vé của bạn.</p>
        </div>
    </div>
</main>
</body>
</html>