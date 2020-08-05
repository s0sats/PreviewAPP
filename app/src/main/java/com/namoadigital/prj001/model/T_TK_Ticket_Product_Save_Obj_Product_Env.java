package com.namoadigital.prj001.model;

public class T_TK_Ticket_Product_Save_Obj_Product_Env {

    private int productCode;
    private String qty;
    private String qtyUsed;

    public T_TK_Ticket_Product_Save_Obj_Product_Env(int productCode, String qty, String qtyUsed) {
        this.productCode = productCode;
        this.qty = qty;
        this.qtyUsed = qtyUsed;
    }

    public int getProductCode() {
        return productCode;
    }

    public void setProductCode(int productCode) {
        this.productCode = productCode;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getQtyUsed() {
        return qtyUsed;
    }

    public void setQtyUsed(String qtyUsed) {
        this.qtyUsed = qtyUsed;
    }

}
