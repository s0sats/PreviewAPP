package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

public class SM_SO_Client {

    @SerializedName("customer_code") private long customer_code;
    @SerializedName("client_code") private int client_code;
    @SerializedName("client_id") private String client_id;
    @SerializedName("client_name") private String client_name;
    @SerializedName("client_email") private String client_email;
    @SerializedName("client_phone") private String client_phone;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getClient_code() {
        return client_code;
    }

    public void setClient_code(int client_code) {
        this.client_code = client_code;
    }

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getClient_name() {
        return client_name;
    }

    public void setClient_name(String client_name) {
        this.client_name = client_name;
    }

    public String getClient_email() {
        return client_email;
    }

    public void setClient_email(String client_email) {
        this.client_email = client_email;
    }

    public String getClient_phone() {
        return client_phone;
    }

    public void setClient_phone(String client_phone) {
        this.client_phone = client_phone;
    }
}
