package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 08/05/2017.
 */

public class TCancelNFC_Rec {

    private String app;
    private String validation;
    private String link_url;
    private String error_msg;
    private String ret_error;
    private String ret;

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

    public String getRet_error() {
        return ret_error;
    }

    public void setRet_error(String ret_error) {
        this.ret_error = ret_error;
    }

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
        this.ret = ret;
    }
}
