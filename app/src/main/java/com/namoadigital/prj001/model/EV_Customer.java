package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 8/3/16.
 */

public class EV_Customer {
    private long customer_code;
    private String date_db_customer;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public String getDate_db_customer() {
        return date_db_customer;
    }

    public void setDate_db_customer(String date_db_customer) {
        this.date_db_customer = date_db_customer;
    }
}
