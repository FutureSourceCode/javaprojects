<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>图书管理系统-登录</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/background/style.css">
</head>

<body>

<div class="container mt-5">
    <div class="row justify-content-center">
        <div class="col-md-6">
            <div class="card">
                <div class="card-header text-center">
                    <h3>图书管理系统</h3>
                </div>
                <div class="card-body">
                    <!-- 登录表单 -->
                    <form action="${pageContext.request.contextPath}/login" method="post">
                        <div class="mb-3">
                            <label class="form-label">用户名</label>
                            <input type="text" name="username" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">密码</label>
                            <input type="password" name="password" class="form-control" required>
                        </div>
                        <!-- 错误提示 -->
                        <div class="text-danger mb-3">${msg}</div>
                        <button type="submit" class="btn btn-primary w-100">登录</button>
                    </form>
                    <!-- 注册入口 -->
                    <div class="text-center mt-3">
                        <span>没有账号？</span>
                        <a href="${pageContext.request.contextPath}/register.jsp">立即注册</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>