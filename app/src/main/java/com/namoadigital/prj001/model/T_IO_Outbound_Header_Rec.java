package com.namoadigital.prj001.model;

import java.util.ArrayList;

public class T_IO_Outbound_Header_Rec {
    private String app;
    private String validation;
    private String link_url;
    private String error_msg;
    private ArrayList<IO_Outbound_Save_Return> result = new ArrayList<>();

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

    public ArrayList<IO_Outbound_Save_Return> getResult() {
        return result;
    }

    public void setResult(ArrayList<IO_Outbound_Save_Return> result) {
        this.result = result;
    }
}
