<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head><title>Sản phẩm</title></head>
<body>
    <h3 class="mb-3">Sản phẩm</h3>

    <div class="row row-cols-2 row-cols-md-3 g-3 mb-4">
        <c:forEach items="${listproduct}" var="product">
            <div class="col">
                <a class="text-decoration-none text-dark"
                   href="<c:url value='/product/detail?id=${product.productId}'/>">
                    <div class="card h-100">
                        <c:if test="${product.images.substring(0,5)=='https'}">
                            <c:url value="${product.images}" var="imgUrl"></c:url>
                        </c:if>
                        <c:if test="${product.images.substring(0,5)!='https'}">
                            <c:url value="/image?fname=${product.images}" var="imgUrl"></c:url>
                        </c:if>
                        <img src="${imgUrl}" class="card-img-top" style="height:180px;object-fit:cover">
                        <div class="card-body">
                            <div class="mb-1">${product.productname}</div>
                            <div class="text-muted small mb-1">${product.category.categoryname}</div>
                            <div class="fw-bold text-primary">
                                <fmt:formatNumber value="${product.price}" type="number" groupingUsed="true" /> đ
                            </div>
                        </div>
                    </div>
                </a>
            </div>
        </c:forEach>
        <c:if test="${empty listproduct}">
            <p class="text-muted">Chưa có sản phẩm nào.</p>
        </c:if>
    </div>

    <c:if test="${totalPages > 1}">
        <nav>
            <ul class="pagination justify-content-center">
                <c:forEach begin="1" end="${totalPages}" var="p">
                    <li class="page-item ${p == page ? 'active' : ''}">
                        <a class="page-link" href="<c:url value='/product?page=${p}'/>">${p}</a>
                    </li>
                </c:forEach>
            </ul>
        </nav>
    </c:if>
</body>
</html>
