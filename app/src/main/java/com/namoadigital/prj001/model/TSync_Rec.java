package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 7/15/16.
 */

public class TSync_Rec {

    private String app;
    private String version;
    private String date_db;
    private String login;
    private String user_code;
    private String link_url;
    private String license;
    private String zip;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getDate_db() {
        return date_db;
    }

    public void setDate_db(String date_db) {
        this.date_db = date_db;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public String getZip() {
        return zip;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }
}
