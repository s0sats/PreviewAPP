package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 08/05/2017.
 * Classe com propriedade que serão enviadas para o
 * servidor na chamada do WS server_cancel_nfc_auth.ws.*
 */

public class TCancelNFC_Env {

    private String app_code;
    private String app_version;
    private String session_app;


    public String getApp_code() {
        return app_code;
    }

    public void setApp_code(String app_code) {
        this.app_code = app_code;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getSession_app() {
        return session_app;
    }

    public void setSession_app(String session_app) {
        this.session_app = session_app;
    }
}
