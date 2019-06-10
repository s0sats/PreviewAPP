package com.namoadigital.prj001.model;

import java.io.Serializable;

public class IO_Inbound_Search_Record implements Serializable {
    private static final long serialVersionUID = -590062680310963072L;
    private int customer_code;
    private int inbound_prefix;
    private int inbound_code;
    private String inbound_id;
    private String inbound_desc;
    private String create_date;
    private String eta_date;
    private String invoice_number;
    private String status;
    private String comments;
    private Float perc_done;
    private String from;
    private String modal;
    private String transport_order;
    //Propriedade usada somente no adapter
    private boolean toDownload;
    private boolean sameSiteAsLoggedOrFree;

    public int getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(int customer_code) {
        this.customer_code = customer_code;
    }

    public int getInbound_prefix() {
        return inbound_prefix;
    }

    public void setInbound_prefix(int inbound_prefix) {
        this.inbound_prefix = inbound_prefix;
    }

    public int getInbound_code() {
        return inbound_code;
    }

    public void setInbound_code(int inbound_code) {
        this.inbound_code = inbound_code;
    }

    public String getInbound_id() {
        return inbound_id;
    }

    public void setInbound_id(String inbound_id) {
        this.inbound_id = inbound_id;
    }

    public String getInbound_desc() {
        return inbound_desc;
    }

    public void setInbound_desc(String inbound_desc) {
        this.inbound_desc = inbound_desc;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getEta_date() {
        return eta_date;
    }

    public void setEta_date(String eta_date) {
        this.eta_date = eta_date;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Float getPerc_done() {
        return perc_done;
    }

    public void setPerc_done(Float perc_done) {
        this.perc_done = perc_done;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getModal() {
        return modal;
    }

    public void setModal(String modal) {
        this.modal = modal;
    }

    public String getTransport_order() {
        return transport_order;
    }

    public void setTransport_order(String transport_order) {
        this.transport_order = transport_order;
    }

    public boolean isToDownload() {
        return toDownload;
    }

    public void setToDownload(boolean toDownload) {
        this.toDownload = toDownload;
    }

    public boolean isSameSiteAsLoggedOrFree() {
        return sameSiteAsLoggedOrFree;
    }

    public void setSameSiteAsLoggedOrFree(boolean sameSiteAsLoggedOrFree) {
        this.sameSiteAsLoggedOrFree = sameSiteAsLoggedOrFree;
    }

    public String getAllFieldForFilter(){

        return  (
                inbound_prefix + "|" +
                inbound_code + "|" +
                inbound_id + "|" +
                inbound_desc + "|" +
                create_date + "|" +
                eta_date + "|" +
                invoice_number + "|" +
                //status + "|" +
                //comments + "|" +
                from + "|" +
                modal)
                .replace("null|","")
                .replace("null","")
                ;
    }
}
