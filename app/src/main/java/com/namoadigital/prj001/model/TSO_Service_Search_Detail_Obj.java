package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class TSO_Service_Search_Detail_Obj implements Serializable {

    @SerializedName("customer_code") private long customer_code;
    @SerializedName("price_list_code") private int price_list_code;
    @SerializedName("pack_code") private int pack_code;
    @SerializedName("service_code") private int service_code;
    @SerializedName("service_desc") private String service_desc;
    @SerializedName("service_desc_full") private String service_desc_full;
    @SerializedName("price") private Double price;
    @SerializedName("price_ref") private Double price_ref;
    @SerializedName("qty") private int qty;
    @SerializedName("manual_price") private int manual_price;
    @SerializedName("optional") private int optional;
    @SerializedName("require_approval") private int require_approval;
    @SerializedName("site_code_selected") private Integer site_code_selected;
    @SerializedName("site_desc_selected") private String site_desc_selected;
    @SerializedName("zone_code_selected") private Integer zone_code_selected;
    @SerializedName("zone_desc_selected") private String zone_desc_selected;
    @SerializedName("partner_code_selected") private Integer partner_code_selected;
    @SerializedName("partner_desc_selected") private String partner_desc_selected;
    @SerializedName("comment") private String comment;
    @SerializedName("isSelected") private boolean isSelected;
    @SerializedName("site_zone") private ArrayList<TSO_Service_Search_Detail_Params_Obj> site_zone;
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

    public Double getPrice_ref() {
        return price_ref;
    }

    public void setPrice_ref(Double price_ref) {
        this.price_ref = price_ref;
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

    public ArrayList<TSO_Service_Search_Detail_Params_Obj> getSite_zone() {
        return site_zone;
    }

    public void setSite_zone(ArrayList<TSO_Service_Search_Detail_Params_Obj> site_zone) {
        this.site_zone = site_zone;
    }

    public Integer getSite_code_selected() {
        return site_code_selected;
    }

    public void setSite_code_selected(Integer site_code_selected) {
        this.site_code_selected = site_code_selected;
    }

    public String getSite_desc_selected() {
        return site_desc_selected;
    }

    public void setSite_desc_selected(String site_desc_selected) {
        this.site_desc_selected = site_desc_selected;
    }

    public Integer getZone_code_selected() {
        return zone_code_selected;
    }

    public void setZone_code_selected(Integer zone_code_selected) {
        this.zone_code_selected = zone_code_selected;
    }

    public String getZone_desc_selected() {
        return zone_desc_selected;
    }

    public void setZone_desc_selected(String zone_desc_selected) {
        this.zone_desc_selected = zone_desc_selected;
    }

    public Integer getPartner_code_selected() {
        return partner_code_selected;
    }

    public void setPartner_code_selected(Integer partner_code_selected) {
        this.partner_code_selected = partner_code_selected;
    }

    public String getPartner_desc_selected() {
        return partner_desc_selected;
    }

    public void setPartner_desc_selected(String partner_desc_selected) {
        this.partner_desc_selected = partner_desc_selected;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
