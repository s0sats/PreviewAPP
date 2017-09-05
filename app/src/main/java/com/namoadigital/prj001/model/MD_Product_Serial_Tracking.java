package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by d.luche on 02/09/2017.
 */

public class MD_Product_Serial_Tracking implements Serializable {
    private static final long serialVersionUID = -2220422244392353101L;

    @Expose
    private long customer_code;
    @Expose
    private long product_code;
    @Expose
    private long serial_code;
    @Expose
    private Long serial_tmp;
    @Expose
    private String tracking;

    public MD_Product_Serial_Tracking() {
        this.customer_code = -1;
        this.product_code =  -1;
        this.serial_code = -1;
        this.serial_tmp =  -1L;
        this.tracking = "";
    }

    public void setPk(MD_Product_Serial md_product_serial){
        this.customer_code = md_product_serial.getCustomer_code();
        this.product_code = md_product_serial.getProduct_code();
        this.serial_code = md_product_serial.getSerial_code();
        this.serial_tmp = md_product_serial.getSerial_tmp();
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
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

    public Long getSerial_tmp() {
        return serial_tmp;
    }

    public void setSerial_tmp(Long serial_tmp) {
        this.serial_tmp = serial_tmp;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }
}
