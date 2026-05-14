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
 * @Description:密码修改
 * @Author:kaige
 * @Date:2025/11/24 10:42
 */

@WebServlet("/user/updatePassword")
public class UpdatePasswordServlet extends HttpServlet {
    private UserDao userDao = new UserDao();

    // 处理GET请求：跳转到修改密码页面
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            // 未登录，跳转登录页
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        // 已登录，转发到修改密码页面
        request.getRequestDispatcher("/user/info/updatePwd.jsp").forward(request, response);
    }

    // 处理POST请求：执行密码修改逻辑（保持不变）
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 获取表单参数
        String oldPwd = request.getParameter("oldPwd");
        String newPwd = request.getParameter("newPwd");
        String confirmPwd = request.getParameter("confirmPwd");

        try {
            // 验证原密码是否正确
            User user = userDao.findUserByUsername(loginUser.getUsername());

            System.out.println("原密码"+user.getPassword());

            if (!user.getPassword().equals(oldPwd)) {
                request.setAttribute("errorMsg", "原密码错误！");
                request.getRequestDispatcher("/user/info/updatePwd.jsp").forward(request, response);
                return;
            }

            // 验证新密码和确认密码是否一致
            if (!newPwd.equals(confirmPwd)) {
                request.setAttribute("errorMsg", "两次输入的新密码不一致！");
                request.getRequestDispatcher("/user/info/updatePwd.jsp").forward(request, response);
                return;
            }

            // 修改密码
            boolean success = userDao.updatePassword(loginUser.getUsername(), newPwd);
            if (success) {
                request.setAttribute("tipMsg", "密码修改成功！请重新登录");
                session.invalidate(); // 销毁session，要求重新登录
                request.getRequestDispatcher("/login.jsp").forward(request, response);
            } else {
                request.setAttribute("errorMsg", "密码修改失败，请重试！");
                request.getRequestDispatcher("/user/info/updatePwd.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("errorMsg", "系统异常：" + e.getMessage());
            request.getRequestDispatcher("/user/info/updatePwd.jsp").forward(request, response);
        }
    }
}