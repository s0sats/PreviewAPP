package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by d.luche on 14/06/2017.
 */

public class SM_SO_Pack {

    private long customer_code;
    private int so_prefix;
    private int so_code;
    private int price_list_code;
    @Expose
    private int pack_code;
    private String pack_id;
    private String pack_desc;
    @Expose
    private String rule;
    @Expose
    private String billing_type;
    @Expose
    private int express;
    @Expose
    private String selection_type;
    @Expose
    private ArrayList<SM_SO_Service> service;

    public SM_SO_Pack() {
        this.customer_code = -1;
        this.so_prefix = -1;
        this.so_code = -1;
        this.service = new ArrayList<>();
    }

    public void setPK(SM_SO so){
        this.customer_code = so.getCustomer_code();
        this.so_prefix = so.getSo_prefix();
        this.so_code = so.getSo_code();

        for(int i=0; i < service.size();i++){
            service.get(i).setPK(this);
        }
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getSo_prefix() {
        return so_prefix;
    }

    public void setSo_prefix(int so_prefix) {
        this.so_prefix = so_prefix;
    }

    public int getSo_code() {
        return so_code;
    }

    public void setSo_code(int so_code) {
        this.so_code = so_code;
    }

    public int getPrice_list_code() {
        return price_list_code;
    }

    public void setPrice_list_code(int price_list_code) {
        this.price_list_code = price_list_code;
    }

    public int getPack_code() {
        return pack_code;
    }

    public void setPack_code(int pack_code) {
        this.pack_code = pack_code;
    }

    public String getPack_id() {
        return pack_id;
    }

    public void setPack_id(String pack_id) {
        this.pack_id = pack_id;
    }

    public String getPack_desc() {
        return pack_desc;
    }

    public void setPack_desc(String pack_desc) {
        this.pack_desc = pack_desc;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public String getBilling_type() {
        return billing_type;
    }

    public void setBilling_type(String billing_type) {
        this.billing_type = billing_type;
    }

    public int getExpress() {
        return express;
    }

    public void setExpress(int express) {
        this.express = express;
    }

    public String getSelection_type() {
        return selection_type;
    }

    public void setSelection_type(String selection_type) {
        this.selection_type = selection_type;
    }

    public ArrayList<SM_SO_Service> getService() {
        return service;
    }

    public void setService(ArrayList<SM_SO_Service> service) {
        this.service = service;
    }
}
