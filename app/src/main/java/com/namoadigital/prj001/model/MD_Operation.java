package com.namoadigital.prj001.model;

/**
 * Created by DANIEL.LUCHE on 16/01/2017.
 */

public class MD_Operation {

    private long customer_code;
    private long operation_code;
    private String operation_id;
    private String operation_desc;
    private int alias_service_oper;
    private int alias_service_com;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public long getOperation_code() {
        return operation_code;
    }

    public void setOperation_code(long operation_code) {
        this.operation_code = operation_code;
    }

    public String getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(String operation_id) {
        this.operation_id = operation_id;
    }

    public String getOperation_desc() {
        return operation_desc;
    }

    public void setOperation_desc(String operation_desc) {
        this.operation_desc = operation_desc;
    }

    public int getAlias_service_oper() {
        return alias_service_oper;
    }

    public void setAlias_service_oper(int alias_service_oper) {
        this.alias_service_oper = alias_service_oper;
    }
    public int getAlias_service_com() {
        return alias_service_com;
    }

    public void setAlias_service_com(int alias_service_com) {
        this.alias_service_com = alias_service_com;
    }
}
