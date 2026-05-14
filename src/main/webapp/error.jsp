<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>系统错误</title>
    <style>
        body {
            font-family: "Microsoft YaHei", sans-serif;
            background-color: #f5f5f5;
            margin: 0;
            padding: 0;
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: 100vh;
        }
        .error-container {
            background-color: white;
            padding: 30px 50px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            text-align: center;
        }
        .error-icon {
            color: #e74c3c;
            font-size: 60px;
            margin-bottom: 20px;
        }
        h1 {
            color: #333;
            margin-bottom: 15px;
        }
        .error-message {
            color: #666;
            margin-bottom: 30px;
            padding: 15px;
            background-color: #f8f9fa;
            border-radius: 4px;
            border-left: 4px solid #e74c3c;
        }
        .back-button {
            display: inline-block;
            padding: 10px 20px;
            background-color: #3498db;
            color: white;
            text-decoration: none;
            border-radius: 4px;
            transition: background-color 0.3s;
            margin: 0 5px;
        }
        .back-button:hover {
            background-color: #2980b9;
        }
    </style>
</head>
<body>
<div class="error-container">
    <div class="error-icon">⚠️</div>
    <h1>系统发生错误</h1>
    <div class="error-message">
        <%
            // 获取错误信息，若不存在则显示默认信息
            String errorMsg = (String) request.getAttribute("errorMsg");
            if (errorMsg == null || errorMsg.trim().isEmpty()) {
                errorMsg = "服务器暂时无法处理您的请求，请稍后再试";
            }
        %>
        <%= errorMsg %> <!-- 改用JSP表达式输出，避免out.print的IDE识别问题 -->
    </div>
    <a href="javascript:history.back()" class="back-button">返回上一页</a>
    <a href="${pageContext.request.contextPath}/login.jsp" class="back-button">返回首页</a>
</div>
</body>
</html>