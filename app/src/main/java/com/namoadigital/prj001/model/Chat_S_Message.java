package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 30/11/2017.
 */

public class Chat_S_Message {

    private String room_code;
    private String type;
    private String data;
    private int tmp;

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getTmp() {
        return tmp;
    }

    public void setTmp(int tmp) {
        this.tmp = tmp;
    }
}
