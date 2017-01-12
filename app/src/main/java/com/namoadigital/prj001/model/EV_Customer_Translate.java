package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 8/3/16.
 */

public class EV_Customer_Translate {

    private long customer_code;
    private long translate_code;
    private String date_db_customer_translate;


    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public long getTranslate_code() {
        return translate_code;
    }

    public void setTranslate_code(long translate_code) {
        this.translate_code = translate_code;
    }

    public String getDate_db_customer_translate() {
        return date_db_customer_translate;
    }

    public void setDate_db_customer_translate(String date_db_customer_translate) {
        this.date_db_customer_translate = date_db_customer_translate;
    }
}
