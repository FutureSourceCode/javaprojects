package com.wn.servlet;
import com.wn.dao.BookDao;
import com.wn.dao.BorrowDao;
import com.wn.entity.Book;
import com.wn.entity.Borrow;
import com.wn.entity.User;
import com.wn.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
/**
 * @Description:普通用户借阅服务
 * @Author:kaige
 * @Date:2025/11/22 21:39
 */

@WebServlet("/borrow/*")
public class BorrowServlet extends HttpServlet{

        private BorrowDao borrowDao = new BorrowDao();
        private BookDao bookDao = new BookDao();

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String path = request.getPathInfo();
            HttpSession session = request.getSession();
            User loginUser = (User) session.getAttribute("loginUser");

            if (loginUser == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            switch (path) {
                case "/list":
                    // 查询用户借阅记录
                    List<Borrow> borrows = null;
                    try {
                        borrows = borrowDao.getBorrowsByUserId(loginUser.getId());
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    request.setAttribute("borrows", borrows);
                    request.getRequestDispatcher("/user/borrow/list.jsp").forward(request, response);
                    break;
                case "/return":
                    // 归还图书
                    String borrowNo = request.getParameter("bookNo");
                    int id = Integer.parseInt(request.getParameter("id"));

                    Connection conn = null;
                    try {

                        conn = DBUtil.getConnection();
                        conn.setAutoCommit(false);
                        borrowDao.returnBook(id);
                        BookDao bookDao = new BookDao();
                        Book book=bookDao.findByBookNo(borrowNo,conn);
                        book.setStock(book.getStock() + 1);

                        bookDao.updateStock(book, conn);

                        conn.commit();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    response.sendRedirect(request.getContextPath() + "/borrow/list");
                    break;
            }
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            String path = request.getPathInfo();
            HttpSession session = request.getSession();
            User loginUser = (User) session.getAttribute("loginUser");

            if (loginUser == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            if (path.equals("/borrowBook")) {
                String bookNo = request.getParameter("bookNo");
                Connection conn = null;
                try {
                    // 获取数据库连接并开启事务
                    conn = DBUtil.getConnection();
                    conn.setAutoCommit(false);

                    Book book = bookDao.findByBookNo(bookNo, conn);

                    if (book != null && book.getStock() > 0) {
                        // 创建借阅记录
                        Borrow borrow = new Borrow();
                        borrow.setBookNo(book.getBookNo());
                        borrow.setUserId(loginUser.getId());
                        borrow.setBorrowData(new Date(System.currentTimeMillis()));
                        borrowDao.borrowBook(borrow);

                        // 减少库存
                        book.setStock(book.getStock() - 1);
                        bookDao.updateStock(book, conn);

                        // 提交事务
                        conn.commit();
                    } else {
                        // 库存不足时的提示
                        request.setAttribute("msg", "图书库存不足或不存在！");
                        request.getRequestDispatcher("/user/book/search").forward(request, response);
                        return;
                    }
                } catch (SQLException e) {
                    // 回滚事务
                    if (conn != null) {
                        try {
                            conn.rollback();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                    throw new RuntimeException(e);
                } finally {
                    DBUtil.close(conn, null, null); // 关闭连接
                }

                response.sendRedirect(request.getContextPath() + "/user/book/search");
            }
        }
}
