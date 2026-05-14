<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>管理员主页</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/background/style.css">
</head>
<body>
<div class="container mt-3">
    <h3>欢迎，${loginUser.username}（管理员）</h3>
    <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger mb-3">退出登录</a>
        <div class="card-body">
            <h5>功能菜单</h5>
            <a href="${pageContext.request.contextPath}/admin/book/list" class="btn btn-primary">图书管理</a>
            <a href="${pageContext.request.contextPath}/admin/user/list" class="btn btn-success btn-lg">用户管理</a>
            <a href="${pageContext.request.contextPath}/admin/reservation/list" class="btn btn-primary">预约管理</a>
            <a href="${pageContext.request.contextPath}/admin/borrow/list" class="btn btn-success btn-lg">借阅记录管理</a>
            <a href="${pageContext.request.contextPath}/admin/announcement/list" class="btn btn-primary">公告管理</a>
            <a href="${pageContext.request.contextPath}/user/info" class="btn btn-success btn-lg">个人信息</a>
        </div>
</div>
</body>
</html>