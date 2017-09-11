package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 22/05/2017.
 */

public class TSerial_Tracking_Search_Rec {

    private String app;
    private String validation;
    private String link_url;
    private String error_msg;
    private int tracking_ret;
    private String tracking_msg;

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

    public int getTracking_ret() {
        return tracking_ret;
    }

    public void setTracking_ret(int tracking_ret) {
        this.tracking_ret = tracking_ret;
    }

    public String getTracking_msg() {
        return tracking_msg;
    }

    public void setTracking_msg(String tracking_msg) {
        this.tracking_msg = tracking_msg;
    }
}
