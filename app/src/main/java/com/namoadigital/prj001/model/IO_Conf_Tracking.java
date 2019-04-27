package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

public class IO_Conf_Tracking {

    @Expose
    private long customer_code;
    @Expose
    private int prefix;
    @Expose
    private int code;
    @Expose
    private int item;
    @Expose
    private String type;
    @Expose
    private String tracking;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getPrefix() {
        return prefix;
    }

    public void setPrefix(int prefix) {
        this.prefix = prefix;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getItem() {
        return item;
    }

    public void setItem(int item) {
        this.item = item;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }
}
