package com.namoadigital.prj001.model;

import java.util.ArrayList;

/**
 * Created by d.luche on 27/06/2017.
 */

public class TSO_Service_Search_Obj {

    private String type_ps;
    private long customer_code;//long
    private int price_list_code;//int
    private int pack_code;//int
    private int service_code;//int
    private String pack_service_desc;
    private String pack_service_desc_full;
    private Double price;//double
    private Double price_ref;//Preenchido pelo app
    private int manual_price;//double
    private int rating;//int
    private Double rating_ref;//double
    private ArrayList<TSO_Service_Search_Detail_Obj> service_list = new ArrayList<>();
    private ArrayList<TSO_Service_Search_Detail_Params_Obj> site_zone;//SiteZone quando type_ps = S
    //Atributos apenas para lista
    private boolean selected;
    private boolean anyNullPrice;
    private boolean detailed;
    private int qty;
    private Integer site_code_selected;
    private Integer zone_code_selected;
    private Integer partner_code_selected;
    private String comment;


    public String getType_ps() {
        return type_ps;
    }

    public void setType_ps(String type_ps) {
        this.type_ps = type_ps;
    }

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

    public int getManual_price() {
        return manual_price;
    }

    public void setManual_price(int manual_price) {
        this.manual_price = manual_price;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Double getRating_ref() {
        return rating_ref;
    }

    public void setRating_ref(Double rating_ref) {
        this.rating_ref = rating_ref;
    }

    public ArrayList<TSO_Service_Search_Detail_Obj> getService_list() {
        return service_list;
    }

    public void setService_list(ArrayList<TSO_Service_Search_Detail_Obj> service_list) {
        this.service_list = service_list;
    }

    public ArrayList<TSO_Service_Search_Detail_Params_Obj> getSite_zone() {
        return site_zone;
    }

    public void setSite_zone(ArrayList<TSO_Service_Search_Detail_Params_Obj> site_zone) {
        this.site_zone = site_zone;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean hasNullPrice() {
        return anyNullPrice;
    }

    public void setNullPrice(boolean anyNullPrice) {
        this.anyNullPrice = anyNullPrice;
    }

    public boolean isDetailed() {
        return detailed;
    }

    public void setDetailed(boolean detailed) {
        this.detailed = detailed;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Integer getSite_code_selected() {
        return site_code_selected;
    }

    public void setSite_code_selected(Integer site_code_selected) {
        this.site_code_selected = site_code_selected;
    }

    public Integer getZone_code_selected() {
        return zone_code_selected;
    }

    public void setZone_code_selected(Integer zone_code_selected) {
        this.zone_code_selected = zone_code_selected;
    }

    public Integer getPartner_code_selected() {
        return partner_code_selected;
    }

    public void setPartner_code_selected(Integer partner_code_selected) {
        this.partner_code_selected = partner_code_selected;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
