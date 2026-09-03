<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Đăng nhập</title></head>
<body>
    <h4 class="mb-3 text-center">Đăng nhập</h4>

    <c:if test="${alert != null}">
        <div class="alert alert-danger py-2">${alert}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/login" method="post" novalidate>
        <div class="mb-3">
            <label class="form-label">Tài khoản</label>
            <input type="text" class="form-control" name="username" required autofocus>
        </div>
        <div class="mb-3">
            <label class="form-label">Mật khẩu</label>
            <input type="password" class="form-control" name="password" required>
        </div>
        <div class="mb-3 form-check">
            <input type="checkbox" class="form-check-input" name="remember" id="remember">
            <label class="form-check-label" for="remember">Nhớ tôi (Cookie)</label>
        </div>
        <button type="submit" class="btn btn-primary w-100">Đăng nhập</button>
    </form>
</body>
</html>
