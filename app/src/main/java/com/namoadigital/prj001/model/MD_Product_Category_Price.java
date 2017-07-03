package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 03/07/2017.
 */

public class MD_Product_Category_Price {

    private long customer_code;
    private long product_code;
    private int category_price_code;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public long getProduct_code() {
        return product_code;
    }

    public void setProduct_code(long product_code) {
        this.product_code = product_code;
    }

    public int getCategory_price_code() {
        return category_price_code;
    }

    public void setCategory_price_code(int category_price_code) {
        this.category_price_code = category_price_code;
    }
}
