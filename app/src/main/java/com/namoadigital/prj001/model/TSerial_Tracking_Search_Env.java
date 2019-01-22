package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 22/05/2017.
 */

public class TSerial_Tracking_Search_Env extends Main_Header_Env  {

    private String site_code;
    private String product_code;
    private String serial_code;
    private String tracking;

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

    public String getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(String serial_code) {
        this.serial_code = serial_code;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }
}
