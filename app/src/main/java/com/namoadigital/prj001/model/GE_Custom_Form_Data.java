package com.namoadigital.prj001.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by neomatrix on 7/22/16.
 */

public class GE_Custom_Form_Data {

    @Expose
    @SerializedName("customer_code")
    private long customer_code;

    @Expose
    @SerializedName("custom_form_type")
    private int custom_form_type;

    @Expose
    @SerializedName("custom_form_code")
    private int custom_form_code;

    @Expose
    @SerializedName("custom_form_version")
    private int custom_form_version;

    @Expose
    @SerializedName("custom_form_data")
    private long custom_form_data; // Indexador

    @Expose
    @SerializedName("custom_form_status")
    private String custom_form_status; // Local (0 = nao sincronizado 1 = sincronizado 2 = Bloqueado para envio)

    @Expose
    @SerializedName("product_code")
    private long product_code;

    @Expose
    @SerializedName("serial_id")
    private String serial_id;

    /**
     * BARRIONUEVO 15-03-2022
     * Class code do serial preenchido no dialog de finalização de form os.
     */
    @Expose
    @SerializedName("class_code")
    private Integer class_code;

    @Expose
    @SerializedName("date_start")
    private String date_start;

    @Expose
    @SerializedName("date_end")
    private String date_end;

    @Expose
    @SerializedName("user_code")
    private long user_code;

    @Expose
    @SerializedName("site_code")
    private String site_code;

    @Expose
    @SerializedName("operation_code")
    private long operation_code;

    @Expose
    @SerializedName("signature")
    private String signature;

    @Expose
    @SerializedName("signature_name")
    private String signature_name;

    @Expose
    @SerializedName("token")
    private String token;

    @Expose
    @SerializedName("location_type")
    private String location_type;

    @Expose
    @SerializedName("location_lat")
    private String location_lat;

    @Expose
    @SerializedName("location_lng")
    private String location_lng;

    @Expose
    @SerializedName("so_prefix")
    private Integer so_prefix;

    @Expose
    @SerializedName("so_code")
    private Integer so_code;
    @Expose
    @SerializedName("zone_code")
    private Integer zone_code;
    @Expose
    @SerializedName("local_code")
    private Integer local_code;
    @SerializedName("location_pendency")
    private int location_pendency;

    @Expose
    @SerializedName("date_gps")
    private String date_gps;

    @SerializedName("dataFields")
    private List<GE_Custom_Form_Data_Field> dataFields;
    //Campos novo agendamento
    @Expose
    @SerializedName("schedule_prefix")
    private Integer schedule_prefix;
    @Expose
    @SerializedName("schedule_code")
    private Integer schedule_code;
    @Expose
    @SerializedName("schedule_exec")
    private Integer schedule_exec;
    //Contem msg de retorno após sae do form no servidor.
    @SerializedName("error_msg")
    private String error_msg;
    //PK Control usada no Ticket.
    @Expose
    @SerializedName("ticket_prefix")
    private Integer ticket_prefix;
    @Expose
    @SerializedName("ticket_code")
    private Integer ticket_code;
    @Expose
    @SerializedName("ticket_seq")
    private Integer ticket_seq;
    @Expose
    @SerializedName("ticket_seq_tmp")
    private Integer ticket_seq_tmp;
    @Expose
    @SerializedName("pipeline_code")
    private Integer pipeline_code;
    @Expose
    @SerializedName("step_code")
    private Integer step_code;
    @Expose
    @SerializedName("ticket_checkin_date")
    private String ticket_checkin_date;
    @Expose
    @SerializedName("tag_operational_code")
    private int tag_operational_code;
    /*
    * CAMPOS FORM O.S
    */
    //Esses campos sys_date.. agora receberam a mesma data de criação e finalização que eram setadas
    //anteriormente em date_start e date_end,pois se o form for o.s,os campo date_start e date_end terão
    //as datas definidas pelo frag de criação de form o.s e dialog de finalização.........
    //Era mais facil chamar essas sys_date... de os_date... e não inverter as coisas....
    // mas a vida tem dessas...
    @Expose
    @SerializedName("sys_date_start")
    private String sys_date_start;
    @Expose
    @SerializedName("sys_date_end")
    private String sys_date_end;
    @Expose
    @SerializedName("order_type_code")
    private Integer order_type_code;
    @Expose
    @SerializedName("backup_product_code")
    private Integer backup_product_code;
    @Expose
    @SerializedName("backup_serial_code")
    private Integer backup_serial_code;
    @Expose
    @SerializedName("device_tp_code")
    private Integer device_tp_code;
    @Expose
    @SerializedName("measure_tp_code")
    private Integer measure_tp_code;
    @Expose
    @SerializedName("measure_value")
    private Float measure_value;
    @Expose
    @SerializedName("measure_cycle_value")
    private Float measure_cycle_value;
    @Expose
    @SerializedName("finalized_service")
    private Integer finalized_service;
    @Expose
    @SerializedName("custom_form_data_partition")
    private Integer custom_form_data_partition;

