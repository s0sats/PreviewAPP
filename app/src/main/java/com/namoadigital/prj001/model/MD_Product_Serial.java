package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 8/9/16.
 */

public class MD_Product_Serial {

    private long customer_code;
    private long product_code;
    private long serial_code;
    private String serial_id;
    private long father_product_code;
    private long father_serial_code;
    private int active;

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

    public String getSerial_id() {
        return serial_id;
    }

    public void setSerial_id(String serial_id) {
        this.serial_id = serial_id;
    }

    public long getFather_product_code() {
        return father_product_code;
    }

    public void setFather_product_code(long father_product_code) {
        this.father_product_code = father_product_code;
    }

    public long getFather_serial_code() {
        return father_serial_code;
    }

    public void setFather_serial_code(long father_serial_code) {
        this.father_serial_code = father_serial_code;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
