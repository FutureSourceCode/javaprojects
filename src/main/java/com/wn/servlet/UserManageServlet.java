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
import java.util.List;

@WebServlet("/admin/user/*")
public class UserManageServlet extends HttpServlet {
    // 初始化UserDao实例
    private UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 统一设置请求编码（防止中文乱码）
        request.setCharacterEncoding("UTF-8");

        // 验证管理员权限，非管理员直接跳转登录页
        if (!checkAdminRole(request, response)) {
            return;
        }

        // 获取请求路径（如/list、/delete）
        String path = request.getPathInfo();

        // 处理用户列表/搜索请求
        if ("/list".equals(path) || path == null) {
            handleUserList(request, response);
        }
        // 处理删除用户请求
        else if ("/delete".equals(path)) {
            handleUserDelete(request, response);
        }
    }

    /**
     * 处理用户列表展示和搜索逻辑
     */
    private void handleUserList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取搜索参数（关键词、角色）
        String keyword = request.getParameter("keyword");
        String role = request.getParameter("role");

        try {
            List<User> userList;
            // 判断是否有搜索条件
            if ((keyword == null || keyword.trim().isEmpty()) && (role == null || role.trim().isEmpty())) {
                // 无搜索条件，查询所有用户
                userList = userDao.findAll();
            } else {
                // 有搜索条件，按关键词+角色筛选
                userList = userDao.searchUsers(keyword, role);
            }
            // 将用户列表和搜索条件存入request，供页面展示
            request.setAttribute("userList", userList);
            request.setAttribute("keyword", keyword);
            request.setAttribute("role", role);
        } catch (SQLException e) {
            // 捕获数据库异常，存入错误信息
            request.setAttribute("errorMsg", "查询用户失败：" + e.getMessage());
            e.printStackTrace();
        }
        // 转发到用户列表页面
        request.getRequestDispatcher("/admin/users/userList.jsp").forward(request, response);
    }

    /**
     * 处理删除用户逻辑
     */
    private void handleUserDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            // 获取要删除的用户ID
            int userId = Integer.parseInt(request.getParameter("id"));
            // 调用Dao删除用户
            boolean success = userDao.deleteById(userId);

            // 获取原搜索条件
            String keyword = request.getParameter("keyword");
            String role = request.getParameter("role");

            // 拼接重定向URL
            String redirectUrl = request.getContextPath() + "/admin/user/list";
            if ((keyword != null && !keyword.trim().isEmpty()) || (role != null && !role.trim().isEmpty())) {
                redirectUrl += "?keyword=" + (keyword == null ? "" : keyword) + "&role=" + (role == null ? "" : role);
            }

            // 根据删除结果设置提示信息
            if (success) {
                request.getSession().setAttribute("tipMsg", "用户删除成功！");
            } else {
                request.getSession().setAttribute("errorMsg", "用户删除失败或不存在！");
            }
            // 重定向回用户列表
            response.sendRedirect(redirectUrl);
        } catch (NumberFormatException e) {
            // 处理ID格式错误
            request.getSession().setAttribute("errorMsg", "用户ID格式错误！");
            response.sendRedirect(request.getContextPath() + "/admin/user/list");
            e.printStackTrace();
        } catch (SQLException e) {
            // 处理数据库删除异常
            request.getSession().setAttribute("errorMsg", "删除用户失败：" + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/user/list");
            e.printStackTrace();
        }
    }

    /**
     * 检查当前登录用户是否为管理员
     * @return true=是管理员，false=非管理员
     */
    private boolean checkAdminRole(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        // 未登录或非管理员，跳转登录页
        if (loginUser == null || !"admin".equals(loginUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return false;
        }
        return true;
    }
}