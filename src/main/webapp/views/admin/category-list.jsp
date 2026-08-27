<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<html>
<head><title>Danh sách Category</title></head>
<body>
    <a href="<c:url value="/admin/category/add"/>">Add Category</a><br>
    <hr>
    <table border="1" width="100%">
        <tr>
            <th>STT</th>
            <th>Images</th>
            <th>Category name</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        <c:forEach items="${listcate}" var="cate" varStatus="STT">
            <tr>
                <td>${STT.index + 1}</td>

                <c:if test="${cate.images != null and fn:startsWith(cate.images, 'https')}">
                    <c:url value="${cate.images}" var="imgUrl"></c:url>
                </c:if>
                <c:if test="${cate.images != null and !fn:startsWith(cate.images, 'https')}">
                    <c:url value="/image?fname=${cate.images}" var="imgUrl"></c:url>
                </c:if>

                <td><img height="120" width="160" src="${imgUrl}" /></td>
                <td>${cate.categoryname}</td>
                <td>
                    <c:if test="${cate.status == 1}">Hoạt động</c:if>
                    <c:if test="${cate.status != 1}">Khóa</c:if>
                </td>
                <td>
                    <a href="<c:url value='/admin/category/edit?id=${cate.categoryId}'/>">Sửa</a>
                    | <a href="<c:url value='/admin/category/delete?id=${cate.categoryId}'/>">Xóa</a>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
