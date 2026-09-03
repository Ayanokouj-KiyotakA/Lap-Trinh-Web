<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Thêm Category</title></head>
<body>
    <div class="bg-white border rounded p-4" style="max-width:600px">
        <h3 class="mb-3">Thêm Category</h3>
        <form action="<c:url value="/admin/category/insert"/>" method="post" enctype="multipart/form-data">
            <div class="mb-3">
                <label class="form-label">Category name</label>
                <input type="text" class="form-control" name="categoryname" required>
            </div>

            <div class="mb-3">
                <label class="form-label">Link ảnh (nếu không upload file)</label>
                <input type="text" class="form-control" name="images">
            </div>

            <div class="mb-3">
                <label class="form-label">Upload ảnh</label>
                <input type="file" class="form-control" name="images1">
            </div>

            <div class="mb-3">
                <label class="form-label d-block">Status</label>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="status" id="statusOn" value="1" checked>
                    <label class="form-check-label" for="statusOn">Hoạt động</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="status" id="statusOff" value="0">
                    <label class="form-check-label" for="statusOff">Khóa</label>
                </div>
            </div>

            <button type="submit" class="btn btn-primary">Insert</button>
            <a class="btn btn-secondary" href="<c:url value='/admin/categories'/>">Hủy</a>
        </form>
    </div>
</body>
</html>
