package com.wn.servlet;

import com.wn.dao.BookDao;
import com.wn.dao.ReservationDao;
import com.wn.dao.AnnouncementDao;
import com.wn.entity.Book;
import com.wn.entity.Reservation;
import com.wn.entity.Announcement;
import com.wn.entity.User;
import com.wn.util.DBUtil;

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
 * @Description:预约管理服务
 * @Author:kaige
 * @Date:2025/11/24 13:58
 */

@WebServlet("/admin/reservation/*")
public class ReservationManageServlet extends HttpServlet {
    private ReservationDao reservationDao = new ReservationDao();
    private AnnouncementDao announcementDao = new AnnouncementDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // 验证管理员权限
        if (!checkAdminRole(request, response)) return;

        String path = request.getPathInfo();

        if ("/list".equals(path) || path == null) {
            // 预约列表
            handleReservationList(request, response);
        } else if ("/notify".equals(path)) {
            // 提醒书籍到库并生成公告
            handleNotifyArrival(request, response);
        }
    }

    /**
     * 处理预约列表展示
     */
    private void handleReservationList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 查询所有预约记录
            List<Reservation> reservations = reservationDao.findAll();

            request.setAttribute("reservations", reservations);
        } catch (SQLException e) {
            request.setAttribute("errorMsg", "查询预约记录失败：" + e.getMessage());
            e.printStackTrace();
        }
        request.getRequestDispatcher("/admin/res/reservationList.jsp").forward(request, response);
    }

    /**
     * 处理书籍到库提醒并生成公告
     */
    private void handleNotifyArrival(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int resId = Integer.parseInt(request.getParameter("id"));
            Reservation reservation = reservationDao.findById(resId);
            if (reservation == null) {
                request.getSession().setAttribute("errorMsg", "预约记录不存在！");
                response.sendRedirect(request.getContextPath() + "/admin/reservation/list");
                return;
            }

            // 自动生成公告内容
            String bookName = reservation.getBookName();
            String username = reservation.getUserName();
            String content = String.format("您预约的《%s》已到库，请尽快到图书馆办理借阅手续！（预约人：%s）", bookName, username);

            // 获取当前管理员信息
            HttpSession session = request.getSession();
            User loginUser = (User) session.getAttribute("loginUser");

            // 创建公告
            Announcement announcement = new Announcement();
            announcement.setContent(content);
            announcement.setPublisher(loginUser.getUsername());
            announcement.setPublishTime(new Date());

            // 保存公告并标记预约为已提醒
            announcementDao.addAnnouncement(announcement);
            reservationDao.markNotified(resId); // 更新预约状态为已提醒

            request.getSession().setAttribute("tipMsg", "已发送到库提醒，公告已生成！");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("errorMsg", "预约ID格式错误！");
            e.printStackTrace();
        } catch (SQLException e) {
            request.getSession().setAttribute("errorMsg", "发送提醒失败：" + e.getMessage());
            e.printStackTrace();
        }
        response.sendRedirect(request.getContextPath() + "/admin/reservation/list");
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
