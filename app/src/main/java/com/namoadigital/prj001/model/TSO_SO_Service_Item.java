package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 27/06/2017.
 *
 * LUCHE - 03/10/2019
 *
 * Objeto que será enviado como PACK no "save" do adicionar serviço
 *
 * Os campos partner_code e zone_code não são usado para nada e por isso não foram colocados no modelo.
 * Eles serão usados no TSO_SO_Service_Item_Detail
 */

import java.util.ArrayList;

public class TSO_SO_Service_Item {
    private String type_ps;
    private int price_list_code;//int
    private int pack_code;//int
    private int pack_seq;//maior que 100 mil e incrementado a cada item adicionado
    private int service_code;//int
    private double price;//double
    private int manual_price;//double
    private int qty;
    private Integer partner_code;
    private String comments;
    private ArrayList<TSO_SO_Service_Item_Detail> service = new ArrayList<>();

    public String getType_ps() {
        return type_ps;
    }

    public void setType_ps(String type_ps) {
        this.type_ps = type_ps;
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

    public int getPack_seq() {
        return pack_seq;
    }

    public void setPack_seq(int pack_seq) {
        this.pack_seq = pack_seq;
    }

    public int getService_code() {
        return service_code;
    }

    public void setService_code(int service_code) {
        this.service_code = service_code;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getManual_price() {
        return manual_price;
    }

    public void setManual_price(int manual_price) {
        this.manual_price = manual_price;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public ArrayList<TSO_SO_Service_Item_Detail> getService() {
        return service;
    }

    public void setService(ArrayList<TSO_SO_Service_Item_Detail> service) {
        this.service = service;
    }
}
