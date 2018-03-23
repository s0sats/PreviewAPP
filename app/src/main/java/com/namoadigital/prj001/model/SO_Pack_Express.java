package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 3/22/18.
 */

public class SO_Pack_Express {

    private long customer_code;
    private long site_code;
    private long operation_code;
    private String express_code;
    private String pack_desc;
    private long product_code;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public long getSite_code() {
        return site_code;
    }

    public void setSite_code(long site_code) {
        this.site_code = site_code;
    }

    public long getOperation_code() {
        return operation_code;
    }

    public void setOperation_code(long operation_code) {
        this.operation_code = operation_code;
    }

    public String getExpress_code() {
        return express_code;
    }

    public void setExpress_code(String express_code) {
        this.express_code = express_code;
    }

    public String getPack_desc() {
        return pack_desc;
    }

    public void setPack_desc(String pack_desc) {
        this.pack_desc = pack_desc;
    }

    public long getProduct_code() {
        return product_code;
    }

    public void setProduct_code(long product_code) {
        this.product_code = product_code;
    }
}
