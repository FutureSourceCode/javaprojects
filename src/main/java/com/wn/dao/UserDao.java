package com.wn.dao;

import com.wn.entity.User;

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
public class UserDao {

        // 用户登录
        public User login(String username, String password) {
            String sql = "SELECT * FROM user WHERE username=?";
            Connection conn = null;
            PreparedStatement pstmt = null;
            ResultSet rs = null;
            try {
                conn = DBUtil.getConnection();
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    String dbPwd = rs.getString("password");
                    // 直接比对明文密码
                    if (password.equals(dbPwd)) {
                        User user = new User();
                        user.setId(rs.getInt("id"));
                        user.setUsername(rs.getString("username"));
                        user.setRole(rs.getString("role"));
                        return user;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn, pstmt, rs);
            }
            return null;
        }

        // 用户注册
        public boolean register(User user) {
            String sql = "INSERT INTO user(username, password, role) VALUES(?, ?, 'user')";
            Connection conn = null;
            PreparedStatement pstmt = null;
            try {
                conn = DBUtil.getConnection();
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, user.getUsername());
                pstmt.setString(2, user.getPassword()); // 明文存储
                return pstmt.executeUpdate() > 0;
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                DBUtil.close(conn, pstmt);
            }
            return false;
        }

    // 检查用户名是否存在
    public User findUserByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM user WHERE username=?";
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setRole(rs.getString("role"));
                return user;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn, pstmt, rs);
        }
        return null;
    }
    //删除用户
    public boolean deleteUser(Integer id) throws SQLException {
        String sql = "DELETE FROM user WHERE id=?";
        Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } finally {
            DBUtil.close(conn, pstmt);
        }
    }

    public List<User> findAllUser() {
            return null;
    }

    /**
     * 修改用户密码
     * @param username 用户名
     * @param newPassword 新密码
     * @return 是否修改成功
     * @throws SQLException 数据库异常
     */
    public boolean updatePassword(String username, String newPassword) throws SQLException {
        String sql = "UPDATE user SET password = ? WHERE username = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 设置参数：1-新密码，2-用户名
            pstmt.setString(1, newPassword);
            pstmt.setString(2, username);

            // 执行更新，返回受影响的行数
            int rowsAffected = pstmt.executeUpdate();

            // 受影响行数>0表示修改成功
            return rowsAffected > 0;

        } catch (SQLException e) {
            // 抛出异常由上层处理（如Servlet）
            throw new SQLException("修改密码失败：" + e.getMessage(), e);
        }
    }

    /**
     * 按关键词和角色搜索用户
     * @param keyword 用户名关键词
     * @param role 角色
     */
    public List<User> searchUsers(String keyword, String role) throws SQLException {
        String sql = "SELECT id, username, role FROM user WHERE 1=1";
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isEmpty()) {
            sql += " AND username LIKE ?";
            params.add("%" + keyword + "%");
        }
        if (role != null && !role.isEmpty()) {
            sql += " AND role = ?";
            params.add(role);
        }
        sql += " ORDER BY id DESC";

        List<User> userList = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                userList.add(user);
            }
        }
        return userList;
    }

    /**
     * 查询所有用户（按ID倒序）
     */
    public List<User> findAll() throws SQLException {
        String sql = "SELECT id, username, role FROM user";
        List<User> userList = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                userList.add(user);
            }
        }
        return userList;
    }

    /**
     * 根据ID删除用户
     */
    public boolean deleteById(int id) throws SQLException {
        String sql = "DELETE FROM user WHERE id = ? and id !=1";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new SQLException("删除用户失败：" + e.getMessage(), e);
        }
    }


}
