package com.chris.model;

/**
 * Created by LJFENG on 2017/2/17 0017.
 */
public class User {
    private Integer id;
    private String name;
    private String password;
    private String salt;
    private String headUrl;

    public User() {
    }

    public User(String name, String password, String salt, String headUrl) {
        this.name = name;
        this.password = password;
        this.salt = salt;
        this.headUrl = headUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", salt='" + salt + '\'' +
                ", headUrl='" + headUrl + '\'' +
                '}';
    }
}
