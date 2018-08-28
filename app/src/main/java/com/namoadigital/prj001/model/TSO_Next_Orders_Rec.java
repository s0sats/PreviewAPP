package com.namoadigital.prj001.model;

import java.util.ArrayList;

public class TSO_Next_Orders_Rec {

    private String app;
    private String validation;
    private String link_url;
    private String error_msg;
    private ArrayList<SO_Next_Orders_Obj> obj;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public ArrayList<SO_Next_Orders_Obj> getObj() {
        return obj;
    }

    public void setObj(ArrayList<SO_Next_Orders_Obj> obj) {
        this.obj = obj;
    }
}

