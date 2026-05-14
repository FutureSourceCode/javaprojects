<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>修改密码</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/background/style.css">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .password-card {
            max-width: 500px;
            margin: 50px auto;
            padding: 20px;
            box-shadow: 0 0 15px rgba(0,0,0,0.1);
            border-radius: 8px;
            background-color: #fff;
        }
        .form-title {
            text-align: center;
            margin-bottom: 25px;
            color: #333;
            font-weight: bold;
        }
        .btn-group {
            display: flex;
            gap: 10px;
            justify-content: center;
            margin-top: 20px;
        }
    </style>

</head>
<body>
<div class="container">
    <!-- 修改密码卡片 -->
    <div class="password-card">
        <h3 class="form-title">修改密码</h3>

        <!-- 提示信息 -->
        <c:if test="${not empty tipMsg}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                    ${tipMsg}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        <c:if test="${not empty errorMsg}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    ${errorMsg}
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <!-- 修改密码表单 -->
        <form action="${pageContext.request.contextPath}/user/updatePassword" method="post" onsubmit="return validateForm()">
            <div class="mb-3">
                <label for="oldPwd" class="form-label">原密码：</label>
                <input type="password" class="form-control" id="oldPwd" name="oldPwd" required placeholder="请输入原密码">
            </div>
            <div class="mb-3">
                <label for="newPwd" class="form-label">新密码：</label>
                <input type="password" class="form-control" id="newPwd" name="newPwd" required placeholder="请输入新密码">
                <!-- 移除密码长度提示 -->
            </div>
            <div class="mb-3">
                <label for="confirmPwd" class="form-label">确认新密码：</label>
                <input type="password" class="form-control" id="confirmPwd" name="confirmPwd" required placeholder="请再次输入新密码">
                <div id="pwdError" class="text-danger d-none">两次输入的密码不一致</div>
            </div>
            <div class="btn-group">
                <button type="submit" class="btn btn-primary">提交修改</button>
                <a href="${pageContext.request.contextPath}/user/info" class="btn btn-outline-secondary">返回个人中心</a>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // 表单验证：仅检查密码一致性
    function validateForm() {
        const newPwd = document.getElementById('newPwd').value;
        const confirmPwd = document.getElementById('confirmPwd').value;
        const errorDiv = document.getElementById('pwdError');

        if (newPwd !== confirmPwd) {
            errorDiv.classList.remove('d-none');
            return false; // 阻止表单提交
        } else {
            errorDiv.classList.add('d-none');
            return true;
        }
    }

    // 实时监听确认密码输入
    document.getElementById('confirmPwd').addEventListener('input', function() {
        const newPwd = document.getElementById('newPwd').value;
        const errorDiv = document.getElementById('pwdError');

        if (this.value !== '' && this.value !== newPwd) {
            errorDiv.classList.remove('d-none');
        } else {
            errorDiv.classList.add('d-none');
        }
    });
</script>
</body>
</html>