package com.wn.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;


/**
 * @Description:数据库连接
 * @Author:kaige
 * @Date:2025/11/22 16:49
 */
public class DBUtil {

        private static HikariDataSource ds;

        static {
            try {
                Properties prop = new Properties();
                prop.load(DBUtil.class.getClassLoader().getResourceAsStream("db.properties"));

                HikariConfig config = new HikariConfig();
                config.setJdbcUrl(prop.getProperty("jdbc.url"));
                config.setUsername(prop.getProperty("jdbc.username"));
                config.setPassword(prop.getProperty("jdbc.password"));
                config.setDriverClassName("com.mysql.cj.jdbc.Driver");
                config.setMaximumPoolSize(10);

                ds = new HikariDataSource(config);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static Connection getConnection() throws SQLException {
            return ds.getConnection();
        }

        public static void close(Connection conn) {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        public static void close(Connection conn, java.sql.PreparedStatement pstmt) {
            close(conn);
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        public static void close(Connection conn, java.sql.PreparedStatement pstmt, java.sql.ResultSet rs) {
            close(conn, pstmt);
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }


}
