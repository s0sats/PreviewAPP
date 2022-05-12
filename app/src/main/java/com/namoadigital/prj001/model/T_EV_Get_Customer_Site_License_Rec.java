package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class T_EV_Get_Customer_Site_License_Rec {

    @SerializedName("app") private String app;
    @SerializedName("validation") private String validation;
    @SerializedName("link_url") private String link_url;
    @SerializedName("error_msg") private String error_msg;
    @SerializedName("login") private String login;
    @SerializedName("valid_time") private int valid_time;
    @SerializedName("tolerance_time") private int tolerance_time;
    @SerializedName("version") private String version;
    @SerializedName("site_license") private ArrayList<SiteLicense> site_license = new ArrayList<>();

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
