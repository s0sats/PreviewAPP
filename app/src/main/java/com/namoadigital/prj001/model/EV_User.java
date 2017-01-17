package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 7/13/16.
 */

public class EV_User {

    private long user_code;
    private String user_nick;
    private String email_p;

    public long getUser_code() {
        return user_code;
    }

    public void setUser_code(long user_code) {
        this.user_code = user_code;
    }

    public String getUser_nick() {
        return user_nick;
    }

    public void setUser_nick(String user_nick) {
        this.user_nick = user_nick;
    }

    public String getEmail_p() {
        return email_p;
    }

    public void setEmail_p(String email_p) {
        this.email_p = email_p;
    }

}
