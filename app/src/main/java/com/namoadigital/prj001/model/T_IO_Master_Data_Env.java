package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

public class T_IO_Master_Data_Env extends Main_Header_Env {
    @SerializedName("site_code") private String site_code;
    @SerializedName("type") private String type;
    @SerializedName("action") private String action;

    public String getSite_code() {
        return site_code;
    }

    public void setSite_code(String site_code) {
        this.site_code = site_code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
