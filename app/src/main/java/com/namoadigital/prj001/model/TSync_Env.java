package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 7/15/16.
 */

public class TSync_Env extends Main_Header_Env {

    private DataPackage data_package;
    private int status_jump;
    public DataPackage getData_package() {
        return data_package;
    }
    private String current_time;

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
}
