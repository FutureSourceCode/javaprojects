package com.wn.entity;

/**
 * @Description:书籍类
 * @Author:kaige
 * @Date:2025/11/22 16:39
 */
public class Book {

        private Integer id;
        private String bookNo; // 书号
        private String bookName; // 书名
        private String author; // 作者
        private Integer stock; // 库存

        // getter/setter
        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }
        public String getBookNo() { return bookNo; }
        public void setBookNo(String bookNo) { this.bookNo = bookNo; }
        public String getBookName() { return bookName; }
        public void setBookName(String bookName) { this.bookName = bookName; }
        public String getAuthor() { return author; }
        public void setAuthor(String author) { this.author = author; }
        public Integer getStock() { return stock; }
        public void setStock(Integer stock) { this.stock = stock; }
}
