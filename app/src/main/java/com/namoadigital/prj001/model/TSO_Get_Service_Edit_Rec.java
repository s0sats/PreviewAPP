package com.namoadigital.prj001.model;

import java.util.ArrayList;

public class TSO_Get_Service_Edit_Rec {
    private String app;
    private String validation;
    private String link_url;
    private String error_msg;
    private ArrayList<TSO_Service_Search_Detail_Params_Obj> site_zone = new ArrayList<>();
    private ArrayList<MD_Partner> partner_list = new ArrayList<>();
    //Atributos apenas para lista
    private Integer site_code_selected;
    private Integer zone_code_selected;
    private Integer partner_code_selected;


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

    public ArrayList<TSO_Service_Search_Detail_Params_Obj> getSite_zone() {
        return site_zone;
    }

    public void setSite_zone(ArrayList<TSO_Service_Search_Detail_Params_Obj> site_zone) {
        this.site_zone = site_zone;
    }

    public ArrayList<MD_Partner> getPartner_list() {
        return partner_list;
    }

    public void setPartner_list(ArrayList<MD_Partner> partner_list) {
        this.partner_list = partner_list;
    }

    public Integer getSite_code_selected() {
        return site_code_selected;
    }

    public void setSite_code_selected(Integer site_code_selected) {
        this.site_code_selected = site_code_selected;
    }

    public Integer getZone_code_selected() {
        return zone_code_selected;
    }

    public void setZone_code_selected(Integer zone_code_selected) {
        this.zone_code_selected = zone_code_selected;
    }

    public Integer getPartner_code_selected() {
        return partner_code_selected;
    }

    public void setPartner_code_selected(Integer partner_code_selected) {
        this.partner_code_selected = partner_code_selected;
    }
}
