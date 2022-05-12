package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DANIEL.LUCHE on 10/02/2017.
 *
 * LUCHE - 20/02/2020
 *
 * Add propriedade error_process
 *
 */

public class TSave_Rec {
    @Expose
    @SerializedName("app") private String app;
    @Expose
    @SerializedName("validation") private String validation;
    @Expose
    @SerializedName("link_url") private String link_url;
    @Expose
    @SerializedName("error_msg") private String error_msg;
    @Expose
    @SerializedName("save") private String save;
    @Expose
    @SerializedName("error_process") private ArrayList<Error_Process> error_process = new ArrayList<>();

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

    public ArrayList<Error_Process> getError_process() {
        return error_process;
    }

    public void setError_process(ArrayList<Error_Process> error_process) {
        this.error_process = error_process;
    }

    public class Error_Process{
        public static final String ERROR_TYPE_SCHEDULE = "SCHEDULE";
        public static final String ERROR_TYPE_TICKET = "TICKET_FORM";

        @Expose
        @SerializedName("customer_code") private int customer_code;
        @Expose
        @SerializedName("custom_form_type") private int custom_form_type;
        @Expose
        @SerializedName("custom_form_code") private int custom_form_code;
        @Expose
        @SerializedName("custom_form_version") private int custom_form_version;
        @Expose
        @SerializedName("custom_form_data") private int custom_form_data;
        @Expose
        @SerializedName("error") private String error;
        //As propriedades abaixo são preenchidas após o retorno do servidor
        //e são usadas somente para exibição do erro na tela.
        @Expose
        @SerializedName("custom_form_desc") private String custom_form_desc;
        @Expose
        @SerializedName("schedule_pk") private String schedule_pk;
        @Expose
        @SerializedName("schedule_desc") private String schedule_desc;
        @Expose
        @SerializedName("error_type") private String error_type;
        @Expose
        @SerializedName("ticket_step_pk") private String ticket_step_pk;
        @Expose
        @SerializedName("ticket_step_desc") private String ticket_step_desc;

        public int getCustomer_code() {
            return customer_code;
        }

        public void setCustomer_code(int customer_code) {
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

        public int getCustom_form_data() {
            return custom_form_data;
        }

        public void setCustom_form_data(int custom_form_data) {
            this.custom_form_data = custom_form_data;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getCustom_form_desc() {
            return custom_form_desc;
        }

        public void setCustom_form_desc(String custom_form_desc) {
            this.custom_form_desc = custom_form_desc;
        }

        public String getSchedule_pk() {
            return schedule_pk;
        }

        public void setSchedule_pk(String schedule_pk) {
            this.schedule_pk = schedule_pk;
        }

        public String getSchedule_desc() {
            return schedule_desc;
        }

        public void setSchedule_desc(String schedule_desc) {
            this.schedule_desc = schedule_desc;
        }

        public String getError_type() {
            return error_type;
        }

        public void setError_type(String error_type) {
            this.error_type = error_type;
        }

        public String getTicket_step_pk() {
            return ticket_step_pk;
        }

        public void setTicket_step_pk(String ticket_step_pk) {
            this.ticket_step_pk = ticket_step_pk;
        }

        public String getTicket_step_desc() {
            return ticket_step_desc;
        }

        public void setTicket_step_desc(String ticket_step_desc) {
            this.ticket_step_desc = ticket_step_desc;
        }
    }
}


