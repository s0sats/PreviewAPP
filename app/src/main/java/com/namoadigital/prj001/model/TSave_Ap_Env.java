package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by d.luche on 23/02/2018.
 */

public class TSave_Ap_Env {

    @Expose
    private String app_code;
    @Expose
    private String app_version;
    @Expose
    private String session_app;
    @Expose
    private String token;
    @Expose
    private ArrayList<GE_Custom_Form_Ap> AP;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<GE_Custom_Form_Ap> getAP() {
        return AP;
    }

    public void setAP(ArrayList<GE_Custom_Form_Ap> AP) {
        this.AP = AP;
    }

}
