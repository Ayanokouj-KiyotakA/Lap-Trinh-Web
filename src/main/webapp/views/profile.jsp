<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<html>
<head><title>Hồ sơ cá nhân</title></head>
<body>
    <div class="row">
        <div class="col-md-8 col-lg-6">
            <h3 class="mb-3">Hồ sơ cá nhân</h3>

            <c:if test="${success != null}">
                <div class="alert alert-success py-2">${success}</div>
            </c:if>

            <div class="card">
                <div class="card-body">
                    <div class="text-center mb-4">
                        <c:choose>
                            <c:when test="${not empty user.avatar}">
                                <c:if test="${user.avatar.substring(0,5)=='https'}">
                                    <c:url value="${user.avatar}" var="avatarUrl"/>
                                </c:if>
                                <c:if test="${user.avatar.substring(0,5)!='https'}">
                                    <c:url value="/image?fname=${user.avatar}" var="avatarUrl"/>
                                </c:if>
                                <img src="${avatarUrl}" class="rounded-circle border"
                                     width="120" height="120" style="object-fit:cover">
                            </c:when>
                            <c:otherwise>
                                <div class="rounded-circle bg-secondary text-white d-inline-flex align-items-center justify-content-center"
                                     style="width:120px;height:120px;font-size:2.2rem">
                                    ${fn:toUpperCase(fn:substring(user.userName,0,1))}
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <dl class="row mb-4">
                        <dt class="col-sm-4">Tài khoản</dt>
                        <dd class="col-sm-8">${user.userName}</dd>
                        <dt class="col-sm-4">Email</dt>
                        <dd class="col-sm-8">${user.email}</dd>
                    </dl>

                    <form action="${pageContext.request.contextPath}/profile" method="post"
                          enctype="multipart/form-data" novalidate>
                        <div class="mb-3">
                            <label class="form-label">Họ tên</label>
                            <input type="text" name="fullname" value="${user.fullName}"
                                   class="form-control ${errors.fullname != null ? 'is-invalid' : ''}">
                            <c:if test="${errors.fullname != null}">
                                <div class="invalid-feedback">${errors.fullname}</div>
                            </c:if>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Số điện thoại</label>
                            <input type="text" name="phone" value="${user.phone}"
                                   class="form-control ${errors.phone != null ? 'is-invalid' : ''}">
                            <c:if test="${errors.phone != null}">
                                <div class="invalid-feedback">${errors.phone}</div>
                            </c:if>
                        </div>

                        <div class="mb-3">
                            <label class="form-label">Ảnh đại diện mới (bỏ trống nếu không đổi)</label>
                            <input type="file" name="avatar1" class="form-control">
                        </div>

                        <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
