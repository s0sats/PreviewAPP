package com.namoadigital.prj001.model;

/**
 * Created by DANIEL.LUCHE on 16/01/2017.
 */

public class MD_Site {

    private long customer_code;
    private String site_code;
    private String site_id;
    private String site_desc;
    private int io_control;
    private int inbound_auto_create;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public String getSite_code() {
        return site_code;
    }

    public void setSite_code(String site_code) {
        this.site_code = site_code;
    }

    public String getSite_id() {
        return site_id;
    }

    public void setSite_id(String site_id) {
        this.site_id = site_id;
    }

    public String getSite_desc() {
        return site_desc;
    }

    public void setSite_desc(String site_desc) {
        this.site_desc = site_desc;
    }

    public int getIo_control() {
        return io_control;
    }

    public void setIo_control(int io_control) {
        this.io_control = io_control;
    }

    public int getInbound_auto_create() {
        return inbound_auto_create;
    }

    public void setInbound_auto_create(int inbound_auto_create) {
        this.inbound_auto_create = inbound_auto_create;
    }
}
