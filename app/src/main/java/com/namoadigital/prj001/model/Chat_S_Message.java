package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by d.luche on 30/11/2017.
 */

public class Chat_S_Message {

    @SerializedName("room_code") private String room_code;
    @SerializedName("type") private String type;
    @SerializedName("data") private String data;
    @SerializedName("tmp") private long tmp;

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

    public long getTmp() {
        return tmp;
    }

    public void setTmp(long tmp) {
        this.tmp = tmp;
    }
}
