<%--
  Created by IntelliJ IDEA.
  User: 小小凯
  Date: 2025/11/24
  Time: 11:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>公告列表</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .announcement-item {
            border: 1px solid #eee;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 15px;
            background-color: #fff;
        }
        .publish-info {
            color: #6c757d;
            font-size: 0.9em;
            margin-bottom: 10px;
        }
    </style>
</head>
<body>
<div class="container mt-4">
    <h3>公告栏</h3>
    <a href="${pageContext.request.contextPath}/user/login.jsp" class="btn btn-secondary mb-3">返回主页</a>
    <hr>
    <!-- 日期筛选表单 -->
    <form action="${pageContext.request.contextPath}/announcement/list" method="get" class="mb-4">
        <div class="row g-3 align-items-end">
            <div class="col-md-4">
                <label class="form-label">开始日期：</label>
                <input type="date" class="form-control" name="startDate" value="${startDate}">
            </div>
            <div class="col-md-4">
                <label class="form-label">结束日期：</label>
                <input type="date" class="form-control" name="endDate" value="${endDate}">
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-primary">查询</button>
            </div>
            <div class="col-md-2">
                <a href="${pageContext.request.contextPath}/announcement/list" class="btn btn-outline-secondary">重置</a>
            </div>
        </div>
    </form>

    <!-- 公告列表 -->
    <c:if test="${empty announcements}">
        <div class="alert alert-info text-center">暂无公告</div>
    </c:if>

    <c:forEach items="${announcements}" var="ann">
        <div class="announcement-item">
            <div class="publish-info">
                <span>发布者：${ann.publisher}</span> |
                <fmt:formatDate value="${ann.publishTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
            </div>
            <div class="content">${ann.content}</div>
        </div>
    </c:forEach>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>