<%--
  Created by IntelliJ IDEA.
  User: 小小凯
  Date: 2025/11/23
  Time: 15:59
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>我的预约</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<div class="container mt-3">
    <h3>我的预约记录</h3>
    <a href="${pageContext.request.contextPath}/user/login.jsp" class="btn btn-secondary mb-3">返回主页</a>

    <table class="table table-bordered">
        <thead>
        <tr>
            <th>预约ID</th>
            <th>图书名称</th>
            <th>预约用户</th>
            <th>预约日期</th>
            <th>预约状态</th>
        </tr>
        </thead>
        <tbody>
        <%-- 同时判断null和empty，确保无数据时不报错 --%>
        <c:if test="${empty reservations or reservations == null}">
            <tr>
                <td colspan="5" class="text-center text-muted">你还没有预约记录</td>
            </tr>
        </c:if>
        <%-- 有数据时遍历展示 --%>
        <c:if test="${not empty reservations and reservations != null}">
            <c:forEach items="${reservations}" var="res">
                <tr>
                    <td>${res.id}</td>
                    <td>${res.bookName}</td>
                    <td>${res.userName}</td>
                    <td>${res.reserveTime}</td>
                    <td>
                        <c:choose>
                            <c:when test="${res.status == 'pending'}">等待中</c:when>
                            <c:when test="${res.status == 'fulfilled'}">已生效</c:when>
                            <c:otherwise>已取消</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </c:if>
        </tbody>
    </table>
</div>
</body>
</html>