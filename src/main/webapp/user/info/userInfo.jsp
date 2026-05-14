<%--
  Created by IntelliJ IDEA.
  User: 小小凯
  Date: 2025/11/24
  Time: 9:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>个人信息</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/background/style.css">
    <style>
        .card {
            max-width: 700px;
            margin: 30px auto;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
        }
        .nav-link.active {
            font-weight: bold;
            color: #0d6efd !important;
        }
    </style>
</head>
<body>
<div class="container">
    <!-- 导航栏 -->
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <div class="container-fluid">
                <c:choose>
                    <c:when test="${user.role == 'admin'}">
                        <a class="navbar-brand" href="${pageContext.request.contextPath}/admin/login.jsp">图书管理系统</a>
                    </c:when>
                    <c:otherwise>
                        <a class="navbar-brand" href="${pageContext.request.contextPath}/user/login.jsp" >图书管理系统</a>
                    </c:otherwise>
                </c:choose>

            <div class="collapse navbar-collapse">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/user/info">个人中心</a>
                    </li>

                </ul>
                <span class="navbar-text me-3">${loginUser.username}</span>
                <a href="${pageContext.request.contextPath}/logout" class="btn btn-sm btn-outline-danger">退出</a>
            </div>
        </div>
    </nav>

    <!-- 个人信息卡片 -->
    <div class="card">
        <div class="card-header bg-primary text-white">
            <h5 class="mb-0">个人信息</h5>
        </div>
        <div class="card-body">
            <div class="row mb-3">
                <label class="col-sm-3 col-form-label">用户ID：</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control-plaintext" value="${user.id}" readonly>
                </div>
            </div>
            <div class="row mb-3">
                <label class="col-sm-3 col-form-label">用户名：</label>
                <div class="col-sm-9">
                    <input type="text" class="form-control-plaintext" value="${user.username}" readonly>
                </div>
            </div>
            <div class="row mb-3">
                <label class="col-sm-3 col-form-label">用户角色：</label>
                <div class="col-sm-9">
                    <span class="badge ${user.role == 'admin' ? 'bg-success' : 'bg-info'}">
                        ${user.role == 'admin' ? '管理员' : '普通用户'}
                    </span>
                </div>
            </div>
            <div class="row mt-4">
                <div class="col-sm-9 offset-sm-3">
                    <a href="${pageContext.request.contextPath}/user/updatePassword" class="btn btn-primary">修改密码</a>
                    <c:choose>
                        <c:when test="${user.role == 'admin'}">
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/admin/login.jsp">返回</a>
                        </c:when>
                        <c:otherwise>
                            <a class="btn btn-primary" href="${pageContext.request.contextPath}/user/login.jsp" >返回</a>
                        </c:otherwise>
                    </c:choose>

                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
