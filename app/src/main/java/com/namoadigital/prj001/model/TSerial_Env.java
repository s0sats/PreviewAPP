package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DANIEL.LUCHE on 03/02/2017.
 */

public class TSerial_Env extends Main_Header_Env{

    @SerializedName("product_code") private Long product_code;
    @SerializedName("serial_id") private String serial_id;

    public Long getProduct_code() {
        return product_code;
    }

    public void setProduct_code(Long product_code) {
        this.product_code = product_code;
    }

    public String getSerial_id() {
        return serial_id;
    }

    public void setSerial_id(String serial_id) {
        this.serial_id = serial_id;
    }
}
