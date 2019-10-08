package com.namoadigital.prj001.model;

public class TSO_SO_Service_Item_Detail {

    private int category_price_code;
    private int service_code;
    private long service_seq;//maior que 100 mil e incrementado a cada item adicionado
    private int qty;
    private Integer partner_code;
    private String price;
    private String comments;
    private Integer site_code;
    private Integer zone_code;

    public TSO_SO_Service_Item_Detail(int category_price_code, int service_code, long service_seq, int qty, Integer partner_code, String price, String comments, Integer site_code, Integer zone_code) {
        this.category_price_code = category_price_code;
        this.service_code = service_code;
        this.service_seq = service_seq;
        this.qty = qty;
        this.partner_code = partner_code;
        this.price = price;
        this.comments = comments;
        this.site_code = site_code;
        this.zone_code = zone_code;
    }

    public TSO_SO_Service_Item_Detail() {}

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

    public Integer getPartner_code() {
        return partner_code;
    }

    public void setPartner_code(Integer partner_code) {
        this.partner_code = partner_code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Integer getZone_code() {
        return zone_code;
    }

    public void setZone_code(Integer zone_code) {
        this.zone_code = zone_code;
    }

    public Integer getSite_code() {
        return site_code;
    }

    public void setSite_code(Integer site_code) {
        this.site_code = site_code;
    }
}
