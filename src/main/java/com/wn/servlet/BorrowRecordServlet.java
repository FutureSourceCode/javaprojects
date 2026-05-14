package com.wn.servlet;

import com.wn.dao.BorrowRecordDao;
import com.wn.dao.AnnouncementDao;
import com.wn.entity.BorrowRecord;
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

@WebServlet("/admin/borrow/*")
public class BorrowRecordServlet extends HttpServlet {
    private BorrowRecordDao borrowDao = new BorrowRecordDao();
    private AnnouncementDao announcementDao = new AnnouncementDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // 验证管理员权限
        if (!checkAdminRole(request, response)) return;

        String path = request.getPathInfo();

        if ("/list".equals(path) || path == null) {
            // 借阅记录列表+按借阅者查询
            handleBorrowList(request, response);
        } else if ("/remind".equals(path)) {
            // 提醒还书（生成公告）
            handleRemindReturn(request, response);
        }
    }

    /**
     * 处理借阅记录列表和查询
     */
    private void handleBorrowList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username"); // 按借阅者查询

        try {
            List<BorrowRecord> borrowRecords;
            if (username == null || username.trim().isEmpty()) {
                // 默认显示所有借阅记录
                borrowRecords = borrowDao.findAll();
            } else {
                // 按借阅者查询
                borrowRecords = borrowDao.findByUsername(username);
            }
            request.setAttribute("borrowRecords", borrowRecords);
            request.setAttribute("queryUsername", username); // 回显查询条件
        } catch (SQLException e) {
            request.setAttribute("errorMsg", "查询借阅记录失败：" + e.getMessage());
            e.printStackTrace();
        }
        request.getRequestDispatcher("/admin/record/borrowRecordList.jsp").forward(request, response);
    }

    /**
     * 处理提醒还书（生成公告）
     */
    private void handleRemindReturn(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int borrowId = Integer.parseInt(request.getParameter("id"));
            BorrowRecord borrowRecord = borrowDao.findById(borrowId);

            if (borrowRecord == null) {
                request.getSession().setAttribute("errorMsg", "借阅记录不存在！");
                response.sendRedirect(request.getContextPath() + "/admin/borrow/list");
                return;
            }

            // 自动生成提醒还书公告内容
            String bookName = borrowRecord.getBookName();
            String username = borrowRecord.getUsername();
            String content = String.format("【还书提醒】您借阅的《%s》已到归还日期，请尽快到图书馆办理归还手续！（借阅者：%s）", bookName, username);

            // 获取当前管理员信息
            HttpSession session = request.getSession();
            User loginUser = (User) session.getAttribute("loginUser");

            // 创建并发布公告
            Announcement announcement = new Announcement();
            announcement.setContent(content);
            announcement.setPublisher(loginUser.getUsername());
            announcement.setPublishTime(new Date());

            announcementDao.addAnnouncement(announcement);
            request.getSession().setAttribute("tipMsg", "已发送还书提醒，公告已生成！");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMsg", "借阅记录ID格式错误！");
            e.printStackTrace();
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMsg", "发送提醒失败：" + e.getMessage());
            e.printStackTrace();
        }
        // 重定向回列表（保留查询条件）
        String username = request.getParameter("username");
        String redirectUrl = request.getContextPath() + "/admin/borrow/list";
        if (username != null && !username.trim().isEmpty()) {
            redirectUrl += "?username=" + username;
        }
        response.sendRedirect(redirectUrl);
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