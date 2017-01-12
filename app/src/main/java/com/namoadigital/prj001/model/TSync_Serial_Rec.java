package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

/**
 * Created by neonhugo on 9/26/2016.
 */

public class TSync_Serial_Rec {

    @Expose
    private String app;

    @Expose
    private String error_msg;

    @Expose
    private String date_db;

    @Expose
    private String version;

    @Expose
    private String link_url;

    @Expose
    private String login;

    @Expose
    private String zip;

    @Expose
    private String user_code;

    @Expose
    private String license;

    @Expose
    private String serial;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getDate_db() {
        return date_db;
    }

    public void setDate_db(String date_db) {
        this.date_db = date_db;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }
}
