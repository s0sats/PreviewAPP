package com.namoadigital.prj001.model;

public class TSO_SO_Service_Item_Detail {

    private int category_price_code;
    private int service_code;
    private long service_seq;
    private int qty;
    private int partner_code;
    private double price;
    private String comments;
    private int zone_code;
    private int site_code;

    public int getCategory_price_code() {
        return category_price_code;
    }

    public void setCategory_price_code(int category_price_code) {
        this.category_price_code = category_price_code;
    }

    public int getService_code() {
        return service_code;
    }

    public void setService_code(int service_code) {
        this.service_code = service_code;
    }

    public long getService_seq() {
        return service_seq;
    }

    public void setService_seq(long service_seq) {
        this.service_seq = service_seq;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getPartner_code() {
        return partner_code;
    }

    public void setPartner_code(int partner_code) {
        this.partner_code = partner_code;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getZone_code() {
        return zone_code;
    }

    public void setZone_code(int zone_code) {
        this.zone_code = zone_code;
    }

    public int getSite_code() {
        return site_code;
    }

    public void setSite_code(int site_code) {
        this.site_code = site_code;
    }
}
