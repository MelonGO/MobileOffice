package com.melon.mobileoffice.model;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

@Table(name = "friend")
public class Friend {
    @Id
    private int id;
    private String friendid;
    private String friendname;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getFriendid() {
        return friendid;
    }
    public void setFriendid(String id) {
        this.friendid = id;
    }

    public String getFriendname() {
        return friendname;
    }
    public void setFriendname(String name) {
        this.friendname = name;
    }

}
