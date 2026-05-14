<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>公告管理</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .container {
            max-width: 1000px;
        }
        .announcement-card {
            border: 1px solid #eee;
            border-radius: 8px;
            padding: 15px;
            margin-bottom: 15px;
            position: relative;
        }
        .publish-info {
            color: #6c757d;
            font-size: 0.9em;
            margin-bottom: 10px;
        }
        .delete-btn {
            position: absolute;
            top: 10px;
            right: 10px;
        }
        .search-bar {
            background-color: #f8f9fa;
            padding: 15px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        .add-form {
            background-color: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 30px;
        }
        .header-bar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<div class="container mt-4">
    <!-- 顶部导航栏：标题 + 返回主页按钮 -->
    <div class="header-bar">
        <h3>公告管理</h3>
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

    <!-- 发布公告表单 -->
    <div class="add-form">
        <h5>发布新公告</h5>
        <form action="${pageContext.request.contextPath}/admin/announcement/add" method="post" class="mt-3">
            <div class="mb-3">
                <label class="form-label">公告内容：</label>
                <textarea class="form-control" name="content" rows="4" required placeholder="请输入公告内容"></textarea>
            </div>
            <div class="mb-3">
                <label class="form-label">发布者：</label>
                <input type="text" class="form-control" value="${loginUser.username}" readonly>
            </div>
            <button type="submit" class="btn btn-primary">发布公告</button>
        </form>
    </div>

    <!-- 日期查询栏 -->
    <div class="search-bar">
        <form action="${pageContext.request.contextPath}/admin/announcement/list" method="get" class="row g-3">
            <div class="col-md-5">
                <label class="form-label">开始日期：</label>
                <input type="date" class="form-control" name="startDate" value="${startDate}">
            </div>
            <div class="col-md-5">
                <label class="form-label">结束日期：</label>
                <input type="date" class="form-control" name="endDate" value="${endDate}">
            </div>
            <div class="col-md-2 align-self-end">
                <button type="submit" class="btn btn-primary w-100">查询</button>
            </div>
        </form>
    </div>

    <!-- 公告列表 -->
    <div class="announcement-list">
        <c:if test="${empty announcements}">
            <div class="alert alert-info text-center">暂无公告数据</div>
        </c:if>
        <c:forEach items="${announcements}" var="ann">
            <div class="announcement-card">
                <button type="button" class="btn btn-sm btn-danger delete-btn" onclick="deleteAnn(${ann.id}, '${startDate}', '${endDate}')">删除</button>

                <div class="publish-info">
                    <span>发布者：${ann.publisher}</span> |
                    <fmt:formatDate value="${ann.publishTime}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </div>
                <div class="content">${ann.content}</div>
            </div>
        </c:forEach>
    </div>
</div>

<!-- 删除确认弹窗 -->
<div class="modal fade" id="deleteModal" tabindex="-1" aria-labelledby="deleteModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="deleteModalLabel">确认删除</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <div class="modal-body">
                确定要删除该公告吗？此操作不可恢复！
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">取消</button>
                <a id="deleteConfirmBtn" href="#" class="btn btn-danger">确认删除</a>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // 删除公告逻辑（带查询条件回传）
    function deleteAnn(annId, startDate, endDate) {
        let deleteUrl = "${pageContext.request.contextPath}/admin/announcement/delete?id=" + annId;
        if (startDate || endDate) {
            deleteUrl += "&startDate=" + (startDate || "") + "&endDate=" + (endDate || "");
        }
        document.getElementById('deleteConfirmBtn').href = deleteUrl;
        new bootstrap.Modal(document.getElementById('deleteModal')).show();
    }
</script>
</body>
</html>