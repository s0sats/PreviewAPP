package com.namoadigital.prj001.model;

public class WS_TK_Ticket_Product_Obj {
    private int product_code;
    private Double qty;
    private Double qty_used;
    private Double qty_returned;

    public int getProduct_code() {
        return product_code;
    }

    public void setProduct_code(int product_code) {
        this.product_code = product_code;
    }

    public Double getQty() {
        return qty;
    }

    public void setQty(Double qty) {
        this.qty = qty;
    }

    public Double getQty_used() {
        return qty_used;
    }

    public void setQty_used(Double qty_used) {
        this.qty_used = qty_used;
    }

    public Double getQty_returned() {
        return qty_returned;
    }

    public void setQty_returned(Double qty_returned) {
        this.qty_returned = qty_returned;
    }
}