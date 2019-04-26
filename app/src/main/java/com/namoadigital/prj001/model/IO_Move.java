package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

public class IO_Move implements Serializable {
    @Expose
    private long customer_code;
    @Expose
    private int move_prefix;
    @Expose
    private int move_code;
    @Expose
    private long product_code;
    @Expose
    private int serial_code;
    private int site_code;
    private Integer from_zone_code;
    private Integer from_local_code;
    private Integer from_class_code;
    private Integer planned_zone_code;
    private Integer planned_local_code;
    private Integer planned_class_code;
    @Expose
    private Integer to_zone_code;
    @Expose
    private Integer to_local_code;
    @Expose
    private Integer to_class_code;
    private String move_type;
    @Expose
    private Integer reason_code;
    @Expose
    private Integer inbound_prefix;
    @Expose
    private Integer inbound_code;
    @Expose
    private Integer inbound_item;
    private Integer outbound_prefix;
    private Integer outbound_code;
    private Integer outbound_item;
    @Expose
    private String done_date;
    @Expose
    private Integer done_user;
    @Expose
    private String done_user_nick;
    @Expose
    private String status;
    //CAMPO EXCLUSIVO PARA RECEBIMENTO DO WS PROCESS DOWNLOAD
    @Expose
    private ArrayList<MD_Product_Serial> serial = new ArrayList<>();
    @Expose
    private int update_required;
    private String token;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getMove_prefix() {
        return move_prefix;
    }

    public void setMove_prefix(int move_prefix) {
        this.move_prefix = move_prefix;
    }

    public int getMove_code() {
        return move_code;
    }

    public void setMove_code(int move_code) {
        this.move_code = move_code;
    }

    public long getProduct_code() {
        return product_code;
    }

    public void setProduct_code(long product_code) {
        this.product_code = product_code;
    }

    public int getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(int serial_code) {
        this.serial_code = serial_code;
    }

    public int getSite_code() {
        return site_code;
    }

    public void setSite_code(int site_code) {
        this.site_code = site_code;
    }

    public Integer getFrom_zone_code() {
        return from_zone_code;
    }

    public void setFrom_zone_code(Integer from_zone_code) {
        this.from_zone_code = from_zone_code;
    }

    public Integer getFrom_local_code() {
        return from_local_code;
    }

    public void setFrom_local_code(Integer from_local_code) {
        this.from_local_code = from_local_code;
    }

    public Integer getFrom_class_code() {
        return from_class_code;
    }

    public void setFrom_class_code(Integer from_class_code) {
        this.from_class_code = from_class_code;
    }

    public Integer getPlanned_zone_code() {
        return planned_zone_code;
    }

    public void setPlanned_zone_code(Integer planned_zone_code) {
        this.planned_zone_code = planned_zone_code;
    }

    public Integer getPlanned_local_code() {
        return planned_local_code;
    }

    public void setPlanned_local_code(Integer planned_local_code) {
        this.planned_local_code = planned_local_code;
    }

    public Integer getPlanned_class_code() {
        return planned_class_code;
    }

    public void setPlanned_class_code(Integer planned_class_code) {
        this.planned_class_code = planned_class_code;
    }

    public Integer getTo_zone_code() {
        return to_zone_code;
    }

    public void setTo_zone_code(Integer to_zone_code) {
        this.to_zone_code = to_zone_code;
    }

    public Integer getTo_local_code() {
        return to_local_code;
    }

    public void setTo_local_code(Integer to_local_code) {
        this.to_local_code = to_local_code;
    }

    public Integer getTo_class_code() {
        return to_class_code;
    }

    public void setTo_class_code(Integer to_class_code) {
        this.to_class_code = to_class_code;
    }

    public String getMove_type() {
        return move_type;
    }

    public void setMove_type(String move_type) {
        this.move_type = move_type;
    }

    public Integer getReason_code() {
        return reason_code;
    }

    public void setReason_code(Integer reason_code) {
        this.reason_code = reason_code;
    }

    public Integer getInbound_prefix() {
        return inbound_prefix;
    }

    public void setInbound_prefix(Integer inbound_prefix) {
        this.inbound_prefix = inbound_prefix;
    }

    public Integer getInbound_code() {
        return inbound_code;
    }

    public void setInbound_code(Integer inbound_code) {
        this.inbound_code = inbound_code;
    }

    public Integer getInbound_item() {
        return inbound_item;
    }

    public void setInbound_item(Integer inbound_item) {
        this.inbound_item = inbound_item;
    }

    public Integer getOutbound_prefix() {
        return outbound_prefix;
    }

    public void setOutbound_prefix(Integer outbound_prefix) {
        this.outbound_prefix = outbound_prefix;
    }

    public Integer getOutbound_code() {
        return outbound_code;
    }

    public void setOutbound_code(Integer outbound_code) {
        this.outbound_code = outbound_code;
    }

    public Integer getOutbound_item() {
        return outbound_item;
    }

    public void setOutbound_item(Integer outbound_item) {
        this.outbound_item = outbound_item;
    }

    public String getDone_date() {
        return done_date;
    }

    public void setDone_date(String done_date) {
        this.done_date = done_date;
    }

    public Integer getDone_user() {
        return done_user;
    }

    public void setDone_user(Integer done_user) {
        this.done_user = done_user;
    }

    public String getDone_user_nick() {
        return done_user_nick;
    }

    public void setDone_user_nick(String done_user_nick) {
        this.done_user_nick = done_user_nick;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<MD_Product_Serial> getSerial() {
        return serial;
    }

    public void setSerial(ArrayList<MD_Product_Serial> serial) {
        this.serial = serial;
    }

    public int getUpdate_required() {
        return update_required;
    }

    public void setUpdate_required(int update_required) {
        this.update_required = update_required;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
