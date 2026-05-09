<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${not empty sessionScope.flashMessage}">
    <c:choose>
        <c:when test="${sessionScope.flashType eq 'success'}">
            <div style="padding: 14px 16px; background: #dcfce7; color: #166534; border: 1px solid #bbf7d0; border-radius: 12px; margin-bottom: 18px; font-weight: 700;">
                ✅ ${sessionScope.flashMessage}
            </div>
        </c:when>

        <c:when test="${sessionScope.flashType eq 'warning'}">
            <div style="padding: 14px 16px; background: #fffbeb; color: #92400e; border: 1px solid #fde68a; border-radius: 12px; margin-bottom: 18px; font-weight: 700;">
                ⚠️ ${sessionScope.flashMessage}
            </div>
        </c:when>

        <c:when test="${sessionScope.flashType eq 'error'}">
            <div style="padding: 14px 16px; background: #fee2e2; color: #991b1b; border: 1px solid #fecaca; border-radius: 12px; margin-bottom: 18px; font-weight: 700;">
                ❌ ${sessionScope.flashMessage}
            </div>
        </c:when>

        <c:otherwise>
            <div style="padding: 14px 16px; background: #eff6ff; color: #1e3a8a; border: 1px solid #bfdbfe; border-radius: 12px; margin-bottom: 18px; font-weight: 700;">
                ℹ️ ${sessionScope.flashMessage}
            </div>
        </c:otherwise>
    </c:choose>

    <c:remove var="flashMessage" scope="session"/>
    <c:remove var="flashType" scope="session"/>
</c:if>