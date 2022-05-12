package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DANIEL.LUCHE on 30/03/2017.
 */

public class Ev_User_Customer_Parameter {

    @SerializedName("customer_code") private long customer_code;
    @SerializedName("parameter_code") private String parameter_code;


    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public String getParameter_code() {
        return parameter_code;
    }

    public void setParameter_code(String parameter_code) {
        this.parameter_code = parameter_code;
    }
}
