package com.wn.dao;

import com.wn.entity.Reservation;
import com.wn.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 * @Description:
 * @Author:kaige
 * @Date:2025/11/24 15:12
 */

public class ReservationDao {
    // 创建预约
    public boolean addReservation(Reservation reservation) throws SQLException {
        String sql = "INSERT INTO reservation(book_name,username,reserve_time) VALUES(?, ?, ?)";
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reservation.getBookName());
            pstmt.setString(2, reservation.getUserName());
            pstmt.setTimestamp(3, new Timestamp(reservation.getReserveTime().getTime()));
            return pstmt.executeUpdate() > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }


    // 查询用户的预约
    public List<Reservation> getReservationsByUserName(String username) throws SQLException {
        String sql = "SELECT * FROM reservation WHERE username=?";
        List<Reservation> reservations = new ArrayList<>();
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Reservation res = new Reservation();
                res.setId(rs.getInt("id"));
                res.setBookName(rs.getString("book_name"));
                res.setUserName(rs.getString("username"));
                res.setReserveTime(rs.getDate("reserve_time"));
                res.setStatus(rs.getString("status"));
                reservations.add(res);
            }
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return reservations;
    }

    /**
     * 取消预约
     */
    public boolean cancelReservation(String username, String bookName) throws SQLException {
        String sql = "DELETE FROM reservation WHERE username = ? AND book_name = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);   // 用户名
            pstmt.setString(2, bookName);   // 书名
            int rows = pstmt.executeUpdate();
            return rows > 0; // 影响行数>0表示取消成功
        }
    }

    /**
     * 查询所有预约记录
     */
    public List<Reservation> findAll() throws SQLException {
        String sql = "SELECT * FROM reservation ORDER BY reserve_time DESC";
        List<Reservation> reservations = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Reservation res = new Reservation();
                res.setId(rs.getInt("id"));
                res.setUserName(rs.getString("username"));
                res.setBookName(rs.getString("book_name"));
                res.setReserveTime(rs.getTimestamp("reserve_time"));
                res.setStatus(rs.getString("status")); // 是否已提醒
                reservations.add(res);
            }
        }
        return reservations;
    }

    /**
     * 根据ID查询预约记录
     */
    public Reservation findById(int id) throws SQLException {
        String sql = "SELECT * FROM reservation WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Reservation res = new Reservation();
                res.setId(rs.getInt("id"));
                res.setUserName(rs.getString("username"));
                res.setBookName(rs.getString("book_name"));
                res.setReserveTime(rs.getTimestamp("reserve_time"));
                res.setStatus(rs.getString("status"));
                return res;
            }
        }
        return null;
    }

    /**
     * 标记预约为已提醒
     */
    public boolean markNotified(int id) throws SQLException {
        String sql= "UPDATE reservation SET status = 'fulfilled' WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
}