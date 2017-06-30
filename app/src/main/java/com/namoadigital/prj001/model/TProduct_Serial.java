package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 22/05/2017.
 */

public class TProduct_Serial {

    private long customer_code;
    private Long product_code;
    private String product_id;
    private String product_desc;
    private String serial_id;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public Long getProduct_code() {
        return product_code;
    }

    public void setProduct_code(Long product_code) {
        this.product_code = product_code;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getSerial_id() {
        return serial_id;
    }

    public void setSerial_id(String serial_id) {
        this.serial_id = serial_id;
    }
}
