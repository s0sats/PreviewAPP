package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by d.luche on 03/07/2017.
 */

public class MD_Product_Segment {

    @SerializedName("customer_code") private long customer_code;
    @SerializedName("product_code") private long product_code;
    @SerializedName("segment_code") private int segment_code;

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

    public int getSegment_code() {
        return segment_code;
    }

    public void setSegment_code(int segment_code) {
        this.segment_code = segment_code;
    }
}
