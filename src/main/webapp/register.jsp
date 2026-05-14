<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>图书管理系统-注册</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/background/style.css">
</head>
<body>
<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header text-center">
                    <h3>用户注册</h3>
                </div>
                <div class="card-body">
                    <!-- 注册表单 -->
                    <form action="${pageContext.request.contextPath}/register" method="post">
                        <div class="mb-3">
                            <label class="form-label">用户名</label>
                            <input type="text" name="username" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">密码</label>
                            <input type="password" name="password" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">确认密码</label>
                            <input type="password" name="repassword" class="form-control" required>
                        </div>
                        <!-- 注册结果提示 -->
                        <div class="text-danger mb-3">${msg}</div>
                        <button type="submit" class="btn btn-success w-100">注册</button>
                    </form>
                    <!-- 返回登录入口 -->
                    <div class="text-center mt-3">
                        <span>已有账号？</span>
                        <a href="${pageContext.request.contextPath}/login.jsp">立即登录</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>