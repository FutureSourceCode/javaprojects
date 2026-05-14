package com.wn.entity;

import java.util.Date;
/**
 * @Description:借阅人类
 * @Author:kaige
 * @Date:2025/11/22 16:41
 */
public class Borrow {

        private Integer id;
        private String bookNo;    // 图书编号
        private Integer userId;   // 用户ID
        private Date borrowData;  // 借阅日期
        private Date returnData;  // 归还日期（null表示未归还）
        private String bookName;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getBookNo() { return bookNo; }
        public void setBookNo(String bookNo) { this.bookNo = bookNo; }
        public Integer getUserId() { return userId; }
        public void setUserId(Integer userId) { this.userId = userId; }
        public Date getBorrowData() { return borrowData; }
        public void setBorrowData(Date borrowData) { this.borrowData = borrowData; }
        public Date getReturnData() { return returnData; }
        public void setReturnData(Date returnData) { this.returnData = returnData; }

        public String getBookName() {
                return bookName;
        }

        public void setBookName(String bookName) {
                this.bookName = bookName;
        }
}
