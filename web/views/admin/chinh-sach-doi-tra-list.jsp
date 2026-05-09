<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chính Sách Đổi Trả - Vé Tàu VN</title>
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
        .page-header { display: flex; justify-content: space-between; align-items: flex-end; margin-bottom: 24px; }
        .page-title h1 { margin: 0 0 4px 0; font-size: 24px; font-weight: 800; color: #0f172a; }

        .btn-add { background: var(--accent-blue); color: white; border: none; padding: 10px 20px; border-radius: 8px; font-weight: 600; text-decoration: none; transition: 0.2s; box-shadow: 0 4px 6px rgba(59, 130, 246, 0.3); }
        .btn-add:hover { background: #2563eb; transform: translateY(-2px); }

        .content-card { background: white; border-radius: 16px; box-shadow: 0 4px 10px rgba(0,0,0,0.03); border: 1px solid #e2e8f0; overflow: hidden; }
        .admin-table { width: 100%; border-collapse: collapse; text-align: left; }
        .admin-table th { background: #f8fafc; padding: 12px; border-bottom: 1px solid #e2e8f0; font-size: 13px; font-weight: 700; color: #64748b; text-transform: uppercase; }
        .admin-table td { padding: 14px 12px; border-bottom: 1px solid #f1f5f9; font-size: 13px; color: #1e293b; vertical-align: middle; }
        
        .btn-action { padding: 4px 8px; border-radius: 4px; text-decoration: none; font-size: 12px; font-weight: bold; }
        .btn-edit { background: #eff6ff; color: #1d4ed8; }
        .btn-delete { background: #fef2f2; color: #dc2626; margin-left: 4px; }

        .status-pill { padding: 4px 10px; border-radius: 20px; font-size: 11px; font-weight: bold; }
        .pill-active { background: #dcfce7; color: #16a34a; }
        .pill-inactive { background: #fee2e2; color: #dc2626; }
    </style>
</head>
<body>

    <aside class="sidebar">
        <div class="sidebar-brand">VETAU<span>ADMIN</span></div>
        <ul class="nav-menu">
            <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/dashboard">📊 Bảng điều khiển</a></li>
            <li class="nav-item active"><a href="${pageContext.request.contextPath}/admin/chinh-sach-doi-tra">📜 Chính sách Đổi Trả</a></li>
            <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/refunds">💸 Duyệt Hoàn tiền</a></li>
            <li class="nav-item" style="margin-top: 40px;"><a href="${pageContext.request.contextPath}/admin/logout" style="color: #fca5a5;">🚪 Đăng xuất</a></li>
        </ul>
    </aside>

    <main class="main-content">
        <div class="page-header">
            <div class="page-title">
                <h1>Chính Sách Đổi & Trả Vé</h1>
                <p>Quy định mức khấu trừ dựa trên thời gian hủy vé và loại đơn hàng.</p>
            </div>
            <a href="${pageContext.request.contextPath}/admin/chinh-sach-doi-tra?action=add" class="btn-add">➕ Thêm Quy Định</a>
        </div>

        <c:if test="${param.msg == 'saved'}">
            <div style="background: #dcfce7; color: #166534; padding: 12px; border-radius: 8px; margin-bottom: 20px; font-weight: bold; border: 1px solid #bbf7d0;">✅ Lưu chính sách thành công!</div>
        </c:if>

        <div class="content-card">
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>Tên chính sách</th>
                        <th>Phạm vi áp dụng</th>
                        <th>Khung thời gian (h)</th>
                        <th>Khấu trừ (%)</th>
                        <th>Phí cố định</th>
                        <th>Đổi / Trả</th>
                        <th>Trạng thái</th>
                        <th>Thao tác</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${listChinhSach}">
                        <tr>
                            <td><b style="color: #0f172a;">${item.tenChinhSach}</b></td>
                            <td>Loại: ${item.loaiDonHangApDung}<br><span style="font-size: 11px; color: #64748b;">Chiều: ${item.chieuTauApDung}</span></td>
                            <td>Từ: ${empty item.truocKhoiHanhTuGio ? '0' : item.truocKhoiHanhTuGio}h<br>Đến: ${empty item.truocKhoiHanhDenGio ? '∞' : item.truocKhoiHanhDenGio}h</td>
                            <td style="color: #ea580c; font-weight: 800;">${item.tyLeKhauTru}%</td>
                            <td><fmt:formatNumber value="${item.phiDoiCoDinh}" pattern="#,###"/> đ</td>
                            <td>
                                ${item.choPhepDoi ? '✅ Đổi' : '❌'}<br>
                                ${item.choPhepTra ? '✅ Trả' : '❌'}
                            </td>
                            <td>
                                <span class="status-pill ${item.trangThai == 'Hoạt động' ? 'pill-active' : 'pill-inactive'}">${item.trangThai}</span>
                            </td>
                            <td>
                                <a href="${pageContext.request.contextPath}/admin/chinh-sach-doi-tra?action=edit&id=${item.id}" class="btn-action btn-edit">Sửa</a>
                                <a href="${pageContext.request.contextPath}/admin/chinh-sach-doi-tra?action=delete&id=${item.id}" onclick="return confirm('Ngưng áp dụng quy định này?')" class="btn-action btn-delete">Xóa</a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty listChinhSach}">
                        <tr><td colspan="8" style="text-align: center; padding: 40px; color: #64748b;">Chưa có chính sách đổi trả nào được thiết lập.</td></tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </main>
</body>
</html>