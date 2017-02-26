package com.chris.model;

import com.alibaba.fastjson.JSONObject;

import java.util.Date;

/**
 * Created by YaoQi on 2017/2/26.
 */
public class Feed {
    private Integer id;
    private int type;
    private int userId;
    private Date createdDate;
    //json string
    private String data;
    //json object
    private JSONObject dataJSON = null;

    public Feed() {
    }

    public Feed(int type, int userId, Date createdDate, String data) {
        this.type = type;
        this.userId = userId;
        this.createdDate = createdDate;
        this.data = data;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getData() {
        return data;
    }
    //设置data的时候，也设置dataJSON 对象
    public void setData(String data) {
        this.data = data;
        dataJSON = JSONObject.parseObject(data);
    }

    public String get(String key) {
        return dataJSON == null ? null : dataJSON.getString(key);
    }
}
