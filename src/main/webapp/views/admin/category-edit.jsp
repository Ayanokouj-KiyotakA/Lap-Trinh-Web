<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Sửa Category</title></head>
<body>
    <div class="bg-white border rounded p-4" style="max-width:600px">
        <h3 class="mb-3">Sửa Category</h3>
        <form action="<c:url value="/admin/category/update"/>" method="post" enctype="multipart/form-data">
            <input type="text" name="categoryid" value="${cate.categoryId}" hidden="hidden">

            <div class="mb-3">
                <label class="form-label">Category name</label>
                <input type="text" class="form-control" name="categoryname" value="${cate.categoryname}" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Link ảnh (nếu không upload file)</label>
                <input type="text" class="form-control" name="images" value="${cate.images}">
            </div>

            <c:if test="${cate.images.substring(0,5)=='https'}">
                <c:url value="${cate.images}" var="imgUrl"></c:url>
            </c:if>
            <c:if test="${cate.images.substring(0,5)!='https'}">
                <c:url value="/image?fname=${cate.images}" var="imgUrl"></c:url>
            </c:if>
            <img class="mb-3" height="120" width="160" src="${imgUrl}" /><br>

            <div class="mb-3">
                <label class="form-label">Upload ảnh mới</label>
                <input type="file" class="form-control" name="images1">
            </div>

            <div class="mb-3">
                <label class="form-label d-block">Status</label>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="status" id="statusOn" value="1" ${cate.status == 1 ? 'checked' : ''}>
                    <label class="form-check-label" for="statusOn">Hoạt động</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="status" id="statusOff" value="0" ${cate.status != 1 ? 'checked' : ''}>
                    <label class="form-check-label" for="statusOff">Khóa</label>
                </div>
            </div>

            <button type="submit" class="btn btn-primary">Update</button>
            <a class="btn btn-secondary" href="<c:url value='/admin/categories'/>">Hủy</a>
        </form>
    </div>
</body>
</html>
