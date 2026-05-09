<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Tìm chuyến tàu - Vé Tàu VN</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    
    <style>
        body { background: #f1f5f9; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; }
        
        /* Header Navigation đồng bộ */
        .main-nav { display: flex; gap: 15px; align-items: center; flex-wrap: wrap; }
        .main-nav a { text-decoration: none; color: #475569; font-weight: 600; font-size: 15px; transition: 0.2s; }
        .main-nav a:hover { color: #1d4ed8; }
        .nav-auth { border-left: 2px solid #e2e8f0; padding-left: 15px; display: flex; gap: 15px; }
        .nav-auth a { color: #1d4ed8; }
        .nav-auth a.logout-btn { color: #dc2626; }

        /* Banner nhỏ */
        .page-header { background: #1e293b; color: white; padding: 40px 20px; text-align: center; }
        .page-header h1 { margin: 0 0 10px 0; font-size: 28px; font-weight: 800; }
        .page-header p { margin: 0; color: #94a3b8; font-size: 15px; }

        /* Form Container */
        .search-container { max-width: 800px; margin: -30px auto 40px auto; background: white; padding: 30px; border-radius: 16px; box-shadow: 0 10px 25px rgba(0,0,0,0.1); position: relative; z-index: 10; }
        
        .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-bottom: 24px; }
        .form-group { display: flex; flex-direction: column; gap: 8px; }
        .form-group.full-width { grid-column: span 2; }
        .form-label { font-size: 14px; font-weight: 600; color: #334155; }
        
        /* Select2 & Input Styling */
        .form-group input[type="date"] { height: 46px; border: 1px solid #cbd5e1; border-radius: 8px; padding: 0 16px; font-size: 16px; font-family: inherit; box-sizing: border-box; width: 100%; }
        .select2-container .select2-selection--single { height: 46px !important; border: 1px solid #cbd5e1 !important; border-radius: 8px !important; display: flex !important; align-items: center !important; }
        .select2-container--default .select2-selection--single .select2-selection__rendered { line-height: normal !important; padding-left: 16px !important; color: #0f172a !important; font-size: 16px; }
        .select2-container--default .select2-selection--single .select2-selection__arrow { height: 44px !important; right: 10px !important; }

        .btn-submit { background: #1d4ed8; color: white; border: none; padding: 14px; border-radius: 8px; font-weight: 800; font-size: 16px; cursor: pointer; transition: 0.2s; width: 100%; display: flex; justify-content: center; align-items: center; gap: 8px; }
        .btn-submit:hover { background: #1e40af; transform: translateY(-2px); box-shadow: 0 4px 12px rgba(29, 78, 216, 0.3); }

        /* Info Cards */
        .info-section { max-width: 1000px; margin: 0 auto 60px auto; padding: 0 20px; display: grid; grid-template-columns: repeat(auto-fit, minmax(300px, 1fr)); gap: 24px; }
        .info-card { background: white; padding: 24px; border-radius: 12px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); border-top: 4px solid #1d4ed8; }
        .info-card h3 { margin-top: 0; color: #0f172a; font-size: 18px; margin-bottom: 12px; }
        .info-card p { margin: 0; color: #64748b; font-size: 14px; line-height: 1.5; }

        @media (max-width: 768px) {
            .form-grid { grid-template-columns: 1fr; }
            .form-group.full-width { grid-column: span 1; }
        }
    </style>
</head>
<body>

<header style="background: white; padding: 16px 24px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 1px 3px rgba(0,0,0,0.1); position: sticky; top: 0; z-index: 1000;">
    <div style="font-size: 24px; font-weight: 800; color: #1d4ed8;">🚂 VETAU VN</div>
    <nav class="main-nav">
        <a href="${pageContext.request.contextPath}/home">Trang chủ</a>
        <a href="${pageContext.request.contextPath}/search-train" style="color: #1d4ed8;">Tìm chuyến</a>
        <a href="${pageContext.request.contextPath}/ticket-check">Tra cứu vé</a>
        <div class="nav-auth">
            <c:choose>
                <c:when test="${not empty sessionScope.currentCustomer}">
                    <a href="${pageContext.request.contextPath}/profile">Tài khoản</a>
                    <a href="${pageContext.request.contextPath}/logout" class="logout-btn">Đăng xuất</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                    <a href="${pageContext.request.contextPath}/register">Đăng ký</a>
                </c:otherwise>
            </c:choose>
        </div>
    </nav>
</header>

<div class="page-header">
    <h1>Tra cứu hành trình</h1>
    <p>Nhập thông tin Ga đi, Ga đến và Ngày khởi hành để tìm chuyến tàu phù hợp nhất.</p>
</div>

<main class="search-container">
    <c:if test="${not empty dbWarning}">
        <div style="background: #fffbeb; color: #b45309; padding: 16px; border-radius: 8px; margin-bottom: 20px; font-weight: 500;">
            ⚠️ ${dbWarning}
        </div>
    </c:if>

    <c:if test="${not empty searchError}">
        <div style="background: #fef2f2; color: #dc2626; padding: 16px; border-radius: 8px; margin-bottom: 20px; font-weight: 500;">
            ❌ ${searchError}
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/search-train" method="get">
        <div class="form-grid">
            <div class="form-group">
                <label class="form-label" for="gaDiId">Ga đi</label>
                <select id="gaDiId" name="gaDiId" class="select2-ui" required>
                    <option value="">-- Gõ để tìm ga đi --</option>
                    <c:forEach var="ga" items="${gaList}">
                        <option value="${ga.id}" ${gaDiIdValue == ga.id ? 'selected' : ''}>
                            ${ga.tenGa} (${ga.maGa})
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group">
                <label class="form-label" for="gaDenId">Ga đến</label>
                <select id="gaDenId" name="gaDenId" class="select2-ui" required>
                    <option value="">-- Gõ để tìm ga đến --</option>
                    <c:forEach var="ga" items="${gaList}">
                        <option value="${ga.id}" ${gaDenIdValue == ga.id ? 'selected' : ''}>
                            ${ga.tenGa} (${ga.maGa})
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="form-group full-width">
                <label class="form-label" for="ngayDi">Ngày đi</label>
                <input id="ngayDi" type="date" name="ngayDi" value="${ngayDiValue}" required>
            </div>
        </div>

        <button type="submit" class="btn-submit">🔍 TÌM CHUYẾN NGAY</button>
    </form>
</main>

<div class="info-section">
    <div class="info-card">
        <h3>🕒 Tra cứu giờ tàu</h3>
        <p>Hệ thống tự động sử dụng lịch dừng thực tế để xác định chính xác giờ đi và giờ đến cho từng chặng bạn chọn.</p>
    </div>
    <div class="info-card">
        <h3>💰 Tra cứu giá vé</h3>
        <p>Giá vé được tính toán minh bạch dựa trên bảng giá chuẩn theo chặng, kết hợp với loại toa và tầng áp dụng.</p>
    </div>
    <div class="info-card">
        <h3>💺 Chọn ghế linh hoạt</h3>
        <p>Bạn sẽ được chuyển sang giao diện sơ đồ đoàn tàu trực quan để tự do chọn toa và vị trí ngồi ưng ý sau khi tìm chuyến.</p>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
<script>
    $(document).ready(function() {
        $('.select2-ui').select2();

        var dtToday = new Date();
        var month = dtToday.getMonth() + 1;
        var day = dtToday.getDate();
        var year = dtToday.getFullYear();
        if(month < 10) month = '0' + month.toString();
        if(day < 10) day = '0' + day.toString();
        var minDate = year + '-' + month + '-' + day;
        $('#ngayDi').attr('min', minDate);

        $('form').on('submit', function(e) {
            if ($('#gaDiId').val() === $('#gaDenId').val()) {
                e.preventDefault();
                alert('Vui lòng chọn Ga đi và Ga đến khác nhau!');
            }
        });
    });
</script>

</body>
</html>