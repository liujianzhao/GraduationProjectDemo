package com.example.graduationproject.entity;

import android.graphics.Bitmap;

/**
 * Created by liuji on 2017/5/2.
 */

public class User {

    private String username;
    private String password;
    private String roleName;
    private boolean remember;

    private Bitmap userpic;
    private String nickname;
    private String gender;
    private String birthday;
    private String address;
    private String sign;

    public User() {
    }

    public User(String username, String password, String roleName, boolean remember, Bitmap userpic, String nickname, String gender, String birthday, String address, String sign) {
        this.username = username;
        this.password = password;
        this.roleName = roleName;
        this.remember = remember;
        this.userpic = userpic;
        this.nickname = nickname;
        this.gender = gender;
        this.birthday = birthday;
        this.address = address;
        this.sign = sign;
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

    public boolean isRemember() {
        return remember;
    }

    public void setRemember(boolean remember) {
        this.remember = remember;
    }

    public Bitmap getUserpic() {
        return userpic;
    }

    public void setUserpic(Bitmap userpic) {
        this.userpic = userpic;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
