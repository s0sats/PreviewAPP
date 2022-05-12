package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by neomatrix on 7/13/16.
 */

public class EV_User {

    @SerializedName("user_code") private long user_code;
    @SerializedName("user_nick") private String user_nick;
    @SerializedName("email_p") private String email_p;
    @SerializedName("admin") private int admin;
    @SerializedName("exist_nfc") private int exist_nfc;
    @SerializedName("nfc_blocked") private int nfc_blocked;

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

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }

    public int getExist_nfc() {
        return exist_nfc;
    }

    public void setExist_nfc(int exist_nfc) {
        this.exist_nfc = exist_nfc;
    }

    public int getNfc_blocked() {
        return nfc_blocked;
    }

    public void setNfc_blocked(int nfc_blocked) {
        this.nfc_blocked = nfc_blocked;
    }
}
