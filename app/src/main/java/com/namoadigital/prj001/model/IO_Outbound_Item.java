package com.namoadigital.prj001.model;

public class IO_Outbound_Item {
    private  long customer_code;
    private  int outbound_prefix;
    private  int outbound_code;
    private  int outbound_item;
    private  long product_code;
    private  long serial_code;
    private  String conf_date;
    private  String status;
    private  Integer inbound_code;
    private  Integer inbound_item;
    private  String comments;
    private  Integer inbound_prefix;

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
}
