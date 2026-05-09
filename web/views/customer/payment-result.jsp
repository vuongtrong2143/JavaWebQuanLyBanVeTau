<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kết quả giao dịch - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <style>
        body { background: #f8fafc; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding-bottom: 60px; }
        
        /* Header Đồng Bộ */
        .top-header { background: white; padding: 12px 24px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.05); position: sticky; top: 0; z-index: 1000; }
        .main-nav { display: flex; gap: 18px; align-items: center; }
        .main-nav a { text-decoration: none; color: #475569; font-weight: 600; font-size: 14px; transition: 0.2s; }
        .main-nav a:hover { color: #1d4ed8; }

        /* Container kết quả */
        .result-container { max-width: 600px; margin: 60px auto 0; padding: 0 20px; text-align: center; }
        
        /* Box Trạng thái chung */
        .status-card { background: white; border-radius: 24px; padding: 40px 30px; box-shadow: 0 20px 40px rgba(0,0,0,0.08); position: relative; overflow: hidden; }
        
        /* Icon vòng tròn kết quả */
        .icon-circle { width: 100px; height: 100px; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin: 0 auto 24px; font-size: 48px; box-shadow: 0 10px 20px rgba(0,0,0,0.1); }
        .icon-success { background: #dcfce7; color: #16a34a; border: 4px solid #bbf7d0; }
        .icon-fail { background: #fee2e2; color: #dc2626; border: 4px solid #fecaca; }

        .status-title { font-size: 28px; font-weight: 900; margin: 0 0 12px 0; }
        .title-success { color: #15803d; }
        .title-fail { color: #b91c1c; }

        .status-msg { font-size: 16px; color: #475569; line-height: 1.6; margin-bottom: 30px; padding: 16px; border-radius: 12px; }
        .msg-success { background: #f0fdf4; border: 1px dashed #86efac; }
        .msg-fail { background: #fef2f2; border: 1px dashed #fca5a5; }

        /* Nút hành động */
        .btn-group { display: flex; flex-direction: column; gap: 12px; }
        .btn { padding: 16px 24px; border-radius: 12px; font-weight: bold; font-size: 16px; text-decoration: none; transition: 0.2s; cursor: pointer; border: none; display: flex; align-items: center; justify-content: center; gap: 8px; }
        
        .btn-primary { background: #1d4ed8; color: white; box-shadow: 0 4px 12px rgba(29, 78, 216, 0.3); }
        .btn-primary:hover { background: #1e40af; transform: translateY(-2px); }
        
        .btn-outline { background: white; color: #475569; border: 2px solid #e2e8f0; }
        .btn-outline:hover { background: #f8fafc; border-color: #cbd5e1; }
        
        .btn-retry { background: #ea580c; color: white; box-shadow: 0 4px 12px rgba(234, 88, 12, 0.3); }
        .btn-retry:hover { background: #c2410c; transform: translateY(-2px); }

        /* Background họa tiết vé mờ */
        .status-card::before { content: ''; position: absolute; top: -50px; left: -50px; width: 150px; height: 150px; background: rgba(241, 245, 249, 0.5); border-radius: 50%; z-index: 0; pointer-events: none; }
    </style>
</head>
<body>

<header class="top-header">
    <div style="font-size: 22px; font-weight: 800; color: #1d4ed8; letter-spacing: -0.5px;">
        <a href="${pageContext.request.contextPath}/home" style="text-decoration: none; color: inherit;">🚂 VETAU VN</a>
    </div>
    <nav class="main-nav">
        <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
        <a href="${pageContext.request.contextPath}/my-bookings" style="color: #1d4ed8;">Lịch sử đặt vé</a>
    </nav>
</header>

<main class="result-container">
    <div class="status-card">
        
        <c:choose>
            <c:when test="${success}">
                <div class="icon-circle icon-success">✓</div>
                <h1 class="status-title title-success">Thanh toán hoàn tất!</h1>
                
                <div class="status-msg msg-success">
                    <b>${message}</b><br>
                    <span style="font-size: 14px; margin-top: 8px; display: inline-block;">
                        Thông tin vé và mã QR lên tàu đã được lưu an toàn trong tài khoản của bạn. Bạn có thể tra cứu và tải vé PDF bất cứ lúc nào.
                    </span>
                </div>

                <div class="btn-group">
                    <a href="${pageContext.request.contextPath}/my-bookings" class="btn btn-primary">
                        🎫 Xem vé điện tử của tôi ➔
                    </a>
                    <a href="${pageContext.request.contextPath}/home" class="btn btn-outline">
                        Về trang chủ
                    </a>
                </div>
            </c:when>
            
            <c:otherwise>
                <div class="icon-circle icon-fail">✗</div>
                <h1 class="status-title title-fail">Giao dịch thất bại</h1>
                
                <div class="status-msg msg-fail">
                    <b>${message}</b><br>
                    <span style="font-size: 14px; margin-top: 8px; display: inline-block; color: #991b1b;">
                        Đừng lo, chỗ ngồi của bạn vẫn đang được tạm giữ trong hệ thống. Vui lòng thử thanh toán lại bằng phương thức khác.
                    </span>
                </div>

                <div class="btn-group">
                    <a href="${pageContext.request.contextPath}/my-bookings" class="btn btn-retry">
                        🔄 Thử thanh toán lại
                    </a>
                    <a href="${pageContext.request.contextPath}/home" class="btn btn-outline">
                        Hủy và về trang chủ
                    </a>
                </div>
            </c:otherwise>
        </c:choose>

    </div>
</main>

</body>
</html>