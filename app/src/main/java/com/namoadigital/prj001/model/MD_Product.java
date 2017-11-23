package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 8/9/16.
 */

public class MD_Product {

    private long customer_code;
    private long product_code;
    private String product_id;
    private String product_desc;
    private int require_serial;
    private int allow_new_serial_cl;
    private String un;
    private Integer sketch_code;
    private String sketch_url;
    private String sketch_url_local;
    private Integer sketch_lines;
    private Integer sketch_columns;
    private String sketch_color;


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

    public int getRequire_serial() {
        return require_serial;
    }

    public void setRequire_serial(int require_serial) {
        this.require_serial = require_serial;
    }

    public int getAllow_new_serial_cl() {
        return allow_new_serial_cl;
    }

    public void setAllow_new_serial_cl(int allow_new_serial_cl) {
        this.allow_new_serial_cl = allow_new_serial_cl;
    }

    public String getUn() {
        return un;
    }

    public void setUn(String un) {
        this.un = un;
    }

    public Integer getSketch_code() {
        return sketch_code;
    }

    public void setSketch_code(Integer sketch_code) {
        this.sketch_code = sketch_code;
    }

    public String getSketch_url() {
        return sketch_url;
    }

    public void setSketch_url(String sketch_url) {
        this.sketch_url = sketch_url;
    }

    public String getSketch_url_local() {
        return sketch_url_local;
    }

    public void setSketch_url_local(String sketch_url_local) {
        this.sketch_url_local = sketch_url_local;
    }

    public Integer getSketch_lines() {
        return sketch_lines;
    }

    public void setSketch_lines(Integer sketch_lines) {
        this.sketch_lines = sketch_lines;
    }

    public Integer getSketch_columns() {
        return sketch_columns;
    }

    public void setSketch_columns(Integer sketch_columns) {
        this.sketch_columns = sketch_columns;
    }

    public String getSketch_color() {
        return sketch_color;
    }

    public void setSketch_color(String sketch_color) {
        this.sketch_color = sketch_color;
    }
}
