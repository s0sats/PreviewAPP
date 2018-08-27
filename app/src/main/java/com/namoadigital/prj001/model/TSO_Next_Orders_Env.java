package com.namoadigital.prj001.model;

public class TSO_Next_Orders_Env {

    private String app_code;
    private String app_version;
    private String session_app;
    private long customer_code;
    private String site_code;
    private int zone_code;
    private long operation_code;

    public String getApp_code() {
        return app_code;
    }

    public void setApp_code(String app_code) {
        this.app_code = app_code;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getSession_app() {
        return session_app;
    }

    public void setSession_app(String session_app) {
        this.session_app = session_app;
    }

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

    public int getZone_code() {
        return zone_code;
    }

    public void setZone_code(int zone_code) {
        this.zone_code = zone_code;
    }

    public long getOperation_code() {
        return operation_code;
    }

    public void setOperation_code(long operation_code) {
        this.operation_code = operation_code;
    }
}

