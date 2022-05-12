package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by d.luche on 23/06/2017.
 */

public class MD_Brand {

    @SerializedName("customer_code") private long customer_code;
    @SerializedName("brand_code") private int brand_code;
    @SerializedName("brand_id") private String brand_id;
    @SerializedName("brand_desc") private String brand_desc;

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

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_desc() {
        return brand_desc;
    }

    public void setBrand_desc(String brand_desc) {
        this.brand_desc = brand_desc;
    }
}
