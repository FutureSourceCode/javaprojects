package com.wn.servlet;

import com.wn.dao.BookDao;
import com.wn.entity.Book;
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

@WebServlet("/admin/book/*")
public class AdminBookServlet extends HttpServlet {

    private BookDao bookDAO = new BookDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // 1. 权限校验
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginUser");
        if (loginUser == null || !"admin".equals(loginUser.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        // 2. 处理path为null的情况
        String path = request.getPathInfo();

        if (path == null) {
            path = "/list"; // 默认访问图书列表
        }

        try {
            switch (path) {
                case "/list":
                    List<Book> books = bookDAO.findAll();
                    request.setAttribute("books", books);
                    request.getRequestDispatcher("/admin/books/list.jsp").forward(request, response);

                    break;
                case "/add":
                    request.getRequestDispatcher("/admin/books/add.jsp").forward(request, response);
                    break;
                case "/edit":
                    // 3. 校验ID参数
                    String idStr = request.getParameter("id");
                    if (idStr == null || !idStr.matches("\\d+")) {
                        response.sendError(400, "无效的图书ID");
                        return;
                    }
                    Integer id = Integer.parseInt(idStr);
                    Book book = bookDAO.findById(id);
                    if (book == null) {
                        response.sendError(404, "图书不存在");
                        return;
                    }
                    request.setAttribute("book", book);
                    request.getRequestDispatcher("/admin/books/edit.jsp").forward(request, response);
                    break;
                case "/delete":

                    System.out.println("开始删除");
                    String delIdStr = request.getParameter("id");
                    String delNameStr = request.getParameter("id");

                    System.out.println(delNameStr);

                    if (delIdStr == null || !delIdStr.matches("\\d+")) {
                        response.sendError(400, "无效的图书ID");
                        return;
                    }
                    Integer delId = Integer.parseInt(delIdStr);
                    bookDAO.delete(delId,delNameStr);
                    response.sendRedirect(request.getContextPath() + "/admin/book/list");
                    break;
                default:
                    response.sendError(404, "请求路径不存在");
            }
        } catch (SQLException e) {
            // 处理数据库异常
            request.setAttribute("errorMsg", "数据库操作失败：" + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMsg", "系统异常：" + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String path = request.getPathInfo();
        if (path == null) {
            response.sendError(400, "无效的请求路径");
            return;
        }

        try {
            switch (path) {
                case "/add":
                    Book addBook = new Book();
                    addBook.setBookNo(request.getParameter("bookNo"));
                    addBook.setBookName(request.getParameter("bookName"));
                    addBook.setAuthor(request.getParameter("author"));
                    // 校验库存参数
                    String stockStr = request.getParameter("stock");
                    if (stockStr == null || !stockStr.matches("\\d+")) {
                        request.setAttribute("msg", "库存必须为数字");
                        request.getRequestDispatcher("/admin/books/add.jsp").forward(request, response);
                        return;
                    }
                    addBook.setStock(Integer.parseInt(stockStr));
                    bookDAO.add(addBook);
                    response.sendRedirect(request.getContextPath() + "/admin/book/list");
                    break;
                case "/edit":
                    String idStr = request.getParameter("id");
                    if (idStr == null || !idStr.matches("\\d+")) {
                        response.sendError(400, "无效的图书ID");
                        return;
                    }
                    Book editBook = new Book();
                    editBook.setId(Integer.parseInt(idStr));
                    editBook.setBookNo(request.getParameter("bookNo"));
                    editBook.setBookName(request.getParameter("bookName"));
                    editBook.setAuthor(request.getParameter("author"));
                    String editStockStr = request.getParameter("stock");
                    if (editStockStr == null || !editStockStr.matches("\\d+")) {
                        request.setAttribute("msg", "库存必须为数字");
                        request.getRequestDispatcher("/admin/books/edit.jsp").forward(request, response);
                        return;
                    }
                    editBook.setStock(Integer.parseInt(editStockStr));
                    bookDAO.update(editBook);
                    response.sendRedirect(request.getContextPath() + "/admin/book/list");
                    break;
                default:
                    response.sendError(404, "请求路径不存在");
            }
        } catch (SQLException e) {
            request.setAttribute("errorMsg", "数据库操作失败：" + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("errorMsg", "系统异常：" + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}