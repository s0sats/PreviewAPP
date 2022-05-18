package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

public class T_IO_Address_Suggestion_Env extends Main_Header_Env {

    @SerializedName("site_code") private String site_code;
    @SerializedName("product_code") private String product_code;

    public String getSite_code() {
        return site_code;
    }

    public void setSite_code(String site_code) {
        this.site_code = site_code;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }
}
