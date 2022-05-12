package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by neomatrix on 5/9/16.
 */
public class TGC_Env extends Main_Header_Env{

    @SerializedName("device_code")
    private String device_code;
    @SerializedName("email_p")
    private String email_p;
    @SerializedName("password")
    private String password;
    @SerializedName("nfc_code")
    private String nfc_code;
    @SerializedName("status_jump")
    private int status_jump;
    @SerializedName("current_time")
    private String current_time;

    public String getDevice_code() {
        return device_code;
    }

    public void setDevice_code(String device_code) {
        this.device_code = device_code;
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

    public int getStatus_jump() {
        return status_jump;
    }

    public void setStatus_jump(int status_jump) {
        this.status_jump = status_jump;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }
}
