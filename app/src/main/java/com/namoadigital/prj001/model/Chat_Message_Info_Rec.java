package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 10/01/2018.
 */

public class Chat_Message_Info_Rec {

    private int msg_prefix;
    private int msg_code;
    private int user_code;
    private String user_nick;
    private int delivered;
    private String delivered_date;
    private int read;
    private String read_date;
    private int on_line;
    private String sys_user_image;
    private String sys_user_image_name;

    public int getMsg_prefix() {
        return msg_prefix;
    }

    public void setMsg_prefix(int msg_prefix) {
        this.msg_prefix = msg_prefix;
    }

    public int getMsg_code() {
        return msg_code;
    }

    public void setMsg_code(int msg_code) {
        this.msg_code = msg_code;
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

    public int getDelivered() {
        return delivered;
    }

    public void setDelivered(int delivered) {
        this.delivered = delivered;
    }

    public String getDelivered_date() {
        return delivered_date;
    }

    public void setDelivered_date(String delivered_date) {
        this.delivered_date = delivered_date;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public String getRead_date() {
        return read_date;
    }

    public void setRead_date(String read_date) {
        this.read_date = read_date;
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
