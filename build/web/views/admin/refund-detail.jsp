<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chi tiết hoàn tiền #${refund.hoanTienId} - Vé Tàu VN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/status.css">
    
    <style>
        :root {
            --sidebar-width: 260px;
            --primary-dark: #0f172a;
            --accent-blue: #3b82f6;
        }

        body {
            background: #f8fafc;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            margin: 0;
            display: flex;
        }

        .sidebar {
            width: var(--sidebar-width);
            background: var(--primary-dark);
            height: 100vh;
            position: fixed;
            color: white;
            padding: 20px 0;
            z-index: 100;
        }

        .sidebar-brand {
            padding: 0 24px 30px;
            font-size: 22px;
            font-weight: 900;
            letter-spacing: -1px;
        }

        .sidebar-brand span {
            color: var(--accent-blue);
        }

        .nav-menu {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        .nav-item a {
            display: flex;
            align-items: center;
            padding: 14px 24px;
            color: #94a3b8;
            text-decoration: none;
            transition: 0.2s;
            font-size: 15px;
            font-weight: 500;
        }

        .nav-item a:hover,
        .nav-item.active a {
            background: rgba(255,255,255,0.05);
            color: white;
            border-left: 4px solid var(--accent-blue);
        }

        .main-content {
            margin-left: var(--sidebar-width);
            flex: 1;
            padding: 30px;
            min-width: 0;
        }

        .page-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 24px;
            gap: 16px;
            flex-wrap: wrap;
        }

        .page-header h1 {
            margin: 0;
            font-size: 24px;
            font-weight: 900;
            color: #0f172a;
        }

        .page-header p {
            color: #64748b;
            margin-top: 4px;
            margin-bottom: 0;
        }

        .btn-back {
            background: white;
            color: #475569;
            border: 1px solid #cbd5e1;
            padding: 9px 16px;
            border-radius: 8px;
            font-weight: 800;
            text-decoration: none;
            transition: 0.2s;
        }

        .btn-back:hover {
            background: #f1f5f9;
        }

        .alert {
            padding: 12px 16px;
            border-radius: 10px;
            margin-bottom: 18px;
            font-weight: 700;
        }

        .alert-danger {
            background: #fee2e2;
            color: #991b1b;
            border: 1px solid #fecaca;
        }

        .detail-grid {
            display: grid;
            grid-template-columns: 2fr 1fr;
            gap: 24px;
            align-items: start;
        }

        .info-card {
            background: white;
            border-radius: 16px;
            padding: 24px;
            box-shadow: 0 4px 10px rgba(0,0,0,0.03);
            border: 1px solid #e2e8f0;
            margin-bottom: 24px;
        }

        .info-title {
            margin-top: 0;
            font-size: 18px;
            color: #0f172a;
            margin-bottom: 16px;
            padding-bottom: 12px;
            border-bottom: 2px solid #f1f5f9;
        }

        .info-row {
            display: flex;
            justify-content: space-between;
            gap: 16px;
            margin-bottom: 12px;
            font-size: 15px;
            border-bottom: 1px dashed #f1f5f9;
            padding-bottom: 8px;
        }

        .info-label {
            color: #64748b;
            font-weight: 700;
        }

        .info-value {
            color: #1e293b;
            font-weight: 800;
            text-align: right;
        }

        .status-pill {
            padding: 6px 12px;
            border-radius: 999px;
            font-size: 13px;
            font-weight: 900;
            display: inline-flex;
            align-items: center;
            gap: 4px;
        }

        .pill-pending {
            background: #ffedd5;
            color: #9a3412;
        }

        .pill-success {
            background: #dcfce7;
            color: #166534;
        }

        .pill-danger {
            background: #fee2e2;
            color: #991b1b;
        }

        .pill-muted {
            background: #e5e7eb;
            color: #374151;
            border: 1px solid #d1d5db;
        }

        .calc-box {
            background: #f8fafc;
            border: 1px dashed #cbd5e1;
            padding: 20px;
            border-radius: 12px;
            margin-top: 20px;
        }

        .refund-total {
            border-top: 2px solid #cbd5e1;
            margin-top: 12px;
            padding-top: 12px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 16px;
        }

        .refund-total .label {
            font-weight: 900;
            color: #0f172a;
            font-size: 18px;
        }

        .refund-total .value {
            font-weight: 900;
            color: #16a34a;
            font-size: 24px;
            white-space: nowrap;
        }

        .btn-action-group {
            display: flex;
            gap: 12px;
            margin-top: 24px;
        }

        .btn-action-group form {
            flex: 1;
        }

        .btn-action-group button {
            width: 100%;
            padding: 14px;
            border-radius: 10px;
            font-size: 15px;
            font-weight: 900;
            border: none;
            cursor: pointer;
            transition: 0.2s;
        }

        .btn-approve {
            background: #16a34a;
            color: white;
            box-shadow: 0 4px 12px rgba(22, 163, 74, 0.3);
        }

        .btn-approve:hover {
            background: #15803d;
            transform: translateY(-2px);
        }

        .btn-reject {
            background: #fef2f2;
            color: #dc2626;
            border: 1px solid #fecaca !important;
        }

        .btn-reject:hover {
            background: #fee2e2;
        }

        .processed-note {
            margin-top: 18px;
            padding: 14px 16px;
            border-radius: 12px;
            background: #f8fafc;
            border: 1px solid #e2e8f0;
            color: #475569;
            font-weight: 800;
        }

        @media (max-width: 980px) {
            .detail-grid {
                grid-template-columns: 1fr;
            }
        }
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
        <li class="nav-item"><a href="${pageContext.request.contextPath}/admin/bookings">📦 Quản lý Đơn hàng</a></li>
        <li class="nav-item active"><a href="${pageContext.request.contextPath}/admin/refunds">💸 Duyệt Hoàn tiền</a></li>
        <li class="nav-item" style="margin-top: 40px;"><a href="${pageContext.request.contextPath}/admin/logout" style="color: #fca5a5;">🚪 Đăng xuất</a></li>
    </ul>
