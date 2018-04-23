package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 27/06/2017.
 */

public class TSO_SO_Service_Item {
    private String type_ps;
    private String customer_code;//long
    private String price_list_code;//int
    private String pack_code;//int
    private String service_code;//int
    private String pack_service_desc;
    private String pack_service_desc_full;
    private String price;//double
    private String manual_price;//double
    private String rating;//int
    private String rating_ref;//double
    private int qty;
    private double price_ref;
    private String comments;
    private boolean selected = true;

    public String getType_ps() {
        return type_ps;
    }

    public void setType_ps(String type_ps) {
        this.type_ps = type_ps;
    }

    public String getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(String customer_code) {
        this.customer_code = customer_code;
    }

    public String getPrice_list_code() {
        return price_list_code;
    }

    public void setPrice_list_code(String price_list_code) {
        this.price_list_code = price_list_code;
    }

    public String getPack_code() {
        return pack_code;
    }

    public void setPack_code(String pack_code) {
        this.pack_code = pack_code;
    }

    public String getService_code() {
        return service_code;
    }

    public void setService_code(String service_code) {
        this.service_code = service_code;
    }

    public String getPack_service_desc() {
        return pack_service_desc;
    }

    public void setPack_service_desc(String pack_service_desc) {
        this.pack_service_desc = pack_service_desc;
    }

    public String getPack_service_desc_full() {
        return pack_service_desc_full;
    }

    public void setPack_service_desc_full(String pack_service_desc_full) {
        this.pack_service_desc_full = pack_service_desc_full;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getManual_price() {
        return manual_price;
    }

    public void setManual_price(String manual_price) {
        this.manual_price = manual_price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating_ref() {
        return rating_ref;
    }

    public void setRating_ref(String rating_ref) {
        this.rating_ref = rating_ref;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public double getPrice_ref() {
        return price_ref;
    }

    public void setPrice_ref(double price_ref) {
        this.price_ref = price_ref;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
