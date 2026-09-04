<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head><title>${product.productname}</title></head>
<body>
    <p class="mb-3">
        <a href="<c:url value='/product'/>">&larr; Quay lại danh sách sản phẩm</a>
    </p>

    <div class="row bg-white border rounded p-4">
        <div class="col-md-5 mb-3 mb-md-0">
            <c:if test="${product.images.substring(0,5)=='https'}">
                <c:url value="${product.images}" var="imgUrl"></c:url>
            </c:if>
            <c:if test="${product.images.substring(0,5)!='https'}">
                <c:url value="/image?fname=${product.images}" var="imgUrl"></c:url>
            </c:if>
            <img src="${imgUrl}" class="img-fluid rounded" style="max-height:360px;object-fit:cover">
        </div>
        <div class="col-md-7">
            <h3>${product.productname}</h3>
            <p class="text-muted">Danh mục: ${product.category.categoryname}</p>
            <h4 class="text-primary">
                <fmt:formatNumber value="${product.price}" type="number" groupingUsed="true" /> đ
            </h4>
            <p class="mt-3" style="white-space:pre-line">${product.description}</p>
        </div>
    </div>
</body>
</html>
