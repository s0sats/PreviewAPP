package com.namoadigital.prj001.model;

import java.util.ArrayList;

/**
 * Created by d.luche on 27/06/2017.
 */

public class TSO_Service_Search_Rec {
    private String app;
    private String validation;
    private String link_url;
    private String error_msg;
    private ArrayList<TSO_Service_Search_Obj> data = new ArrayList<>();
    private ArrayList<MD_Partner> partner_list = new ArrayList<>();

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

    public ArrayList<TSO_Service_Search_Obj> getData() {
        return data;
    }

    public void setData(ArrayList<TSO_Service_Search_Obj> data) {
        this.data = data;
    }

    public ArrayList<MD_Partner> getPartner_list() {
        return partner_list;
    }

    public void setPartner_list(ArrayList<MD_Partner> partner_list) {
        this.partner_list = partner_list;
    }
}
