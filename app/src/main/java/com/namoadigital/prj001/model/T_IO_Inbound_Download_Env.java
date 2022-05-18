package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

public class T_IO_Inbound_Download_Env extends Main_Header_Env {

    @SerializedName("inbound") private String inbound;

    public String getInbound() {
        return inbound;
    }

    public void setInbound(String inbound) {
        this.inbound = inbound;
    }
}
