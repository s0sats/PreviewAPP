package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class IO_Move_Tracking implements Serializable {
    @Expose
    private long customer_code;
    @Expose
    private int move_prefix;
    @Expose
    private int move_code;
    @Expose
    private String tracking;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getMove_prefix() {
        return move_prefix;
    }

    public void setMove_prefix(int move_prefix) {
        this.move_prefix = move_prefix;
    }

    public int getMove_code() {
        return move_code;
    }

    public void setMove_code(int move_code) {
        this.move_code = move_code;
    }

    public String getTracking() {
        return tracking;
    }

    public void setTracking(String tracking) {
        this.tracking = tracking;
    }
}
