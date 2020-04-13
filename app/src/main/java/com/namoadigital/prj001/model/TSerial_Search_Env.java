package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 22/05/2017.
 */

public class TSerial_Search_Env extends Main_Header_Env  {

    private String product_code;
    private String product_id;
    private String serial_code;
    private String serial_id;
    private int serial_exact;
    private String tracking;
    private String site_code;
    private int profile_check;

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(String serial_code) {
        this.serial_code = serial_code;
    }

    public String getSerial_id() {
        return serial_id;
    }

    public void setSerial_id(String serial_id) {
        this.serial_id = serial_id;
    }

    public int getSerial_exact() {
        return serial_exact;
    }

    public void setSerial_exact(int serial_exact) {
        this.serial_exact = serial_exact;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    public String getSite_code() {
        return site_code;
    }

    public void setSite_code(String site_code) {
        this.site_code = site_code;
    }

    public int getProfile_check() {
        return profile_check;
    }

    public void setProfile_check(int profile_check) {
        this.profile_check = profile_check;
    }
}
