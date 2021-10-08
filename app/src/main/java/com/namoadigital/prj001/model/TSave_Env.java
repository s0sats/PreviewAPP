package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * Created by DANIEL.LUCHE on 10/02/2017.
 */

public class TSave_Env extends Main_Header_Env {

    @Expose
    private String site_code;
    @Expose
    private long operation_code;
    @Expose
    private String token;
    @Expose
    private List<GE_Custom_Form_Data> form_datas;
    @Expose
    private List<GE_Custom_Form_Data_Field> form_data_fields;
    //LISTA DE ITEM DA O.S
    @Expose
    private List<GeOsDeviceItem> form_items;


    public String getSite_code() {
        return site_code;
    }

    public void setSite_code(String site_code) {
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

    public List<GE_Custom_Form_Data> getForm_datas() {
        return form_datas;
    }

    public void setForm_datas(List<GE_Custom_Form_Data> form_datas) {
        this.form_datas = form_datas;
    }

    public List<GE_Custom_Form_Data_Field> getForm_data_fields() {
        return form_data_fields;
    }

    public void setForm_data_fields(List<GE_Custom_Form_Data_Field> form_data_fields) {
        this.form_data_fields = form_data_fields;
    }

    public List<GeOsDeviceItem> getForm_items() {
        return form_items;
    }

    public void setForm_items(List<GeOsDeviceItem> form_items) {
        this.form_items = form_items;
    }
}
