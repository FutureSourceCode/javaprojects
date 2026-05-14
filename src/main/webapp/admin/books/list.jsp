<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>图书列表</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-3">
    <h3>图书管理</h3>
    <a href="${pageContext.request.contextPath}/admin/book/add" class="btn btn-success mb-3">添加图书</a>
    <a href="${pageContext.request.contextPath}/admin/login.jsp" class="btn btn-secondary mb-3">返回主页</a>

    <table class="table table-bordered">
        <thead>
        <tr>
            <th>ID</th>
            <th>书号</th>
            <th>书名</th>
            <th>作者</th>
            <th>库存</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${books}" var="book">
            <tr>
                <td>${book.id}</td>
                <td>${book.bookNo}</td>
                <td>${book.bookName}</td>
                <td>${book.author}</td>
                <td>${book.stock}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/admin/book/edit?id=${book.id}" class="btn btn-sm btn-primary">编辑</a>
                    <a href="${pageContext.request.contextPath}/admin/book/delete?id=${book.id}&bookName=${book.bookName}" class="btn btn-sm btn-danger" onclick="return confirm('确定删除？')">删除</a>
                </td>
            </tr>

        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>