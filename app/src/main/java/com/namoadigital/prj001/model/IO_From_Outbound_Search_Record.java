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
    private int outbound_prefix;
    private int outbound_code;
    private String outbound_id;
    private String outbound_desc;
    private int carrier_code;
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

    public int getOutbound_prefix() {
        return outbound_prefix;
    }

    public void setOutbound_prefix(int outbound_prefix) {
        this.outbound_prefix = outbound_prefix;
    }

    public int getOutbound_code() {
        return outbound_code;
    }

    public void setOutbound_code(int outbound_code) {
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

    public int getCarrier_code() {
        return carrier_code;
    }

    public void setCarrier_code(int carrier_code) {
        this.carrier_code = carrier_code;
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
