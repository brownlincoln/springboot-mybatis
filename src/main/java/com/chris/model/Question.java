package com.chris.model;

import java.util.Date;

/**
 * Created by YaoQi on 2017/2/18.
 */
public class Question {
    private Integer id;
    private String title;
    private String content;
    private String createdDate;
    private Integer userId;
    private int commentCount;

    public Question() {
    }

    public Question(String title, String content, String createdDate, Integer userId, int commentCount) {
        this.title = title;
        this.content = content;
        this.createdDate = createdDate;
        this.userId = userId;
        this.commentCount = commentCount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
