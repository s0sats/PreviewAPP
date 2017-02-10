package com.namoadigital.prj001.model;

import java.util.ArrayList;

/**
 * Created by DANIEL.LUCHE on 10/02/2017.
 */

public class TSave_Env {
    private String app_code;
    private String app_version;
    private String session_app;
    private long site_code;
    private long operation_code;
    private String token;
    private ArrayList<GE_Custom_Form_Data>  form_datas;
    private ArrayList<GE_Custom_Form_Data_Field> form_data_fields;

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

    public long getSite_code() {
        return site_code;
    }

    public void setSite_code(long site_code) {
        this.site_code = site_code;
    }

    public long getOperation_code() {
        return operation_code;
    }

    public void setOperation_code(long operation_code) {
        this.operation_code = operation_code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<GE_Custom_Form_Data> getForm_datas() {
        return form_datas;
    }

    public void setForm_datas(ArrayList<GE_Custom_Form_Data> form_datas) {
        this.form_datas = form_datas;
    }

    public ArrayList<GE_Custom_Form_Data_Field> getForm_data_fields() {
        return form_data_fields;
    }

    public void setForm_data_fields(ArrayList<GE_Custom_Form_Data_Field> form_data_fields) {
        this.form_data_fields = form_data_fields;
    }
}
