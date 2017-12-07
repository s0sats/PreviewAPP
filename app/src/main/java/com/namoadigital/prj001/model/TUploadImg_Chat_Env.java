package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 20/02/17.
 */

public class TUploadImg_Chat_Env {

    private long msg_prefix;
    private long msg_code;
    private String socket_id;

    public long getMsg_prefix() {
        return msg_prefix;
    }

    public void setMsg_prefix(long msg_prefix) {
        this.msg_prefix = msg_prefix;
    }

    public long getMsg_code() {
        return msg_code;
    }

    public void setMsg_code(long msg_code) {
        this.msg_code = msg_code;
    }

    public String getSocket_id() {
        return socket_id;
    }

    public void setSocket_id(String socket_id) {
        this.socket_id = socket_id;
    }
}
