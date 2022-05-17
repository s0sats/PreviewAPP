package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TSO_SO_Service_Rec {
    @Expose
    @SerializedName("app") private String app;
    @Expose
    @SerializedName("validation") private String validation;
    @Expose
    @SerializedName("link_url") private String link_url;
    @Expose
    @SerializedName("error_msg") private String error_msg;
    @Expose
    @SerializedName("so_return") private ArrayList<SO_Save_Return> so_return;
    @Expose
    @SerializedName("so_list") private TSO_SO_Service_List so_list;

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


