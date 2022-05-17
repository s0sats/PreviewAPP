package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by d.luche on 27/07/2017.
 */

public class TSO_Approval_Rec {

    @SerializedName("app") private String app;
    @SerializedName("validation") private String validation;
    @SerializedName("link_url") private String link_url;
    @SerializedName("error_msg") private String error_msg;
    @SerializedName("so_status") private ArrayList<SO_Save_Return> so_status;
    @SerializedName("so") private ArrayList<SM_SO> so;

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

    public ArrayList<SO_Save_Return> getSo_status() {
        return so_status;
    }

    public void setSo_status(ArrayList<SO_Save_Return> so_status) {
        this.so_status = so_status;
    }

    public ArrayList<SM_SO> getSo() {
        return so;
    }

    public void setSo(ArrayList<SM_SO> so) {
        this.so = so;
    }
}
