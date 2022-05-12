package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

public class IO_Move_Reason {

    @SerializedName("customer_code") private long customer_code;
    @SerializedName("reason_code") private int reason_code;
    @SerializedName("reason_id") private String reason_id;
    @SerializedName("reason_desc") private String reason_desc;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getReason_code() {
        return reason_code;
    }

    public void setReason_code(int reason_code) {
        this.reason_code = reason_code;
    }

    public String getReason_id() {
        return reason_id;
    }

    public void setReason_id(String reason_id) {
        this.reason_id = reason_id;
    }

    public String getReason_desc() {
        return reason_desc;
    }

    public void setReason_desc(String reason_desc) {
        this.reason_desc = reason_desc;
    }
}
