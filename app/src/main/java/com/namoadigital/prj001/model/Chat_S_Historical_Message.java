package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 12/12/2017.
 */

public class Chat_S_Historical_Message {

    private String room_code;
    private Integer msg_ref_prefix;
    private Integer msg_ref_code;
    private String action;

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }

    public Integer getMsg_ref_prefix() {
        return msg_ref_prefix;
    }

    public void setMsg_ref_prefix(Integer msg_ref_prefix) {
        this.msg_ref_prefix = msg_ref_prefix;
    }

    public Integer getMsg_ref_code() {
        return msg_ref_code;
    }

    public void setMsg_ref_code(Integer msg_ref_code) {
        this.msg_ref_code = msg_ref_code;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
