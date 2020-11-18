package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

/**
 * LUCHE - 18/11/2020
 * CLASSE CRIADA CM A PROPOSTA DE SER A CLASSE PAI DE TODOS OS OBJ DE RESPONSE, CONTENDO OS RETORNOS
 * PADRÕES DO WS
 */
public class Main_Header_Rec {
    @Expose
    protected String app;
    @Expose
    protected String validation;
    @Expose
    protected String link_url;
    @Expose
    protected String error_msg;
    @Expose
    protected int valid_time = -1 ;
    @Expose
    protected long tolerance_time = -1;

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
