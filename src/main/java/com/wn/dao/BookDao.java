package com.wn.dao;

import com.wn.entity.Book;
import com.wn.util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * @Description:
 * @Author:kaige
 * @Date:2025/11/22 16:42
 */
public class BookDao {

        // 查询所有图书
        public List<Book> findAll() throws SQLException {
            String sql = "SELECT * FROM book";
            List<Book> books = new ArrayList<>();
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = conn.prepareStatement(sql);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getInt("id"));
                    book.setBookNo(rs.getString("book_no"));
                    book.setBookName(rs.getString("book_name"));
                    book.setAuthor(rs.getString("author"));
                    book.setStock(rs.getInt("stock"));
                    books.add(book);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn, pstmt, rs);
            }
            return books;
        }

        // 按书号/书名搜索
        public List<Book> search(String bookNo, String keyword) throws SQLException {
            String sql = "SELECT * FROM book WHERE book_no=? OR book_name LIKE CONCAT('%', ?, '%')";
            List<Book> books = new ArrayList<>();
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, bookNo);
                pstmt.setString(2, keyword);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getInt("id"));
                    book.setBookNo(rs.getString("book_no"));
                    book.setBookName(rs.getString("book_name"));
                    book.setAuthor(rs.getString("author"));
                    book.setStock(rs.getInt("stock"));
                    books.add(book);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn, pstmt, rs);
            }
            return books;
        }

        // 添加图书
        public boolean add(Book book) throws SQLException {
            String sql = "INSERT INTO book(book_no, book_name, author, stock) VALUES(?, ?, ?, ?)";
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = null;
            try {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, book.getBookNo());
                pstmt.setString(2, book.getBookName());
                pstmt.setString(3, book.getAuthor());
                pstmt.setInt(4, book.getStock());
                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn, pstmt);
            }
            return false;
        }

        // 修改图书
        public boolean update(Book book) throws SQLException {
            String sql = "UPDATE book SET book_name=?, author=?, stock=? WHERE book_no=?";
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = null;
            try {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, book.getBookName());
                pstmt.setString(2, book.getAuthor());
                pstmt.setInt(3, book.getStock());
                pstmt.setString(4, book.getBookNo());
                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn, pstmt);
            }
            return false;
        }

        // 删除图书
        public void delete(int bookId,String bookName) throws SQLException {
            Connection conn = null;
            try {
                conn = DBUtil.getConnection();
                conn.setAutoCommit(false);

                // 1.删除预约表相关记录
                String deleteReservationSql = "DELETE FROM reservation WHERE book_name = ?";
                try (PreparedStatement pstmt1 = conn.prepareStatement(deleteReservationSql)) {
                    pstmt1.setString(1, bookName);
                    pstmt1.executeUpdate();
                }

                //2. 删除图书本身
                String deleteBookSql = "DELETE FROM book WHERE id = ?";
                try (PreparedStatement pstmt2 = conn.prepareStatement(deleteBookSql)) {
                    pstmt2.setInt(1, bookId);
                    pstmt2.executeUpdate();
                }

                conn.commit(); // 提交事务
            } catch (SQLException e) {
                if (conn != null) conn.rollback(); // 回滚事务
                throw e;
            } finally {
                if (conn != null) conn.close();
            }
        }

        // 按ID查询图书
        public Book findById(Integer id) throws SQLException {
            String sql = "SELECT * FROM book WHERE id=?";
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, id);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    Book book = new Book();
                    book.setId(rs.getInt("id"));
                    book.setBookNo(rs.getString("book_no"));
                    book.setBookName(rs.getString("book_name"));
                    book.setAuthor(rs.getString("author"));
                    book.setStock(rs.getInt("stock"));
                    return book;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn, pstmt, rs);
            }
            return null;
        }

    // 更新图书库存
    public void updateStock(Book book, Connection conn) {
        String sql = "UPDATE book SET stock=? WHERE book_no=?";
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, book.getStock());
            pstmt.setString(2, book.getBookNo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    // 更新图书库存
    public void updateStock(String bookNam, Connection conn) {
        String sql = "UPDATE book SET stock=1 WHERE book_name=?";
        PreparedStatement pstmt = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookNam);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    // 根据书号查询图书
    public Book findByBookNo(String bookNo, Connection conn) {
        String sql = "SELECT * FROM book WHERE book_no=?";
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookNo);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                Book book = new Book();
                book.setId(rs.getInt("id"));
                book.setBookNo(rs.getString("book_no"));
                book.setBookName(rs.getString("book_name"));
                book.setAuthor(rs.getString("author"));
                book.setStock(rs.getInt("stock"));
                return book;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return null;
    }
}
