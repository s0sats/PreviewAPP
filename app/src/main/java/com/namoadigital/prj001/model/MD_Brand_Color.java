package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 23/06/2017.
 */

public class MD_Brand_Color {

    private long customer_code;
    private int brand_code;
    private int color_code;
    private String color_id;
    private String color_desc;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getBrand_code() {
        return brand_code;
    }

    public void setBrand_code(int brand_code) {
        this.brand_code = brand_code;
    }

    public int getColor_code() {
        return color_code;
    }

    public void setColor_code(int color_code) {
        this.color_code = color_code;
    }

    public String getColor_id() {
        return color_id;
    }

    public void setColor_id(String color_id) {
        this.color_id = color_id;
    }

    public String getColor_desc() {
        return color_desc;
    }

    public void setColor_desc(String color_desc) {
        this.color_desc = color_desc;
    }
}
