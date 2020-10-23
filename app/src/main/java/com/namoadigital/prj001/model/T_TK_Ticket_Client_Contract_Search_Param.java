package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 29/11/2019.
 */

public class T_TK_Ticket_Client_Contract_Search_Param {

    private long customer_code;
    private String contract_id;
    private String client_id;
    private String ticket_id;

    public T_TK_Ticket_Client_Contract_Search_Param() {
    }

    public T_TK_Ticket_Client_Contract_Search_Param(long customer_code, String contract_id, String client_id, String ticket_id) {
        this.customer_code = customer_code;
        this.contract_id = contract_id;
        this.client_id = client_id;
        this.ticket_id = ticket_id;
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public String getContract_id() {
        return contract_id;
    }

    public void setContract_id(String contract_id) {
        this.contract_id = contract_id;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }
}
