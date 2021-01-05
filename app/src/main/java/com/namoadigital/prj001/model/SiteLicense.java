package com.namoadigital.prj001.model;

public class SiteLicense {
    private int customer_code;
    private int site_code;
    private String site_desc;
    private int user_level_code;
    private String user_level_id;
    private int user_level_value;
    private int license_available;
    private int distinct_level;
    private int user_level_changed;

    public int getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(int customer_code) {
        this.customer_code = customer_code;
    }

    public int getSite_code() {
        return site_code;
    }

    public void setSite_code(int site_code) {
        this.site_code = site_code;
    }

    public String getSite_desc() {
        return site_desc;
    }

    public void setSite_desc(String site_desc) {
        this.site_desc = site_desc;
    }

    public int getUser_level_code() {
        return user_level_code;
    }

    public void setUser_level_code(int user_level_code) {
        this.user_level_code = user_level_code;
    }

    public String getUser_level_id() {
        return user_level_id;
    }

    public void setUser_level_id(String user_level_id) {
        this.user_level_id = user_level_id;
    }

    public int getUser_level_value() {
        return user_level_value;
    }

    public void setUser_level_value(int user_level_value) {
        this.user_level_value = user_level_value;
    }

    public int getLicense_available() {
        return license_available;
    }

    public void setLicense_available(int license_available) {
        this.license_available = license_available;
    }

    public int getDistinct_level() {
        return distinct_level;
    }

    public void setDistinct_level(int distinct_level) {
        this.distinct_level = distinct_level;
    }

    public int getUser_level_changed() {
        return user_level_changed;
    }

    public void setUser_level_changed(int user_level_changed) {
        this.user_level_changed = user_level_changed;
    }
}
