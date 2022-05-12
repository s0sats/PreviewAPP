package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * LUCHE - 18/11/2020
 * CLASSE CRIADA CM A PROPOSTA DE SER A CLASSE PAI DE TODOS OS OBJ DE RESPONSE, CONTENDO OS RETORNOS
 * PADRÕES DO WS
 */
public class Main_Header_Rec {
    @Expose
    @SerializedName("app") protected String app;
    @Expose
    @SerializedName("validation")protected String validation;
    @Expose
    @SerializedName("link_url")protected String link_url;
    @Expose
    @SerializedName("error_msg")protected String error_msg;

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
}
