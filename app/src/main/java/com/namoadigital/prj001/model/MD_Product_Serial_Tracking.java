package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

/**
 * Created by d.luche on 02/09/2017.
 */

public class MD_Product_Serial_Tracking {

    @Expose
    private long customer_code;
    @Expose
    private long product_code;
    @Expose
    private long serial_code;
    @Expose
    private Long serial_tmp;
    @Expose
    private long tracking;

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

    public long getTracking() {
        return tracking;
    }

    public void setTracking(long tracking) {
        this.tracking = tracking;
    }
}
