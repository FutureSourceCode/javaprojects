<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>图书查询</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        /* 修复模态框遮罩层覆盖问题 */
        .modal-backdrop {
            position: fixed;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background-color: rgba(0,0,0,0.5);
            z-index: 1040;
        }
        .modal {
            z-index: 1050;
        }
        /* 空数据样式优化 */
        .empty-data {
            text-align: center;
            color: #6c757d;
            padding: 20px;
        }
    </style>
</head>
<body>
<div class="container mt-3">
    <h3>图书查询</h3>
    <a href="${pageContext.request.contextPath}/user/login.jsp" class="btn btn-secondary mb-3">返回主页</a>

    <!-- 提示弹窗：优化关闭逻辑和自动隐藏 -->
    <c:if test="${not empty tipMsg}">
        <div class="modal-backdrop fade show"></div>
        <div class="modal fade show d-block" tabindex="-1" role="dialog">
            <div class="modal-dialog modal-sm" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">提示</h5>
                        <button type="button" class="btn-close" onclick="closeModal()"></button>
                    </div>
                    <div class="modal-body">
                            ${tipMsg}
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" onclick="closeModal()">确定</button>
                    </div>
                </div>
            </div>
        </div>
    </c:if>

    <form action="${pageContext.request.contextPath}/user/book/search" method="post">
        <div class="row mb-3">
            <div class="col-md-5">
                <input type="text" name="bookNo" class="form-control" placeholder="输入书号查询"
                       value="${param.bookNo}"> <!-- 保留上次输入内容 -->
            </div>
            <div class="col-md-5">
                <input type="text" name="keyword" class="form-control" placeholder="输入书名关键词查询"
                       value="${param.keyword}"> <!-- 保留上次输入内容 -->
            </div>
            <div class="col-md-2">
                <button type="submit" class="btn btn-primary w-100">查询</button>
            </div>
        </div>
    </form>

    <table class="table table-bordered">
        <thead>
        <tr>
            <th>书号</th>
            <th>书名</th>
            <th>作者</th>
            <th>库存</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty books}">
                <c:forEach items="${books}" var="book">
                    <tr>
                        <td>${book.bookNo}</td>
                        <td>${book.bookName}</td>
                        <td>${book.author}</td>
                        <td>${book.stock}</td>
                        <td>
                            <c:if test="${book.stock > 0}">
                                <form action="${pageContext.request.contextPath}/borrow/borrowBook" method="post" style="display: inline;">
                                    <input type="hidden" name="bookNo" value="${book.bookNo}">
                                    <button type="submit" class="btn btn-sm btn-primary">借阅</button>
                                </form>
                            </c:if>
                            <c:if test="${book.stock == 0}">
                                <!-- 优化预约状态判断逻辑 -->
                                <c:set var="isReserved" value="false"/>
                                <c:if test="${not empty reservedBooks}">
                                    <c:forEach items="${reservedBooks}" var="reservedBook">
                                        <c:if test="${(reservedBook.userName == loginUser.username)&&(reservedBook.bookName==book.bookName)&&(reservedBook.status=='pending')}">
                                            <c:set var="isReserved" value="true"/>
                                        </c:if>
                                    </c:forEach>
                                </c:if>

                                <c:choose>
                                    <c:when test="${isReserved}">
                                        <!-- 已预约：显示取消预约按钮 -->
                                        <form action="${pageContext.request.contextPath}/reservation/cancel" method="post" style="display: inline;">
                                            <input type="hidden" name="bookNo" value="${book.bookNo}">
                                            <input type="hidden" name="bookName" value="${book.bookName}">
                                            <button type="submit" class="btn btn-sm btn-danger">取消预约</button>
                                        </form>
                                    </c:when>
                                    <c:otherwise>
                                        <!-- 未预约：显示预约按钮 -->
                                        <form action="${pageContext.request.contextPath}/reservation/add" method="post" style="display: inline;">
                                            <input type="hidden" name="bookNo" value="${book.bookNo}">
                                            <input type="hidden" name="bookName" value="${book.bookName}">
                                            <button type="submit" class="btn btn-sm btn-warning">预约</button>
                                        </form>
                                        <span class="text-danger ms-2">库存不足</span>
                                    </c:otherwise>
                                </c:choose>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <!-- 空数据提示 -->
                <tr>
                    <td colspan="5" class="empty-data">暂无图书数据</td>
                </tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>
</div>

<script>
    // 关闭弹窗函数（同时移除遮罩层）
    function closeModal() {
        const modal = document.querySelector('.modal');
        const backdrop = document.querySelector('.modal-backdrop');
        if (modal) modal.remove();
        if (backdrop) backdrop.remove();
    }

    // 3秒后自动关闭弹窗（提升用户体验）
    setTimeout(() => {
        closeModal();
    }, 3000);
</script>

<!-- 引入Bootstrap JS确保模态框功能正常 -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>