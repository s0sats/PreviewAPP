package com.namoadigital.prj001.model;

public class IO_Outbound_Search_Record {

    private int customer_code;
    private int outbound_prefix;
    private int outbound_code;
    private String outbound_id;
    private String outbound_desc;
    private String create_date;
    private String eta_date;
    private String invoice_number;
    private String status;
    private String comments;
    private Float perc_done;
    private String to;
    private String modal;
    //Propriedade usada somente no adapter
    private boolean toDownload;
    private boolean sameSiteAsLoggedOrFree;

    public int getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(int customer_code) {
        this.customer_code = customer_code;
    }

    public int getOutbound_prefix() {
        return outbound_prefix;
    }

    public void setOutbound_prefix(int outbound_prefix) {
        this.outbound_prefix = outbound_prefix;
    }

    public int getOutbound_code() {
        return outbound_code;
    }

    public void setOutbound_code(int outbound_code) {
        this.outbound_code = outbound_code;
    }

    public String getOutbound_id() {
        return outbound_id;
    }

    public void setOutbound_id(String outbound_id) {
        this.outbound_id = outbound_id;
    }

    public String getOutbound_desc() {
        return outbound_desc;
    }

    public void setOutbound_desc(String outbound_desc) {
        this.outbound_desc = outbound_desc;
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

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getModal() {
        return modal;
    }

    public void setModal(String modal) {
        this.modal = modal;
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
                outbound_prefix + "|" +
                        outbound_code + "|" +
                        outbound_id + "|" +
                        outbound_desc + "|" +
                        create_date + "|" +
                        eta_date + "|" +
                        invoice_number + "|" +
                        //status + "|" +
                        //comments + "|" +
                        to + "|" +
                        modal)
                .replace("null|","")
                .replace("null","")
                ;
    }
}
