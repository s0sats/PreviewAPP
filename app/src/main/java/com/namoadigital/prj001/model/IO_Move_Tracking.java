package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class IO_Move_Tracking implements Serializable {
    @Expose
    @SerializedName("customer_code") private long customer_code;
    @Expose
    @SerializedName("move_prefix") private int move_prefix;
    @Expose
    @SerializedName("move_code") private int move_code;
    @Expose
    @SerializedName("tracking") private String tracking;

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
