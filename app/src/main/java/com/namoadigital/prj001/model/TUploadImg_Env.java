package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 20/02/17.
 */

public class TUploadImg_Env {

    private String app_code;
    private String app_version;
    private String device_code;
    private String file_path;
    private int support;
    private String customer_desc;
    private String user_nick;

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

    public String getDevice_code() {
        return device_code;
    }

    public void setDevice_code(String device_code) {
        this.device_code = device_code;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public int getSupport() {
        return support;
    }

    public void setSupport(int support) {
        this.support = support;
    }

    public String getCustomer_desc() {
        return customer_desc;
    }

    public void setCustomer_desc(String customer_desc) {
        this.customer_desc = customer_desc;
    }

    public String getUser_nick() {
        return user_nick;
    }

    public void setUser_nick(String user_nick) {
        this.user_nick = user_nick;
    }
}
