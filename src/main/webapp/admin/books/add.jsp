<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>添加图书</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-3">
    <h3>添加图书</h3>
    <a href="${pageContext.request.contextPath}/admin/book/list" class="btn btn-secondary mb-3">返回列表</a>

    <form action="${pageContext.request.contextPath}/admin/book/add" method="post">
        <div class="mb-3">
            <label>书号</label>
            <input type="text" name="bookNo" class="form-control" required>
        </div>
        <div class="mb-3">
            <label>书名</label>
            <input type="text" name="bookName" class="form-control" required>
        </div>
        <div class="mb-3">
            <label>作者</label>
            <input type="text" name="author" class="form-control" required>
        </div>
        <div class="mb-3">
            <label>库存</label>
            <input type="number" name="stock" class="form-control" required>
        </div>
        <button type="submit" class="btn btn-primary">提交</button>
    </form>
</div>
</body>
</html>