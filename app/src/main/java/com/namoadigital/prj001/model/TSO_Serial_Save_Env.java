package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by d.luche on 27/06/2017.
 */

public class TSO_Serial_Save_Env {

    @Expose
    private String app_code;
    @Expose
    private String app_version;
    @Expose
    private String session_app;
    @Expose
    private String token;
    @Expose
    private ArrayList<SM_SO> so;
    @Expose
    private ArrayList<MD_Product_Serial> serial;

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

    public ArrayList<SM_SO> getSo() {
        return so;
    }

    public void setSo(ArrayList<SM_SO> so) {
        this.so = so;
    }

    public ArrayList<MD_Product_Serial> getSerial() {
        return serial;
    }

    public void setSerial(ArrayList<MD_Product_Serial> serial) {
        this.serial = serial;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
