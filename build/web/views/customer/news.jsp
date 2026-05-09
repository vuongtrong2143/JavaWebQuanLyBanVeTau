<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tin tức Ngành đường sắt - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <style>
        /* GIỮ NGUYÊN CSS HEADER */
        body { background: #f8fafc; font-family: 'Segoe UI', sans-serif; margin: 0; padding-bottom: 60px; }
        .top-header { background: white; padding: 12px 24px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.05); position: sticky; top: 0; z-index: 1000; }
        .main-nav { display: flex; gap: 18px; align-items: center; }
        .main-nav a { text-decoration: none; color: #475569; font-weight: 600; font-size: 14px; transition: 0.2s; }
        .main-nav a:hover { color: #1d4ed8; }
        .nav-auth { border-left: 2px solid #e2e8f0; padding-left: 18px; display: flex; gap: 18px; align-items: center; }
        .nav-auth a.login-btn { color: #1d4ed8; font-weight: 600; text-decoration: none; font-size: 14px; }
        .nav-auth a.register-btn { background: #1d4ed8; color: white; padding: 8px 16px; border-radius: 8px; font-weight: 600; font-size: 14px; text-decoration: none; }
        .nav-auth a.logout-btn { color: #ef4444; font-weight: 600; font-size: 14px; text-decoration: none; }
        
        .container { max-width: 1000px; margin: 40px auto; padding: 0 20px; display: grid; grid-template-columns: 2fr 1fr; gap: 30px; }
        
        .news-list { display: flex; flex-direction: column; gap: 24px; }
        .news-card { background: white; border-radius: 12px; overflow: hidden; display: flex; border: 1px solid #e2e8f0; transition: 0.3s; cursor: pointer; }
        .news-card:hover { box-shadow: 0 10px 20px rgba(0,0,0,0.05); transform: translateY(-3px); }
        .news-img { width: 250px; height: 180px; object-fit: cover; }
        .news-content { padding: 20px; flex: 1; display: flex; flex-direction: column; justify-content: center; }
        .news-date { color: #1d4ed8; font-size: 13px; font-weight: bold; margin-bottom: 8px; }
        .news-title { font-size: 18px; margin: 0 0 10px 0; color: #0f172a; }
        .news-excerpt { color: #64748b; font-size: 14px; line-height: 1.5; margin: 0; display: -webkit-box; -webkit-line-clamp: 3; -webkit-box-orient: vertical; overflow: hidden; }

        .sidebar { background: white; padding: 24px; border-radius: 12px; border: 1px solid #e2e8f0; align-self: start; }
        .sidebar h3 { margin-top: 0; color: #0f172a; font-size: 18px; border-bottom: 2px solid #f1f5f9; padding-bottom: 12px; }
        .side-news { font-size: 14px; color: #334155; margin-bottom: 16px; padding-bottom: 16px; border-bottom: 1px dashed #e2e8f0; display: block; text-decoration: none; }
        .side-news:hover { color: #1d4ed8; }

        @media (max-width: 800px) {
            .container { grid-template-columns: 1fr; }
            .news-card { flex-direction: column; }
            .news-img { width: 100%; height: 200px; }
        }
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
        <a href="${pageContext.request.contextPath}/guide">Hướng dẫn</a>
        <a href="${pageContext.request.contextPath}/news" style="color: #1d4ed8;">Tin tức</a>
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

<main class="container">
    <div class="news-list">
        <h1 style="margin: 0 0 10px 0; color: #0f172a;">Tin tức & Sự kiện</h1>
        
        <div class="news-card">
            <img src="https://images.unsplash.com/photo-1541888035252-87588ab4d318?auto=format&fit=crop&w=400&q=80" alt="News 1" class="news-img">
            <div class="news-content">
                <div class="news-date">Hôm nay, 08:30</div>
                <h3 class="news-title">Chính thức mở bán vé tàu Tết Nguyên Đán 2027</h3>
                <p class="news-excerpt">Ngành đường sắt chính thức công bố kế hoạch mở bán vé tàu Tết 2027 với nhiều ưu đãi hấp dẫn. Khách hàng có thể đặt vé qua website, ứng dụng hoặc trực tiếp tại các nhà ga trên toàn quốc bắt đầu từ đầu tháng sau.</p>
            </div>
        </div>

        <div class="news-card">
            <img src="https://images.unsplash.com/photo-1474487548417-781cbc7149d4?auto=format&fit=crop&w=400&q=80" alt="News 2" class="news-img">
            <div class="news-content">
                <div class="news-date">05/05/2026</div>
                <h3 class="news-title">Nâng cấp chất lượng dịch vụ trên tuyến Bắc - Nam</h3>
                <p class="news-excerpt">Các đoàn tàu SE1/2, SE3/4 được trang bị thêm nội thất hiện đại, wifi miễn phí và hệ thống suất ăn hàng không, mang đến trải nghiệm tiện nghi tối đa cho hành khách trong các chuyến đi dài.</p>
            </div>
        </div>
    </div>

    <div class="sidebar">
        <h3>Tin nổi bật</h3>
        <a href="#" class="side-news">
            <b>Cảnh báo:</b> Xuất hiện tình trạng vé tàu giả mạo, hành khách lưu ý chỉ mua vé qua các kênh chính thức.
        </a>
        <a href="#" class="side-news">
            Hướng dẫn quy định về hành lý xách tay khi lên tàu từ năm 2026.
        </a>
        <a href="#" class="side-news" style="border-bottom: none;">
            Lịch chạy tàu tăng cường tuyến Sài Gòn - Nha Trang dịp Hè.
        </a>
    </div>
</main>
</body>
</html>