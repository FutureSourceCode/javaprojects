<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>用户主页</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/background/style.css">
</head>
<body>
<div class="container mt-5">
    <div class="row">
        <div class="col-md-8 offset-md-2">
            <div class="card">
                <div class="card-header text-center">
                    <h3>图书管理系统 - 用户中心</h3>
                </div>
                <div class="card-body">
                    <div class="text-center mb-4">
                        <h4>欢迎您，${loginUser.username}！</h4>
                        <p class="text-muted">当前角色：普通用户</p>
                    </div>

                    <div class="d-grid gap-3">
                        <a href="${pageContext.request.contextPath}/user/book/search" class="btn btn-primary btn-lg">图书查询</a>
                        <a href="${pageContext.request.contextPath}/borrow/list" class="btn btn-success btn-lg">我的借阅记录</a>
                        <a href="${pageContext.request.contextPath}/reservation/list" class="btn btn-primary btn-lg">我的预约</a>
                        <a href="${pageContext.request.contextPath}/user/info" class="btn btn-success btn-lg">个人信息</a>
                        <a href="${pageContext.request.contextPath}/announcement/list" class="btn btn-success btn-lg">公告栏</a>
                    </div>
                </div>
                <div class="card-footer text-center">
                    <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger">退出登录</a>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>