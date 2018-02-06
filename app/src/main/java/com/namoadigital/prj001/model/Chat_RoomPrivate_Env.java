package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 13/12/2017.
 */

public class Chat_RoomPrivate_Env {

    private String session_app;
    private Long customer_code;
    private int user_code;
    private int active;

    public String getSession_app() {
        return session_app;
    }

    public void setSession_app(String session_app) {
        this.session_app = session_app;
    }

    public Long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(Long customer_code) {
        this.customer_code = customer_code;
    }

    public int getUser_code() {
        return user_code;
    }

    public void setUser_code(int user_code) {
        this.user_code = user_code;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
