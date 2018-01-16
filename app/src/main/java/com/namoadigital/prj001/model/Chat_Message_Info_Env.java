package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 10/01/2018.
 */

public class Chat_Message_Info_Env {

    private String socket_id;
    private int msg_prefix;
    private int msg_code;
    private int show_myself;

    public String getSocket_id() {
        return socket_id;
    }

    public void setSocket_id(String socket_id) {
        this.socket_id = socket_id;
    }

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

    public int getShow_myself() {
        return show_myself;
    }

    public void setShow_myself(int show_myself) {
        this.show_myself = show_myself;
    }
}
