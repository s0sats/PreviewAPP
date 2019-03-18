package com.namoadigital.prj001.model;

import java.util.ArrayList;

public class T_IO_Serial_Process_Response {
    private String app;
    private String validation;
    private String link_url;
    private String error_msg;
    private ArrayList<IO_Serial_Process_Record> record;
    private long record_count;
    private long record_page;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public ArrayList<IO_Serial_Process_Record> getRecord() {
        return record;
    }

    public void setRecord(ArrayList<IO_Serial_Process_Record> record) {
        this.record = record;
    }

    public long getRecord_count() {
        return record_count;
    }

    public void setRecord_count(long record_count) {
        this.record_count = record_count;
    }

    public long getRecord_page() {
        return record_page;
    }

    public void setRecord_page(long record_page) {
        this.record_page = record_page;
    }
}
