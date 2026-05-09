<%-- 
    Document   : booking-detail
    Created on : May 8, 2026, 3:17:05 PM
    Author     : trong
--%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết Đơn hàng #${datCho.maDatCho} - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <style>
        :root { --sidebar-width: 260px; --primary-dark: #0f172a; --accent-blue: #3b82f6; }
        body { background: #f8fafc; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; display: flex; }
        
        .sidebar { width: var(--sidebar-width); background: var(--primary-dark); height: 100vh; position: fixed; color: white; padding: 20px 0; z-index: 100;}
        .sidebar-brand { padding: 0 24px 30px; font-size: 22px; font-weight: 900; letter-spacing: -1px; }
        .sidebar-brand span { color: var(--accent-blue); }
        .nav-menu { list-style: none; padding: 0; margin: 0; }
        .nav-item a { display: flex; align-items: center; padding: 14px 24px; color: #94a3b8; text-decoration: none; transition: 0.2s; font-size: 15px; font-weight: 500; }
        .nav-item a:hover, .nav-item.active a { background: rgba(255,255,255,0.05); color: white; border-left: 4px solid var(--accent-blue); }

        .main-content { margin-left: var(--sidebar-width); flex: 1; padding: 30px; }
        
        .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
        .btn-back { background: white; color: #475569; border: 1px solid #cbd5e1; padding: 8px 16px; border-radius: 8px; font-weight: bold; text-decoration: none; transition: 0.2s; }
        .btn-back:hover { background: #f1f5f9; }

        .detail-grid { display: grid; grid-template-columns: 1fr 2fr; gap: 24px; align-items: start; }
        
        .info-card { background: white; border-radius: 16px; padding: 24px; box-shadow: 0 4px 10px rgba(0,0,0,0.03); border: 1px solid #e2e8f0; margin-bottom: 24px; }
        .info-title { margin-top: 0; font-size: 18px; color: #0f172a; margin-bottom: 16px; padding-bottom: 12px; border-bottom: 2px solid #f1f5f9; }
        
        .info-row { display: flex; justify-content: space-between; margin-bottom: 12px; font-size: 14px; }
        .info-label { color: #64748b; font-weight: 600; }
        .info-value { color: #1e293b; font-weight: 700; text-align: right; }

        .admin-table { width: 100%; border-collapse: collapse; text-align: left; }
        .admin-table th { background: #f8fafc; padding: 12px 16px; font-size: 13px; font-weight: 700; color: #64748b; border-bottom: 1px solid #e2e8f0; }
        .admin-table td { padding: 12px 16px; border-bottom: 1px solid #f1f5f9; font-size: 14px; color: #1e293b; }

        .btn-danger { background: #fef2f2; color: #dc2626; border: 1px solid #fecaca; padding: 12px; border-radius: 8px; font-weight: bold; width: 100%; cursor: pointer; transition: 0.2s; }
        .btn-danger:hover { background: #fee2e2; }
        
        .status-pill { padding: 4px 8px; border-radius: 4px; font-size: 12px; font-weight: bold; }
        .pill-success { background: #dcfce7; color: #166534; }
        .pill-danger { background: #fee2e2; color: #dc2626; }
    </style>
</head>
<body>

    <aside class="sidebar">
        <div class="sidebar-brand">🚂 VETAU<span>ADMIN</span></div>
        <ul class="nav-menu">
            <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/dashboard">📊 Bảng điều khiển</a></li>
            <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/tau">🚆 Quản lý Tàu</a></li>
            <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/ga">🚉 Quản lý Nhà Ga</a></li>
            <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/chuyen-tau">🗓️ Quản lý Lịch trình</a></li>
            <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/lich-dung">📍 Quản lý Lịch Dừng</a></li>
            <li class="nav-item active"><a href="${pageContext.request.contextPath}/admin/bookings">📦 Quản lý Đơn hàng</a></li>
        </ul>
    </aside>

    <main class="main-content">
        <div class="page-header">
            <div>
                <h1 style="margin: 0; font-size: 24px; font-weight: 800; color: #0f172a;">Chi tiết Đơn hàng: <span style="color: #1d4ed8; font-family: monospace;">#${datCho.maDatCho}</span></h1>
            </div>
            <a href="${pageContext.request.contextPath}/admin/bookings" class="btn-back">← Quay lại danh sách</a>
        </div>

        <div class="detail-grid">
            <div>
                <div class="info-card">
                    <h3 class="info-title">🧾 Thông tin Giao dịch</h3>
                    <div class="info-row">
                        <span class="info-label">Ngày tạo đơn:</span>
                        <span class="info-value">${datCho.ngayDat}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Loại đơn:</span>
                        <span class="info-value">${datCho.loaiDonHang}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Hành trình:</span>
                        <span class="info-value">${datCho.loaiHanhTrinh}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Trạng thái đơn:</span>
                        <span class="info-value" style="color: ${datCho.trangThai == 'Đang giữ chỗ' ? '#d97706' : (datCho.trangThai == 'Hoàn tất' ? '#16a34a' : '#dc2626')};">${datCho.trangThai}</span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">Hạn thanh toán:</span>
                        <span class="info-value" style="color: #dc2626;">${datCho.thoiGianHetHan}</span>
                    </div>
                    <div class="info-row" style="margin-top: 16px; padding-top: 16px; border-top: 1px dashed #cbd5e1; font-size: 16px;">
                        <span class="info-label">Tổng thanh toán:</span>
                        <span class="info-value" style="color: #ea580c; font-size: 20px;"><fmt:formatNumber value="${datCho.tongThanhToan}" pattern="#,###"/> đ</span>
                    </div>
                </div>

                <c:if test="${datCho.trangThai == 'Đang giữ chỗ'}">
                    <form action="${pageContext.request.contextPath}/admin/bookings" method="post" style="margin-top: 20px;">
                        <input type="hidden" name="action" value="cancel">
                        <input type="hidden" name="id" value="${datCho.id}">
                        <button type="submit" class="btn-danger" onclick="return confirm('Bạn có chắc chắn muốn hủy đơn hàng này? Khách hàng sẽ bị mất chỗ ngồi.')">
                            ⚠️ Hủy khẩn cấp đơn hàng này
                        </button>
                    </form>
                </c:if>
            </div>

            <div class="info-card">
                <h3 class="info-title">🎫 Danh sách Vé trong đơn</h3>
                
                <table class="admin-table">
                    <thead>
                        <tr>
                            <th>Mã Vé</th>
                            <th>Chuyến Tàu ID</th>
                            <th>Khách ID</th>
                            <th>Trạng Thái</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="ve" items="${listVe}">
                            <tr>
                                <td style="font-family: monospace; font-weight: bold; color: #1d4ed8;">${ve.maVe}</td>
                                <td>${ve.chuyenTauId}</td>
                                <td>HK-${ve.hanhKhachId}</td>
                                <td>
                                    <span class="status-pill ${ve.trangThai == 'Hợp lệ' ? 'pill-success' : 'pill-danger'}">
                                        ${ve.trangThai}
                                    </span>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty listVe}">
                            <tr><td colspan="4" style="text-align: center; color: #94a3b8; padding: 20px;">Đơn hàng này chưa phát hành vé (hoặc chưa thanh toán).</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</body>
</html>