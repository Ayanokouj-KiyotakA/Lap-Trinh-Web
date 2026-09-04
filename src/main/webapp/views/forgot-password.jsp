<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Quên mật khẩu</title></head>
<body>
    <h4 class="mb-3 text-center">Quên mật khẩu</h4>
    <p class="text-muted text-center">Nhập email đã đăng ký để nhận mã OTP đặt lại mật khẩu</p>

    <c:if test="${alert != null}">
        <div class="alert alert-danger py-2">${alert}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/forgot-password" method="post" novalidate>
        <div class="mb-3">
            <label class="form-label">Email</label>
            <input type="email" class="form-control" name="email" value="${email}" required autofocus>
        </div>
        <button type="submit" class="btn btn-primary w-100">Gửi mã OTP</button>
    </form>

    <p class="text-center mt-3 mb-0">
        <a href="${pageContext.request.contextPath}/login">Quay lại đăng nhập</a>
    </p>
</body>
</html>
