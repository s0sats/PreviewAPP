package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 7/22/16.
 */

public class GE_Custom_Form {

    private long customer_code;
    private int custom_form_type;
    private int custom_form_code;
    private int custom_form_version;
    private int require_signature;
    private int require_location;
    private String automatic_fill;

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

    public int getRequire_signature() {
        return require_signature;
    }

    public void setRequire_signature(int require_signature) {
        this.require_signature = require_signature;
    }

    public int getRequire_location() {
        return require_location;
    }

    public void setRequire_location(int require_location) {
        this.require_location = require_location;
    }

    public String getAutomatic_fill() {
        return automatic_fill;
    }

    public void setAutomatic_fill(String automatic_fill) {
        this.automatic_fill = automatic_fill;
    }
}
