package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

public class T_IO_Transport_Order_Out_Search_Env extends Main_Header_Env {

    @SerializedName("transport_order") private String transport_order;

    public String getTransport_order() {
        return transport_order;
    }

    public void setTransport_order(String transport_order) {
        this.transport_order = transport_order;
    }
}
