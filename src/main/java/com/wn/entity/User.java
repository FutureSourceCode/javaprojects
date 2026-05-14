package com.wn.entity;

/**
 * @Description:用户类
 * @Author:kaige
 * @Date:2025/11/22 16:39
 */
public class User {
        private Integer id;
        private String username;
        private String password;
        private String role; // admin/user

        // getter/setter
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

}
