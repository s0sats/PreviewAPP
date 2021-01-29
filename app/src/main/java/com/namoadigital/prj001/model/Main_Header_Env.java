package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

public class Main_Header_Env {

    public Main_Header_Env() {
    }

    public Main_Header_Env(String app_code, String app_version, String app_type, String session_app) {
        this.app_code = app_code;
        this.app_version = app_version;
        this.app_type = app_type;
        this.session_app = session_app;
    }

    @Expose
    private String app_code;
    @Expose
    private String app_version;
    @Expose
    private String app_type;
    @Expose
    private String session_app;

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

    public String getApp_type() {
        return app_type;
    }

    public void setApp_type(String app_type) {
        this.app_type = app_type;
    }

    public String getSession_app() {
        return session_app;
    }

    public void setSession_app(String session_app) {
        this.session_app = session_app;
    }
}
