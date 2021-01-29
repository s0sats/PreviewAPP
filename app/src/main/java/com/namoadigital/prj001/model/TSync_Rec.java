package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 7/15/16.
 */

public class TSync_Rec {

    private String app;
    private String validation;
    private String link_url;
    private String error_msg;
    private String zip;
    private Integer db_version;
    private Integer user_code;
    private int valid_time = -1 ;
    private long tolerance_time = -1;

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

    public Integer getUser_code() {
        return user_code;
    }

    public void setUser_code(Integer user_code) {
        this.user_code = user_code;
    }

    public int getValid_time() {
        return valid_time;
    }

    public void setValid_time(int valid_time) {
        this.valid_time = valid_time;
    }

    public long getTolerance_time() {
        return tolerance_time;
    }

    public void setTolerance_time(long tolerance_time) {
        this.tolerance_time = tolerance_time;
    }
}
