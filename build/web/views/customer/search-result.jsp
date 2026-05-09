<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Kết quả tìm chuyến</title>
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
    <link href="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/css/select2.min.css" rel="stylesheet" />
    
    <style>
        body { background: #f1f5f9; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; margin: 0; }
        .container-rs { max-width: 1000px; margin: 30px auto; padding: 0 20px; }
        
        /* 1. Thanh tìm kiếm nhanh (Sticky Search) */
        .sticky-search { background: white; padding: 15px 0; border-bottom: 1px solid #e2e8f0; position: sticky; top: 0; z-index: 100; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }
        .search-mini-form { display: flex; gap: 15px; align-items: flex-end; justify-content: center; flex-wrap: wrap; }
        .mini-group { display: flex; flex-direction: column; gap: 5px; }
        .mini-group label { font-size: 13px; font-weight: bold; color: #64748b; }
        
        /* Ép chiều cao input và Select2 trong thanh mini bằng nhau */
        .mini-group input[type="date"], .btn-mini-search { height: 40px; border: 1px solid #cbd5e1; border-radius: 6px; padding: 0 12px; font-family: inherit; box-sizing: border-box; }
        .btn-mini-search { background: #1d4ed8; color: white; border: none; padding: 0 24px; font-weight: bold; cursor: pointer; transition: 0.2s; }
        .btn-mini-search:hover { background: #1e40af; }
        
        .select2-container .select2-selection--single { height: 40px !important; border: 1px solid #cbd5e1 !important; border-radius: 6px !important; display: flex !important; align-items: center !important; }
        .select2-container--default .select2-selection--single .select2-selection__rendered { line-height: normal !important; padding-left: 12px !important; color: #0f172a !important; font-size: 15px; }
        .select2-container--default .select2-selection--single .select2-selection__arrow { height: 38px !important; right: 6px !important; }

        /* 2. Train Card (Thẻ chuyến tàu) */
        .train-card { background: white; border-radius: 12px; margin-bottom: 20px; padding: 24px; display: flex; align-items: center; box-shadow: 0 2px 8px rgba(0,0,0,0.08); transition: 0.3s; border: 1px solid transparent; gap: 20px; }
        .train-card:hover { transform: translateY(-3px); box-shadow: 0 12px 20px rgba(0,0,0,0.1); border-color: #3b82f6; }

        .train-info { flex: 2; border-right: 1px dashed #cbd5e1; padding-right: 24px; }
        .train-id { font-size: 18px; font-weight: 800; color: #1d4ed8; margin-bottom: 12px; display: block; }
        
        .route-timeline { display: flex; align-items: center; gap: 15px; justify-content: space-between; margin: 20px 0; }
        .time-point { text-align: center; min-width: 80px; }
        .time-val { font-size: 22px; font-weight: 800; color: #0f172a; }
        .ga-name { font-size: 14px; color: #64748b; margin-top: 4px; font-weight: 600; }
        
        .duration-line { flex-grow: 1; height: 2px; background: #cbd5e1; position: relative; display: flex; justify-content: center; }
        .duration-line::after { content: '▶'; position: absolute; right: -8px; top: -11px; color: #cbd5e1; font-size: 18px; }
        .duration-text { background: white; padding: 0 8px; color: #94a3b8; font-size: 12px; margin-top: -8px; }

        .price-info { flex: 1; text-align: right; display: flex; flex-direction: column; justify-content: center; }
        .price-label { font-size: 13px; color: #64748b; }
        .price-val { font-size: 24px; font-weight: 800; color: #ea580c; display: block; margin: 4px 0 8px 0; }
        .seat-count { font-size: 14px; color: #16a34a; font-weight: 600; display: inline-flex; align-items: center; gap: 4px; justify-content: flex-end; }
        .seat-count::before { content: '•'; font-size: 20px; }
        
        .btn-select { background: #1d4ed8; color: white; border: none; padding: 12px 24px; border-radius: 8px; font-weight: bold; cursor: pointer; margin-top: 16px; width: 100%; transition: 0.2s; font-size: 15px; }
        .btn-select:hover { background: #1e40af; }

        /* 3. Empty State */
        .empty-state { text-align: center; padding: 60px 20px; background: white; border-radius: 16px; margin-top: 40px; box-shadow: 0 2px 8px rgba(0,0,0,0.05); }
        .empty-state img { width: 120px; opacity: 0.6; margin-bottom: 20px; }
        .empty-state h3 { color: #0f172a; font-size: 20px; margin-bottom: 8px; }

        @media (max-width: 768px) {
            .train-card { flex-direction: column; text-align: center; }
            .train-info { border-right: none; border-bottom: 1px dashed #cbd5e1; padding-right: 0; padding-bottom: 20px; width: 100%; }
            .price-info { text-align: center; width: 100%; align-items: center; }
            .seat-count { justify-content: center; }
            .search-mini-form { flex-direction: column; align-items: stretch; }
            .mini-group select { width: 100% !important; }
            .select2-container { width: 100% !important; }
        }
    </style>
</head>
<body>

    <header style="background: white; padding: 16px 24px; display: flex; justify-content: space-between; align-items: center; box-shadow: 0 1px 3px rgba(0,0,0,0.1);">
        <div style="font-size: 24px; font-weight: 800; color: #1d4ed8;">🚂 VETAU VN</div>
        <nav style="display: flex; gap: 20px;">
            <a href="${pageContext.request.contextPath}/home" style="text-decoration: none; color: #475569; font-weight: 600;">Trang chủ</a>
            <a href="${pageContext.request.contextPath}/search-train" style="text-decoration: none; color: #1d4ed8; font-weight: 600;">Tìm chuyến</a>
            <a href="${pageContext.request.contextPath}/ticket-check" style="text-decoration: none; color: #475569; font-weight: 600;">Tra cứu vé</a>
        </nav>
    </header>

    <div class="sticky-search">
        <div class="container-rs">
            <form action="${pageContext.request.contextPath}/search-train" method="get" class="search-mini-form">
                <div class="mini-group" style="flex: 1; min-width: 200px;">
                    <label>Ga đi</label>
                    <select name="gaDiId" class="select2-ui" required>
                        <option value="">-- Chọn Ga Đi --</option>
                        <c:forEach var="ga" items="${gaList}">
                            <option value="${ga.id}" ${gaDiId == ga.id ? 'selected' : ''}>${ga.tenGa}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mini-group" style="flex: 1; min-width: 200px;">
                    <label>Ga đến</label>
                    <select name="gaDenId" class="select2-ui" required>
                        <option value="">-- Chọn Ga Đến --</option>
                        <c:forEach var="ga" items="${gaList}">
                            <option value="${ga.id}" ${gaDenId == ga.id ? 'selected' : ''}>${ga.tenGa}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="mini-group" style="flex: 1; min-width: 150px;">
                    <label>Ngày đi</label>
                    <input type="date" name="ngayDi" value="${ngayDi}" required>
                </div>
                <button type="submit" class="btn-mini-search">🔍 Tìm lại</button>
            </form>
        </div>
    </div>

    <c:if test="${not empty message}">
        <div class="container-rs">
            <div style="background: #fef2f2; color: #dc2626; padding: 16px; border-radius: 8px; border-left: 4px solid #ef4444; font-weight: 500;">
                ${message}
            </div>
        </div>
    </c:if>

    <main class="container-rs">
        <h2 style="font-size: 20px; margin-bottom: 24px; color: #334155;">
            Kết quả tìm kiếm ngày: <b style="color: #0f172a;">${ngayDi}</b>
            <c:if test="${not empty totalResults}">
                <span style="font-size: 14px; font-weight: normal; color: #64748b; margin-left: 10px;">(Tìm thấy ${totalResults} chuyến)</span>
            </c:if>
        </h2>

        <c:choose>
            <c:when test="${empty results}">
                <div class="empty-state">
                    <img src="https://cdn-icons-png.flaticon.com/512/6134/6134065.png" alt="No Train">
                    <h3>Không tìm thấy chuyến tàu nào phù hợp!</h3>
                    <p style="color: #64748b; margin-top: 8px;">Vui lòng kiểm tra lại ngày đi hoặc thử tìm kiếm một chặng đường khác.</p>
                </div>
            </c:when>
            
            <c:otherwise>
                <c:forEach var="item" items="${results}">
                    <div class="train-card">
                        <div class="train-info">
                            <span class="train-id">🚂 ${item.tenTau} (${item.maTau})</span>
                            
                            <div class="route-timeline">
                                <div class="time-point">
                                    <div class="time-val">${not empty item.thoiGianDiText ? item.thoiGianDiText : fn:substring(item.thoiGianDi, 11, 16)}</div>
                                    <div class="ga-name">${item.gaDiTen}</div>
                                </div>
                                
                                <div class="duration-line">
                                    <span class="duration-text">${item.maChuyen}</span>
                                </div>
                                
                                <div class="time-point">
                                    <div class="time-val">${not empty item.thoiGianDenText ? item.thoiGianDenText : fn:substring(item.thoiGianDen, 11, 16)}</div>
                                    <div class="ga-name">${item.gaDenTen}</div>
                                </div>
                            </div>
                        </div>
                        
                        <div class="price-info">
                            <span class="price-label">Giá vé chỉ từ</span>
                            
                            <span class="price-val">
                                <c:choose>
                                    <c:when test="${not empty item.giaThapNhatText}">
                                        ${item.giaThapNhatText}
                                    </c:when>
                                    <c:otherwise>
                                        <fmt:formatNumber value="${item.giaThapNhat}" pattern="#,###"/> đ
                                    </c:otherwise>
                                </c:choose>
                            </span>
                            
                            <c:set var="gheCon" value="${not empty item.soGheConLai ? item.soGheConLai : (item.soGheHoatDong - item.soGheDaBiChiem)}" />
                            <span class="seat-count">Còn ${gheCon} chỗ trống</span>
                            
                            <form action="${pageContext.request.contextPath}/seat-selection" method="get">
                                <input type="hidden" name="chuyenTauId" value="${item.chuyenTauId}">
                                <input type="hidden" name="gaDiId" value="${gaDiId}">
                                <input type="hidden" name="gaDenId" value="${gaDenId}">
                                <button type="submit" class="btn-select">CHỌN CHỖ</button>
                            </form>
                        </div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </main>

    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-rc.0/dist/js/select2.min.js"></script>
    <script>
        $(document).ready(function() {
            // Khởi tạo select2 cho thanh tìm kiếm nhanh
            $('.select2-ui').select2();
        });
    </script>
</body>
</html>