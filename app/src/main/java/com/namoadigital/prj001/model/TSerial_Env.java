package com.namoadigital.prj001.model;

/**
 * Created by DANIEL.LUCHE on 03/02/2017.
 */

public class TSerial_Env extends Main_Header_Env{

    private Long product_code;
    private String serial_id;

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
