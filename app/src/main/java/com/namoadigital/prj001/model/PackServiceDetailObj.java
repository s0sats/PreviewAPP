package com.namoadigital.prj001.model;

import java.util.ArrayList;

public class PackServiceDetailObj {

    private long customer_code;
    private int price_list_code;
    private int pack_code;
    private int service_code;
    private String service_desc;
    private String service_desc_full;
    private Double price;
    private int qty;
    private int manual_price;
    private int optional;
    private int require_approval;
    private ArrayList<PackServiceDetailsSiteZonePartnerObj> site_zone;
    //private JSONArray site_zone;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
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

    public int getService_code() {
        return service_code;
    }

    public void setService_code(int service_code) {
        this.service_code = service_code;
    }

    public String getService_desc() {
        return service_desc;
    }

    public void setService_desc(String service_desc) {
        this.service_desc = service_desc;
    }

    public String getService_desc_full() {
        return service_desc_full;
    }

    public void setService_desc_full(String service_desc_full) {
        this.service_desc_full = service_desc_full;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getManual_price() {
        return manual_price;
    }

    public void setManual_price(int manual_price) {
        this.manual_price = manual_price;
    }

    public int getOptional() {
        return optional;
    }

    public void setOptional(int optional) {
        this.optional = optional;
    }

    public int getRequire_approval() {
        return require_approval;
    }

    public void setRequire_approval(int require_approval) {
        this.require_approval = require_approval;
    }

    public ArrayList<PackServiceDetailsSiteZonePartnerObj> getSite_zone() {
        return site_zone;
    }

    public void setSite_zone(ArrayList<PackServiceDetailsSiteZonePartnerObj> site_zone) {
        this.site_zone = site_zone;
    }
}
