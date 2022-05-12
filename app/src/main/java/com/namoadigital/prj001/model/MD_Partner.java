package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by d.luche on 26/06/2017.
 */

public class MD_Partner {

    @SerializedName("customer_code") private long customer_code;
    @SerializedName("partner_code") private int partner_code;
    @SerializedName("partner_id") private String partner_id;
    @SerializedName("partner_desc") private String partner_desc;
    @SerializedName("operational") private int operational;
    @SerializedName("billing") private int billing;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getPartner_code() {
        return partner_code;
    }

    public void setPartner_code(int partner_code) {
        this.partner_code = partner_code;
    }

    public String getPartner_id() {
        return partner_id;
    }

    public void setPartner_id(String partner_id) {
        this.partner_id = partner_id;
    }

    public String getPartner_desc() {
        return partner_desc;
    }

    public void setPartner_desc(String partner_desc) {
        this.partner_desc = partner_desc;
    }

    public int getOperational() {
        return operational;
    }

    public void setOperational(int operational) {
        this.operational = operational;
    }

    public int getBilling() {
        return billing;
    }

    public void setBilling(int billing) {
        this.billing = billing;
    }
}
