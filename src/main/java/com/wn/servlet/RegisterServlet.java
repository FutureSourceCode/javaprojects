package com.wn.servlet;

import com.wn.dao.UserDao;
import com.wn.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @Description:
 * @Author:kaige
 * @Date:2025/11/23 11:51
 */

@WebServlet("/register")
public class RegisterServlet extends HttpServlet{
        private UserDao userDao = new UserDao();

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");

            // 获取表单参数
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String repassword = request.getParameter("repassword");

            // 表单验证
            if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
                request.setAttribute("msg", "用户名和密码不能为空！");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }
            if (!password.equals(repassword)) {
                request.setAttribute("msg", "两次密码输入不一致！");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
                return;
            }

            // 检查用户名是否已存在
            try {
                if (userDao.findUserByUsername(username) != null) {
                    request.setAttribute("msg", "用户名已存在！");
                    request.getRequestDispatcher("/register.jsp").forward(request, response);
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // 保存用户
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setRole("user"); // 默认普通用户

            if (userDao.register(user)) {
                // 注册成功，跳转登录页并提示
                request.setAttribute("msg", "注册成功，请登录！");
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            } else {
                request.setAttribute("msg", "注册失败，请重试！");
                request.getRequestDispatcher("/register.jsp").forward(request, response);
            }
        }
}
