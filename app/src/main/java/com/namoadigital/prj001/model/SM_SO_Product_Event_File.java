package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

/**
 * Created by d.luche on 30/10/2017.
 */

public class SM_SO_Product_Event_File {

    private long customer_code; //pk
    private int so_prefix; //pk
    private int so_code; //pk
    private int seq; //pk - server
    private int seq_tmp; //pk
    //
    @Expose
    private int file_code;
    @Expose
    private int file_tmp;
    @Expose
    private String file_name;
    private String file_url;
    private String file_url_local;

    public SM_SO_Product_Event_File() {
        this.customer_code = -1;
        this.so_prefix = -1;
        this.so_code = -1;
        this.seq = -1;
        this.file_name = "";
        this.file_url = "";
        this.file_url_local = "";
    }

    public void setPK(SM_SO_Product_Event productEvent) {
        this.customer_code = productEvent.getCustomer_code();
        this.so_prefix = productEvent.getSo_prefix();
        this.so_code = productEvent.getSo_code();
        this.seq = productEvent.getSeq();
        this.seq_tmp = productEvent.getSeq_tmp();
    }

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getSo_prefix() {
        return so_prefix;
    }

    public void setSo_prefix(int so_prefix) {
        this.so_prefix = so_prefix;
    }

    public int getSo_code() {
        return so_code;
    }

    public void setSo_code(int so_code) {
        this.so_code = so_code;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getSeq_tmp() {
        return seq_tmp;
    }

    public void setSeq_tmp(int seq_tmp) {
        this.seq_tmp = seq_tmp;
    }

    public int getFile_code() {
        return file_code;
    }

    public void setFile_code(int file_code) {
        this.file_code = file_code;
    }

    public int getFile_tmp() {
        return file_tmp;
    }

    public void setFile_tmp(int file_tmp) {
        this.file_tmp = file_tmp;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    public String getFile_url_local() {
        return file_url_local;
    }

    public void setFile_url_local(String file_url_local) {
        this.file_url_local = file_url_local;
    }
}
