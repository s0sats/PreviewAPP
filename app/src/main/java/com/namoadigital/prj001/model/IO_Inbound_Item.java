package com.namoadigital.prj001.model;

import java.io.Serializable;

public class IO_Inbound_Item implements Serializable {
    private static final long serialVersionUID = 6534975604003631255L;

    private long customer_code;
    private int inbound_prefix;
    private int inbound_code;
    private int inbound_item;
    private long product_code;
    private long serial_code;
    private Integer site_code;
    private Integer zone_code;
    private String zone_id;
    private String zone_desc;
    private Integer local_code;
    private String local_id;
    private String conf_date;
    private String status;
    private String comments;
    private Integer planned_zone_code;
    private Integer planned_local_code;
    private Integer planned_class_code;

    public IO_Inbound_Item() {
        this.customer_code = -1;
        this.inbound_prefix = -1;
        this.inbound_code = -1;
        this.inbound_item = -1;
    }

    public void setPK(IO_Inbound io_inbound){
        this.customer_code = io_inbound.getCustomer_code();
        this.inbound_prefix = io_inbound.getInbound_prefix();
        this.inbound_code = io_inbound.getInbound_code();
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getInbound_prefix() {
        return inbound_prefix;
    }

    public void setInbound_prefix(int inbound_prefix) {
        this.inbound_prefix = inbound_prefix;
    }

    public int getInbound_code() {
        return inbound_code;
    }

    public void setInbound_code(int inbound_code) {
        this.inbound_code = inbound_code;
    }

    public int getInbound_item() {
        return inbound_item;
    }

    public void setInbound_item(int inbound_item) {
        this.inbound_item = inbound_item;
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

    public Integer getSite_code() {
        return site_code;
    }

    public void setSite_code(Integer site_code) {
        this.site_code = site_code;
    }

    public Integer getZone_code() {
        return zone_code;
    }

    public void setZone_code(Integer zone_code) {
        this.zone_code = zone_code;
    }

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public String getZone_desc() {
        return zone_desc;
    }

    public void setZone_desc(String zone_desc) {
        this.zone_desc = zone_desc;
    }

    public Integer getLocal_code() {
        return local_code;
    }

    public void setLocal_code(Integer local_code) {
        this.local_code = local_code;
    }

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
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
}
