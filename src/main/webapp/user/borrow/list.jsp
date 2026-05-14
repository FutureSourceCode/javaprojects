
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>借阅记录</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-3">
    <h3>我的借阅记录</h3>
    <a href="${pageContext.request.contextPath}/user/login.jsp" class="btn btn-secondary mb-3">返回主页</a>

    <table class="table table-bordered">
        <thead>
        <tr>
            <th>借阅ID</th>
            <th>图书编号</th>
            <th>图书名称</th>
            <th>借阅日期</th>
            <th>归还日期</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${borrows}" var="borrow">
            <tr>
                <td>${borrow.id}</td>
                <td>${borrow.bookNo}</td>
                <td>${borrow.bookName}</td>
                <td>${borrow.borrowData}</td>
                <td>${borrow.returnData == null ? "未归还" : borrow.returnData}</td>
                <td>
                    <c:if test="${borrow.returnData == null}">
                        <a href="${pageContext.request.contextPath}/borrow/return?id=${borrow.id}&bookNo=${borrow.bookNo}" class="btn btn-sm btn-success">归还</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
</body>
</html>