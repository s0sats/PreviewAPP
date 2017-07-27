package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

/**
 * Created by d.luche on 27/07/2017.
 */

public class SO_Save_Return {

    @Expose
    private long customer_code;
    @Expose
    private int so_prefix;
    @Expose
    private int so_code;
    @Expose
    private int so_scn;
    @Expose
    private int so_update;
    @Expose
    private String ret_status;
    @Expose
    private String ret_msg;

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

    public int getSo_scn() {
        return so_scn;
    }

    public void setSo_scn(int so_scn) {
        this.so_scn = so_scn;
    }

    public int getSo_update() {
        return so_update;
    }

    public void setSo_update(int so_update) {
        this.so_update = so_update;
    }

    public String getRet_status() {
        return ret_status;
    }

    public void setRet_status(String ret_status) {
        this.ret_status = ret_status;
    }

    public String getRet_msg() {
        return ret_msg;
    }

    public void setRet_msg(String ret_msg) {
        this.ret_msg = ret_msg;
    }
}
