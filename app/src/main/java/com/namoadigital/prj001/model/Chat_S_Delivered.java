package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 13/12/2017.
 */

public class Chat_S_Delivered {

    int msg_prefix;
    int msg_code;
    String room_code;
    int read;

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

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }
}
