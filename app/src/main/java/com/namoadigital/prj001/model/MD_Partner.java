package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 26/06/2017.
 */

public class MD_Partner {

    private long customer_code;
    private int partner_code;
    private String partner_id;
    private String partner_desc;

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
}
