package com.namoadigital.prj001.model;

import java.util.ArrayList;

/**
 * Created by d.luche on 23/02/2018.
 */

public class TSO_Pack_Express_Rec {


    private String app;
    private String validation;
    private String link_url;
    private String error_msg;
    private String save;

    private ArrayList<SO_Pack_Express_Local> pack_express;

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

    public ArrayList<SO_Pack_Express_Local> getPack_express() {
        return pack_express;
    }

    public void setPack_express(ArrayList<SO_Pack_Express_Local> pack_express) {
        this.pack_express = pack_express;
    }
}
