package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

/**
 * Created by neomatrix on 8/9/16.
 */

public class MD_Product_Serial {

    @Expose
    private long customer_code;
    @Expose
    private long product_code;
    @Expose
    private long serial_code;
    @Expose
    private String serial_id;
    @Expose
    private Integer site_code;
    @Expose
    private Integer zone_code;
    @Expose
    private Integer local_code;
    @Expose
    private Integer site_code_owner;
    @Expose
    private Integer brand_code;
    @Expose
    private Integer model_code;
    @Expose
    private Integer color_code;
    @Expose
    private Integer segment_code;
    @Expose
    private Integer category_price_code;
    @Expose
    private String add_inf1;
    @Expose
    private String add_inf2;
    @Expose
    private String add_inf3;
    @Expose
    private int update_required;
    //SOMENTE PARA ENVIO NO WS
    @Expose
    private int only_position;

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

    public Integer getLocal_code() {
        return local_code;
    }

    public void setLocal_code(Integer local_code) {
        this.local_code = local_code;
    }

    public Integer getSite_code_owner() {
        return site_code_owner;
    }

    public void setSite_code_owner(Integer site_code_owner) {
        this.site_code_owner = site_code_owner;
    }

    public Integer getBrand_code() {
        return brand_code;
    }

    public void setBrand_code(Integer brand_code) {
        this.brand_code = brand_code;
    }

    public Integer getModel_code() {
        return model_code;
    }

    public void setModel_code(Integer model_code) {
        this.model_code = model_code;
    }

    public Integer getColor_code() {
        return color_code;
    }

    public void setColor_code(Integer color_code) {
        this.color_code = color_code;
    }

    public Integer getSegment_code() {
        return segment_code;
    }

    public void setSegment_code(Integer segment_code) {
        this.segment_code = segment_code;
    }

    public Integer getCategory_price_code() {
        return category_price_code;
    }

    public void setCategory_price_code(Integer category_price_code) {
        this.category_price_code = category_price_code;
    }

    public String getAdd_inf1() {
        return add_inf1;
    }

    public void setAdd_inf1(String add_inf1) {
        this.add_inf1 = add_inf1;
    }

    public String getAdd_inf2() {
        return add_inf2;
    }

    public void setAdd_inf2(String add_inf2) {
        this.add_inf2 = add_inf2;
    }

    public String getAdd_inf3() {
        return add_inf3;
    }

    public void setAdd_inf3(String add_inf3) {
        this.add_inf3 = add_inf3;
    }

    public int getUpdate_required() {
        return update_required;
    }

    public void setUpdate_required(int update_required) {
        this.update_required = update_required;
    }

    public int getOnly_position() {
        return only_position;
    }

    public void setOnly_position(int only_position) {
        this.only_position = only_position;
    }
}
