package com.wn.dao;


import com.wn.entity.BorrowRecord;
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
 * @Date:2025/11/24 15:09
 */

public class BorrowRecordDao {

    /**
     * 查询所有借阅记录
     */
    public List<BorrowRecord> findAll() throws SQLException {
        String sql = "SELECT " +
                "b.id, " +
                "b.book_no, " +
                "b.user_id, " +
                "b.borrow_data, " +
                "b.return_data, " +
                "bk.book_name,  " +
                "u.username     " +
                "FROM borrow b " +
                "LEFT JOIN book bk ON b.book_no = bk.book_no " +
                "LEFT JOIN user u ON b.user_id = u.id " +
                "ORDER BY b.borrow_data DESC";

        return queryBorrowRecords(sql, null);
    }

    /**
     * 按借阅者用户名查询（
     */
    public List<BorrowRecord> findByUsername(String username) throws SQLException {
        String sql = "SELECT " +
                "b.id, " +
                "b.book_no, " +
                "b.user_id, " +
                "b.borrow_data, " +
                "b.return_data, " +
                "bk.book_name, " +
                "u.username " +
                "FROM borrow b " +
                "LEFT JOIN book bk ON b.book_no = bk.book_no " +
                "LEFT JOIN user u ON b.user_id = u.id " +
                "WHERE u.username LIKE ? " +
                "ORDER BY b.borrow_data DESC";

        List<Object> params = new ArrayList<>();
        params.add("%" + username + "%");
        return queryBorrowRecords(sql, params);
    }

    /**
     * 根据ID查询单条借阅记录
     */
    public BorrowRecord findById(int id) throws SQLException {
        String sql = "SELECT " +
                "b.id, " +
                "b.book_no, " +
                "b.user_id, " +
                "b.borrow_data, " +
                "b.return_data, " +
                "bk.book_name, " +
                "u.username " +
                "FROM borrow b " +
                "LEFT JOIN book bk ON b.book_no = bk.book_no " +
                "LEFT JOIN user u ON b.user_id = u.id " +
                "WHERE b.id = ?";

        List<Object> params = new ArrayList<>();
        params.add(id);
        List<BorrowRecord> records = queryBorrowRecords(sql, params);
        return records.isEmpty() ? null : records.get(0);
    }

    /**
     * 通用查询方法
     */
    private List<BorrowRecord> queryBorrowRecords(String sql, List<Object> params) throws SQLException {
        List<BorrowRecord> records = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置查询参数
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(i + 1, params.get(i));
                }
            }

            // 执行查询并封装结果
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                BorrowRecord record = new BorrowRecord();
                record.setId(rs.getInt("id"));
                record.setBookNo(rs.getString("book_no"));
                record.setUserId(rs.getInt("user_id"));
                record.setBorrowData(rs.getTimestamp("borrow_data"));
                record.setReturnData(rs.getTimestamp("return_data"));
                record.setBookName(rs.getString("book_name"));
                record.setUsername(rs.getString("username"));
                records.add(record);
            }
        }
        return records;
    }
}