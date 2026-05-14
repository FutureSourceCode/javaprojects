<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>用户管理</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .table-container {
            max-width: 900px;
            margin: 0 auto;
        }
        .search-bar {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<div class="container mt-4">
    <div class="table-container">
        <h3 class="text-center mb-4">用户管理</h3>
        <a href="${pageContext.request.contextPath}/admin/login.jsp" class="btn btn-secondary mb-3">返回主页</a>

        <hr>

        <!-- 提示信息 -->
        <c:if test="${not empty tipMsg}">
            <div class="alert alert-success">${tipMsg}</div>
            <c:remove var="tipMsg" scope="session"/> <!-- 显示后清除提示 -->
        </c:if>
        <c:if test="${not empty errorMsg}">
            <div class="alert alert-danger">${errorMsg}</div>
            <c:remove var="errorMsg" scope="session"/> <!-- 显示后清除提示 -->
        </c:if>

        <!-- 搜索栏 -->
        <div class="search-bar">
            <form action="${pageContext.request.contextPath}/admin/user/list" method="get" class="row g-3">
                <div class="col-md-5">
                    <input type="text" class="form-control" name="keyword" placeholder="输入用户名搜索" value="${keyword}">
                </div>
                <div class="col-md-4">
                    <select class="form-select" name="role">
                        <option value="">所有角色</option>
                        <option value="user" ${role == 'user' ? 'selected' : ''}>普通用户</option>
                        <option value="admin" ${role == 'admin' ? 'selected' : ''}>管理员</option>
                    </select>
                </div>
                <div class="col-md-3">
                    <button type="submit" class="btn btn-primary w-100">搜索</button>
                </div>
            </form>
        </div>

        <!-- 用户列表 -->
        <table class="table table-bordered table-hover">
            <thead class="table-dark">
            <tr>
                <th>用户ID</th>
                <th>用户名</th>
                <th>角色</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${empty userList}">
                <tr>
                    <td colspan="4" class="text-center text-muted">暂无用户数据</td>
                </tr>
            </c:if>
            <c:forEach items="${userList}" var="user">
                <tr>
                    <td>${user.id}</td>
                    <td>${user.username}</td>
                    <td>
                        <c:choose>
                            <%-- ID为1显示超级管理员 --%>
                            <c:when test="${user.id == 1}">
                                <span class="badge bg-dark">超级管理员</span>
                            </c:when>
                            <%-- 其他ID按角色显示 --%>
                            <c:otherwise>
                                <span class="badge ${user.role == 'admin' ? 'bg-danger' : 'bg-primary'}">
                                        ${user.role == 'admin' ? '管理员' : '普通用户'}
                                </span>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>
                        <c:choose>
                            <%-- ID为1禁止删除 --%>
                            <c:when test="${user.id == 1}">
                                <button class="btn btn-sm btn-secondary" disabled>不可删除</button>
                            </c:when>
                            <%-- 其他用户显示删除按钮 --%>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/admin/user/delete?id=${user.id}&keyword=${keyword}&role=${role}"
                                   class="btn btn-sm btn-danger"
                                   onclick="return confirm('确定删除用户【${user.username}】吗？此操作不可恢复！')">
                                    删除
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>