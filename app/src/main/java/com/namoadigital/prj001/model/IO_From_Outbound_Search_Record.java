package com.namoadigital.prj001.model;

import java.io.Serializable;

/**
 * Modelo compartilhado entre os Ws de busca From Outbound e Transport Order
 * no cab de inbound
 *
 */

public class IO_From_Outbound_Search_Record implements Serializable {

    private Integer customer_code;
    private int count;
    private String from_type;
    private int outbound_from_site_code;
    private String outbound_from_site_id;
    private String outbound_from_site_desc;
    private int outbound_to_site_code;
    private String outbound_to_site_id;
    private String outbound_to_site_desc;
    private Integer outbound_prefix;
    private Integer outbound_code;
    private String outbound_id;
    private String outbound_desc;
    private Integer carrier_code;
    private String carrier_id;
    private String carrier_desc;
    private String truck_number;
    private String driver;

    public Integer getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(Integer customer_code) {
        this.customer_code = customer_code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getFrom_type() {
        return from_type;
    }

    public void setFrom_type(String from_type) {
        this.from_type = from_type;
    }

    public int getOutbound_from_site_code() {
        return outbound_from_site_code;
    }

    public void setOutbound_from_site_code(int outbound_from_site_code) {
        this.outbound_from_site_code = outbound_from_site_code;
    }

    public String getOutbound_from_site_id() {
        return outbound_from_site_id;
    }

    public void setOutbound_from_site_id(String outbound_from_site_id) {
        this.outbound_from_site_id = outbound_from_site_id;
    }

    public String getOutbound_from_site_desc() {
        return outbound_from_site_desc;
    }

    public void setOutbound_from_site_desc(String outbound_from_site_desc) {
        this.outbound_from_site_desc = outbound_from_site_desc;
    }

    public int getOutbound_to_site_code() {
        return outbound_to_site_code;
    }

    public void setOutbound_to_site_code(int outbound_to_site_code) {
        this.outbound_to_site_code = outbound_to_site_code;
    }

    public String getOutbound_to_site_id() {
        return outbound_to_site_id;
    }

    public void setOutbound_to_site_id(String outbound_to_site_id) {
        this.outbound_to_site_id = outbound_to_site_id;
    }

    public String getOutbound_to_site_desc() {
        return outbound_to_site_desc;
    }

    public void setOutbound_to_site_desc(String outbound_to_site_desc) {
        this.outbound_to_site_desc = outbound_to_site_desc;
    }

    public Integer getOutbound_prefix() {
        return outbound_prefix;
    }

    public void setOutbound_prefix(Integer outbound_prefix) {
        this.outbound_prefix = outbound_prefix;
    }

    public Integer getOutbound_code() {
        return outbound_code;
    }

    public void setOutbound_code(Integer outbound_code) {
        this.outbound_code = outbound_code;
    }

    public String getOutbound_id() {
        return outbound_id;
    }

    public void setOutbound_id(String outbound_id) {
        this.outbound_id = outbound_id;
    }

    public String getOutbound_desc() {
        return outbound_desc;
    }

    public void setOutbound_desc(String outbound_desc) {
        this.outbound_desc = outbound_desc;
    }

    public Integer getCarrier_code() {
        return carrier_code;
    }

    public void setCarrier_code(Integer carrier_code) {
        this.carrier_code = carrier_code;
    }

    public String getCarrier_id() {
        return carrier_id;
    }

    public void setCarrier_id(String carrier_id) {
        this.carrier_id = carrier_id;
    }

    public String getCarrier_desc() {
        return carrier_desc;
    }

    public void setCarrier_desc(String carrier_desc) {
        this.carrier_desc = carrier_desc;
    }

    public String getTruck_number() {
        return truck_number;
    }

    public void setTruck_number(String truck_number) {
        this.truck_number = truck_number;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }
}
