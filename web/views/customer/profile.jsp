<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hồ sơ cá nhân - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <style>
        body { background: #f8fafc; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; padding-bottom: 60px; }
        
        /* Header Đồng Bộ */
        .top-header { background: white; padding: 12px 24px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 2px 10px rgba(0,0,0,0.05); position: sticky; top: 0; z-index: 1000; }
        .main-nav { display: flex; gap: 18px; align-items: center; }
        .main-nav a { text-decoration: none; color: #475569; font-weight: 600; font-size: 14px; transition: 0.2s; }
        .main-nav a:hover { color: #1d4ed8; }
        .nav-auth { border-left: 2px solid #e2e8f0; padding-left: 18px; display: flex; gap: 18px; align-items: center; }
        .nav-auth a { color: #1d4ed8; font-weight: 600; text-decoration: none; font-size: 14px;}
        .nav-auth a.logout-btn { color: #ef4444; }

        .container-profile { max-width: 1000px; margin: 40px auto; padding: 0 20px; }

        /* Alerts */
        .alert-box { padding: 16px; border-radius: 12px; margin-bottom: 24px; font-weight: 500; font-size: 15px; display: flex; align-items: center; gap: 8px; }
        .alert-success { background: #dcfce7; color: #166534; border: 1px solid #bbf7d0; }
        .alert-error { background: #fef2f2; color: #dc2626; border: 1px solid #fecaca; }

        /* Thẻ Thành Viên (Membership Card) */
        .member-card { background: linear-gradient(135deg, #1e40af, #3b82f6); border-radius: 20px; padding: 40px; color: white; display: flex; align-items: center; gap: 24px; box-shadow: 0 10px 25px rgba(29, 78, 216, 0.2); margin-bottom: 30px; position: relative; overflow: hidden; }
        .member-card::after { content: ''; position: absolute; right: -20px; top: -50px; width: 200px; height: 200px; border-radius: 50%; background: rgba(255,255,255,0.1); pointer-events: none; }
        .avatar-circle { width: 80px; height: 80px; border-radius: 50%; background: white; color: #1d4ed8; display: flex; justify-content: center; align-items: center; font-size: 32px; font-weight: 900; box-shadow: 0 4px 10px rgba(0,0,0,0.1); }
        .member-info h1 { margin: 0 0 8px 0; font-size: 28px; font-weight: 800; }
        .member-info p { margin: 0; font-size: 15px; opacity: 0.9; }
        .member-badge { background: rgba(255,255,255,0.2); padding: 4px 12px; border-radius: 20px; font-size: 13px; font-weight: bold; text-transform: uppercase; margin-top: 8px; display: inline-block; border: 1px solid rgba(255,255,255,0.3); }

        /* Bố cục 2 cột cho các form */
        .profile-grid { display: grid; grid-template-columns: 3fr 2fr; gap: 30px; }
        
        .form-card { background: white; border-radius: 16px; padding: 30px; box-shadow: 0 4px 15px rgba(0,0,0,0.03); border: 1px solid #e2e8f0; }
        .form-card h2 { margin-top: 0; font-size: 20px; color: #0f172a; margin-bottom: 24px; padding-bottom: 12px; border-bottom: 2px solid #f1f5f9; display: flex; align-items: center; gap: 8px; }

        /* Form elements */
        .form-row { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
        .form-group { margin-bottom: 16px; }
        .form-group.full-width { grid-column: span 2; }
        .form-group label { display: block; font-size: 14px; font-weight: bold; color: #334155; margin-bottom: 8px; }
        
        .form-control { width: 100%; padding: 12px 16px; border: 1px solid #cbd5e1; border-radius: 10px; font-size: 15px; font-family: inherit; box-sizing: border-box; transition: 0.2s; outline: none; }
        .form-control:focus { border-color: #3b82f6; box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1); }
        .form-control:disabled { background: #f1f5f9; color: #94a3b8; cursor: not-allowed; }

        .btn-submit { background: #1d4ed8; color: white; border: none; padding: 14px 24px; border-radius: 10px; font-weight: 800; font-size: 15px; cursor: pointer; transition: 0.2s; width: 100%; margin-top: 10px; box-shadow: 0 4px 12px rgba(29, 78, 216, 0.3); }
        .btn-submit:hover { background: #1e40af; transform: translateY(-2px); }
        
        .btn-warning { background: #ea580c; box-shadow: 0 4px 12px rgba(234, 88, 12, 0.3); }
        .btn-warning:hover { background: #c2410c; }

        @media (max-width: 800px) {
            .profile-grid { grid-template-columns: 1fr; }
            .form-row { grid-template-columns: 1fr; }
            .form-group.full-width { grid-column: span 1; }
            .member-card { flex-direction: column; text-align: center; padding: 30px 20px; }
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
        <a href="${pageContext.request.contextPath}/promotion">Khuyến mãi</a>
        <a href="${pageContext.request.contextPath}/guide">Hướng dẫn</a>
        <div class="nav-auth">
            <a href="${pageContext.request.contextPath}/profile" style="color: #1d4ed8;">Tài khoản</a>
            <a href="${pageContext.request.contextPath}/my-bookings">Lịch sử đặt vé</a>
            <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
        </div>
    </nav>
</header>

<main class="container-profile">
    <jsp:include page="/views/common/flash-message.jsp"/>
    <c:if test="${not empty success}">
        <div class="alert-box alert-success">✅ ${success}</div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="alert-box alert-error">❌ ${error}</div>
    </c:if>

    <div class="member-card">
        <div class="avatar-circle">
            ${fn:substring(khachHang.hoTen, 0, 1)}
        </div>
        <div class="member-info">
            <h1>${khachHang.hoTen}</h1>
            <p>📧 ${khachHang.email}</p>
            <div class="member-badge">⭐ Hội viên Thân thiết</div>
        </div>
    </div>

    <div class="profile-grid">
        <div class="form-card">
            <h2>📝 Thông tin Cá nhân</h2>
            <form action="${pageContext.request.contextPath}/profile" method="post">
                
                <div class="form-group">
                    <label>Địa chỉ Email (Tài khoản)</label>
                    <input type="email" value="${khachHang.email}" class="form-control" disabled>
                    <span style="font-size: 12px; color: #64748b; margin-top: 4px; display: block;">Email được dùng để đăng nhập nên không thể thay đổi.</span>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="hoTen">Họ và tên</label>
                        <input id="hoTen" type="text" name="hoTen" value="${khachHang.hoTen}" class="form-control" required>
                    </div>
                    <div class="form-group">
                        <label for="soDienThoai">Số điện thoại</label>
                        <input id="soDienThoai" type="text" name="soDienThoai" value="${khachHang.soDienThoai}" class="form-control">
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label for="ngaySinh">Ngày sinh</label>
                        <input id="ngaySinh" type="date" name="ngaySinh" value="${khachHang.ngaySinh}" class="form-control">
                    </div>
                    <div class="form-group">
                        <label for="gioiTinh">Giới tính</label>
                        <select id="gioiTinh" name="gioiTinh" class="form-control">
                            <option value="">-- Chọn --</option>
                            <option value="Nam" ${khachHang.gioiTinh == 'Nam' ? 'selected' : ''}>Nam</option>
                            <option value="Nữ" ${khachHang.gioiTinh == 'Nữ' ? 'selected' : ''}>Nữ</option>
                            <option value="Khác" ${khachHang.gioiTinh == 'Khác' ? 'selected' : ''}>Khác</option>
                        </select>
                    </div>
                </div>

                <div class="form-group">
                    <label for="diaChi">Địa chỉ thường trú</label>
                    <textarea id="diaChi" name="diaChi" rows="2" class="form-control" placeholder="Nhập địa chỉ của bạn...">${khachHang.diaChi}</textarea>
                </div>

                <button type="submit" class="btn-submit">💾 Lưu Thay Đổi</button>
            </form>
        </div>

        <div class="form-card">
            <h2>🔒 Đổi Mật Khẩu</h2>
            <form action="${pageContext.request.contextPath}/change-password" method="post">
                <div class="form-group">
                    <label for="matKhauCu">Mật khẩu hiện tại</label>
                    <input id="matKhauCu" type="password" name="matKhauCu" class="form-control" placeholder="Nhập mật khẩu cũ" required>
                </div>

                <div class="form-group">
                    <label for="matKhauMoi">Mật khẩu mới</label>
                    <input id="matKhauMoi" type="password" name="matKhauMoi" class="form-control" minlength="6" placeholder="Tối thiểu 6 ký tự" required>
                </div>

                <div class="form-group">
                    <label for="xacNhanMatKhau">Xác nhận mật khẩu mới</label>
                    <input id="xacNhanMatKhau" type="password" name="xacNhanMatKhau" class="form-control" minlength="6" placeholder="Nhập lại mật khẩu mới" required>
                </div>

                <button type="submit" class="btn-submit btn-warning">🔄 Cập nhật Mật khẩu</button>
            </form>
            
            <div style="margin-top: 30px; padding-top: 20px; border-top: 1px dashed #cbd5e1;">
                <p style="margin: 0 0 12px 0; font-size: 14px; font-weight: bold; color: #64748b;">Lối tắt hữu ích:</p>
                <a href="${pageContext.request.contextPath}/my-bookings" style="display: block; color: #1d4ed8; text-decoration: none; margin-bottom: 8px; font-weight: 500;">➔ Xem lại Lịch sử Đặt vé của bạn</a>
                <a href="${pageContext.request.contextPath}/search-train" style="display: block; color: #1d4ed8; text-decoration: none; font-weight: 500;">➔ Tìm chuyến tàu mới</a>
            </div>
        </div>
    </div>
</main>

</body>
</html>