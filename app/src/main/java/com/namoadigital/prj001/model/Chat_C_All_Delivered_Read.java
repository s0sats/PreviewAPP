package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by d.luche on 29/11/2017.
 */

public class Chat_C_All_Delivered_Read {
    @SerializedName("msg_prefix") private int msg_prefix;
    @SerializedName("msg_code") private int msg_code;

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
}
