package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 20/02/17.
 */

public class TUpload_Support_Env extends Main_Header_Env {


    private String device_code;
    private String file_path;
    private String customer_desc;
    private long customer_code;
    private String user_code;
    private String user_nick;
    private int support;
    private String support_msg;

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

    public String getCustomer_desc() {
        return customer_desc;
    }

    public void setCustomer_desc(String customer_desc) {
        this.customer_desc = customer_desc;
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public String getUser_code() {
        return user_code;
    }

    public void setUser_code(String user_code) {
        this.user_code = user_code;
    }

    public String getUser_nick() {
        return user_nick;
    }

    public void setUser_nick(String user_nick) {
        this.user_nick = user_nick;
    }

    public int getSupport() {
        return support;
    }

    public void setSupport(int support) {
        this.support = support;
    }

    public String getSupport_msg() {
        return support_msg;
    }

    public void setSupport_msg(String support_msg) {
        this.support_msg = support_msg;
    }
}