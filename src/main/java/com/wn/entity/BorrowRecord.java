package com.wn.entity;

import java.util.Date;

public class BorrowRecord {
    private int id;
    private String bookNo;
    private String bookName;
    private int userId;
    private String username;
    private Date borrowData;
    private Date returnData;

    // getter和setter方法
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getBookNo() { return bookNo; }
    public void setBookNo(String bookNo) { this.bookNo = bookNo; }

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Date getBorrowData() { return borrowData; }
    public void setBorrowData(Date borrowData) { this.borrowData = borrowData; }

    public Date getReturnData() { return returnData; }
    public void setReturnData(Date returnData) { this.returnData = returnData; }
}