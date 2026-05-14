<%--
  Created by IntelliJ IDEA.
  User: kaige
  Date: 2025/11/24
  Time: 15:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>借阅记录管理</title>
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
        .search-bar {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        .record-table {
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
        }
        .status-unreturned {
            color: #dc3545;
            font-weight: 500;
        }
        .status-returned {
            color: #6c757d;
        }
    </style>
</head>
<body>
<div class="container mt-4">
    <!-- 顶部导航栏 -->
    <div class="header-bar">
        <h3>借阅记录管理</h3>
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

    <!-- 按借阅者查询栏 -->
    <div class="search-bar">
        <form action="${pageContext.request.contextPath}/admin/borrow/list" method="get" class="row g-3">
            <div class="col-md-8">
                <label class="form-label">借阅者用户名：</label>
                <input type="text" class="form-control" name="username" placeholder="输入借阅者用户名查询" value="${queryUsername}">
            </div>
            <div class="col-md-4 align-self-end">
                <button type="submit" class="btn btn-primary w-100">查询</button>
            </div>
        </form>
    </div>

    <!-- 借阅记录列表 -->
    <div class="card record-table">
        <div class="card-body">
            <table class="table table-hover">
                <thead class="table-dark">
                <tr>
                    <th>借阅ID</th>
                    <th>图书编号</th>
                    <th>图书名称</th>
                    <th>借阅者</th>
                    <th>借阅时间</th>
                    <th>归还时间</th>
                    <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${empty borrowRecords}">
                    <tr>
                        <td colspan="7" class="text-center text-muted">暂无借阅记录</td>
                    </tr>
                </c:if>
                <c:forEach items="${borrowRecords}" var="record">
                    <tr>
                        <td>${record.id}</td>
                        <td>${record.bookNo}</td>
                        <td>${record.bookName}</td>
                        <td>${record.username}</td>
                        <td><fmt:formatDate value="${record.borrowData}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                        <td>
                            <c:choose>
                                <c:when test="${empty record.returnData}">
                                    <span class="status-unreturned">未归还</span>
                                </c:when>
                                <c:otherwise>
                                    <span class="status-returned"><fmt:formatDate value="${record.returnData}" pattern="yyyy-MM-dd HH:mm:ss"/></span>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>
                            <c:if test="${empty record.returnData}">
                                <a href="${pageContext.request.contextPath}/admin/borrow/remind?id=${record.id}&username=${queryUsername}"
                                   class="btn btn-sm btn-warning"
                                   onclick="return confirm('确认发送《${record.bookName}》的还书提醒给${record.username}吗？')">
                                    提醒还书
                                </a>
                            </c:if>
                            <c:if test="${not empty record.returnData}">
                                <button class="btn btn-sm btn-secondary" disabled>已归还</button>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>