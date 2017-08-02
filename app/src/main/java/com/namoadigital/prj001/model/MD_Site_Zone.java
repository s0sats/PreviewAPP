package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 23/06/2017.
 */

public class MD_Site_Zone {

    private long customer_code;
    private int site_code;
    private int zone_code;
    private String zone_id;
    private String zone_desc;
    private int blocked;
    private Integer process_seq;

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getSite_code() {
        return site_code;
    }

    public void setSite_code(int site_code) {
        this.site_code = site_code;
    }

    public int getZone_code() {
        return zone_code;
    }

    public void setZone_code(int zone_code) {
        this.zone_code = zone_code;
    }

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public String getZone_desc() {
        return zone_desc;
    }

    public void setZone_desc(String zone_desc) {
        this.zone_desc = zone_desc;
    }

    public int getBlocked() {
        return blocked;
    }

    public void setBlocked(int blocked) {
        this.blocked = blocked;
    }

    public Integer getProcess_seq() {
        return process_seq;
    }

    public void setProcess_seq(Integer process_seq) {
        this.process_seq = process_seq;
    }
}
