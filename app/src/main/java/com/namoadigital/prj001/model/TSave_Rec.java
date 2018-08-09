package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

/**
 * Created by DANIEL.LUCHE on 10/02/2017.
 */

public class TSave_Rec {
    @Expose
    private String app;
    @Expose
    private String validation;
    @Expose
    private String link_url;
    @Expose
    private String error_msg;
    @Expose
    private String save;

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

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }
}
