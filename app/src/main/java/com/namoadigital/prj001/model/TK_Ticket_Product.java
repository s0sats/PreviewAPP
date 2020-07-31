package com.namoadigital.prj001.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;

public class TK_Ticket_Product {
    @Expose
    private long customer_code;//pk
    @Expose
    private int ticket_prefix;//pk
    @Expose
    private int ticket_code;//pk
    @Expose
    private int product_code;//pk
    private String product_id;
    private String product_desc;
    private String un;
    @Nullable
    private Double qty;
    @Nullable
    private Double qty_used;
    @Nullable
    private String pickup_status;
    @Nullable
    private Double qty_returned;
    @Nullable
    private String return_status;

    public TK_Ticket_Product() {
        this.customer_code = -1;
        this.ticket_prefix = -1;
        this.ticket_code = -1;
    }

    public void setPK(TK_Ticket tk_ticket) {
        this.customer_code = tk_ticket.getCustomer_code();
        this.ticket_prefix = tk_ticket.getTicket_prefix();
        this.ticket_code = tk_ticket.getTicket_code();
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getTicket_prefix() {
        return ticket_prefix;
    }

    public void setTicket_prefix(int ticket_prefix) {
        this.ticket_prefix = ticket_prefix;
    }

    public int getTicket_code() {
        return ticket_code;
    }

    public void setTicket_code(int ticket_code) {
        this.ticket_code = ticket_code;
    }

    public int getProduct_code() {
        return product_code;
    }

    public void setProduct_code(int product_code) {
        this.product_code = product_code;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public String getUn() {
        return un;
    }

    public void setUn(String un) {
        this.un = un;
    }

    @Nullable
    public Double getQty() {
        return qty;
    }

    public void setQty(@Nullable Double qty) {
        this.qty = qty;
    }

    @Nullable
    public Double getQty_used() {
        return qty_used;
    }

    public void setQty_used(@Nullable Double qty_used) {
        this.qty_used = qty_used;
    }

    @Nullable
    public String getPickup_status() {
        return pickup_status;
    }

    public void setPickup_status(@Nullable String pickup_status) {
        this.pickup_status = pickup_status;
    }

    @Nullable
    public Double getQty_returned() {
        return qty_returned;
    }

    public void setQty_returned(@Nullable Double qty_returned) {
        this.qty_returned = qty_returned;
    }

    @Nullable
    public String getReturn_status() {
        return return_status;
    }

    public void setReturn_status(@Nullable String return_status) {
        this.return_status = return_status;
    }
}
