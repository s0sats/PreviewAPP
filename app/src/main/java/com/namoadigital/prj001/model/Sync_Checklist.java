package com.namoadigital.prj001.model;

/**
 * Created by DANIEL.LUCHE on 03/02/2017.
 */

public class Sync_Checklist {

    private long customer_code;
    private long product_code;
    private String last_update;


    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public long getProduct_code() {
        return product_code;
    }

    public void setProduct_code(long product_code) {
        this.product_code = product_code;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }
}
