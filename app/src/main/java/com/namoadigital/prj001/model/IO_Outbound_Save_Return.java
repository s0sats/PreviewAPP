package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class IO_Outbound_Save_Return {
    @Expose
    private long customer_code;
    @Expose
    private int outbound_prefix;
    @Expose
    private int outbound_code;
    @Expose
    private int scn;
    @Expose
    private String ret_status;
    @Expose
    private String ret_msg;
    @Expose
    private ArrayList<IO_Outbound> outbound = new ArrayList<>();

    public long getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(long customer_code) {
        this.customer_code = customer_code;
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

    public ArrayList<IO_Outbound> getOutbound() {
        return outbound;
    }

    public void setOutbound(ArrayList<IO_Outbound> outbound) {
        this.outbound = outbound;
    }
}
