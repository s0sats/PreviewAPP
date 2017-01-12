package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 8/9/16.
 */

public class MD_Product_Category {

    private long customer_code;
    private int category_code;
    private int category_code_father;
    private String struc_type;
    private long product_code;
    private String category_desc;
    private int active;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getCategory_code() {
        return category_code;
    }

    public void setCategory_code(int category_code) {
        this.category_code = category_code;
    }

    public int getCategory_code_father() {
        return category_code_father;
    }

    public void setCategory_code_father(int category_code_father) {
        this.category_code_father = category_code_father;
    }

    public String getStruc_type() {
        return struc_type;
    }

    public void setStruc_type(String struc_type) {
        this.struc_type = struc_type;
    }

    public long getProduct_code() {
        return product_code;
    }

    public void setProduct_code(long product_code) {
        this.product_code = product_code;
    }

    public String getCategory_desc() {
        return category_desc;
    }

    public void setCategory_desc(String category_desc) {
        this.category_desc = category_desc;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }
}
