package com.namoadigital.prj001.model;

import java.util.List;

public class T_TK_Ticket_Main_User_List_Rec {
    private String app;
    private String validation;
    private String link_url;
    private String error_msg;
    private int scn_valid;
    private List<T_TK_Main_User_Rec> data;

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

    public int getScn_valid() {
        return scn_valid;
    }

    public void setScn_valid(int scn_valid) {
        this.scn_valid = scn_valid;
    }

    public List<T_TK_Main_User_Rec> getData() {
        return data;
    }

    public void setData(List<T_TK_Main_User_Rec> data) {
        this.data = data;
    }
}
