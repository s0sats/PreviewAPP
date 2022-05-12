package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by neomatrix on 7/15/16.
 */

public class TSync_Env extends Main_Header_Env {

    @SerializedName("data_package") private DataPackage data_package;
    @SerializedName("status_jump") private int status_jump;
    @SerializedName("current_time") private String current_time;
    @SerializedName("valid_time") private int valid_time;

    public DataPackage getData_package() {
        return data_package;
    }

    public void setData_package(DataPackage data_package) {
        this.data_package = data_package;
    }

    public int getStatus_jump() {
        return status_jump;
    }

    public void setStatus_jump(int status_jump) {
        this.status_jump = status_jump;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }

    public int getValid_time() {
        return valid_time;
    }

    public void setValid_time(int valid_time) {
        this.valid_time = valid_time;
    }
}
