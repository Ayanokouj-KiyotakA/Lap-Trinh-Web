<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Xác nhận OTP</title></head>
<body>
    <h4 class="mb-3 text-center">Xác nhận mã OTP</h4>
    <p class="text-muted text-center">
        Mã OTP đã được gửi tới email <strong>${email}</strong>
    </p>

    <c:if test="${alert != null}">
        <div class="alert alert-danger py-2">${alert}</div>
    </c:if>
    <c:if test="${success != null}">
        <div class="alert alert-success py-2">${success}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/verify-otp" method="post" novalidate>
        <div class="mb-3">
            <label class="form-label">Mã OTP (6 số)</label>
            <input type="text" class="form-control" name="otp" maxlength="6" required autofocus>
        </div>
        <button type="submit" name="action" value="verify" class="btn btn-primary w-100 mb-2">
            Xác nhận kích hoạt
        </button>
        <button type="submit" name="action" value="resend" class="btn btn-outline-secondary w-100">
            Gửi lại mã OTP
        </button>
    </form>
</body>
</html>
