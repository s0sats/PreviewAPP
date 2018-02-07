package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 13/12/2017.
 */

public class Chat_Leave_Room_Env {

    private String session_app;
    private String user_code;
    private String room_code;

    public String getSession_app() {
        return session_app;
    }

    public void setSession_app(String session_app) {
        this.session_app = session_app;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }
}
