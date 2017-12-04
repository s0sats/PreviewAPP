package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 01/12/2017.
 */

public class CH_Message {

    private int msg_prefix;
    private int msg_code;
    private long tmp;
    private String room_code;
    private String msg_date;
    private String msg_obj;
    private String message_image_local;
    private String msg_origin;
    private int delivered;
    private String delivered_date;
    private int read;
    private String read_date;
    private String msg_pk;
    private int user_code;
    private String user_nick;

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

    public long getTmp() {
        return tmp;
    }

    public void setTmp(long tmp) {
        this.tmp = tmp;
    }

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }

    public String getMsg_date() {
        return msg_date;
    }

    public void setMsg_date(String msg_date) {
        this.msg_date = msg_date;
    }

    public String getMsg_obj() {
        return msg_obj;
    }

    public void setMsg_obj(String msg_obj) {
        this.msg_obj = msg_obj;
    }

    public String getMessage_image_local() {
        return message_image_local;
    }

    public void setMessage_image_local(String message_image_local) {
        this.message_image_local = message_image_local;
    }

    public String getMsg_origin() {
        return msg_origin;
    }

    public void setMsg_origin(String msg_origin) {
        this.msg_origin = msg_origin;
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

    public String getMsg_pk() {
        return msg_pk;
    }

    public void setMsg_pk(String msg_pk) {
        this.msg_pk = msg_pk;
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


}
