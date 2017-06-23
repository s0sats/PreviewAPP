package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 23/06/2017.
 */

public class MD_Brand_Model {

    private long customer_code;
    private int brand_code;
    private int model_code;
    private String model_id;
    private String model_desc;

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

    public int getModel_code() {
        return model_code;
    }

    public void setModel_code(int model_code) {
        this.model_code = model_code;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public String getModel_desc() {
        return model_desc;
    }

    public void setModel_desc(String model_desc) {
        this.model_desc = model_desc;
    }
}
