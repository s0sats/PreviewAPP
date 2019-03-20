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
    private int brand_code;
    private String brand_id;
    private String brand_desc;
    private int model_code;
    private String model_id;
    private String model_desc;
    private int color_code;
    private String color_id;
    private String color_desc;
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

    public int getBrand_code() {
        return brand_code;
    }

    public void setBrand_code(int brand_code) {
        this.brand_code = brand_code;
    }

    public String getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(String brand_id) {
        this.brand_id = brand_id;
    }

    public String getBrand_desc() {
        return brand_desc;
    }

    public void setBrand_desc(String brand_desc) {
        this.brand_desc = brand_desc;
    }

    public int getModel_code() {
        return model_code;
    }

    public void setModel_code(int model_code) {
        this.model_code = model_code;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public String getModel_desc() {
        return model_desc;
    }

    public void setModel_desc(String model_desc) {
        this.model_desc = model_desc;
    }

    public int getColor_code() {
        return color_code;
    }

    public void setColor_code(int color_code) {
        this.color_code = color_code;
    }

    public String getColor_id() {
        return color_id;
    }

    public void setColor_id(String color_id) {
        this.color_id = color_id;
    }

    public String getColor_desc() {
        return color_desc;
    }

    public void setColor_desc(String color_desc) {
        this.color_desc = color_desc;
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

    public class TrackingObj implements Serializable {
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
