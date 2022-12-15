package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

public class TNForm_PDF_Env extends Main_Header_Env {
    @SerializedName("sys_pk") private String sys_pk;
    @SerializedName("sys_type") private String sys_type;

    public String getSys_pk() {
        return sys_pk;
    }

    public void setSys_pk(String sys_pk) {
        this.sys_pk = sys_pk;
    }

    public String getSys_type() {
        return sys_type;
    }

    public void setSys_type(String sys_type) {
        this.sys_type = sys_type;
    }
}
