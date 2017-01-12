package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by neonhugo on 9/26/2016.
 */

public class TSync_Answer_Env {

    @Expose
    private long user_code;

    @Expose
    private long customer_code;

    @Expose
    private int translate_code;

    //private String date_db_customer;
    @Expose
    private String app_code;

    @Expose
    private String app_version;
    //private String date_db;

    @Expose
    private String email_p;

    @Expose
    private String password;

    @Expose
    private String nfc_code;

    @Expose
    private String device_code;

    @Expose
    private String token;

    @Expose
    private String manufacturer;

    @Expose
    private String model;

    @Expose
    private String os;

    @Expose
    private String version_os;
    //private String force_login;
    //private String date_db_translate;
    //private String date_db_customer_translate;
    @Expose
    private ArrayList<GE_Custom_Form_Data> form_datas;
    @Expose
    private ArrayList<GE_Custom_Form_Data_Field> form_data_fields;

    public long getUser_code() {
        return user_code;
    }

    public void setUser_code(long user_code) {
        this.user_code = user_code;
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getTranslate_code() {
        return translate_code;
    }

    public void setTranslate_code(int translate_code) {
        this.translate_code = translate_code;
    }

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

    public String getEmail_p() {
        return email_p;
    }

    public void setEmail_p(String email_p) {
        this.email_p = email_p;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNfc_code() {
        return nfc_code;
    }

    public void setNfc_code(String nfc_code) {
        this.nfc_code = nfc_code;
    }

    public String getDevice_code() {
        return device_code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setDevice_code(String device_code) {
        this.device_code = device_code;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getVersion_os() {
        return version_os;
    }

    public void setVersion_os(String version_os) {
        this.version_os = version_os;
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
