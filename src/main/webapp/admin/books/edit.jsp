<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>编辑图书</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-3">
    <h3>编辑图书</h3>
    <a href="${pageContext.request.contextPath}/admin/book/list" class="btn btn-secondary mb-3">返回图书列表</a>

    <form action="${pageContext.request.contextPath}/admin/book/edit" method="post">
        <input type="hidden" name="id" value="${book.id}">

        <div class="mb-3">
            <label class="form-label">书号</label>
            <input type="text" name="bookNo" class="form-control" value="${book.bookNo}" readonly>
            <div class="form-text">书号不可修改</div>
        </div>

        <div class="mb-3">
            <label class="form-label">书名</label>
            <input type="text" name="bookName" class="form-control" value="${book.bookName}" required>
        </div>

        <div class="mb-3">
            <label class="form-label">作者</label>
            <input type="text" name="author" class="form-control" value="${book.author}" required>
        </div>

        <div class="mb-3">
            <label class="form-label">库存</label>
            <input type="number" name="stock" class="form-control" value="${book.stock}" min="0" required>
        </div>

        <button type="submit" class="btn btn-primary">提交修改</button>
    </form>
</div>
</body>
</html>