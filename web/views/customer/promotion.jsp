<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chương trình Khuyến mãi - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <style>
        body { background: #f8fafc; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding-bottom: 60px; }
        .top-header { background: white; padding: 12px 24px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.05); position: sticky; top: 0; z-index: 1000; }
        .main-nav { display: flex; gap: 18px; align-items: center; }
        .main-nav a { text-decoration: none; color: #475569; font-weight: 600; font-size: 14px; transition: 0.2s; }
        .main-nav a:hover { color: #1d4ed8; }
        .nav-auth { border-left: 2px solid #e2e8f0; padding-left: 18px; display: flex; gap: 18px; align-items: center; }
        .nav-auth a.login-btn { color: #1d4ed8; font-weight: 600; text-decoration: none; font-size: 14px; }
        .nav-auth a.register-btn { background: #1d4ed8; color: white; padding: 8px 16px; border-radius: 8px; font-weight: 600; font-size: 14px; text-decoration: none; }
        .nav-auth a.logout-btn { color: #ef4444; font-weight: 600; font-size: 14px; text-decoration: none; }
        
        .page-banner { background: linear-gradient(135deg, #ea580c, #f97316); color: white; text-align: center; padding: 60px 20px; }
        .page-banner h1 { margin: 0 0 10px 0; font-size: 36px; font-weight: 900; }
        
        .container { max-width: 1000px; margin: -40px auto 0; padding: 0 20px; }
        .promo-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 24px; }
        .promo-card { background: white; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.05); transition: 0.3s; border: 1px solid #e2e8f0; }
        .promo-card:hover { transform: translateY(-5px); box-shadow: 0 15px 35px rgba(0,0,0,0.1); border-color: #fdba74; }
        
        .promo-img { width: 100%; height: 180px; object-fit: cover; background: #cbd5e1; }
        .promo-content { padding: 24px; }
        .promo-badge { background: #fee2e2; color: #dc2626; padding: 4px 12px; border-radius: 20px; font-size: 12px; font-weight: bold; display: inline-block; margin-bottom: 12px; }
        .promo-title { font-size: 18px; font-weight: 800; color: #0f172a; margin: 0 0 10px 0; line-height: 1.4; }
        .promo-desc { color: #64748b; font-size: 14px; margin-bottom: 20px; line-height: 1.6; }
        .btn-promo { display: block; text-align: center; background: #fff7ed; color: #ea580c; border: 1px solid #fed7aa; padding: 12px; border-radius: 8px; font-weight: bold; text-decoration: none; transition: 0.2s; }
        .btn-promo:hover { background: #ea580c; color: white; }
    </style>
</head>
<body>
<header class="top-header">
    <div style="font-size: 22px; font-weight: 800; color: #1d4ed8; letter-spacing: -0.5px;">🚂 VETAU VN</div>
    <nav class="main-nav">
        <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
        <a href="${pageContext.request.contextPath}/search-train">Tìm chuyến</a>
        <a href="${pageContext.request.contextPath}/ticket-check">Tra cứu vé</a>
        <a href="${pageContext.request.contextPath}/promotion" style="color: #1d4ed8;">Khuyến mãi</a>
        <a href="${pageContext.request.contextPath}/guide">Hướng dẫn</a>
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
    <h1>Khuyến mãi & Ưu đãi</h1>
    <p>Tổng hợp các chương trình giảm giá vé tàu mới nhất từ hệ thống</p>
</div>

<main class="container">
    <div class="promo-grid">
        <div class="promo-card">
            <img src="https://images.unsplash.com/photo-1541888035252-87588ab4d318?auto=format&fit=crop&w=600&q=80" alt="Sinh viên" class="promo-img">
            <div class="promo-content">
                <div class="promo-badge">GIẢM 20%</div>
                <h3 class="promo-title">Ưu đãi đồng hành cùng Sinh viên</h3>
                <p class="promo-desc">Giảm ngay 20% giá vé cho học sinh, sinh viên các trường Đại học, Cao đẳng trên toàn quốc khi xuất trình thẻ hợp lệ.</p>
                <a href="${pageContext.request.contextPath}/search-train" class="btn-promo">Đặt vé ngay</a>
            </div>
        </div>

        <div class="promo-card">
            <img src="https://images.unsplash.com/photo-1506869640319-fea1a2882006?auto=format&fit=crop&w=600&q=80" alt="Tết" class="promo-img">
            <div class="promo-content">
                <div class="promo-badge">HOT - SẮP HẾT HẠN</div>
                <h3 class="promo-title">Săn vé sớm - Đón Tết sum vầy</h3>
                <p class="promo-desc">Chiết khấu 10% cho khách hàng mua vé khứ hồi dịp Tết Nguyên Đán 2027 trước ngày 15/12/2026.</p>
                <a href="${pageContext.request.contextPath}/search-train" class="btn-promo">Đặt vé ngay</a>
            </div>
        </div>

        <div class="promo-card">
            <img src="https://images.unsplash.com/photo-1515162816999-a0c47dc192f7?auto=format&fit=crop&w=600&q=80" alt="Người cao tuổi" class="promo-img">
            <div class="promo-content">
                <div class="promo-badge">CHÍNH SÁCH THƯỜNG XUYÊN</div>
                <h3 class="promo-title">Giảm 15% cho Người cao tuổi</h3>
                <p class="promo-desc">Công dân Việt Nam từ 60 tuổi trở lên luôn được hưởng mức giá ưu đãi giảm 15% trên mọi chuyến đi.</p>
                <a href="${pageContext.request.contextPath}/search-train" class="btn-promo">Đặt vé ngay</a>
            </div>
        </div>
    </div>
</main>
</body>
</html>