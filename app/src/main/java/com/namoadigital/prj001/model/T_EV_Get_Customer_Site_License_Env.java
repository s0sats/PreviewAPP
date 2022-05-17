package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class T_EV_Get_Customer_Site_License_Env extends Main_Header_Env {

    @Expose
    @SerializedName("current_time")
    private String current_time;
    @Expose
    @SerializedName("device_code")
    private String device_code;
    @Expose
    @SerializedName("email_p")
    private String email_p;
    @Expose
    @SerializedName("nfc_code")
    private String nfc_code;
    @Expose
    @SerializedName("password")
    private String password;
    @Expose
    @SerializedName("status_jump")
    private String status_jump;
    @Expose
    @SerializedName("customer_code")
    private String customer_code;

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }

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

    public String getNfc_code() {
        return nfc_code;
    }

    public void setNfc_code(String nfc_code) {
        this.nfc_code = nfc_code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus_jump() {
        return status_jump;
    }

    public void setStatus_jump(String status_jump) {
        this.status_jump = status_jump;
    }

    public String getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(String customer_code) {
        this.customer_code = customer_code;
    }
}
