package com.namoadigital.prj001.model;

public class T_IO_Address_Suggestion_Rec{

    private String app;
    private String validation;
    private String link_url;
    private String error_msg;

    private Integer site_code;
    private Integer zone_code;
    private Integer local_code;

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

    public Integer getSite_code() {
        return site_code;
    }

    public void setSite_code(Integer site_code) {
        this.site_code = site_code;
    }

    public Integer getZone_code() {
        return zone_code;
    }

    public void setZone_code(Integer zone_code) {
        this.zone_code = zone_code;
    }

    public Integer getLocal_code() {
        return local_code;
    }

    public void setLocal_code(Integer local_code) {
        this.local_code = local_code;
    }
}
