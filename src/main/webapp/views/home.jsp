<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head><title>Trang chủ</title></head>
<body>
    <h2>Xin chào, ${sessionScope.account.userName}</h2>
    <p>roleid = ${sessionScope.account.roleid}</p>
    <a href="${pageContext.request.contextPath}/admin/categories">Quản lý danh mục (JPA)</a><br>
    <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
</body>
</html>
