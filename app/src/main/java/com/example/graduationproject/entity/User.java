package com.example.graduationproject.entity;

/**
 * Created by liuji on 2017/5/2.
 */

public class User {

    private String username;
    private String password;
    private String roleName;

    public User() {
    }

    public User(String username, String password, String roleName) {
        this.username = username;
        this.password = password;
        this.roleName = roleName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
