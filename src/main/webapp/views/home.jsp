<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head><title>Trang chủ</title></head>
<body>
    <div class="p-4 bg-white border rounded">
        <h2>Xin chào, ${sessionScope.account.userName}</h2>
        <p class="text-muted mb-0">roleid = ${sessionScope.account.roleid}</p>
    </div>
</body>
</html>
