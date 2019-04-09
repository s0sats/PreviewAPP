package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by d.luche on 27/07/2017.
 */

public class IO_Inbound_Save_Return {

    @Expose
    private long customer_code;
    @Expose
    private int inbound_prefix;
    @Expose
    private int inbound_code;
    @Expose
    private int scn;
    @Expose
    private String ret_status;
    @Expose
    private String ret_msg;
    @Expose
    private ArrayList<IO_Inbound> inbound = new ArrayList<>();

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
    }

    public int getInbound_prefix() {
        return inbound_prefix;
    }

    public void setInbound_prefix(int inbound_prefix) {
        this.inbound_prefix = inbound_prefix;
    }

    public int getInbound_code() {
        return inbound_code;
    }

    public void setInbound_code(int inbound_code) {
        this.inbound_code = inbound_code;
    }

    public int getScn() {
        return scn;
    }

    public void setScn(int scn) {
        this.scn = scn;
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

    public ArrayList<IO_Inbound> getInbound() {
        return inbound;
    }

    public void setInbound(ArrayList<IO_Inbound> inbound) {
        this.inbound = inbound;
    }
}
