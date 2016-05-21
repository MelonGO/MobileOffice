package com.melon.mobileoffice.model;

import java.io.Serializable;

public class User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int userid;
    private String username;
    private String userpassword;
    private String gender;
    private int age;
    private String address;

    public int getUserid() {
        return userid;
    }
    public void setUserid(int id) {
        this.userid = id;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String name) {
        this.username = name;
    }

    public String getUserpassword() {
        return userpassword;
    }
    public void setUserpassword(String password) {
        this.userpassword = password;
    }

    public String getGender() {
        return gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }

}
