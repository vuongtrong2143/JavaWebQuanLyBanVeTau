<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký tài khoản - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <style>
        body { background: #f8fafc; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding-bottom: 60px; }
        
        /* Header Đồng Bộ */
        .top-header { background: white; padding: 12px 24px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.05); position: sticky; top: 0; z-index: 1000; }
        .main-nav { display: flex; gap: 18px; align-items: center; }
        .main-nav a { text-decoration: none; color: #475569; font-weight: 600; font-size: 14px; transition: 0.2s; }
        .main-nav a:hover { color: #1d4ed8; }
        .nav-auth { border-left: 2px solid #e2e8f0; padding-left: 18px; display: flex; gap: 18px; align-items: center; }
        .nav-auth a.login-btn { color: #1d4ed8; font-weight: 600; text-decoration: none; font-size: 14px; }
        .nav-auth a.register-btn { background: #1d4ed8; color: white; padding: 8px 16px; border-radius: 8px; font-weight: 600; font-size: 14px; transition: 0.2s; text-decoration: none; }
        .nav-auth a.register-btn:hover { background: #1e40af; transform: translateY(-2px); box-shadow: 0 4px 10px rgba(29, 78, 216, 0.3); }

        /* Bố cục Split-Screen đảo ngược */
        .auth-container { max-width: 1100px; margin: 50px auto 0; background: white; border-radius: 24px; box-shadow: 0 20px 40px rgba(0,0,0,0.08); display: flex; overflow: hidden; min-height: 600px; }
        
        .auth-banner { flex: 4; background: linear-gradient(to bottom, rgba(234, 88, 12, 0.6), rgba(15, 23, 42, 0.9)), url('https://images.unsplash.com/photo-1515162816999-a0c47dc192f7?q=80&w=1000&auto=format&fit=crop') center/cover; padding: 50px; display: flex; flex-direction: column; justify-content: center; color: white; }
        .auth-banner h2 { font-size: 36px; font-weight: 900; margin: 0 0 16px 0; line-height: 1.2; }
        .auth-banner p { font-size: 16px; opacity: 0.9; line-height: 1.6; }

        .auth-form-section { flex: 5; padding: 40px 60px; display: flex; flex-direction: column; justify-content: center; }
        .auth-form-section h1 { margin: 0 0 8px 0; font-size: 28px; color: #0f172a; font-weight: 800; }
        .auth-form-section .subtitle { color: #64748b; font-size: 15px; margin-bottom: 24px; }

        /* Form dạng lưới cho đăng ký */
        .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
        .form-group { margin-bottom: 16px; }
        .form-group.full-width { grid-column: span 2; }
        .form-group label { display: block; font-size: 13px; font-weight: bold; color: #334155; margin-bottom: 6px; }
        .form-control { width: 100%; padding: 12px 16px; border: 1px solid #cbd5e1; border-radius: 10px; font-size: 14px; font-family: inherit; box-sizing: border-box; transition: 0.2s; outline: none; }
        .form-control:focus { border-color: #ea580c; box-shadow: 0 0 0 4px rgba(234, 88, 12, 0.1); }
        
        .password-box { position: relative; }
        .toggle-password { position: absolute; right: 14px; top: 50%; transform: translateY(-50%); cursor: pointer; color: #94a3b8; font-size: 18px; user-select: none; }
        .toggle-password:hover { color: #ea580c; }

        .btn-submit { width: 100%; background: #ea580c; color: white; border: none; padding: 14px; border-radius: 10px; font-weight: 800; font-size: 16px; cursor: pointer; transition: 0.2s; margin-top: 10px; box-shadow: 0 4px 12px rgba(234, 88, 12, 0.3); }
        .btn-submit:hover { background: #c2410c; transform: translateY(-2px); }

        .auth-links { margin-top: 24px; text-align: center; font-size: 15px; color: #475569; }
        .auth-links a { color: #ea580c; font-weight: bold; text-decoration: none; }
        .auth-links a:hover { text-decoration: underline; }

        @media (max-width: 800px) {
            .auth-container { flex-direction: column; margin: 20px; }
            .auth-banner { padding: 40px 30px; min-height: 200px; }
            .auth-form-section { padding: 40px 30px; }
            .form-grid { grid-template-columns: 1fr; }
            .form-group.full-width { grid-column: span 1; }
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
        <h2>Bắt đầu hành trình!</h2>
        <p>Tham gia cộng đồng thành viên Vé Tàu VN ngay hôm nay để tận hưởng khả năng quản lý vé tập trung và không bỏ lỡ bất kỳ chương trình khuyến mãi nào.</p>
    </div>

    <div class="auth-form-section">
        <h1>Tạo tài khoản mới</h1>
        <div class="subtitle">Vui lòng điền đầy đủ thông tin bên dưới</div>

        <c:if test="${not empty error}">
            <div style="background: #fef2f2; color: #dc2626; padding: 12px 16px; border-radius: 8px; margin-bottom: 20px; font-weight: bold; border-left: 4px solid #ef4444; font-size: 14px;">
                ❌ ${error}
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post">
            <div class="form-grid">
                
                <div class="form-group full-width">
                    <label for="hoTen">Họ và tên</label>
                    <input id="hoTen" type="text" name="hoTen" value="${hoTenValue}" class="form-control" placeholder="Ví dụ: Nguyễn Văn A" required>
                </div>

                <div class="form-group">
                    <label for="email">Địa chỉ Email</label>
                    <input id="email" type="email" name="email" value="${emailValue}" class="form-control" placeholder="Email của bạn" required>
                </div>

                <div class="form-group">
                    <label for="soDienThoai">Số điện thoại</label>
                    <input id="soDienThoai" type="text" name="soDienThoai" value="${soDienThoaiValue}" class="form-control" placeholder="Ví dụ: 0901234567" required>
                </div>

                <div class="form-group">
                    <label for="matKhau">Mật khẩu</label>
                    <div class="password-box">
                        <input id="matKhau" type="password" name="matKhau" class="form-control" minlength="6" placeholder="Tối thiểu 6 ký tự" required>
                        <span class="toggle-password" onclick="togglePassword('matKhau', this)">👁️</span>
                    </div>
                </div>

                <div class="form-group">
                    <label for="xacNhanMatKhau">Xác nhận mật khẩu</label>
                    <div class="password-box">
                        <input id="xacNhanMatKhau" type="password" name="xacNhanMatKhau" class="form-control" minlength="6" placeholder="Nhập lại mật khẩu" required>
                        <span class="toggle-password" onclick="togglePassword('xacNhanMatKhau', this)">👁️</span>
                    </div>
                </div>

            </div>

            <button type="submit" class="btn-submit">Hoàn Tất Đăng Ký ➔</button>
        </form>

        <div class="auth-links">
            Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập tại đây</a>
        </div>
    </div>
</main>

<script>
    function togglePassword(inputId, iconElement) {
        const input = document.getElementById(inputId);
        if (input.type === "password") {
            input.type = "text";
            iconElement.style.color = "#ea580c"; 
        } else {
            input.type = "password";
            iconElement.style.color = "#94a3b8"; 
        }
    }
</script>
</body>
</html>