<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<title><sitemesh:write property='title' /> - Bài tập 04</title>
	<link rel="stylesheet"
		href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
	<sitemesh:write property='head' />
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark mb-4">
		<div class="container">
			<a class="navbar-brand" href="<c:url value='/waiting'/>">Bài tập 04</a>
			<button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav">
				<span class="navbar-toggler-icon"></span>
			</button>
			<div class="collapse navbar-collapse" id="mainNav">
				<ul class="navbar-nav me-auto">
					<c:if test="${sessionScope.account != null}">
						<c:choose>
							<c:when test="${sessionScope.account.roleid == 1}">
								<li class="nav-item">
									<a class="nav-link" href="<c:url value='/admin/home'/>">Trang chủ Admin</a>
								</li>
								<li class="nav-item">
									<a class="nav-link" href="<c:url value='/admin/categories'/>">Quản lý danh mục</a>
								</li>
								<li class="nav-item">
									<a class="nav-link" href="<c:url value='/admin/products'/>">Quản lý sản phẩm</a>
								</li>
							</c:when>
							<c:otherwise>
								<li class="nav-item">
									<a class="nav-link" href="<c:url value='/home'/>">Trang chủ</a>
								</li>
							</c:otherwise>
						</c:choose>
						<li class="nav-item">
							<a class="nav-link" href="<c:url value='/profile'/>">Hồ sơ cá nhân</a>
						</li>
					</c:if>
				</ul>
				<ul class="navbar-nav">
					<c:if test="${sessionScope.account != null}">
						<li class="nav-item">
							<span class="navbar-text text-light me-3">
								Xin chào, <strong>${sessionScope.account.userName}</strong>
							</span>
						</li>
						<li class="nav-item">
							<a class="nav-link" href="<c:url value='/logout'/>">Đăng xuất</a>
						</li>
					</c:if>
				</ul>
			</div>
		</div>
	</nav>

	<div class="container pb-5">
		<sitemesh:write property='body' />
	</div>

	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
