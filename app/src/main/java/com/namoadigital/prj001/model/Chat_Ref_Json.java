package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by d.luche on 16/01/2018.
 */

public class Chat_Ref_Json {

    @SerializedName("msg_prefix") private Integer msg_prefix;
    @SerializedName("msg_code") private Integer msg_code;

    public Integer getMsg_prefix() {
        return msg_prefix;
    }

    public void setMsg_prefix(Integer msg_prefix) {
        this.msg_prefix = msg_prefix;
    }

    public Integer getMsg_code() {
        return msg_code;
    }

    public void setMsg_code(Integer msg_code) {
        this.msg_code = msg_code;
    }
}
