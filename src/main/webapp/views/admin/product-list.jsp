<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head><title>Danh sách Product</title></head>
<body>
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="mb-0">Danh sách Product</h3>
        <a class="btn btn-primary" href="<c:url value="/admin/product/add"/>">+ Add Product</a>
    </div>

    <table class="table table-bordered table-hover align-middle bg-white">
        <thead class="table-light">
        <tr>
            <th>STT</th>
            <th>Images</th>
            <th>Tên sản phẩm</th>
            <th>Danh mục</th>
            <th>Giá</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${listproduct}" var="product" varStatus="STT">
            <tr>
                <td>${STT.index + 1}</td>

                <c:if test="${product.images.substring(0,5)=='https'}">
                    <c:url value="${product.images}" var="imgUrl"></c:url>
                </c:if>
                <c:if test="${product.images.substring(0,5)!='https'}">
                    <c:url value="/image?fname=${product.images}" var="imgUrl"></c:url>
                </c:if>

                <td><img height="80" width="110" src="${imgUrl}" /></td>
                <td>${product.productname}</td>
                <td>${product.category.categoryname}</td>
                <td><fmt:formatNumber value="${product.price}" type="number" groupingUsed="true" /></td>
                <td>
                    <c:if test="${product.status == 1}"><span class="badge bg-success">Hoạt động</span></c:if>
                    <c:if test="${product.status != 1}"><span class="badge bg-secondary">Khóa</span></c:if>
                </td>
                <td>
                    <a class="btn btn-sm btn-outline-primary" href="<c:url value='/admin/product/edit?id=${product.productId}'/>">Sửa</a>
                    <a class="btn btn-sm btn-outline-danger" href="<c:url value='/admin/product/delete?id=${product.productId}'/>"
                       onclick="return confirm('Xóa product này?');">Xóa</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</body>
</html>
