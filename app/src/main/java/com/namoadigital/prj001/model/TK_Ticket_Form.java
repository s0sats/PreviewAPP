
package com.namoadigital.prj001.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.namoadigital.prj001.util.ConstantBaseApp;

import java.io.Serializable;

public class TK_Ticket_Form implements Serializable {
    @Expose
    private long customer_code;//pk
    @Expose
    private int ticket_prefix;//pk
    @Expose
    private int ticket_code;//pk
    @Expose
    private int ticket_seq;//pk
    @Expose
    private int ticket_seq_tmp;//pk
    @Expose
    private int step_code;//pk
    @Expose
    private String form_status;
    private int custom_form_type;
    private int custom_form_code;
    private int custom_form_version;
    private String custom_form_desc;
    @Nullable
    private Integer custom_form_data;
    @Nullable
    private String score_status;
    @Nullable
    private String score_perc;
    private int nc;
    @Nullable
    private Integer custom_form_data_tmp;
    @Nullable
    private Integer pdf_code;
    @Nullable
    private String pdf_name;
    @Nullable
    private String pdf_url;
    @Nullable
    private String pdf_url_local;

    public TK_Ticket_Form() {
        this.customer_code = -1;
        this.ticket_prefix = -1;
        this.ticket_code = -1;
        this.ticket_seq = -1;
        this.ticket_seq_tmp = -1;
        this.step_code = -1;
    }

    public void setPK(TK_Ticket_Ctrl tk_ticket_ctrl) {
        this.customer_code = tk_ticket_ctrl.getCustomer_code();
        this.ticket_prefix = tk_ticket_ctrl.getTicket_prefix();
        this.ticket_code = tk_ticket_ctrl.getTicket_code();
        this.ticket_seq = tk_ticket_ctrl.getTicket_seq();
        this.ticket_seq_tmp = tk_ticket_ctrl.getTicket_seq_tmp();
        this.step_code = tk_ticket_ctrl.getStep_code();
        this.form_status = tk_ticket_ctrl.getCtrl_status();
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getTicket_prefix() {
        return ticket_prefix;
    }

    public void setTicket_prefix(int ticket_prefix) {
        this.ticket_prefix = ticket_prefix;
    }

    public int getTicket_code() {
        return ticket_code;
    }

    public void setTicket_code(int ticket_code) {
        this.ticket_code = ticket_code;
    }

    public int getTicket_seq() {
        return ticket_seq;
    }

    public void setTicket_seq(int ticket_seq) {
        this.ticket_seq = ticket_seq;
    }

    public int getTicket_seq_tmp() {
        return ticket_seq_tmp;
    }

    public void setTicket_seq_tmp(int ticket_seq_tmp) {
        this.ticket_seq_tmp = ticket_seq_tmp;
    }

    public int getStep_code() {
        return step_code;
    }

    public void setStep_code(int step_code) {
        this.step_code = step_code;
    }

    public String getForm_status() {
        return form_status;
    }

    public void setForm_status(String form_status) {
        this.form_status = form_status;
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

    public String getCustom_form_desc() {
        return custom_form_desc;
    }

    public void setCustom_form_desc(String custom_form_desc) {
        this.custom_form_desc = custom_form_desc;
    }

    @Nullable
    public Integer getCustom_form_data() {
        return custom_form_data;
    }

    public void setCustom_form_data(@Nullable Integer custom_form_data) {
        this.custom_form_data = custom_form_data;
    }

    @Nullable
    public String getScore_status() {
        return score_status;
    }

    public void setScore_status(@Nullable String score_status) {
        this.score_status = score_status;
    }

    @Nullable
    public String getScore_perc() {
        return score_perc;
    }

    public void setScore_perc(@Nullable String score_perc) {
        this.score_perc = score_perc;
    }

    public int getNc() {
        return nc;
    }

    public void setNc(int nc) {
        this.nc = nc;
    }

    @Nullable
    public Integer getCustom_form_data_tmp() {
        return custom_form_data_tmp;
    }

    public void setCustom_form_data_tmp(@Nullable Integer custom_form_data_tmp) {
        this.custom_form_data_tmp = custom_form_data_tmp;
    }

    @Nullable
    public Integer getPdf_code() {
        return pdf_code;
    }

    public void setPdf_code(@Nullable Integer pdf_code) {
        this.pdf_code = pdf_code;
    }

    @Nullable
    public String getPdf_name() {
        return pdf_name;
    }

    public void setPdf_name(@Nullable String pdf_name) {
        this.pdf_name = pdf_name;
    }

    @Nullable
    public String getPdf_url() {
        return pdf_url;
    }

    public void setPdf_url(@Nullable String pdf_url) {
        this.pdf_url = pdf_url;
    }

    @Nullable
    public String getPdf_url_local() {
        return pdf_url_local;
    }

    public void setPdf_url_local(@Nullable String pdf_url_local) {
        this.pdf_url_local = pdf_url_local;
    }
    
    public String getPdfUrlLocalName(boolean withExtension){
            return
                ConstantBaseApp.N_FORM_PDF_PREFIX +
                this.customer_code + "_" +
                this.custom_form_type+ "_" +
                this.custom_form_code+ "_" +
                this.custom_form_version+ "_" +
                this.custom_form_data +
                (withExtension ? ".pdf" : "");
    }
}
