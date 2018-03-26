package com.namoadigital.prj001.model;

import java.util.ArrayList;

/**
 * Created by d.luche on 23/02/2018.
 */

public class TSO_Pack_Express_Env {

    private String app_code;
    private String app_version;
    private String session_app;
    private String token;

    private ArrayList<SO_Pack_Express_Local> pack_express;

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

    public ArrayList<SO_Pack_Express_Local> getPack_express() {
        return pack_express;
    }

    public void setPack_express(ArrayList<SO_Pack_Express_Local> pack_express) {
        this.pack_express = pack_express;
    }
}