    @Expose
    @SerializedName("custom_form_version_partition")
    private Integer custom_form_version_partition;
    @Expose
    @SerializedName("kanban_reschedule_date")
    private String kanban_reschedule_date;
    @Expose
    @Nullable
    @SerializedName("trip_prefix") private Integer trip_prefix;
    @Expose
    @Nullable
    @SerializedName("trip_code") private Integer trip_code;
    @Expose
    @Nullable
    @SerializedName("destination_seq") private Integer destination_seq;

    @Expose
    @Nullable
    @SerializedName("initial_is_serial_stopped") private Integer initial_is_serial_stopped;

    @Expose
    @Nullable
    @SerializedName("initial_stopped_date") private String initial_stopped_date;

    @Expose
    @Nullable
    @SerializedName("initial_unavailability_reason") private String initial_unavailability_reason;

    @Expose
    @Nullable
    @SerializedName("final_is_serial_stopped") private Integer final_is_serial_stopped;

    @Expose
    @Nullable
    @SerializedName("final_unavailability_reason") private String final_unavailability_reason;

    @Expose
    @SerializedName("allow_form_in_the_past") private Integer allow_form_in_the_past;


    public GE_Custom_Form_Data() {
        this.customer_code = -1L;
        this.custom_form_type = -1;
        this.custom_form_code = -1;
        this.custom_form_version = -1;
        this.custom_form_data = -1L;
        this.custom_form_status = "2";
        this.product_code = -1L;
        this.serial_id = "";
        this.class_code = null;
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
        this.sys_date_start = this.date_start;
        this.sys_date_end = this.date_end;
        this.order_type_code = null;
        this.backup_product_code = null;
        this.backup_serial_code = null;
        this.device_tp_code = null;
        this.measure_tp_code = null;
        this.measure_value = null;
        this.measure_cycle_value = null;
        this.finalized_service = null;
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

    public Integer getClass_code() {
        return class_code;
    }

    public void setClass_code(Integer class_code) {
        this.class_code = class_code;
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

    public String getSys_date_start() {
        return sys_date_start;
    }

    public void setSys_date_start(String sys_date_start) {
        this.sys_date_start = sys_date_start;
    }

    public String getSys_date_end() {
        return sys_date_end;
    }

    public void setSys_date_end(String sys_date_end) {
        this.sys_date_end = sys_date_end;
    }

    public Integer getOrder_type_code() {
        return order_type_code;
    }

    public void setOrder_type_code(Integer order_type_code) {
        this.order_type_code = order_type_code;
    }

    public Integer getBackup_product_code() {
        return backup_product_code;
    }

    public void setBackup_product_code(Integer backup_product_code) {
        this.backup_product_code = backup_product_code;
    }

    public Integer getBackup_serial_code() {
        return backup_serial_code;
    }

    public void setBackup_serial_code(Integer backup_serial_code) {
        this.backup_serial_code = backup_serial_code;
    }

    public Integer getDevice_tp_code() {
        return device_tp_code;
    }

    public void setDevice_tp_code(Integer device_tp_code) {
        this.device_tp_code = device_tp_code;
    }

    public Integer getMeasure_tp_code() {
        return measure_tp_code;
    }

    public void setMeasure_tp_code(Integer measure_tp_code) {
        this.measure_tp_code = measure_tp_code;
    }

    public Float getMeasure_value() {
        return measure_value;
    }

    public void setMeasure_value(Float measure_value) {
        this.measure_value = measure_value;
    }

    public Float getMeasure_cycle_value() {
        return measure_cycle_value;
    }

    public void setMeasure_cycle_value(Float measure_cycle_value) {
        this.measure_cycle_value = measure_cycle_value;
    }

    public Integer getFinalized_service() {
        return finalized_service;
    }

    public void setFinalized_service(Integer finalized_service) {
        this.finalized_service = finalized_service;
    }

    public Integer getCustom_form_data_partition() {
        return custom_form_data_partition;
    }

    public void setCustom_form_data_partition(Integer custom_form_data_partition) {
        this.custom_form_data_partition = custom_form_data_partition;
    }

    public Integer getCustom_form_version_partition() {
        return custom_form_version_partition;
    }

    public void setCustom_form_version_partition(Integer custom_form_version_partition) {
        this.custom_form_version_partition = custom_form_version_partition;
    }

    public String getKanban_reschedule_date() {
        return kanban_reschedule_date;
    }

    public void setKanban_reschedule_date(String kanban_reschedule_date) {
        this.kanban_reschedule_date = kanban_reschedule_date;
    }

    @Nullable
    public Integer getTrip_prefix() {
        return trip_prefix;
    }

    public void setTrip_prefix(@Nullable Integer trip_prefix) {
        this.trip_prefix = trip_prefix;
    }

    @Nullable
    public Integer getTrip_code() {
        return trip_code;
    }

    public void setTrip_code(@Nullable Integer trip_code) {
        this.trip_code = trip_code;
    }

    @Nullable
    public Integer getDestination_seq() {
        return destination_seq;
    }

    public void setDestination_seq(@Nullable Integer destination_seq) {
        this.destination_seq = destination_seq;
    }

    @Nullable
    public Integer getInitial_is_serial_stopped() {
        return initial_is_serial_stopped;
    }

    public void setInitial_is_serial_stopped(@Nullable Integer initial_is_serial_stopped) {
        this.initial_is_serial_stopped = initial_is_serial_stopped;
    }

    @Nullable
    public String getInitial_stopped_date() {
        return initial_stopped_date;
    }

    public void setInitial_stopped_date(@Nullable String initial_stopped_date) {
        this.initial_stopped_date = initial_stopped_date;
    }

    @Nullable
    public String getInitial_unavailability_reason() {
        return initial_unavailability_reason;
    }

    public void setInitial_unavailability_reason(@Nullable String initial_unavailability_reason) {
        this.initial_unavailability_reason = initial_unavailability_reason;
    }

    @Nullable
    public Integer getFinal_is_serial_stopped() {
        return final_is_serial_stopped;
    }

    public void setFinal_is_serial_stopped(@Nullable Integer final_is_serial_stopped) {
        this.final_is_serial_stopped = final_is_serial_stopped;
    }

    @Nullable
    public String getFinal_unavailability_reason() {
        return final_unavailability_reason;
    }

    public void setFinal_unavailability_reason(@Nullable String final_unavailability_reason) {
        this.final_unavailability_reason = final_unavailability_reason;
    }

    public Integer getAllow_form_in_the_past() {
        return allow_form_in_the_past;
    }

    public void setAllow_form_in_the_past(Integer allow_form_in_the_past) {
        this.allow_form_in_the_past = allow_form_in_the_past;
    }
}
