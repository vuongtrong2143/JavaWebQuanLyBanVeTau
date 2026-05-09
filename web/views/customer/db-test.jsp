<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Kiểm tra kết nối database</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/main.css">
</head>
<body>
<main class="container narrow">
    <section class="card">
        <h1>Kiểm tra kết nối MSSQL</h1>

        <p class="${dbOk ? 'success' : 'alert'}">
            ${dbMessage}
        </p>

        <h2>Thông tin kiểm tra</h2>
        <table>
            <tr>
                <th>Database đang kết nối</th>
                <td>${dbName}</td>
            </tr>
            <tr>
                <th>JDBC URL</th>
                <td><code>${dbUrl}</code></td>
            </tr>
            <tr>
                <th>SQL Server</th>
                <td>${empty serverInfo ? 'Chưa lấy được vì kết nối thất bại' : serverInfo}</td>
            </tr>
            <tr>
                <th>Số dòng trong bảng GA</th>
                <td>${gaCount}</td>
            </tr>
        </table>

        <p>
            Nếu kết nối thất bại, hãy kiểm tra:
        </p>
        <ul>
            <li>SQL Server đang chạy chưa.</li>
            <li>SQL Server có bật TCP/IP và port 1433 chưa.</li>
            <li>Tên database trong <code>DBConnection.java</code> có đúng không.</li>
            <li>Tài khoản <code>sa</code> và mật khẩu có đúng không.</li>
            <li>Đã thêm file <code>mssql-jdbc-*.jar</code> vào Libraries của NetBeans chưa.</li>
        </ul>

        <p><a href="${pageContext.request.contextPath}/home">Quay lại trang chủ</a></p>
    </section>
</main>
</body>
</html>
