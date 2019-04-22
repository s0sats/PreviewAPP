package com.namoadigital.prj001.model;

import java.util.ArrayList;

public class IO_Blind_Move {

    private long customer_code;
    private int blind_tmp;
    private Integer blind_prefix;
    private Integer blind_code;
    private long product_code;
    private int serial_code;
    private String serial_id;
    private int site_code;
    private int zone_code;
    private int local_code;
    private int reason_code;
    private Integer class_code;
    private int flag_blind;
    private String status;
    private String save_date;
    private String token;
    private String error_msg;
    private ArrayList<IO_Blind_Move_Tracking> tracking_list = new ArrayList<>();

    public void setPk(){
        for (int i = 0; i < tracking_list.size(); i++) {
            tracking_list.get(i).setPk(this);
        }
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getBlind_tmp() {
        return blind_tmp;
    }

    public void setBlind_tmp(int blind_tmp) {
        this.blind_tmp = blind_tmp;
    }

    public Integer getBlind_prefix() {
        return blind_prefix;
    }

    public void setBlind_prefix(Integer blind_prefix) {
        this.blind_prefix = blind_prefix;
    }

    public Integer getBlind_code() {
        return blind_code;
    }

    public void setBlind_code(Integer blind_code) {
        this.blind_code = blind_code;
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

    public String getSerial_id() {
        return serial_id;
    }

    public void setSerial_id(String serial_id) {
        this.serial_id = serial_id;
    }

    public int getSite_code() {
        return site_code;
    }

    public void setSite_code(int site_code) {
        this.site_code = site_code;
    }

    public int getZone_code() {
        return zone_code;
    }

    public void setZone_code(int zone_code) {
        this.zone_code = zone_code;
    }

    public int getLocal_code() {
        return local_code;
    }

    public void setLocal_code(int local_code) {
        this.local_code = local_code;
    }

    public int getReason_code() {
        return reason_code;
    }

    public void setReason_code(int reason_code) {
        this.reason_code = reason_code;
    }

    public Integer getClass_code() {
        return class_code;
    }

    public void setClass_code(Integer class_code) {
        this.class_code = class_code;
    }

    public int getFlag_blind() {
        return flag_blind;
    }

    public void setFlag_blind(int flag_blind) {
        this.flag_blind = flag_blind;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSave_date() {
        return save_date;
    }

    public void setSave_date(String save_date) {
        this.save_date = save_date;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public ArrayList<IO_Blind_Move_Tracking> getTracking_list() {
        return tracking_list;
    }

    public void setTracking_list(ArrayList<IO_Blind_Move_Tracking> tracking_list) {
        this.tracking_list = tracking_list;
    }
}
