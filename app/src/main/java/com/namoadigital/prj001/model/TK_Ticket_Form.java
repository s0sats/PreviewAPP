
package com.namoadigital.prj001.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.namoadigital.prj001.util.ConstantBaseApp;

import java.io.Serializable;

public class TK_Ticket_Form implements Serializable {
    @Expose
    @SerializedName("customer_code") private long customer_code;//pk
    @Expose
    @SerializedName("ticket_prefix") private int ticket_prefix;//pk
    @Expose
    @SerializedName("ticket_code") private int ticket_code;//pk
    @Expose
    @SerializedName("ticket_seq") private int ticket_seq;//pk
    @Expose
    @SerializedName("ticket_seq_tmp") private int ticket_seq_tmp;//pk
    @Expose
    @SerializedName("step_code") private int step_code;//pk
    @Expose
    @SerializedName("form_status") private String form_status;
    @SerializedName("custom_form_type") private int custom_form_type;
    @SerializedName("custom_form_code") private int custom_form_code;
    @SerializedName("custom_form_version") private int custom_form_version;
    @SerializedName("custom_form_desc") private String custom_form_desc;
    @Nullable
    @SerializedName("custom_form_data") private Integer custom_form_data;
    @Nullable
    @SerializedName("score_status") private String score_status;
    @Nullable
    @SerializedName("score_perc") private String score_perc;
    @SerializedName("nc") private int nc;
    @SerializedName("is_so") private int is_so;
    @Nullable
    @SerializedName("custom_form_data_tmp") private Integer custom_form_data_tmp;
    @Nullable
    @SerializedName("pdf_code") private Integer pdf_code;
    @Nullable
    @SerializedName("pdf_name") private String pdf_name;
    @Nullable
    @SerializedName("pdf_url") private String pdf_url;
    @Nullable
    @SerializedName("pdf_url_local") private String pdf_url_local;
    @SerializedName("custom_form_data_partition") private Integer custom_form_data_partition;
    @SerializedName("custom_form_version_partition") private Integer custom_form_version_partition;
    @SerializedName("order_type_code") private Integer order_type_code;
    @SerializedName("order_type_desc") private String order_type_desc;
    @SerializedName("process_type") private String process_type;
    @SerializedName("measure_tp_code") private Integer measure_tp_code;
    @SerializedName("measure_tp_desc") private String measure_tp_desc;
    @SerializedName("measure_value") private Double measure_value;
    @SerializedName("measure_cycle_value") private Float measure_cycle_value;
    @SerializedName("value_sufix") private String value_sufix;
    @SerializedName("date_start") private String start_date;
    @SerializedName("partition_min_date") private String partition_min_date;

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

    public int getIs_so() {
        return is_so;
    }

    public void setIs_so(int is_so) {
        this.is_so = is_so;
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

    public Integer getCustom_form_data_partition() {
        return custom_form_data_partition;
    }

    public void setCustom_form_data_partition(Integer custom_form_data_partition) {
        this.custom_form_data_partition = custom_form_data_partition;
    }

    public Integer getOrder_type_code() {
        return order_type_code;
    }

    public void setOrder_type_code(Integer order_type_code) {
        this.order_type_code = order_type_code;
    }

    public String getOrder_type_desc() {
        return order_type_desc;
    }

    public void setOrder_type_desc(String order_type_desc) {
        this.order_type_desc = order_type_desc;
    }

    public String getProcess_type() {
        return process_type;
    }

    public void setProcess_type(String process_type) {
        this.process_type = process_type;
    }

    public Integer getMeasure_tp_code() {
        return measure_tp_code;
    }

    public void setMeasure_tp_code(Integer measure_tp_code) {
        this.measure_tp_code = measure_tp_code;
    }

    public String getMeasure_tp_desc() {
        return measure_tp_desc;
    }

    public void setMeasure_tp_desc(String measure_tp_desc) {
        this.measure_tp_desc = measure_tp_desc;
    }

    public Double getMeasure_value() {
        return measure_value;
    }

    public void setMeasure_value(Double measure_value) {
        this.measure_value = measure_value;
    }

    public Float getMeasure_cycle_value() {
        return measure_cycle_value;
    }

    public void setMeasure_cycle_value(Float measure_cycle_value) {
        this.measure_cycle_value = measure_cycle_value;
    }

    public String getValue_sufix() {
        return value_sufix;
    }

    public void setValue_sufix(String value_sufix) {
        this.value_sufix = value_sufix;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getPartition_min_date() {
        return partition_min_date;
    }

    public void setPartition_min_date(String partition_min_date) {
        this.partition_min_date = partition_min_date;
    }

    public Integer getCustom_form_version_partition(){
        return this.custom_form_version_partition;
    }

    public void setCustom_form_version_partition(Integer custom_form_version_partition){
        this.custom_form_version_partition = custom_form_version_partition;
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
