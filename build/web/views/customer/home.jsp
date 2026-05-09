<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Vé Tàu VN - Đặt vé tàu hỏa trực tuyến</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    
    <style>
        /* Header Đồng Bộ Toàn Hệ Thống */
        .top-header { background: white; padding: 12px 24px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.05); position: sticky; top: 0; z-index: 1000; }
        .main-nav { display: flex; gap: 18px; align-items: center; }
        .main-nav a { text-decoration: none; color: #475569; font-weight: 600; font-size: 14px; transition: 0.2s; }
        .main-nav a:hover { color: #1d4ed8; }
        
        .nav-auth { border-left: 2px solid #e2e8f0; padding-left: 18px; display: flex; gap: 18px; align-items: center; }
        .nav-auth a.login-btn { color: #1d4ed8; font-weight: 600; text-decoration: none; font-size: 14px; }
        .nav-auth a.register-btn { background: #1d4ed8; color: white; padding: 8px 16px; border-radius: 8px; font-weight: 600; font-size: 14px; transition: 0.2s; }
        .nav-auth a.register-btn:hover { background: #1e40af; transform: translateY(-2px); box-shadow: 0 4px 10px rgba(29, 78, 216, 0.3); }
        .nav-auth a.logout-btn { color: #ef4444; font-weight: 600; font-size: 14px; }
        .nav-auth a.profile-btn { color: #1d4ed8; font-weight: 600; font-size: 14px; }
        
        body { margin: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background: #f1f5f9; }
        
        /* 1. Hero Banner */
        .hero-section {
            /* Bạn có thể tải một ảnh tàu hỏa đẹp đặt tên là hero-bg.jpg và để vào thư mục images */
            background: linear-gradient(to bottom, rgba(15, 23, 42, 0.7), rgba(15, 23, 42, 0.4)), 
                        url('${pageContext.request.contextPath}/assets/images/anhnen.jpg') center/cover no-repeat;
            background-color: #1e293b; /* Màu nền dự phòng nếu ảnh lỗi */
            min-height: 450px; display: flex; flex-direction: column; align-items: center; justify-content: center;
            padding: 20px; color: white; text-align: center;
        }
        .hero-title { font-size: 3rem; font-weight: 800; margin-bottom: 10px; text-shadow: 0 2px 4px rgba(0,0,0,0.5); }
        .hero-subtitle { font-size: 1.2rem; margin-bottom: 40px; font-weight: 300; }

        /* 2. Form Tìm Kiếm Nổi */
        .search-box {
            background: white; padding: 24px; border-radius: 16px; box-shadow: 0 10px 25px rgba(0,0,0,0.15);
            width: 100%; max-width: 1000px; margin-top: -60px; position: relative; z-index: 10;
        }
        .search-grid { display: grid; grid-template-columns: 1fr 1fr 1fr auto; gap: 16px; align-items: end; }
        .form-group label { display: block; font-size: 14px; font-weight: 600; color: #475569; margin-bottom: 8px; }
        
        /* FIX LỖI CHIỀU CAO INPUT VÀ BUTTON */
        .form-group input[type="date"], .btn-search {
            height: 46px; /* Ép cứng chiều cao 46px */
            box-sizing: border-box;
        }
        .form-group input[type="date"] {
            width: 100%; padding: 0 12px; border: 1px solid #cbd5e1; border-radius: 8px; font-size: 16px; font-family: inherit;
        }
        .btn-search {
            background: #1d4ed8; color: white; border: none; padding: 0 32px; border-radius: 8px; font-size: 16px; font-weight: bold; cursor: pointer; transition: 0.2s;
        }
        .btn-search:hover { background: #1e40af; transform: translateY(-2px); }

        /* FIX LỖI CHIỀU CAO SELECT2 (CĂN BẰNG VỚI INPUT DATE) */
        .select2-container .select2-selection--single {
            height: 46px !important; /* Ép chiều cao khớp với thẻ input date */
            border: 1px solid #cbd5e1 !important;
            border-radius: 8px !important;
            display: flex !important;
            align-items: center !important; /* Căn giữa text theo chiều dọc */
        }
        .select2-container--default .select2-selection--single .select2-selection__rendered {
            line-height: normal !important;
            padding-left: 12px !important;
            font-size: 16px;
            color: #0f172a !important;
        }
        .select2-container--default .select2-selection--single .select2-selection__arrow {
            height: 44px !important; /* Mũi tên thả xuống */
            top: 1px !important;
            right: 8px !important;
        }

        /* 3. Bố cục 2 cột */
        .main-layout {
            max-width: 1200px; margin: 40px auto; padding: 0 20px;
            display: flex; gap: 24px; align-items: flex-start;
        }
        .content-left { flex: 7; display: flex; flex-direction: column; gap: 24px; }
        .sidebar-right { flex: 3; display: flex; flex-direction: column; gap: 20px; position: sticky; top: 80px; }

        .banner-img { width: 100%; border-radius: 12px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); object-fit: cover; transition: transform 0.3s; cursor: pointer; background: #e2e8f0; min-height: 100px; }
        .banner-img:hover { transform: translateY(-3px); box-shadow: 0 10px 15px rgba(0,0,0,0.1); }
        
        .main-banner { max-height: 350px; }
        .side-banner { height: auto; }

        .support-box { background: white; border-radius: 12px; padding: 20px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); text-align: center; border-top: 4px solid #1d4ed8; }
        .support-box h3 { margin-top: 0; color: #0f172a; font-size: 16px; margin-bottom: 16px; }
        .hotline { font-size: 24px; color: #ea580c; font-weight: bold; margin: 8px 0; }
        .hotline-sub { color: #64748b; font-size: 13px; }

        .section-title { font-size: 20px; color: #0f172a; font-weight: bold; margin-bottom: 16px; border-left: 4px solid #1d4ed8; padding-left: 12px; }
        .routes-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 16px; }
        .route-card { background: white; border-radius: 12px; overflow: hidden; box-shadow: 0 2px 10px rgba(0,0,0,0.05); transition: 0.3s; text-decoration: none; display: block; }
        .route-card:hover { transform: translateY(-5px); box-shadow: 0 10px 20px rgba(0,0,0,0.1); }
        .route-img { width: 100%; height: 140px; object-fit: cover; background: #cbd5e1; }
        .route-info { padding: 16px; }
        .route-name { font-size: 16px; font-weight: bold; color: #1e293b; margin-bottom: 4px; }
        .route-price { color: #ea580c; font-weight: bold; font-size: 14px; }

        @media (max-width: 992px) {
            .main-layout { flex-direction: column; }
            .sidebar-right { position: static; width: 100%; }
            .search-grid { grid-template-columns: 1fr 1fr; }
        }
        @media (max-width: 576px) {
            .search-grid { grid-template-columns: 1fr; }
            .search-box { margin-top: -20px; padding: 16px; }
        }
    </style>
</head>
<body>

    <header class="top-header">
        <div style="font-size: 22px; font-weight: 800; color: #1d4ed8; letter-spacing: -0.5px;">
            <a href="${pageContext.request.contextPath}/home" style="text-decoration: none; color: inherit;">🚂 VETAU VN</a>
        </div>
        <nav class="main-nav">
            <a href="${pageContext.request.contextPath}/home" style="color: #1d4ed8;">Trang chủ</a>
            <a href="${pageContext.request.contextPath}/search-train">Tìm chuyến</a>
            <a href="${pageContext.request.contextPath}/ticket-check">Tra cứu vé</a>
            <a href="${pageContext.request.contextPath}/promotion">Khuyến mãi</a>
            <a href="${pageContext.request.contextPath}/guide">Hướng dẫn</a>
            <a href="${pageContext.request.contextPath}/news">Tin tức</a> <div class="nav-auth">
            
            <div class="nav-auth">
                <c:choose>
                    <c:when test="${not empty sessionScope.currentCustomer}">
                        <a href="${pageContext.request.contextPath}/profile" class="profile-btn">Tài khoản</a>
                        <a href="${pageContext.request.contextPath}/my-bookings" class="profile-btn">Lịch sử đặt vé</a>
                        <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login" class="login-btn">Đăng nhập</a>
                        <a href="${pageContext.request.contextPath}/register" class="register-btn">Đăng ký ngay</a>
                    </c:otherwise>
                </c:choose>
            </div>
        </nav>
    </header>

    <c:if test="${not empty dbWarning}">
        <div style="background: #fef2f2; color: #dc2626; padding: 12px; text-align: center; font-weight: bold;">${dbWarning}</div>
    </c:if>

    <div class="hero-section">
        <h1 class="hero-title">Khám phá Việt Nam qua những chuyến tàu</h1>
        <p class="hero-subtitle">Nhanh chóng - Tiện lợi - An toàn. Đặt vé ngay hôm nay!</p>
    </div>

    <div style="display: flex; justify-content: center; padding: 0 20px;">
        <div class="search-box">
            <form action="${pageContext.request.contextPath}/search-train" method="get">
                <div class="search-grid">
                    <div class="form-group">
                        <label>Ga Đi</label>
                        <select name="gaDiId" id="gaDi" class="select2-ui" required>
                            <option value="">-- Chọn Ga Đi --</option>
                            <c:forEach var="ga" items="${listGa}">
                                <option value="${ga.id}">${ga.tenGa} (${ga.maGa})</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Ga Đến</label>
                        <select name="gaDenId" id="gaDen" class="select2-ui" required>
                            <option value="">-- Chọn Ga Đến --</option>
                            <c:forEach var="ga" items="${listGa}">
                                <option value="${ga.id}">${ga.tenGa} (${ga.maGa})</option>
                            </c:forEach>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Ngày Khởi Hành</label>
                        <input type="date" name="ngayDi" id="ngayDi" value="${ngayDiValue}" required>
                    </div>
                    <div class="form-group">
                        <button type="submit" class="btn-search">🔍 Tìm Chuyến</button>
                    </div>
                </div>
            </form>
        </div>
    </div>

    <div class="main-layout">
        
        <div class="content-left">
            
            <img src="${pageContext.request.contextPath}/assets/images/anhchinh1.jpg" 
                 alt="Bán vé Tết" class="banner-img main-banner" onerror="this.src='https://placehold.co/1200x400/e2e8f0/475569?text=Banner+Khuyen+Mai'">
            
            <img src="${pageContext.request.contextPath}/assets/images/anhchinh2.png" 
                 alt="Đặt vé tàu giá tốt" class="banner-img main-banner" onerror="this.src='https://placehold.co/1200x400/e2e8f0/475569?text=Banner+5+Cua+O'">

            <div style="margin-top: 16px;">
                <h2 class="section-title">Khám phá các tuyến phổ biến</h2>
                <div class="routes-grid">
                    <a href="#" class="route-card">
                        <img src="${pageContext.request.contextPath}/assets/images/hanoidanang.jpg" alt="Hà Nội" class="route-img" onerror="this.src='https://placehold.co/500x300/e2e8f0/475569?text=Route+HN-DN'">
                        <div class="route-info">
                            <div class="route-name">Hà Nội ➔ Đà Nẵng</div>
                            <div class="route-price">Từ 480.000 đ</div>
                        </div>
                    </a>
                    <a href="#" class="route-card">
                        <img src="${pageContext.request.contextPath}/assets/images/saigonnhatrang.jpg" alt="Sài Gòn" class="route-img" onerror="this.src='https://placehold.co/500x300/e2e8f0/475569?text=Route+SG-NT'">
                        <div class="route-info">
                            <div class="route-name">Sài Gòn ➔ Nha Trang</div>
                            <div class="route-price">Từ 260.000 đ</div>
                        </div>
                    </a>
                    <a href="#" class="route-card">
                        <img src="${pageContext.request.contextPath}/assets/images/hanoihue.jpg" alt="Huế" class="route-img" onerror="this.src='https://placehold.co/500x300/e2e8f0/475569?text=Route+HN-Hue'">
                        <div class="route-info">
                            <div class="route-name">Hà Nội ➔ Huế</div>
                            <div class="route-price">Từ 420.000 đ</div>
                        </div>
                    </a>
                </div>
            </div>
        </div>

        <div class="sidebar-right">
            
            <div class="support-box">
                <h3>☎️ KẾT NỐI VỚI CHÚNG TÔI</h3>
                <hr style="border: 1px solid #e2e8f0; margin-bottom: 16px;">
                <div class="hotline-sub">Tổng đài bán vé Miền Bắc</div>
                <div class="hotline">1900 0109</div>
                
                <div class="hotline-sub" style="margin-top: 16px;">Tổng đài bán vé Miền Nam</div>
                <div class="hotline">1900 1520</div>
            </div>

            <div class="support-box" style="padding: 16px;">
            <h3>🎥 TIN TỨC & CẢNH BÁO</h3>
            <hr style="border: 1px solid #e2e8f0; margin-bottom: 16px;">
                <div style="border-radius: 12px; overflow: hidden; margin-bottom: 20px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); aspect-ratio: 16/9;">
                    <iframe width="100%" height="100%" 
                            src="https://www.youtube.com/embed/watch?v=fxmTQXJiDq8" 
                            title="Video Cảnh Báo" frameborder="0" 
                            allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" 
                            allowfullscreen>
                    </iframe>
                </div>
            </div>

            <img src="${pageContext.request.contextPath}/assets/images/khuyenmai2.png" 
                 alt="Hành trình tàu thực tế" class="banner-img side-banner" onerror="this.src='https://placehold.co/400x400/e2e8f0/475569?text=Hanh+Trinh'">

        </div>
    </div>

    <footer style="background: #0f172a; color: #94a3b8; text-align: center; padding: 24px; margin-top: 40px;">
        <p style="margin: 0;">© 2026 Tổng công ty Đường sắt Việt Nam - Đồ án Java Web</p>
    </footer>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    
    <script>
        $(document).ready(function() {
            // Kích hoạt Select2
            $('.select2-ui').select2({ width: '100%', placeholder: 'Gõ để tìm ga...' });

            var dtToday = new Date();
            var month = dtToday.getMonth() + 1;
            var day = dtToday.getDate();
            var year = dtToday.getFullYear();
            if(month < 10) month = '0' + month.toString();
            if(day < 10) day = '0' + day.toString();
            var minDate = year + '-' + month + '-' + day;
            $('#ngayDi').attr('min', minDate);

            $('form').on('submit', function(e) {
                if ($('#gaDi').val() === $('#gaDen').val()) {
                    e.preventDefault();
                    alert('Lỗi: Ga đi và Ga đến không được trùng nhau!');
                }
            });
        });
    </script>
</body>
</html>