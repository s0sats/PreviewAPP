package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 10/01/2018.
 */

public class Chat_Room_Info_Env {

    private String socket_id;
    private String room_code;
    private int active;

    public String getSocket_id() {
        return socket_id;
    }

    public void setSocket_id(String socket_id) {
        this.socket_id = socket_id;
    }

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
