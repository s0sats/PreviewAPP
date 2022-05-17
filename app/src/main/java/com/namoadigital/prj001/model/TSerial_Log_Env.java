package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by d.luche on 22/05/2017.
 */

public class TSerial_Log_Env extends Main_Header_Env{

    @SerializedName("customer_code") private long customer_code;
    @SerializedName("product_code") private long product_code;
    @SerializedName("serial_code") private long serial_code;

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
}
