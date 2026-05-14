package com.wn.servlet;

import com.wn.dao.UserDao;
import com.wn.entity.User;
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
 * @Date:2025/11/22 16:42
 */
@WebServlet("/login")
public class LoginServlet extends HttpServlet{
        private UserDao userDAO = new UserDao();

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            // 设置编码
            request.setCharacterEncoding("UTF-8");
            response.setContentType("text/html;charset=UTF-8");

            // 获取参数
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            // 登录验证
            User user = userDAO.login(username, password);
            if (user != null) {
                // 存储用户信息到Session
                HttpSession session = request.getSession();
                session.setAttribute("loginUser", user);

                // 根据角色跳转
                if ("admin".equals(user.getRole())) {
                    response.sendRedirect(request.getContextPath() + "/admin/login.jsp");
                } else {
                    response.sendRedirect(request.getContextPath() + "/user/login.jsp");
                }
            } else {
                // 登录失败，返回登录页并提示
                request.setAttribute("msg", "用户名或密码错误！");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            }
        }
}
