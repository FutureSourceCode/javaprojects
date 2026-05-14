package com.wn.servlet;

import com.wn.dao.BookDao;
import com.wn.dao.ReservationDao;
import com.wn.entity.Book;
import com.wn.entity.Reservation;
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
 * @Description:
 * @Author:kaige
 * @Date:2025/11/23 15:58
 */

@WebServlet("/reservation/*")
public class ReservationServlet extends HttpServlet {
    private ReservationDao reservationDao = new ReservationDao();
    private BookDao bookDao = new BookDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String path = request.getPathInfo();

        if ("/list".equals(path)) {
            try {
                List<Reservation> reservations = reservationDao.getReservationsByUserName(user.getUsername());

                request.setAttribute("reservations", reservations);
                request.getRequestDispatcher("/user/reservation/view.jsp").forward(request, response);
            } catch (SQLException e) {
                request.setAttribute("errorMsg", "查询预约失败");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
            }
        }


    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginUser");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }
        String path = request.getPathInfo();

        if ("/add".equals(path)) {
            String bookNo = request.getParameter("bookNo");
            System.out.println("进来了");
            try {
                Book book = bookDao.findByBookNo(bookNo, null);
                if (book != null && book.getStock() == 0) { // 只有库存为0时可预约
                    Reservation reservation = new Reservation();
                    reservation.setBookName(book.getBookName());
                    reservation.setUserName(user.getUsername());
                    reservation.setReserveTime(new Date());
                    reservationDao.addReservation(reservation);
                    request.setAttribute("msg", "预约成功");
                } else {
                    request.setAttribute("msg", "图书可直接借阅或不存在");
                }
            } catch (SQLException e) {
                request.setAttribute("msg", "预约失败：" + e.getMessage());
            }
            // 转发回图书查询页面
            try {
                request.setAttribute("books", bookDao.findAll());
                // 重新查询最新的预约列表（确保前端预约状态更新）
                request.setAttribute("reservedBooks", reservationDao.getReservationsByUserName(user.getUsername()));
                request.getRequestDispatcher("/user/book/search.jsp").forward(request, response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        if ("/cancel".equals(path)) {
            // 1. 从前端获取书名参数
            String bookName = request.getParameter("bookName");
            try {
                // 2. 调用带用户名+书名的取消方法
                boolean success = reservationDao.cancelReservation(user.getUsername(), bookName);

                if (success) {
                    request.setAttribute("tipMsg", "取消预约成功！");
                } else {
                    request.setAttribute("tipMsg", "取消失败，预约记录不存在！");
                }
            } catch (SQLException e) {
                request.setAttribute("tipMsg", "系统异常：" + e.getMessage());
                e.printStackTrace();
            }

            // 转发回图书查询页面
            try {
                request.setAttribute("books", bookDao.findAll());
                // 重新查询最新的预约列表
                request.setAttribute("reservedBooks", reservationDao.getReservationsByUserName(user.getUsername()));
                request.getRequestDispatcher("/user/book/search.jsp").forward(request, response);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}