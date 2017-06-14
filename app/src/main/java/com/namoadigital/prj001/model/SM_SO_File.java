package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

/**
 * Created by d.luche on 14/06/2017.
 */

public class SM_SO_File {

    @Expose
    private long customer_code;
    @Expose
    private int so_prefix;
    @Expose
    private int so_code;
    @Expose
    private int file_code;

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


}
