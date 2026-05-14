<%--
  Created by IntelliJ IDEA.
  User: kaige
  Date: 2025/11/24
  Time: 14:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>预约管理</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .container {
            max-width: 1200px;
        }
        .header-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
        .reservation-table {
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .status-notified {
            color: #6c757d;
        }
        .status-pending {
            color: #dc3545;
        }
    </style>
</head>
<body>
<div class="container mt-4">
    <!-- 顶部导航栏 -->
    <div class="header-bar">
        <h3>预约管理</h3>
        <a href="${pageContext.request.contextPath}/admin/login.jsp" class="btn btn-outline-primary">返回主页</a>
    </div>
    <hr>

    <!-- 提示信息 -->
    <c:if test="${not empty tipMsg}">
        <div class="alert alert-success">${tipMsg}</div>
        <c:remove var="tipMsg" scope="session"/>
    </c:if>
    <c:if test="${not empty errorMsg}">
        <div class="alert alert-danger">${errorMsg}</div>
        <c:remove var="errorMsg" scope="session"/>
    </c:if>

    <!-- 预约列表 -->
    <div class="card reservation-table">
        <div class="card-body">
            <table class="table table-hover">
                <thead class="table-dark">
                <tr>
                    <th>预约ID</th>
                    <th>预约用户</th>
                    <th>书籍名称</th>
                    <th>预约时间</th>
                    <th>状态</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${empty reservations}">
                    <tr>
                        <td colspan="6" class="text-center text-muted">暂无预约记录</td>
                    </tr>
                </c:if>
                <c:forEach items="${reservations}" var="res">
                    <tr>
                        <td>${res.id}</td>
                        <td>${res.userName}</td>
                        <td>${res.bookName}</td>
                        <td><fmt:formatDate value="${res.reserveTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td>
                            <c:choose>
                                <%-- 状态为'a'表示已提醒 --%>
                                <c:when test="${res.status == 'fulfilled'}">
                                    <span class="status-notified"><i class="bi bi-check-circle"></i> 已提醒</span>
                                </c:when>
                                <%-- 状态为'b'表示未提醒 --%>
                                <c:otherwise>
                                    <span class="status-pending"><i class="bi bi-clock"></i> 待提醒</span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${res.status == 'fulfilled'}">
                                    <button class="btn btn-sm btn-secondary" disabled>已提醒</button>
                                </c:when>
                                <c:otherwise>
                                    <a href="${pageContext.request.contextPath}/admin/reservation/notify?id=${res.id}"
                                       class="btn btn-sm btn-primary"
                                       onclick="return confirm('确认发送《${res.bookName}》到库提醒给${res.userName}吗？')">
                                        提醒到库
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
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<!-- 引入Bootstrap图标 -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
</body>
</html>
