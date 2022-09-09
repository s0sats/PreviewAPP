package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by d.luche on 23/02/2018.
 */

public class TSave_Ap_Rec {

    @SerializedName("app") private String app;
    @SerializedName("validation") private String validation;
    @SerializedName("link_url") private String link_url;
    @SerializedName("error_msg") private String error_msg;
    @SerializedName("save") private String save;
    @SerializedName("ap") private ArrayList<ApSaveStatus> ap = new ArrayList<>();

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

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public ArrayList<ApSaveStatus> getAp() {
        return ap;
    }

    public void setAp(ArrayList<ApSaveStatus> ap) {
        this.ap = ap;
    }

    public static class ApSaveStatus {
        @SerializedName("customer_code") private long customer_code;
        @SerializedName("custom_form_type") private int custom_form_type;
        @SerializedName("custom_form_code") private int custom_form_code;
        @SerializedName("custom_form_version") private int custom_form_version;
        @SerializedName("custom_form_data") private long custom_form_data;
        @SerializedName("ap_code") private int ap_code;
        @SerializedName("ap_scn") private int ap_scn;
        @SerializedName("status_code") private int status_code;
        @SerializedName("status_msg") private String status_msg;

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

        public int getAp_code() {
            return ap_code;
        }

        public void setAp_code(int ap_code) {
            this.ap_code = ap_code;
        }

        public int getAp_scn() {
            return ap_scn;
        }

        public void setAp_scn(int ap_scn) {
            this.ap_scn = ap_scn;
        }

        public int getStatus_code() {
            return status_code;
        }

        public void setStatus_code(int status_code) {
            this.status_code = status_code;
        }

        public String getStatus_msg() {
            return status_msg;
        }

        public void setStatus_msg(String status_msg) {
            this.status_msg = status_msg;
        }
    }
}
