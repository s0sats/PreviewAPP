package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 7/22/16.
 */

public class GE_Custom_Form_Field {

    private long customer_code;
    private int custom_form_type;
    private int custom_form_code;
    private int custom_form_version;
    private int custom_form_seq;
    private String custom_form_data_type;
    private Integer custom_form_data_size;
    private String custom_form_data_mask;
    private String custom_form_data_content;
    private String custom_form_local_link;
    private int custom_form_order;
    private int page;
    private int required;
    private String comment;

    public GE_Custom_Form_Field() {
        this.custom_form_local_link = "";
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

    public int getCustom_form_seq() {
        return custom_form_seq;
    }

    public void setCustom_form_seq(int custom_form_seq) {
        this.custom_form_seq = custom_form_seq;
    }

    public String getCustom_form_data_type() {
        return custom_form_data_type;
    }

    public void setCustom_form_data_type(String custom_form_data_type) {
        this.custom_form_data_type = custom_form_data_type;
    }

    public Integer getCustom_form_data_size() {
        return custom_form_data_size;
    }

    public void setCustom_form_data_size(Integer custom_form_data_size) {
        this.custom_form_data_size = custom_form_data_size;
    }

    public String getCustom_form_data_mask() {
        return custom_form_data_mask;
    }

    public void setCustom_form_data_mask(String custom_form_data_mask) {
        this.custom_form_data_mask = custom_form_data_mask;
    }

    public String getCustom_form_data_content() {
        return custom_form_data_content;
    }

    public void setCustom_form_data_content(String custom_form_data_content) {
        this.custom_form_data_content = custom_form_data_content;
    }

    public String getCustom_form_local_link() {
        return custom_form_local_link;
    }

    public void setCustom_form_local_link(String custom_form_local_link) {
        this.custom_form_local_link = custom_form_local_link;
    }

    public int getCustom_form_order() {
        return custom_form_order;
    }

    public void setCustom_form_order(int custom_form_order) {
        this.custom_form_order = custom_form_order;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRequired() {
        return required;
    }

    public void setRequired(int required) {
        this.required = required;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
