package com.wn.dao;

import com.wn.entity.Borrow;
import com.wn.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * @Description:
 * @Author:kaige
 * @Date:2025/11/22 16:42
 */
public class BorrowDao {

        // 借阅图书
        public boolean borrowBook(Borrow borrow) throws SQLException {
            String sql = "INSERT INTO borrow(book_no, user_id, borrow_data) VALUES(?, ?, ?)";
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = null;
            try {
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, borrow.getBookNo());
                pstmt.setInt(2, borrow.getUserId());
                pstmt.setDate(3, new java.sql.Date(borrow.getBorrowData().getTime()));
                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn, pstmt);
            }
            return false;
        }

        // 归还图书
        public boolean returnBook(Integer id) throws SQLException {
            String sql = "UPDATE borrow SET return_data=? WHERE id=?";
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = null;
            try {
                pstmt = conn.prepareStatement(sql);
                pstmt.setDate(1, new java.sql.Date(System.currentTimeMillis()));
                pstmt.setInt(2, id);

                System.out.println("还书成功");

                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn, pstmt);
            }
            return false;
        }

        // 根据用户ID查询借阅记录
        public List<Borrow> getBorrowsByUserId(Integer userId) throws SQLException {

            String sql = "SELECT b.*, book.book_name \n" +
                    "FROM borrow b\n" +
                    "LEFT JOIN book ON b.book_no = book.book_no \n" +
                    "WHERE b.user_id = ?;";

            List<Borrow> borrows = new ArrayList<>();
            Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, userId);
                rs = pstmt.executeQuery();
                while (rs.next()) {
                    Borrow borrow = new Borrow();
                    borrow.setId(rs.getInt("id"));
                    borrow.setBookNo(rs.getString("book_no"));
                    borrow.setUserId(rs.getInt("user_id"));
                    borrow.setBookName(rs.getString("book_name"));
                    borrow.setBorrowData(rs.getDate("borrow_data"));
                    borrow.setReturnData(rs.getDate("return_data"));
                    borrows.add(borrow);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn, pstmt, rs);
            }
            return borrows;
        }
}
