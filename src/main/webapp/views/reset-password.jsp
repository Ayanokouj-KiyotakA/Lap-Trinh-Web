<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Đặt lại mật khẩu</title></head>
<body>
    <h4 class="mb-3 text-center">Đặt lại mật khẩu</h4>
    <p class="text-muted text-center">
        Mã OTP đã được gửi tới email <strong>${email}</strong>
    </p>

    <c:if test="${alert != null}">
        <div class="alert alert-danger py-2">${alert}</div>
    </c:if>
    <c:if test="${success != null}">
        <div class="alert alert-success py-2">${success}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/reset-password" method="post" novalidate>
        <div class="mb-3">
            <label class="form-label">Mã OTP (6 số)</label>
            <input type="text" class="form-control" name="otp" maxlength="6" required autofocus>
        </div>
        <div class="mb-3">
            <label class="form-label">Mật khẩu mới</label>
            <input type="password" class="form-control" name="newPassword" required>
        </div>
        <div class="mb-3">
            <label class="form-label">Nhập lại mật khẩu mới</label>
            <input type="password" class="form-control" name="confirmPassword" required>
        </div>
        <button type="submit" name="action" value="reset" class="btn btn-primary w-100 mb-2">
            Đặt lại mật khẩu
        </button>
        <button type="submit" name="action" value="resend" class="btn btn-outline-secondary w-100">
            Gửi lại mã OTP
        </button>
    </form>
</body>
</html>
