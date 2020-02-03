package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 29/11/2019.
 */

public class T_TK_Ticket_Search_Serial_PK_Env {

    private long customer_code;
    private String product_code;
    private String serial_code;

    public T_TK_Ticket_Search_Serial_PK_Env() {
    }

    public T_TK_Ticket_Search_Serial_PK_Env(long customer_code, String product_code, String serial_code) {
        this.customer_code = customer_code;
        this.product_code = product_code;
        this.serial_code = serial_code;
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(String serial_code) {
        this.serial_code = serial_code;
    }
}
