<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hệ thống Quản trị - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <style>
        body { 
            background: #0f172a; /* Nền tối chuyên nghiệp cho Admin */
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .login-card {
            background: #ffffff;
            width: 100%;
            max-width: 420px;
            padding: 40px;
            border-radius: 20px;
            box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
            text-align: center;
        }

        .admin-logo {
            font-size: 28px;
            font-weight: 900;
            color: #1e293b;
            letter-spacing: -1px;
            margin-bottom: 10px;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 8px;
        }
        .admin-logo span { color: #1d4ed8; }

        .login-title {
            font-size: 14px;
            color: #64748b;
            margin-bottom: 30px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        .form-group { text-align: left; margin-bottom: 20px; }
        .form-group label { display: block; font-size: 13px; font-weight: bold; color: #475569; margin-bottom: 8px; }
        
        .form-control {
            width: 100%;
            padding: 14px 16px;
            border: 2px solid #e2e8f0;
            border-radius: 10px;
            font-size: 15px;
            font-family: inherit;
            box-sizing: border-box;
            transition: 0.2s;
            outline: none;
            background: #f8fafc;
        }
        .form-control:focus {
            border-color: #3b82f6;
            background: white;
            box-shadow: 0 0 0 4px rgba(59, 130, 246, 0.1);
        }

        .btn-admin-login {
            width: 100%;
            background: #1e293b;
            color: white;
            border: none;
            padding: 16px;
            border-radius: 10px;
            font-weight: 800;
            font-size: 16px;
            cursor: pointer;
            transition: 0.2s;
            margin-top: 10px;
            display: flex;
            justify-content: center;
            align-items: center;
            gap: 8px;
        }
        .btn-admin-login:hover {
            background: #0f172a;
            transform: translateY(-2px);
            box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1);
        }

        .error-msg {
            background: #fef2f2;
            color: #dc2626;
            padding: 12px;
            border-radius: 8px;
            font-size: 14px;
            margin-bottom: 20px;
            font-weight: bold;
            border: 1px solid #fecaca;
        }

        .back-to-site {
            margin-top: 25px;
            display: inline-block;
            color: #64748b;
            text-decoration: none;
            font-size: 14px;
            font-weight: 500;
            transition: 0.2s;
        }
        .back-to-site:hover { color: #1d4ed8; text-decoration: underline; }
    </style>
</head>
<body>

    <div class="login-card">
        <div class="admin-logo">🚂 VETAU<span>VN</span></div>
        <div class="login-title">Hệ Thống Quản Trị Nội Bộ</div>

        <c:if test="${not empty error}">
            <div class="error-msg">⚠️ ${error}</div>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/login" method="post">
            <input type="hidden" name="returnUrl" value="${param.returnUrl}">
            
            <div class="form-group">
                <label>Email Admin</label>
                <input type="email" name="email" class="form-control" value="admin@vetau.vn" required>
            </div>

            <div class="form-group">
                <label>Mật khẩu hệ thống</label>
                <input type="password" name="password" class="form-control" value="admin123" required>
            </div>

            <button type="submit" class="btn-admin-login">Đăng Nhập ➔</button>
        </form>

        <a href="${pageContext.request.contextPath}/home" class="back-to-site">← Trở về trang dành cho Khách hàng</a>
    </div>

</body>
</html>