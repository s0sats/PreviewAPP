package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class T_IO_Inbound_Header_Env extends Main_Header_Env {

    @Expose
    private String token;
    @Expose
    private ArrayList<IO_Inbound> inbound = new ArrayList<>();
    @Expose
    private int reprocess;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<IO_Inbound> getInbound() {
        return inbound;
    }

    public void setInbound(ArrayList<IO_Inbound> inbound) {
        this.inbound = inbound;
    }

    public int getReprocess() {
        return reprocess;
    }

    public void setReprocess(int reprocess) {
        this.reprocess = reprocess;
    }
}
