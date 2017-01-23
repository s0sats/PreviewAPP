package com.namoadigital.prj001.model;

/**
 * Created by DANIEL.LUCHE on 23/01/2017.
 */

public class MD_Product_Group_Product {

    private long customer_code;
    private long group_code;
    private long product_code;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public long getGroup_code() {
        return group_code;
    }

    public void setGroup_code(long group_code) {
        this.group_code = group_code;
    }

    public long getProduct_code() {
        return product_code;
    }

    public void setProduct_code(long product_code) {
        this.product_code = product_code;
    }
}
