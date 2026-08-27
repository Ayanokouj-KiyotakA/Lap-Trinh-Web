<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Thêm Category</title></head>
<body>
    <h2>Thêm Category</h2>
    <form action="<c:url value="/admin/category/insert"/>" method="post" enctype="multipart/form-data">
        <label>Category name:</label><br>
        <input type="text" name="categoryname"><br>

        <label>Link ảnh (nếu không upload file):</label><br>
        <input type="text" name="images"><br>

        <label>Upload ảnh:</label><br>
        <input type="file" name="images1"><br>

        <label>Status</label><br>
        <input type="radio" name="status" value="1" checked> Hoạt động<br>
        <input type="radio" name="status" value="0"> Khóa

        <br><br>
        <input type="submit" value="Insert">
    </form>
</body>
</html>
