<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Đăng ký tài khoản</title></head>
<body>
    <h4 class="mb-3 text-center">Đăng ký tài khoản</h4>

    <c:if test="${alert != null}">
        <div class="alert alert-danger py-2">${alert}</div>
    </c:if>

    <form action="${pageContext.request.contextPath}/register" method="post" novalidate>
        <div class="mb-3">
            <label class="form-label">Tài khoản</label>
            <input type="text" class="form-control ${errors.username != null ? 'is-invalid' : ''}"
                   name="username" value="${username}" required autofocus>
            <c:if test="${errors.username != null}">
                <div class="invalid-feedback">${errors.username}</div>
            </c:if>
        </div>

        <div class="mb-3">
            <label class="form-label">Email</label>
            <input type="email" class="form-control ${errors.email != null ? 'is-invalid' : ''}"
                   name="email" value="${email}" required>
            <c:if test="${errors.email != null}">
                <div class="invalid-feedback">${errors.email}</div>
            </c:if>
        </div>

        <div class="mb-3">
            <label class="form-label">Họ tên</label>
            <input type="text" class="form-control" name="fullname" value="${fullname}">
        </div>

        <div class="mb-3">
            <label class="form-label">Số điện thoại</label>
            <input type="text" class="form-control" name="phone" value="${phone}">
        </div>

        <div class="mb-3">
            <label class="form-label">Mật khẩu</label>
            <input type="password" class="form-control ${errors.password != null ? 'is-invalid' : ''}"
                   name="password" required>
            <c:if test="${errors.password != null}">
                <div class="invalid-feedback">${errors.password}</div>
            </c:if>
        </div>

        <div class="mb-3">
            <label class="form-label">Nhập lại mật khẩu</label>
            <input type="password" class="form-control ${errors.confirmPassword != null ? 'is-invalid' : ''}"
                   name="confirmPassword" required>
            <c:if test="${errors.confirmPassword != null}">
                <div class="invalid-feedback">${errors.confirmPassword}</div>
            </c:if>
        </div>

        <button type="submit" class="btn btn-primary w-100">Đăng ký</button>
    </form>

    <p class="text-center mt-3 mb-0">
        <a href="${pageContext.request.contextPath}/login">Đã có tài khoản? Đăng nhập</a>
    </p>
</body>
</html>
