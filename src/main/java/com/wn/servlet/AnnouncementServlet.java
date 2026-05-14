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
 * @Description:用户公告服务类
 * @Author:kaige
 * @Date:2025/11/24 11:27
 */

@WebServlet("/announcement/*")
public class AnnouncementServlet extends HttpServlet {
    private AnnouncementDao announcementDao = new AnnouncementDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();


        // 普通用户公告列表（支持日期筛选）
        if ("/list".equals(path) || path == null) {
            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");
            List<Announcement> announcements;

            try {
                if ((startDate == null || startDate.isEmpty()) && (endDate == null || endDate.isEmpty())) {
                    // 无筛选条件时查询所有公告
                    announcements = announcementDao.findAll();
                } else {
                    // 按日期范围查询
                    announcements = announcementDao.findByDateRange(startDate, endDate);
                }
                request.setAttribute("announcements", announcements);
                request.setAttribute("startDate", startDate);
                request.setAttribute("endDate", endDate);
            } catch (SQLException e) {
                request.setAttribute("errorMsg", "查询公告失败：" + e.getMessage());
                e.printStackTrace();
            }
            request.getRequestDispatcher("/user/ann/list.jsp").forward(request, response);
        }

        // 管理员公告管理列表
        else if ("/admin/list".equals(path)) {
            // 检查管理员权限
            if (!checkAdminRole(request, response)) return;

            String startDate = request.getParameter("startDate");
            String endDate = request.getParameter("endDate");

            try {
                List<Announcement> announcements;
                if ((startDate == null || startDate.isEmpty()) && (endDate == null || endDate.isEmpty())) {
                    announcements = announcementDao.findAll();
                } else {
                    announcements = announcementDao.findByDateRange(startDate, endDate);
                }
                request.setAttribute("announcements", announcements);
                request.setAttribute("startDate", startDate);
                request.setAttribute("endDate", endDate);
            } catch (SQLException e) {
                request.setAttribute("errorMsg", "查询公告失败：" + e.getMessage());
                e.printStackTrace();
            }
            request.getRequestDispatcher("/announcement/adminList.jsp").forward(request, response);
        }

        // 管理员删除公告
        else if ("/admin/delete".equals(path)) {
            // 检查管理员权限
            if (!checkAdminRole(request, response)) return;

            try {
                int annId = Integer.parseInt(request.getParameter("id"));
                boolean success = announcementDao.deleteAnnouncement(annId);
                if (success) {
                    request.setAttribute("tipMsg", "公告删除成功！");
                } else {
                    request.setAttribute("errorMsg", "公告删除失败，记录不存在！");
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMsg", "公告ID格式错误！");
                e.printStackTrace();
            } catch (SQLException e) {
                request.setAttribute("errorMsg", "删除公告失败：" + e.getMessage());
                e.printStackTrace();
            }
            // 重定向回管理员列表页
            response.sendRedirect(request.getContextPath() + "/announcement/admin/list");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        // 普通发布公告（保留兼容）
        if ("/add".equals(path)) {
            if (!checkAdminRole(request, response)) return;
            publishAnnouncement(request, response);
        }

        // 管理员发布公告（集成在管理页）
        else if ("/admin/add".equals(path)) {
            if (!checkAdminRole(request, response)) return;
            publishAnnouncement(request, response);
            // 发布后刷新管理员列表页
            response.sendRedirect(request.getContextPath() + "/announcement/admin/list");
        }
    }

    /**
     * 检查是否为管理员权限
     */
    private boolean checkAdminRole(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null || !"admin".equals(loginUser.getRole())) {
            // 非管理员跳转到普通公告列表
            response.sendRedirect(request.getContextPath() + "/announcement/list");
            return false;
        }
        return true;
    }

    /**
     * 发布公告的通用方法
     */
    private void publishAnnouncement(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");

        String content = request.getParameter("content");
        if (content == null || content.trim().isEmpty()) {
            request.setAttribute("errorMsg", "公告内容不能为空！");
            request.getRequestDispatcher("/announcement/adminList.jsp").forward(request, response);
            return;
        }

        Announcement announcement = new Announcement();
        announcement.setContent(content);
        announcement.setPublisher(loginUser.getUsername());
        announcement.setPublishTime(new Date());

        try {
            boolean success = announcementDao.addAnnouncement(announcement);
            if (success) {
                request.setAttribute("tipMsg", "公告发布成功！");
            } else {
                request.setAttribute("errorMsg", "公告发布失败！");
            }
        } catch (SQLException e) {
            request.setAttribute("errorMsg", "发布公告失败：" + e.getMessage());
            e.printStackTrace();
        }
    }
}