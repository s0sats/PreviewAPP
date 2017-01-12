package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 7/13/16.
 */

public class User {

    private long user_code;
    private String user_nick;
    private String email_p;
    private String password;
    private String nfc_code;
    private String dtsync;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNfc_code() {
        return nfc_code;
    }

    public void setNfc_code(String nfc_code) {
        this.nfc_code = nfc_code;
    }

    public String getDtsync() {
        return dtsync;
    }

    public void setDtsync(String dtsync) {
        this.dtsync = dtsync;
    }
}
