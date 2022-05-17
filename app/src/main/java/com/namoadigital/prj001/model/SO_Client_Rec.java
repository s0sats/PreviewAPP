package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SO_Client_Rec {
    @SerializedName("app") private String app;
    @SerializedName("validation") private String validation;
    @SerializedName("link_url") private String link_url;
    @SerializedName("error_msg") private String error_msg;
    @SerializedName("client") private ArrayList<SM_SO_Client> client;

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

    public ArrayList<SM_SO_Client> getClient() {
        return client;
    }

    public void setClient(ArrayList<SM_SO_Client> client) {
        this.client = client;
    }
}
