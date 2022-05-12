package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by d.luche on 23/06/2017.
 */

public class MD_Category_Price {

    @SerializedName("customer_code") private long customer_code;
    @SerializedName("category_price_code") private int category_price_code;
    @SerializedName("category_price_id") private String category_price_id;
    @SerializedName("category_price_desc") private String category_price_desc;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getCategory_price_code() {
        return category_price_code;
    }

    public void setCategory_price_code(int category_price_code) {
        this.category_price_code = category_price_code;
    }

    public String getCategory_price_id() {
        return category_price_id;
    }

    public void setCategory_price_id(String category_price_id) {
        this.category_price_id = category_price_id;
    }

    public String getCategory_price_desc() {
        return category_price_desc;
    }

    public void setCategory_price_desc(String category_price_desc) {
        this.category_price_desc = category_price_desc;
    }
}
