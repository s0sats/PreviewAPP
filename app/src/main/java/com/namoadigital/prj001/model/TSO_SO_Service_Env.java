package com.namoadigital.prj001.model;

import java.util.ArrayList;

public class TSO_SO_Service_Env {

    private String app_code;
    private String app_version;
    private String session_app;
    private String token;
    private ArrayList<TSO_SO_Service> so;

    public TSO_SO_Service_Env() {
        this.so = new ArrayList<>();
    }

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSession_app() {
        return session_app;
    }

    public void setSession_app(String session_app) {
        this.session_app = session_app;
    }

    public ArrayList<TSO_SO_Service> getSo() {
        return so;
    }

    public void setSo(ArrayList<TSO_SO_Service> so) {
        this.so = so;
    }
}
