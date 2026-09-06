<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head><title>Sửa Product</title></head>
<body>
    <div class="bg-white border rounded p-4" style="max-width:600px">
        <h3 class="mb-3">Sửa Product</h3>
        <form action="<c:url value="/admin/product/update"/>" method="post" enctype="multipart/form-data">
            <input type="text" name="productid" value="${product.productId}" hidden="hidden">

            <div class="mb-3">
                <label class="form-label">Tên sản phẩm</label>
                <input type="text" class="form-control ${errors.productname != null ? 'is-invalid' : ''}"
                       name="productname" value="${product.productname}" required>
                <c:if test="${errors.productname != null}">
                    <div class="invalid-feedback">${errors.productname}</div>
                </c:if>
            </div>

            <div class="mb-3">
                <label class="form-label">Mô tả</label>
                <textarea class="form-control" name="description" rows="3">${product.description}</textarea>
            </div>

            <div class="mb-3">
                <label class="form-label">Giá</label>
                <input type="number" step="0.01" min="0" class="form-control ${errors.price != null ? 'is-invalid' : ''}"
                       name="price" value="${product.price}" required>
                <c:if test="${errors.price != null}">
                    <div class="invalid-feedback">${errors.price}</div>
                </c:if>
            </div>

            <div class="mb-3">
                <label class="form-label">Danh mục</label>
                <select class="form-select ${errors.categoryid != null ? 'is-invalid' : ''}" name="categoryid" required>
                    <c:forEach items="${listcate}" var="c">
                        <option value="${c.categoryId}" ${c.categoryId == product.category.categoryId ? 'selected' : ''}>${c.categoryname}</option>
                    </c:forEach>
                </select>
                <c:if test="${errors.categoryid != null}">
                    <div class="invalid-feedback">${errors.categoryid}</div>
                </c:if>
            </div>

            <div class="mb-3">
                <label class="form-label">Link ảnh (nếu không upload file)</label>
                <input type="text" class="form-control" name="images" value="${product.images}">
            </div>

            <c:if test="${product.images.substring(0,5)=='https'}">
                <c:url value="${product.images}" var="imgUrl"></c:url>
            </c:if>
            <c:if test="${product.images.substring(0,5)!='https'}">
                <c:url value="/image?fname=${product.images}" var="imgUrl"></c:url>
            </c:if>
            <img class="mb-3" height="120" width="160" src="${imgUrl}" /><br>

            <div class="mb-3">
                <label class="form-label">Upload ảnh mới</label>
                <input type="file" class="form-control" name="images1">
            </div>

            <div class="mb-3">
                <label class="form-label d-block">Status</label>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="status" id="statusOn" value="1" ${product.status == 1 ? 'checked' : ''}>
                    <label class="form-check-label" for="statusOn">Hoạt động</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="status" id="statusOff" value="0" ${product.status != 1 ? 'checked' : ''}>
                    <label class="form-check-label" for="statusOff">Khóa</label>
                </div>
                <c:if test="${errors.status != null}">
                    <div class="text-danger small mt-1">${errors.status}</div>
                </c:if>
            </div>

            <button type="submit" class="btn btn-primary">Update</button>
            <a class="btn btn-secondary" href="<c:url value='/admin/products'/>">Hủy</a>
        </form>
    </div>
</body>
</html>
