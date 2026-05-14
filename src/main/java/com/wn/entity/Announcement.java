package com.wn.entity;

import java.util.Date;

/**
 * @Description:公告实体类
 * @Author:kaige
 * @Date:2025/11/24 11:25
 */

public class Announcement {
    private int id;
    private String content;
    private String publisher;
    private Date publishTime;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }

    public Date getPublishTime() { return publishTime; }
    public void setPublishTime(Date publishTime) { this.publishTime = publishTime; }
}