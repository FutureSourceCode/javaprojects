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
import java.util.List;
/**
 * @Description:
 * @Author:kaige
 * @Date:2025/11/22 16:43
 */
@WebServlet("/user/book/search")
public class UserBookServlet extends HttpServlet {
    private BookDao bookDao = new BookDao();
    private ReservationDao reservationDao = new ReservationDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("loginUser");
        try {

            List<Book> books = bookDao.findAll(); // 查询所有图书
            List<Reservation> rbooks = reservationDao.getReservationsByUserName(user.getUsername());
            request.setAttribute("reservedBooks",rbooks);
            request.setAttribute("books", books);
            // 转发到查询界面JSP
            request.getRequestDispatcher("/user/book/search.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "加载图书失败");
            request.getRequestDispatcher("/user/book/search.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 获取表单提交的参数（书号和关键字）
        String bookNo = request.getParameter("bookNo");
        String keyword = request.getParameter("keyword");

        // 处理空参数（去除首尾空格，空字符串视为无输入）
        bookNo = (bookNo == null) ? "" : bookNo.trim();
        keyword = (keyword == null) ? "" : keyword.trim();

        try {
            List<Book> books;
            if (bookNo.isEmpty() && keyword.isEmpty()) {
                books = bookDao.findAll();
            } else {
                books = bookDao.search(bookNo, keyword);
            }
            request.setAttribute("books", books);
            request.getRequestDispatcher("/user/book/search.jsp").forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "查询失败");
            request.getRequestDispatcher("/user/book/search.jsp").forward(request, response);
        }
    }
}
