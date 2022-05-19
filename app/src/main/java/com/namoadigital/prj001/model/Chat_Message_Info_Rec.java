package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by d.luche on 10/01/2018.
 */

public class Chat_Message_Info_Rec {

    @SerializedName("msg_prefix") private int msg_prefix;
    @SerializedName("msg_code") private int msg_code;
    @SerializedName("user_code") private int user_code;
    @SerializedName("user_nick") private String user_nick;
    @SerializedName("delivered") private int delivered;
    @SerializedName("delivered_date") private String delivered_date;
    @SerializedName("read") private int read;
    @SerializedName("read_date") private String read_date;
    @SerializedName("on_line") private int on_line;
    @SerializedName("sys_user_image") private String sys_user_image;
    @SerializedName("sys_user_image_name") private String sys_user_image_name;

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
