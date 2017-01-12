package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 5/9/16.
 */
public class Cus_Trans_Dt {

    //chave
    private long customer_code;
    private int translate_code;
    private String date_db_customer_translate;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getTranslate_code() {
        return translate_code;
    }

    public void setTranslate_code(int translate_code) {
        this.translate_code = translate_code;
    }

    public String getDate_db_customer_translate() {
        return date_db_customer_translate;
    }

    public void setDate_db_customer_translate(String date_db_customer_translate) {
        this.date_db_customer_translate = date_db_customer_translate;
    }
}
