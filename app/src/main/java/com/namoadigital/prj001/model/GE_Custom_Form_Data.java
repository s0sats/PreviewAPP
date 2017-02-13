package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 7/22/16.
 */

public class GE_Custom_Form_Data {

    @Expose
    private long customer_code;

    @Expose
    private int custom_form_type;

    @Expose
    private int custom_form_code;

    @Expose
    private int custom_form_version;

    @Expose
    private long custom_form_data; // Indexador

    @Expose
    private String custom_form_status; // Local (0 = nao sincronizado 1 = sincronizado 2 = Bloqueado para envio)

    @Expose
    private long product_code;

    @Expose
    private String serial_id;

    @Expose
    private String date_start;

    @Expose
    private String date_end;

    @Expose
    private long user_code;

    @Expose
    private long site_code;

    @Expose
    private long operation_code;

    @Expose
    private String signature;

    @Expose
    private String token;

    private List<GE_Custom_Form_Data_Field> dataFields;

    public GE_Custom_Form_Data() {
        this.customer_code = -1L;
        this.custom_form_type = -1;
        this.custom_form_code = -1;
        this.custom_form_version = -1;
        this.custom_form_data = -1L;
        this.custom_form_status = "2";
        this.product_code = -1L;
        this.serial_id = "";
        this.date_start = "1900-01-01";
        this.date_end = "1900-01-01";
        this.user_code = -1;
        this.site_code = -1;
        this.operation_code = -1;
        this.signature = "";
        this.token = "";
        this.dataFields = new ArrayList<>();
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getCustom_form_type() {
        return custom_form_type;
    }

    public void setCustom_form_type(int custom_form_type) {
        this.custom_form_type = custom_form_type;
    }

    public int getCustom_form_code() {
        return custom_form_code;
    }

    public void setCustom_form_code(int custom_form_code) {
        this.custom_form_code = custom_form_code;
    }

    public int getCustom_form_version() {
        return custom_form_version;
    }

    public void setCustom_form_version(int custom_form_version) {
        this.custom_form_version = custom_form_version;
    }

    public long getCustom_form_data() {
        return custom_form_data;
    }

    public void setCustom_form_data(long custom_form_data) {
        this.custom_form_data = custom_form_data;
    }

    public String getCustom_form_status() {
        return custom_form_status;
    }

    public void setCustom_form_status(String custom_form_status) {
        this.custom_form_status = custom_form_status;
    }

    public long getProduct_code() {
        return product_code;
    }

    public void setProduct_code(long product_code) {
        this.product_code = product_code;
    }

    public String getSerial_id() {
        return serial_id;
    }

    public void setSerial_id(String serial_id) {
        this.serial_id = serial_id;
    }

    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public long getUser_code() {
        return user_code;
    }

    public void setUser_code(long user_code) {
        this.user_code = user_code;
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

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<GE_Custom_Form_Data_Field> getDataFields() {
        return dataFields;
    }

    public void setDataFields(List<GE_Custom_Form_Data_Field> dataFields) {
        this.dataFields = dataFields;
    }
}
