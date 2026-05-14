package com.wn.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @Description:
 * @Author:kaige
 * @Date:2025/11/22 16:44
 */

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet{
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            // 销毁Session
            HttpSession session = request.getSession();
            session.invalidate();
            // 跳转登录页
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        }
}
