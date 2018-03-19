package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 10/01/2018.
 */

public class Chat_Room_Info_Env {

    private String session_app;
    private String room_code;
    private int active;

    public String getSession_app() {
        return session_app;
    }

    public void setSession_app(String session_app) {
        this.session_app = session_app;
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
