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
    private String site_code;

    @Expose
    private long operation_code;

    @Expose
    private String signature;

    @Expose
    private String signature_name;

    @Expose
    private String token;

    @Expose
    private String location_type;

    @Expose
    private String location_lat;

    @Expose
    private String location_lng;

    @Expose
    private Integer so_prefix;

    @Expose
    private Integer so_code;
    @Expose
    private Integer zone_code;
    @Expose
    private Integer local_code;

    private int location_pendency;

    @Expose
    private String date_gps;

    private List<GE_Custom_Form_Data_Field> dataFields;
    //Campos novo agendamento
    @Expose
    private Integer schedule_prefix;
    @Expose
    private Integer schedule_code;
    @Expose
    private Integer schedule_exec;
    //Contem msg de retorno após sae do form no servidor.
    private String error_msg;
    //PK Control usada no Ticket.
    @Expose
    private Integer ticket_prefix;
    @Expose
    private Integer ticket_code;
    @Expose
    private Integer ticket_seq;
    @Expose
    private Integer ticket_seq_tmp;
    @Expose
    private Integer pipeline_code;
    @Expose
    private Integer step_code;
    @Expose
    private String ticket_checkin_date;
    @Expose
    private int tag_operational_code;

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
        this.site_code = "-1";
        this.operation_code = -1;
        this.signature = "";
        this.signature_name = "";
        this.token = "";
        this.dataFields = new ArrayList<>();
        this.location_type = "";
        this.location_lat = "";
        this.location_lng = "";
        this.so_prefix = null;
        this.so_code = null;
        this.zone_code = null;
        this.local_code = null;
        this.schedule_prefix = null;
        this.schedule_code = null;
        this.schedule_exec = null;
        this.error_msg = null;
        this.location_pendency = 0;
        this.date_gps="";
        this.ticket_prefix=null;
        this.ticket_code=null;
        this.ticket_seq=null;
        this.ticket_seq_tmp=null;
        this.pipeline_code=null;
        this.step_code=null;
        this.ticket_checkin_date = null;
        this.tag_operational_code = -1;
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

    public String getSite_code() {
        return site_code;
    }

    public void setSite_code(String site_code) {
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

    public String getSignature_name() {
        return signature_name;
    }

    public void setSignature_name(String signature_name) {
        this.signature_name = signature_name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    public String getLocation_lat() {
        return location_lat;
    }

    public void setLocation_lat(String location_lat) {
        this.location_lat = location_lat;
    }

    public String getLocation_lng() {
        return location_lng;
    }

    public void setLocation_lng(String location_lng) {
        this.location_lng = location_lng;
    }

    public Integer getSo_prefix() {
        return so_prefix;
    }

    public void setSo_prefix(Integer so_prefix) {
        this.so_prefix = so_prefix;
    }

    public Integer getSo_code() {
        return so_code;
    }

    public void setSo_code(Integer so_code) {
        this.so_code = so_code;
    }

    public Integer getZone_code() {
        return zone_code;
    }

    public void setZone_code(Integer zone_code) {
        this.zone_code = zone_code;
    }

    public Integer getLocal_code() {
        return local_code;
    }

    public void setLocal_code(Integer local_code) {
        this.local_code = local_code;
    }

    public List<GE_Custom_Form_Data_Field> getDataFields() {
        return dataFields;
    }

    public void setDataFields(List<GE_Custom_Form_Data_Field> dataFields) {
        this.dataFields = dataFields;
    }

    public Integer getSchedule_prefix() {
        return schedule_prefix;
    }

    public void setSchedule_prefix(Integer schedule_prefix) {
        this.schedule_prefix = schedule_prefix;
    }

    public Integer getSchedule_code() {
        return schedule_code;
    }

    public void setSchedule_code(Integer schedule_code) {
        this.schedule_code = schedule_code;
    }

    public Integer getSchedule_exec() {
        return schedule_exec;
    }

    public void setSchedule_exec(Integer schedule_exec) {
        this.schedule_exec = schedule_exec;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public int getLocation_pendency() {
        return location_pendency;
    }

    public void setLocation_pendency(int location_pendency) {
        this.location_pendency = location_pendency;
    }

    public String getDate_gps() {
        return date_gps;
    }

    public void setDate_gps(String date_gps) {
        this.date_gps = date_gps;
    }

    public Integer getTicket_prefix() {
        return ticket_prefix;
    }

    public void setTicket_prefix(Integer ticket_prefix) {
        this.ticket_prefix = ticket_prefix;
    }

    public Integer getTicket_code() {
        return ticket_code;
    }

    public void setTicket_code(Integer ticket_code) {
        this.ticket_code = ticket_code;
    }

    public Integer getTicket_seq() {
        return ticket_seq;
    }

    public void setTicket_seq(Integer ticket_seq) {
        this.ticket_seq = ticket_seq;
    }

    public Integer getTicket_seq_tmp() {
        return ticket_seq_tmp;
    }

    public void setTicket_seq_tmp(Integer ticket_seq_tmp) {
        this.ticket_seq_tmp = ticket_seq_tmp;
    }

    public Integer getPipeline_code() {
        return pipeline_code;
    }

    public void setPipeline_code(Integer pipeline_code) {
        this.pipeline_code = pipeline_code;
    }

    public Integer getStep_code() {
        return step_code;
    }

    public void setStep_code(Integer step_code) {
        this.step_code = step_code;
    }

    public String getTicket_checkin_date() {
        return ticket_checkin_date;
    }

    public void setTicket_checkin_date(String ticket_checkin_date) {
        this.ticket_checkin_date = ticket_checkin_date;
    }

    public int getTag_operational_code() {
        return tag_operational_code;
    }

    public void setTag_operational_code(int tag_operational_code) {
        this.tag_operational_code = tag_operational_code;
    }
}
