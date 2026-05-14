package com.wn.dao;

import com.wn.entity.Announcement;
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
 * @Date:2025/11/24 12:26
 */

public class AnnouncementDao {

    /**
     * 按日期范围查询公告
     * @param startDate 开始日期（格式：yyyy-MM-dd）
     * @param endDate 结束日期（格式：yyyy-MM-dd）
     * @return 公告列表
     */
    public List<Announcement> findByDateRange(String startDate, String endDate) throws SQLException {
        String sql = "SELECT * FROM announcement WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if (startDate != null && !startDate.isEmpty()) {
            sql += " AND DATE(publish_time) >= ?";
            params.add(startDate);
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql += " AND DATE(publish_time) <= ?";
            params.add(endDate);
        }
        sql += " ORDER BY publish_time DESC";

        List<Announcement> announcements = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Announcement ann = new Announcement();
                ann.setId(rs.getInt("id"));
                ann.setContent(rs.getString("content"));
                ann.setPublisher(rs.getString("publisher"));
                ann.setPublishTime(rs.getTimestamp("publish_time"));
                announcements.add(ann);
            }
        }
        return announcements;
    }


     //查询所有公告
     public List<Announcement> findAll() throws SQLException {
         String sql = "SELECT * FROM announcement ORDER BY publish_time DESC";
         List<Announcement> announcements = new ArrayList<>();

         try (Connection conn = DBUtil.getConnection();
              PreparedStatement pstmt = conn.prepareStatement(sql);
              ResultSet rs = pstmt.executeQuery()) {

             while (rs.next()) {
                 Announcement ann = new Announcement();
                 ann.setId(rs.getInt("id"));
                 ann.setContent(rs.getString("content"));
                 ann.setPublisher(rs.getString("publisher"));
                 ann.setPublishTime(rs.getTimestamp("publish_time"));
                 announcements.add(ann);
             }
         }
         return announcements;
     }


     //发布新公告
    public boolean addAnnouncement(Announcement announcement) throws SQLException {
        String sql = "INSERT INTO announcement(content, publisher, publish_time) VALUES(?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, announcement.getContent());
            pstmt.setString(2, announcement.getPublisher());
            pstmt.setTimestamp(3, new java.sql.Timestamp(announcement.getPublishTime().getTime()));
            return pstmt.executeUpdate() > 0;
        }
    }

     // 删除公告
    public boolean deleteAnnouncement(int id) throws SQLException {
        String sql = "DELETE FROM announcement WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        }
    }
}