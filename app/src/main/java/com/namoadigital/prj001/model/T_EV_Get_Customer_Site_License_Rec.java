package com.namoadigital.prj001.model;

import java.util.ArrayList;

public class T_EV_Get_Customer_Site_License_Rec {

    private String app;
    private String validation;
    private String link_url;
    private String error_msg;
    private String login;
    private int valid_time;
    private int tolerance_time;
    private String version;
    private ArrayList<SiteLicense> site_license = new ArrayList<>();

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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getValid_time() {
        return valid_time;
    }

    public void setValid_time(int valid_time) {
        this.valid_time = valid_time;
    }

    public int getTolerance_time() {
        return tolerance_time;
    }

    public void setTolerance_time(int tolerance_time) {
        this.tolerance_time = tolerance_time;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ArrayList<SiteLicense> getSite_license() {
        return site_license;
    }

    public void setSite_license(ArrayList<SiteLicense> site_license) {
        this.site_license = site_license;
    }
}
