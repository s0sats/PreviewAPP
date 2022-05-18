package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

public class T_IO_Outbound_Download_Env extends Main_Header_Env {

    @SerializedName("outbound") private String outbound;

    public String getOutbound() {
        return outbound;
    }

    public void setOutbound(String outbound) {
        this.outbound = outbound;
    }
}
