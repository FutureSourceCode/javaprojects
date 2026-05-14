package com.wn.servlet;

import com.wn.dao.AnnouncementDao;
import com.wn.entity.Announcement;
import com.wn.entity.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * @Description:管理员管理公告服务类
 * @Author:kaige
 * @Date:2025/11/24 13:28
 */

@WebServlet("/admin/announcement/*")
public class AnnouncementManageServlet extends HttpServlet {
    private AnnouncementDao announcementDao = new AnnouncementDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // 验证管理员权限
        if (!checkAdminRole(request, response)) return;

        String path = request.getPathInfo();

        if ("/list".equals(path) || path == null) {
            // 公告列表+日期查询
            handleAnnouncementList(request, response);
        } else if ("/delete".equals(path)) {
            // 删除公告
            handleAnnouncementDelete(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // 验证管理员权限
        if (!checkAdminRole(request, response)) return;

        String path = request.getPathInfo();

        if ("/add".equals(path)) {
            // 发布公告
            handleAnnouncementAdd(request, response);
        }
    }

    /**
     * 处理公告列表和日期查询
     */
    private void handleAnnouncementList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        try {
            List<Announcement> announcements;
            // 按日期范围查询或查询所有
            if ((startDate == null || startDate.isEmpty()) && (endDate == null || endDate.isEmpty())) {
                announcements = announcementDao.findAll();
            } else {
                announcements = announcementDao.findByDateRange(startDate, endDate);
            }
            request.setAttribute("announcements", announcements);
            request.setAttribute("startDate", startDate); // 回显查询条件
            request.setAttribute("endDate", endDate);
        } catch (SQLException e) {
            request.setAttribute("errorMsg", "查询公告失败：" + e.getMessage());
            e.printStackTrace();
        }
        request.getRequestDispatcher("/admin/ann/adminList.jsp").forward(request, response);
    }

    /**
     * 处理发布公告
     */
    private void handleAnnouncementAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String content = request.getParameter("content");
        if (content == null || content.trim().isEmpty()) {
            request.setAttribute("errorMsg", "公告内容不能为空！");
            request.getRequestDispatcher("/admin/ann/adminList.jsp").forward(request, response);
            return;
        }

        // 获取当前登录管理员信息
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        Announcement announcement = new Announcement();
        announcement.setContent(content);
        announcement.setPublisher(loginUser.getUsername());
        announcement.setPublishTime(new Date());

        try {
            announcementDao.addAnnouncement(announcement);
            request.setAttribute("tipMsg", "公告发布成功！");
        } catch (SQLException e) {
            request.setAttribute("errorMsg", "发布公告失败：" + e.getMessage());
            e.printStackTrace();
        }
        // 刷新公告列表
        handleAnnouncementList(request, response);
    }

    /**
     * 处理删除公告
     */
    private void handleAnnouncementDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int annId = Integer.parseInt(request.getParameter("id"));
            boolean success = announcementDao.deleteAnnouncement(annId);

            // 回显查询条件
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            String redirectUrl = request.getContextPath() + "/admin/announcement/list";

            if ((startDate != null && !startDate.isEmpty()) || (endDate != null && !endDate.isEmpty())) {
                redirectUrl += "?startDate=" + (startDate == null ? "" : startDate) + "&endDate=" + (endDate == null ? "" : endDate);
            }

            if (success) {
                request.getSession().setAttribute("tipMsg", "公告删除成功！");
            } else {
                request.getSession().setAttribute("errorMsg", "公告删除失败或不存在！");
            }
            response.sendRedirect(redirectUrl);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMsg", "公告ID格式错误！");
            response.sendRedirect(request.getContextPath() + "/admin/announcement/list");
            e.printStackTrace();
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMsg", "删除公告失败：" + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/announcement/list");
            e.printStackTrace();
        }
    }

    /**
     * 检查管理员权限
     */
    private boolean checkAdminRole(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !"admin".equals(loginUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return false;
        }
        return true;
    }
}