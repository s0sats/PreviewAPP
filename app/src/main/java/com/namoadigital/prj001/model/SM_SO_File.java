package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

/**
 * Created by d.luche on 14/06/2017.
 */

public class SM_SO_File {


    private long customer_code; //pk
    private int so_prefix; //pk
    private int so_code; //pk
    @Expose
    private int file_code; //pk
    private String file_name;
    private String file_url;
    private String file_url_local = "";
    private String file_custom_form_pk;

    public SM_SO_File() {
        this.customer_code = -1;
        this.so_prefix = -1;
        this.so_code = -1;
        this.file_code = -1;
    }

    public void setPK(SM_SO so) {
        this.customer_code = so.getCustomer_code();
        this.so_prefix = so.getSo_prefix();
        this.so_code = so.getSo_code();
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

    public int getFile_code() {
        return file_code;
    }

    public void setFile_code(int file_code) {
        this.file_code = file_code;
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

    public String getFile_custom_form_pk() {
        return file_custom_form_pk;
    }

    public void setFile_custom_form_pk(String file_custom_form_pk) {
        this.file_custom_form_pk = file_custom_form_pk;
    }
}
