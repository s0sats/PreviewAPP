package com.namoadigital.prj001.model;

public class TSO_Create_Room_Rec {
    private String app;
    private String validation;
    private String link_url;
    private String error_msg;
    private String ret_code;
    private String ret_msg;
    private Integer ret_sync_full;
    private Integer ret_so_scn;

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

    public String getRet_code() {
        return ret_code;
    }

    public void setRet_code(String ret_code) {
        this.ret_code = ret_code;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }

    public Integer getRet_sync_full() {
        return ret_sync_full;
    }

    public void setRet_sync_full(Integer ret_sync_full) {
        this.ret_sync_full = ret_sync_full;
    }

    public Integer getRet_so_scn() {
        return ret_so_scn;
    }

    public void setRet_so_scn(Integer ret_so_scn) {
        this.ret_so_scn = ret_so_scn;
    }
}
