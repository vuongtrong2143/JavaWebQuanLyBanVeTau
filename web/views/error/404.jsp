<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Không tìm thấy trang - Vé Tàu VN</title>
    <style>
        body {
            margin: 0;
            background: #f8fafc;
            font-family: 'Segoe UI', Tahoma, sans-serif;
            color: #0f172a;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 24px;
        }

        .error-card {
            background: white;
            border: 1px solid #e2e8f0;
            border-radius: 22px;
            padding: 40px;
            max-width: 560px;
            text-align: center;
            box-shadow: 0 20px 45px rgba(15, 23, 42, 0.08);
        }

        .code {
            font-size: 76px;
            font-weight: 900;
            color: #1d4ed8;
            line-height: 1;
        }

        h1 {
            margin: 18px 0 10px;
            font-size: 26px;
        }

        p {
            color: #64748b;
            font-size: 16px;
            margin-bottom: 26px;
        }

        .btn {
            display: inline-block;
            padding: 12px 20px;
            border-radius: 10px;
            background: #1d4ed8;
            color: white;
            text-decoration: none;
            font-weight: 800;
        }

        .btn:hover {
            background: #1e40af;
        }
    </style>
</head>
<body>
<div class="error-card">
    <div class="code">404</div>
    <h1>Không tìm thấy trang</h1>
    <p>Trang bạn đang truy cập không tồn tại hoặc đã được di chuyển.</p>
    <a class="btn" href="${pageContext.request.contextPath}/home">← Quay về trang chủ</a>
</div>
</body>
</html>