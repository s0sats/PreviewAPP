package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by d.luche on 10/01/2018.
 */

public class Chat_Room_Info_Rec {

    @SerializedName("room_code") private String room_code;
    @SerializedName("user_code") private int user_code;
    @SerializedName("user_nick") private String user_nick;
    @SerializedName("admin") private int admin;
    @SerializedName("active") private int active;
    @SerializedName("on_line") private int on_line;
    @SerializedName("sys_user_image") private String sys_user_image;
    @SerializedName("sys_user_image_name") private String sys_user_image_name;

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }

    public int getUser_code() {
        return user_code;
    }

    public void setUser_code(int user_code) {
        this.user_code = user_code;
    }

    public String getUser_nick() {
        return user_nick;
    }

    public void setUser_nick(String user_nick) {
        this.user_nick = user_nick;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getOn_line() {
        return on_line;
    }

    public void setOn_line(int on_line) {
        this.on_line = on_line;
    }

    public String getSys_user_image() {
        return sys_user_image;
    }

    public void setSys_user_image(String sys_user_image) {
        this.sys_user_image = sys_user_image;
    }

    public String getSys_user_image_name() {
        return sys_user_image_name;
    }

    public void setSys_user_image_name(String sys_user_image_name) {
        this.sys_user_image_name = sys_user_image_name;
    }
}
