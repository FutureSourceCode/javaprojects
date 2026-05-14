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
import java.sql.SQLException;
/**
 * @Description:个人信息服务
 * @Author:kaige
 * @Date:2025/11/24 9:33
 */

@WebServlet("/user/info")
public class UserInfoServlet extends HttpServlet {
    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        try {
            // 获取最新用户信息
            User user = userDao.findUserByUsername(loginUser.getUsername());

            request.setAttribute("user", user);
            request.getRequestDispatcher("/user/info/userInfo.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMsg", "获取个人信息失败：" + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}