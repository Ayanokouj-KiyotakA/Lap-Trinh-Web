<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Danh sách Category</title></head>
<body>
    <div class="d-flex justify-content-between align-items-center mb-3">
        <h3 class="mb-0">Danh sách Category</h3>
        <a class="btn btn-primary" href="<c:url value="/admin/category/add"/>">+ Add Category</a>
    </div>

    <table class="table table-bordered table-hover align-middle bg-white">
        <thead class="table-light">
        <tr>
            <th>STT</th>
            <th>Images</th>
            <th>Category name</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${listcate}" var="cate" varStatus="STT">
            <tr>
                <td>${STT.index + 1}</td>

                <c:if test="${cate.images.substring(0,5)=='https'}">
                    <c:url value="${cate.images}" var="imgUrl"></c:url>
                </c:if>
                <c:if test="${cate.images.substring(0,5)!='https'}">
                    <c:url value="/image?fname=${cate.images}" var="imgUrl"></c:url>
                </c:if>

                <td><img height="80" width="110" src="${imgUrl}" /></td>
                <td>${cate.categoryname}</td>
                <td>
                    <c:if test="${cate.status == 1}"><span class="badge bg-success">Hoạt động</span></c:if>
                    <c:if test="${cate.status != 1}"><span class="badge bg-secondary">Khóa</span></c:if>
                </td>
                <td>
                    <a class="btn btn-sm btn-outline-primary" href="<c:url value='/admin/category/edit?id=${cate.categoryId}'/>">Sửa</a>
                    <a class="btn btn-sm btn-outline-danger" href="<c:url value='/admin/category/delete?id=${cate.categoryId}'/>"
                       onclick="return confirm('Xóa category này?');">Xóa</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</body>
</html>
