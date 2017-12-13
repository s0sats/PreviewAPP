package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 12/12/2017.
 */

public class Chat_S_Historical_Message {

    private String room_code;
    private Integer msg_older_prefix;
    private Integer msg_older_code;
    private int not_delivered;

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }

    public Integer getMsg_older_prefix() {
        return msg_older_prefix;
    }

    public void setMsg_older_prefix(Integer msg_older_prefix) {
        this.msg_older_prefix = msg_older_prefix;
    }

    public Integer getMsg_older_code() {
        return msg_older_code;
    }

    public void setMsg_older_code(Integer msg_older_code) {
        this.msg_older_code = msg_older_code;
    }

    public int getNot_delivered() {
        return not_delivered;
    }

    public void setNot_delivered(int not_delivered) {
        this.not_delivered = not_delivered;
    }
}
