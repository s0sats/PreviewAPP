package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class TSO_SO_Service_Rec {
    @Expose
    private String app;
    @Expose
    private String validation;
    @Expose
    private String link_url;
    @Expose
    private String error_msg;
    @Expose
    private ArrayList<SO_Save_Return> so_return;
    @Expose
    private TSO_SO_Service_List so_list;

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

    public ArrayList<SO_Save_Return> getSo_return() {
        return so_return;
    }

    public void setSo_return(ArrayList<SO_Save_Return> so_return) {
        this.so_return = so_return;
    }

    public TSO_SO_Service_List getSo_list() {
        return so_list;
    }

    public void setSo_list(TSO_SO_Service_List so_list) {
        this.so_list = so_list;
    }
}


