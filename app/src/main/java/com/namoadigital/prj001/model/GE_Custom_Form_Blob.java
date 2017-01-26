package com.namoadigital.prj001.model;

/**
 * Created by DANIEL.LUCHE on 25/01/2017.
 */

public class GE_Custom_Form_Blob {

    private long customer_code;
    private int custom_form_type;
    private int custom_form_code;
    private int custom_form_version;
    private long blob_code;
    private String blob_name;
    private String blob_url;

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

    public long getBlob_code() {
        return blob_code;
    }

    public void setBlob_code(long blob_code) {
        this.blob_code = blob_code;
    }

    public String getBlob_name() {
        return blob_name;
    }

    public void setBlob_name(String blob_name) {
        this.blob_name = blob_name;
    }

    public String getBlob_url() {
        return blob_url;
    }

    public void setBlob_url(String blob_url) {
        this.blob_url = blob_url;
    }
}
