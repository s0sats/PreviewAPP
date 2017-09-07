package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 27/06/2017.
 */

public class TUser_Author_Env {

    private String app_code;
    private String app_version;
    private String session_app;
    private String customer_code;
    private String so_prefix;
    private String so_code;
    private String auth_type;
    private String auth_nick_mail;
    private String auth_password;
    private String auth_nfc;

    public String getApp_code() {
        return app_code;
    }

    public void setApp_code(String app_code) {
        this.app_code = app_code;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getSession_app() {
        return session_app;
    }

    public void setSession_app(String session_app) {
        this.session_app = session_app;
    }

    public String getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(String customer_code) {
        this.customer_code = customer_code;
    }

    public String getSo_prefix() {
        return so_prefix;
    }

    public void setSo_prefix(String so_prefix) {
        this.so_prefix = so_prefix;
    }

    public String getSo_code() {
        return so_code;
    }

    public void setSo_code(String so_code) {
        this.so_code = so_code;
    }

    public String getAuth_type() {
        return auth_type;
    }

    public void setAuth_type(String auth_type) {
        this.auth_type = auth_type;
    }

    public String getAuth_nick_mail() {
        return auth_nick_mail;
    }

    public void setAuth_nick_mail(String auth_nick_mail) {
        this.auth_nick_mail = auth_nick_mail;
    }

    public String getAuth_password() {
        return auth_password;
    }

    public void setAuth_password(String auth_password) {
        this.auth_password = auth_password;
    }

    public String getAuth_nfc() {
        return auth_nfc;
    }

    public void setAuth_nfc(String auth_nfc) {
        this.auth_nfc = auth_nfc;
    }
}
