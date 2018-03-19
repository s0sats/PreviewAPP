package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 10/01/2018.
 */

public class Chat_UserList_Info_Rec {
    private String room_code;
    private int user_code;
    private String user_nick;
    private String user_name;
    private int on_line;
    private String sys_user_image;
    private String sys_user_image_name;

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

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
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
