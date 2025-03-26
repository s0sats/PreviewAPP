package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by neomatrix on 7/22/16.
 */

public class GE_Custom_Form_Field {

    @SerializedName("customer_code")
    private long customer_code;
    @SerializedName("custom_form_type")
    private int custom_form_type;
    @SerializedName("custom_form_code")
    private int custom_form_code;
    @SerializedName("custom_form_version")
    private int custom_form_version;
    @SerializedName("custom_form_seq")
    private int custom_form_seq;
    @SerializedName("custom_form_data_type")
    private String custom_form_data_type;
    @SerializedName("custom_form_data_size")
    private Integer custom_form_data_size;
    @SerializedName("custom_form_data_mask")
    private String custom_form_data_mask;
    @SerializedName("custom_form_data_content")
    private String custom_form_data_content;
    @SerializedName("custom_form_local_link")
    private String custom_form_local_link;
    @SerializedName("custom_form_order")
    private int custom_form_order;
    @SerializedName("page")
    private int page;
    @SerializedName("required")
    private int required;
    @SerializedName("device_tp_code")
    private Integer device_tp_code;
    @SerializedName("automatic")
    private String automatic;
    @SerializedName("comment")
    private String comment;
    @SerializedName("require_photo_on_nc")
    private String require_photo_on_nc;
    @SerializedName("button_nc")
    private Integer button_nc;
    @SerializedName("button_photo")
    private Integer button_photo;
    @SerializedName("button_comment")
    private Integer button_comment;

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

    public Integer getDevice_tp_code() {
        return device_tp_code;
    }

    public void setDevice_tp_code(Integer device_tp_code) {
        this.device_tp_code = device_tp_code;
    }

    public String getAutomatic() {
        return automatic;
    }

    public void setAutomatic(String automatic) {
        this.automatic = automatic;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRequire_photo_on_nc() {
        return require_photo_on_nc;
    }

    public void setRequire_photo_on_nc(String require_photo_on_nc) {
        this.require_photo_on_nc = require_photo_on_nc;
    }

    public Integer getButton_nc() {
        return button_nc;
    }

    public void setButton_nc(Integer button_nc) {
        this.button_nc = button_nc;
    }

    public Integer getButton_photo() {
        return button_photo;
    }

    public void setButton_photo(Integer button_photo) {
        this.button_photo = button_photo;
    }

    public Integer getButton_comment() {
        return button_comment;
    }

    public void setButton_comment(Integer button_comment) {
        this.button_comment = button_comment;
    }
}
