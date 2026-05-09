<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <style>
        body { background: #f1f5f9; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding-bottom: 60px; }
        
        /* Header Đồng Bộ */
        .top-header { background: white; padding: 12px 24px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.05); position: sticky; top: 0; z-index: 1000; }
        .main-nav { display: flex; gap: 18px; align-items: center; }
        .main-nav a { text-decoration: none; color: #475569; font-weight: 600; font-size: 14px; transition: 0.2s; }
        .main-nav a:hover { color: #1d4ed8; }
        .nav-auth { border-left: 2px solid #e2e8f0; padding-left: 18px; display: flex; gap: 18px; align-items: center; }
        .nav-auth a.login-btn { color: #1d4ed8; font-weight: 600; text-decoration: none; font-size: 14px; }
        .nav-auth a.register-btn { background: #1d4ed8; color: white; padding: 8px 16px; border-radius: 8px; font-weight: 600; font-size: 14px; transition: 0.2s; text-decoration: none; }
        .nav-auth a.register-btn:hover { background: #1e40af; transform: translateY(-2px); box-shadow: 0 4px 10px rgba(29, 78, 216, 0.3); }

        /* Bố cục Split-Screen */
        .auth-container { max-width: 1000px; margin: 60px auto 0; background: white; border-radius: 24px; box-shadow: 0 20px 40px rgba(0,0,0,0.08); display: flex; overflow: hidden; min-height: 550px; }
        
        .auth-banner { flex: 1; background: linear-gradient(to bottom, rgba(29, 78, 216, 0.7), rgba(15, 23, 42, 0.8)), url('https://images.unsplash.com/photo-1474487548417-781cbc7149d4?q=80&w=1000&auto=format&fit=crop') center/cover; padding: 50px; display: flex; flex-direction: column; justify-content: center; color: white; }
        .auth-banner h2 { font-size: 36px; font-weight: 900; margin: 0 0 16px 0; line-height: 1.2; }
        .auth-banner p { font-size: 16px; opacity: 0.9; line-height: 1.6; }

        .auth-form-section { flex: 1; padding: 50px 60px; display: flex; flex-direction: column; justify-content: center; }
        .auth-form-section h1 { margin: 0 0 8px 0; font-size: 28px; color: #0f172a; font-weight: 800; }
        .auth-form-section .subtitle { color: #64748b; font-size: 15px; margin-bottom: 24px; }

        .form-group { margin-bottom: 20px; }
        .form-group label { display: block; font-size: 14px; font-weight: bold; color: #334155; margin-bottom: 8px; }
        .form-control { width: 100%; padding: 14px 16px; border: 1px solid #cbd5e1; border-radius: 12px; font-size: 15px; font-family: inherit; box-sizing: border-box; transition: 0.2s; outline: none; }
        .form-control:focus { border-color: #3b82f6; box-shadow: 0 0 0 4px rgba(59, 130, 246, 0.1); }
        
        /* Box chứa mật khẩu có icon mắt */
        .password-box { position: relative; }
        .toggle-password { position: absolute; right: 16px; top: 50%; transform: translateY(-50%); cursor: pointer; color: #94a3b8; font-size: 18px; user-select: none; }
        .toggle-password:hover { color: #1d4ed8; }

        .btn-submit { width: 100%; background: #1d4ed8; color: white; border: none; padding: 14px; border-radius: 12px; font-weight: 800; font-size: 16px; cursor: pointer; transition: 0.2s; margin-top: 10px; box-shadow: 0 4px 12px rgba(29, 78, 216, 0.3); }
        .btn-submit:hover { background: #1e40af; transform: translateY(-2px); }

        .auth-links { margin-top: 24px; text-align: center; font-size: 15px; color: #475569; }
        .auth-links a { color: #1d4ed8; font-weight: bold; text-decoration: none; }
        .auth-links a:hover { text-decoration: underline; }

        .demo-box { margin-top: 30px; background: #f8fafc; border: 1px dashed #cbd5e1; padding: 16px; border-radius: 12px; font-size: 13px; color: #475569; }

        @media (max-width: 800px) {
            .auth-container { flex-direction: column; margin: 20px; }
            .auth-banner { padding: 40px 30px; min-height: 200px; }
            .auth-form-section { padding: 40px 30px; }
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
            <a href="${pageContext.request.contextPath}/login" class="login-btn">Đăng nhập</a>
            <a href="${pageContext.request.contextPath}/register" class="register-btn">Đăng ký ngay</a>
        </div>
    </nav>
</header>

<main class="auth-container">
    <div class="auth-banner">
        <h2>Chào mừng trở lại!</h2>
        <p>Đăng nhập để trải nghiệm quy trình đặt vé siêu tốc, quản lý lịch sử chuyến đi dễ dàng và nhận ngay các ưu đãi đặc quyền dành riêng cho thành viên.</p>
    </div>

    <div class="auth-form-section">
        <h1>Đăng nhập</h1>
        <div class="subtitle">Truy cập vào tài khoản Vé Tàu VN của bạn</div>

        <c:if test="${not empty message}">
            <div style="background: #eff6ff; color: #1d4ed8; padding: 12px 16px; border-radius: 8px; margin-bottom: 20px; font-weight: 500; font-size: 14px;">
                ℹ️ ${message}
            </div>
        </c:if>
        <c:if test="${not empty error}">
            <div style="background: #fef2f2; color: #dc2626; padding: 12px 16px; border-radius: 8px; margin-bottom: 20px; font-weight: bold; border-left: 4px solid #ef4444; font-size: 14px;">
                ❌ ${error}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <input type="hidden" name="returnUrl" value="${returnUrl}">

            <div class="form-group">
                <label for="email">Địa chỉ Email</label>
                <input id="email" type="email" name="email" value="${emailValue}" class="form-control" placeholder="nguyenvana@example.com" required>
            </div>

            <div class="form-group">
                <label for="matKhau">Mật khẩu</label>
                <div class="password-box">
                    <input id="matKhau" type="password" name="matKhau" class="form-control" placeholder="Nhập mật khẩu" required>
                    <span class="toggle-password" onclick="togglePassword('matKhau', this)">👁️</span>
                </div>
            </div>

            <button type="submit" class="btn-submit">Đăng Nhập ➔</button>
        </form>

        <div class="auth-links">
            Chưa có tài khoản? <a href="${pageContext.request.contextPath}/register">Đăng ký ngay</a>
        </div>

        <div class="demo-box">
            <b>🔑 Tài khoản Demo (Chấm điểm):</b><br>
            Email: <code style="color: #1d4ed8;">nguyenvana@example.com</code><br>
            Mật khẩu: <code style="color: #1d4ed8;">123456</code>
        </div>
    </div>
</main>

<script>
    // Tích hợp Javascript nội bộ xử lý ẩn/hiện mật khẩu mượt mà
    function togglePassword(inputId, iconElement) {
        const input = document.getElementById(inputId);
        if (input.type === "password") {
            input.type = "text";
            iconElement.style.color = "#1d4ed8"; // Sáng lên khi hiện
        } else {
            input.type = "password";
            iconElement.style.color = "#94a3b8"; // Mờ đi khi ẩn
        }
    }
</script>
</body>
</html>