package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 5/9/16.
 */
public class TGC_Env extends Main_Header_Env {

    private String device_code;

    private String email_p;
    private String password;
    private String nfc_code;
    private int status_jump;


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
}
