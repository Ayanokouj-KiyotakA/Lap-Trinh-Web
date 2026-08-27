<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<html>
<head><title>Sửa Category</title></head>
<body>
    <h2>Sửa Category</h2>
    <form action="<c:url value="/admin/category/update"/>" method="post" enctype="multipart/form-data">
        <input type="text" name="categoryid" value="${cate.categoryId}" hidden="hidden">

        <label>Category name:</label><br>
        <input type="text" name="categoryname" value="${cate.categoryname}"><br>

        <label>Link ảnh (nếu không upload file):</label><br>
        <input type="text" name="images" value="${cate.images}"><br>

        <c:if test="${cate.images != null and fn:startsWith(cate.images, 'https')}">
            <c:url value="${cate.images}" var="imgUrl"></c:url>
        </c:if>
        <c:if test="${cate.images != null and !fn:startsWith(cate.images, 'https')}">
            <c:url value="/image?fname=${cate.images}" var="imgUrl"></c:url>
        </c:if>
        <img height="120" width="160" src="${imgUrl}" /><br>

        <label>Upload ảnh mới:</label><br>
        <input type="file" name="images1"><br>

        <label>Status</label><br>
        <input type="radio" name="status" value="1" ${cate.status == 1 ? 'checked' : ''}> Hoạt động<br>
        <input type="radio" name="status" value="0" ${cate.status != 1 ? 'checked' : ''}> Khóa

        <br><br>
        <input type="submit" value="Update">
    </form>
</body>
</html>
