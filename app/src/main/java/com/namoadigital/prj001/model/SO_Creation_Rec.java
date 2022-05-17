package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SO_Creation_Rec{

    @SerializedName("app") private String app;
    @SerializedName("validation") private String validation;
    @SerializedName("link_url") private String link_url;
    @SerializedName("error_msg") private String error_msg;
    @SerializedName("so") private ArrayList<SM_SO> so;
    @SerializedName("so_return") private ArrayList<SO_Save_Return> so_return;

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

    public ArrayList<SM_SO> getSo() {
        return so;
    }

    public void setSo(ArrayList<SM_SO> so) {
        this.so = so;
    }

    public ArrayList<SO_Save_Return> getSo_return() {
        return so_return;
    }

    public void setSo_return(ArrayList<SO_Save_Return> so_return) {
        this.so_return = so_return;
    }
}
