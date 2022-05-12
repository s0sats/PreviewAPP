package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * LUCHE - 13/04/2020
 *
 * USADO SOMENTE NO PROCESSAMENTO DO AGENDAMENTO NÃO POSSUI TABLE, É UMA LISTA TMP
 */

public class MD_Schedule_Exec_Operation {

    @SerializedName("customer_code") private long customer_code;
    @SerializedName("operation_code") private int operation_code;
    @SerializedName("operation_id") private String operation_id;
    @SerializedName("operation_desc") private String operation_desc;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getOperation_code() {
        return operation_code;
    }

    public void setOperation_code(int operation_code) {
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
}
