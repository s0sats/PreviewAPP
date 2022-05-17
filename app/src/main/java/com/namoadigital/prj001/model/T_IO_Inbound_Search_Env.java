package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

public class T_IO_Inbound_Search_Env extends Main_Header_Env {

    @SerializedName("site_code") private String site_code;
    @SerializedName("zone_code") private String zone_code;
    @SerializedName("local_code") private String local_code;
    @SerializedName("code_id") private String code_id;
    @SerializedName("invoice") private String invoice;

    public String getSite_code() {
        return site_code;
    }

    public void setSite_code(String site_code) {
        this.site_code = site_code;
    }

    public String getZone_code() {
        return zone_code;
    }

    public void setZone_code(String zone_code) {
        this.zone_code = zone_code;
    }

    public String getLocal_code() {
        return local_code;
    }

    public void setLocal_code(String local_code) {
        this.local_code = local_code;
    }

    public String getCode_id() {
        return code_id;
    }

    public void setCode_id(String code_id) {
        this.code_id = code_id;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }
}
