package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 5/9/16.
 */
public class TGC_Rec {

    private String app;
    private String error_msg;
    private String version;
    private String link_url;
    private String login;
    private String zip;
    private Integer db_version;

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

    public Integer getDb_version() {
        return db_version;
    }

    public void setDb_version(Integer db_version) {
        this.db_version = db_version;
    }
}
