package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

public class T_TK_Ticket_Product_Save_Obj_Product_Env {
    @Expose
    private int product_code;
    @Expose
    private String qty;
    @Expose
    private String qty_used;

    public T_TK_Ticket_Product_Save_Obj_Product_Env(int product_code, String qty, String qty_used) {
        this.product_code = product_code;
        this.qty = qty;
        this.qty_used = qty_used;
    }

    public int getProduct_code() {
        return product_code;
    }

    public void setProduct_code(int product_code) {
        this.product_code = product_code;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getQty_used() {
        return qty_used;
    }

    public void setQty_used(String qty_used) {
        this.qty_used = qty_used;
    }
}
