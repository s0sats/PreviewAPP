package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 05/06/2018.
 */

public class GE_Custom_Form_Site {

    private long customer_code;
    private int custom_form_type;
    private int custom_form_code;
    private int custom_form_version;
    private int site_code;

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

    public int getSite_code() {
        return site_code;
    }

    public void setSite_code(int site_code) {
        this.site_code = site_code;
    }
}
