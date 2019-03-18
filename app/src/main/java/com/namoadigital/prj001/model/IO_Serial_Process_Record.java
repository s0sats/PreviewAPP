package com.namoadigital.prj001.model;

import java.io.Serializable;
import java.util.ArrayList;

public class IO_Serial_Process_Record implements Serializable {
    private static final long serialVersionUID = -3829891887006189025L;

    private long customer_code;
    private long product_code;
    private String product_id;
    private String product_desc;
    private int serial_code;
    private String serial_id;
    private int site_code;
    private String site_id;
    private String site_desc;
    private int zone_code;
    private String zone_id;
    private String zone_desc;
    private int local_code;
    private String local_id;
    private String process_type;
    private ArrayList<TrackingObj> tracking_list;

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

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public int getSerial_code() {
        return serial_code;
    }

    public void setSerial_code(int serial_code) {
        this.serial_code = serial_code;
    }

    public String getSerial_id() {
        return serial_id;
    }

    public void setSerial_id(String serial_id) {
        this.serial_id = serial_id;
    }

    public int getSite_code() {
        return site_code;
    }

    public void setSite_code(int site_code) {
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

    public int getLocal_code() {
        return local_code;
    }

    public void setLocal_code(int local_code) {
        this.local_code = local_code;
    }

    public String getLocal_id() {
        return local_id;
    }

    public void setLocal_id(String local_id) {
        this.local_id = local_id;
    }

    public String getProcess_type() {
        return process_type;
    }

    public void setProcess_type(String process_type) {
        this.process_type = process_type;
    }

    public ArrayList<TrackingObj> getTracking_list() {
        return tracking_list;
    }

    public void setTracking_list(ArrayList<TrackingObj> tracking_list) {
        this.tracking_list = tracking_list;
    }

    public class TrackingObj implements Serializable{
        private static final long serialVersionUID = 4479285985116101246L;

        private String tracking;

        public String getTracking() {
            return tracking;
        }

        public void setTracking(String tracking) {
            this.tracking = tracking;
        }
    }
}
