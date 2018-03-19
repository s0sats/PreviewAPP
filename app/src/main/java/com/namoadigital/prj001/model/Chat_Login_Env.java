package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 29/11/2017.
 */

public class Chat_Login_Env {

    private String user_code;
    private String customer_code;
    private String session_id;
    private String session_type;
    private String translate_code;
    private int force;
    private String device_code;

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(String customer_code) {
        this.customer_code = customer_code;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getSession_type() {
        return session_type;
    }

    public void setSession_type(String session_type) {
        this.session_type = session_type;
    }

    public String getTranslate_code() {
        return translate_code;
    }

    public void setTranslate_code(String translate_code) {
        this.translate_code = translate_code;
    }

    public int getForce() {
        return force;
    }

    public void setForce(int force) {
        this.force = force;
    }

    public String getDevice_code() {
        return device_code;
    }

    public void setDevice_code(String device_code) {
        this.device_code = device_code;
    }
}
