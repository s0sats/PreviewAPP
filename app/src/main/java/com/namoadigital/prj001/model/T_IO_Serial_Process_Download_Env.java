package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

public class T_IO_Serial_Process_Download_Env extends Main_Header_Env {

    @SerializedName("product_code") private String product_code;
    @SerializedName("serial_code") private String serial_code;

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(String serial_code) {
        this.serial_code = serial_code;
    }
}
