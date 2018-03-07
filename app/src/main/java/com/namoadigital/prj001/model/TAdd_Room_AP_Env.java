package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 07/03/2018.
 */

public class TAdd_Room_AP_Env {

    private long customer_code;
    private String translate_code;
    private String user_code;
    private String session_app;
    private int custom_form_type;
    private int custom_form_code;
    private int custom_form_version;
    private int custom_form_data;
    private int ap_code;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public String getTranslate_code() {
        return translate_code;
    }

    public void setTranslate_code(String translate_code) {
        this.translate_code = translate_code;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getSession_app() {
        return session_app;
    }

    public void setSession_app(String session_app) {
        this.session_app = session_app;
    }

    public int getCustom_form_type() {
        return custom_form_type;
    }

    public void setCustom_form_type(int custom_form_type) {
        this.custom_form_type = custom_form_type;
    }

    public int getCustom_form_code() {
        return custom_form_code;
    }

    public void setCustom_form_code(int custom_form_code) {
        this.custom_form_code = custom_form_code;
    }

    public int getCustom_form_version() {
        return custom_form_version;
    }

    public void setCustom_form_version(int custom_form_version) {
        this.custom_form_version = custom_form_version;
    }

    public int getCustom_form_data() {
        return custom_form_data;
    }

    public void setCustom_form_data(int custom_form_data) {
        this.custom_form_data = custom_form_data;
    }

    public int getAp_code() {
        return ap_code;
    }

    public void setAp_code(int ap_code) {
        this.ap_code = ap_code;
    }
}
