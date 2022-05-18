package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class T_IO_Outbound_Header_Env extends Main_Header_Env {

    @Expose
    @SerializedName("token") private String token;
    @Expose
    @SerializedName("outbound") private ArrayList<IO_Outbound> outbound = new ArrayList<>();
    @Expose
    @SerializedName("reprocess") private int reprocess;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<IO_Outbound> getOutbound() {
        return outbound;
    }

    public void setOutbound(ArrayList<IO_Outbound> outbound) {
        this.outbound = outbound;
    }

    public int getReprocess() {
        return reprocess;
    }

    public void setReprocess(int reprocess) {
        this.reprocess = reprocess;
    }
}
