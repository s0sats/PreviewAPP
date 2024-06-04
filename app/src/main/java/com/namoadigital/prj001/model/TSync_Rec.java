package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by neomatrix on 7/15/16.
 */

public class TSync_Rec {

    @SerializedName("app") private String app;
    @SerializedName("validation") private String validation;
    @SerializedName("link_url") private String link_url;
    @SerializedName("error_msg") private String error_msg;
    @SerializedName("zip") private String zip;
    @SerializedName("db_version") private Integer db_version;
    @SerializedName("user_code") private Integer user_code;
    @SerializedName("files_waiting") private Boolean files_waiting;
    @SerializedName("valid_time") private int valid_time = -1 ;
    @SerializedName("tolerance_time") private long tolerance_time = -1;

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

    public Boolean getFiles_waiting() {
        return files_waiting;
    }

    public void setFiles_waiting(Boolean files_waiting) {
        this.files_waiting = files_waiting;
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
