package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 22/05/2017.
 */

public class TProduct_Serial {

    private long CUSTOMER_CODE;
    private long PRODUCT_CODE;
    private String PRODUCT_ID;
    private String PRODUCT_DESC;
    private long SERIAL_CODE;
    private String SERIAL_ID;

    public long getCUSTOMER_CODE() {
        return CUSTOMER_CODE;
    }

    public void setCUSTOMER_CODE(long CUSTOMER_CODE) {
        this.CUSTOMER_CODE = CUSTOMER_CODE;
    }

    public long getPRODUCT_CODE() {
        return PRODUCT_CODE;
    }

    public void setPRODUCT_CODE(long PRODUCT_CODE) {
        this.PRODUCT_CODE = PRODUCT_CODE;
    }

    public String getPRODUCT_ID() {
        return PRODUCT_ID;
    }

    public void setPRODUCT_ID(String PRODUCT_ID) {
        this.PRODUCT_ID = PRODUCT_ID;
    }

    public String getPRODUCT_DESC() {
        return PRODUCT_DESC;
    }

    public void setPRODUCT_DESC(String PRODUCT_DESC) {
        this.PRODUCT_DESC = PRODUCT_DESC;
    }

    public long getSERIAL_CODE() {
        return SERIAL_CODE;
    }

    public void setSERIAL_CODE(long SERIAL_CODE) {
        this.SERIAL_CODE = SERIAL_CODE;
    }

    public String getSERIAL_ID() {
        return SERIAL_ID;
    }

    public void setSERIAL_ID(String SERIAL_ID) {
        this.SERIAL_ID = SERIAL_ID;
    }
}
