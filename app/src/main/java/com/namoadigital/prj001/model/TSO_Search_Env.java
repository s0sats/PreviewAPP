package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 27/06/2017.
 */

public class TSO_Search_Env {

    private String app_code;
    private String app_version;
    private String session_app;
    private Long product_code;
    private String serial_id;
    private String so_mult;
    private int profile_check;

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

    public Long getProduct_code() {
        return product_code;
    }

    public void setProduct_code(Long product_code) {
        this.product_code = product_code;
    }

    public String getSerial_id() {
        return serial_id;
    }

    public void setSerial_id(String serial_id) {
        this.serial_id = serial_id;
    }

    public String getSo_mult() {
        return so_mult;
    }

    public void setSo_mult(String so_mult) {
        this.so_mult = so_mult;
    }

    public void setProfile_check(int profile_check) {
        this.profile_check = profile_check;
    }
}
