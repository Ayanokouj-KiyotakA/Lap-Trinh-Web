<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Đăng nhập</title></head>
<body>
    <h2>Đăng nhập</h2>

    <c:if test="${alert != null}">
        <p style="color:red">${alert}</p>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post">
        Tài khoản: <input type="text" name="username"><br>
        Mật khẩu: <input type="password" name="password"><br>
        <input type="checkbox" name="remember"> Nhớ tôi (Cookie)<br>
        <button type="submit">Đăng nhập</button>
    </form>
</body>
</html>
