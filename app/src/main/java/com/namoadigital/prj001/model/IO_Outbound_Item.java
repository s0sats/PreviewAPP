package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class IO_Outbound_Item implements Serializable {

    @Expose
    @SerializedName("customer_code") private  long customer_code;
    @Expose
    @SerializedName("outbound_prefix") private  int outbound_prefix;
    @Expose
    @SerializedName("outbound_code") private  int outbound_code;
    @Expose
    @SerializedName("outbound_item") private  int outbound_item;
    @Expose
    @SerializedName("product_code") private  long product_code;
    @Expose
    @SerializedName("serial_code") private  long serial_code;
    @Expose
    @SerializedName("class_code") private Integer class_code;
    @Expose
    @SerializedName("class_id") private String class_id;
    @Expose
    @SerializedName("conf_date") private  String conf_date;
    @Expose
    @SerializedName("status") private  String status;
    @Expose
    @SerializedName("inbound_prefix") private  Integer inbound_prefix;
    @Expose
    @SerializedName("inbound_code") private  Integer inbound_code;
    @Expose
    @SerializedName("inbound_item") private  Integer inbound_item;
    @Expose
    @SerializedName("comments") private  String comments;
    @Expose
    @SerializedName("save_date") private  String save_date;
    @Expose
    @SerializedName("update_required") private  int update_required;
    @Expose
    @SerializedName("out_conf_done") private  int out_conf_done;

    @SerializedName("serial") private ArrayList<MD_Product_Serial> serial = new ArrayList<>();
    @SerializedName("move") private ArrayList<IO_Move> move = new ArrayList<>();
    //Somente para envio dos tracking quando out_conf\/
    @Expose
    @SerializedName("tracking_list") private ArrayList<IO_Conf_Tracking> tracking_list = new ArrayList<>();
    public void IO_Outbound_Item(){
        this.customer_code = -1;
        this.outbound_prefix = -1;
        this.outbound_code = -1;
        this.outbound_item = -1;
    }

    public void setPK(IO_Outbound io_outbound){
        this.customer_code = io_outbound.getCustomer_code();
        this.outbound_prefix = io_outbound.getOutbound_prefix();
        this.outbound_code = io_outbound.getOutbound_code();
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getOutbound_prefix() {
        return outbound_prefix;
    }

    public void setOutbound_prefix(int outbound_prefix) {
        this.outbound_prefix = outbound_prefix;
    }

    public int getOutbound_code() {
        return outbound_code;
    }

    public void setOutbound_code(int outbound_code) {
        this.outbound_code = outbound_code;
    }

    public int getOutbound_item() {
        return outbound_item;
    }

    public void setOutbound_item(int outbound_item) {
        this.outbound_item = outbound_item;
    }

    public long getProduct_code() {
        return product_code;
    }

    public void setProduct_code(long product_code) {
        this.product_code = product_code;
    }

    public long getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(long serial_code) {
        this.serial_code = serial_code;
    }

    public Integer getClass_code() {
        return class_code;
    }

    public void setClass_code(Integer class_code) {
        this.class_code = class_code;
    }

    public String getClass_id() {
        return class_id;
    }

    public void setClass_id(String class_id) {
        this.class_id = class_id;
    }

    public String getConf_date() {
        return conf_date;
    }

    public void setConf_date(String conf_date) {
        this.conf_date = conf_date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getInbound_prefix() {
        return inbound_prefix;
    }

    public void setInbound_prefix(Integer inbound_prefix) {
        this.inbound_prefix = inbound_prefix;
    }

    public String getSave_date() {
        return save_date;
    }

    public void setSave_date(String save_date) {
        this.save_date = save_date;
    }

    public int getUpdate_required() {
        return update_required;
    }

    public void setUpdate_required(int update_required) {
        this.update_required = update_required;
    }

    public int getOut_conf_done() {
        return out_conf_done;
    }

    public void setOut_conf_done(int out_conf_done) {
        this.out_conf_done = out_conf_done;
    }

    public ArrayList<MD_Product_Serial> getSerial() {
        return serial;
    }

    public void setSerial(ArrayList<MD_Product_Serial> serial) {
        this.serial = serial;
    }

    public ArrayList<IO_Move> getMove() {
        return move;
    }

    public void setMove(ArrayList<IO_Move> move) {
        this.move = move;
    }

    public ArrayList<IO_Conf_Tracking> getTracking_list() {
        return tracking_list;
    }

    public void setTracking_list(ArrayList<IO_Conf_Tracking> tracking_list) {
        this.tracking_list = tracking_list;
    }
}