</aside>

<main class="main-content">
    <jsp:include page="/views/common/flash-message.jsp"/>
    <div class="page-header">
        <div>
            <h1>Chi tiết yêu cầu hoàn tiền</h1>
            <p>
                ID hoàn tiền:
                <b style="font-family:monospace;color:#1d4ed8;">#${refund.hoanTienId}</b>
                · Mã vé:
                <b style="font-family:monospace;color:#1d4ed8;">${refund.maVe}</b>
            </p>
        </div>

        <a href="${pageContext.request.contextPath}/admin/refunds" class="btn-back">← Danh sách yêu cầu</a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">${error}</div>
    </c:if>

    <div class="detail-grid">
        <div>
            <div class="info-card">
                <h3 class="info-title">🧾 Thông tin hoàn tiền</h3>

                <div class="info-row">
                    <span class="info-label">Trạng thái hiện tại:</span>
                    <span class="info-value">
                        <c:choose>
                            <c:when test="${refund.trangThai eq 'Chờ xử lý' || refund.trangThai eq 'Pending'}">
                                <span class="status-pill pill-pending">⏳ Chờ xử lý</span>
                            </c:when>
                            <c:when test="${refund.trangThai eq 'Hoàn tất'}">
                                <span class="status-pill pill-success">✓ Hoàn tất</span>
                            </c:when>
                            <c:when test="${refund.trangThai eq 'Từ chối'}">
                                <span class="status-pill pill-danger">✗ Từ chối</span>
                            </c:when>
                            <c:otherwise>
                                <span class="status-pill pill-muted">${refund.trangThai}</span>
                            </c:otherwise>
                        </c:choose>
                    </span>
                </div>

                <div class="info-row">
                    <span class="info-label">Thời gian yêu cầu:</span>
                    <span class="info-value">${refund.thoiGianYeuCauText}</span>
                </div>

                <div class="info-row">
                    <span class="info-label">Thời gian hoàn tất:</span>
                    <span class="info-value">${refund.thoiGianHoanTatText}</span>
                </div>

                <div class="info-row">
                    <span class="info-label">Mã giao dịch hoàn:</span>
                    <span class="info-value">${empty refund.maGiaoDichHoan ? '-' : refund.maGiaoDichHoan}</span>
                </div>

                <div class="info-row">
                    <span class="info-label">Lý do khách hàng:</span>
                    <span class="info-value" style="font-style:italic;color:#dc2626;">
                        "${empty refund.lyDoTraVe ? 'Không cung cấp lý do' : refund.lyDoTraVe}"
                    </span>
                </div>

                <div class="calc-box">
                    <div class="info-row" style="border:none;color:#dc2626;">
                        <span class="info-label">Phí đổi/trả cố định:</span>
                        <span class="info-value">${empty refund.phiDoiTra ? '0' : refund.phiDoiTra} đ</span>
                    </div>

                    <div class="info-row" style="border:none;color:#dc2626;">
                        <span class="info-label">Tỷ lệ khấu trừ:</span>
                        <span class="info-value">${empty refund.tyLeKhauTru ? '0' : refund.tyLeKhauTru}%</span>
                    </div>

                    <div class="refund-total">
                        <span class="label">Thực tế hoàn lại:</span>
                        <span class="value">${refund.soTienHoanText}</span>
                    </div>
                </div>

                <c:if test="${refund.trangThai eq 'Chờ xử lý' || refund.trangThai eq 'Pending'}">
                    <div class="btn-action-group">
                        <form action="${pageContext.request.contextPath}/admin/refunds" method="post">
                            <input type="hidden" name="action" value="reject">
                            <input type="hidden" name="id" value="${refund.hoanTienId}">
                            <button type="submit"
                                    class="btn-reject"
                                    onclick="return confirm('Bạn chắc chắn TỪ CHỐI yêu cầu hoàn tiền này chứ?');">
                                ❌ Từ chối yêu cầu
                            </button>
                        </form>

                        <form action="${pageContext.request.contextPath}/admin/refunds" method="post">
                            <input type="hidden" name="action" value="approve">
                            <input type="hidden" name="id" value="${refund.hoanTienId}">
                            <button type="submit"
                                    class="btn-approve"
                                    onclick="return confirm('Xác nhận DUYỆT hoàn tiền? Hệ thống sẽ cập nhật trạng thái.');">
                                ✅ Xác nhận hoàn tiền
                            </button>
                        </form>
                    </div>

                    <p style="margin-top:12px;font-size:13px;color:#64748b;text-align:center;">
                        Việc xác nhận hoàn tiền sẽ cập nhật bảng HOAN_TIEN và ghi nhận trạng thái xử lý.
                    </p>
                </c:if>

                <c:if test="${refund.trangThai ne 'Chờ xử lý' && refund.trangThai ne 'Pending'}">
                    <div class="processed-note">
                        Yêu cầu này đã được xử lý. Không thể duyệt hoặc từ chối lại.
                    </div>
                </c:if>
            </div>
        </div>

        <div>
            <div class="info-card" style="background:#f8fafc;">
                <h3 class="info-title" style="border-bottom-color:#e2e8f0;">🔍 Thông tin tham chiếu</h3>

                <div class="info-row" style="border-color:#e2e8f0;">
                    <span class="info-label">Mã đặt chỗ:</span>
                    <span class="info-value" style="font-family:monospace;color:#1d4ed8;">#${refund.maDatCho}</span>
                </div>

                <div class="info-row" style="border-color:#e2e8f0;">
                    <span class="info-label">Mã vé:</span>
                    <span class="info-value" style="font-family:monospace;color:#1d4ed8;">${refund.maVe}</span>
                </div>

                <div class="info-row" style="border-color:#e2e8f0;">
                    <span class="info-label">Hành khách:</span>
                    <span class="info-value">${refund.tenHanhKhach}</span>
                </div>

                <div class="info-row" style="border-color:#e2e8f0;">
                    <span class="info-label">Số giấy tờ:</span>
                    <span class="info-value">${refund.soGiayTo}</span>
                </div>
            </div>

            <div class="info-card">
                <h3 class="info-title">👤 Khách hàng</h3>

                <div class="info-row">
                    <span class="info-label">Họ tên:</span>
                    <span class="info-value">${refund.tenKhachHang}</span>
                </div>

                <div class="info-row">
                    <span class="info-label">Email:</span>
                    <span class="info-value">${refund.emailKhachHang}</span>
                </div>

                <div class="info-row">
                    <span class="info-label">Số điện thoại:</span>
                    <span class="info-value">${refund.soDienThoaiKhachHang}</span>
                </div>
            </div>

            <div class="info-card">
                <h3 class="info-title">💳 Thanh toán gốc</h3>

                <div class="info-row">
                    <span class="info-label">Phương thức:</span>
                    <span class="info-value">${refund.phuongThucThanhToan}</span>
                </div>

                <div class="info-row">
                    <span class="info-label">Mã giao dịch gốc:</span>
                    <span class="info-value">${refund.maGiaoDichGoc}</span>
                </div>
            </div>
        </div>
    </div>
</main>
</body>
</html>
