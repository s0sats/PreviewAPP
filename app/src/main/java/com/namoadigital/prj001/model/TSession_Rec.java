package com.namoadigital.prj001.model;

/**
 * Created by DANIEL.LUCHE on 20/01/2017.
 * Classe com propriedade que serão recebidas do
 * servidor na chamada do WS server_get_session.*
 */

public class TSession_Rec {

    private String app;
    private String validation;
    private String link_url;
    private String error_msg;
    private String session_app;

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

    public String getSession_app() {
        return session_app;
    }

    public void setSession_app(String session_app) {
        this.session_app = session_app;
    }
}
