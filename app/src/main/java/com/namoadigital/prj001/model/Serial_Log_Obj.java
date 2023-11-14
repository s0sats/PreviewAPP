package com.namoadigital.prj001.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.namoadigital.prj001.util.Constant;
import com.namoadigital.prj001.util.ToolBox_Inf;

/**
 * Created by d.luche on 22/05/2017.
 */

public class Serial_Log_Obj {

    @SerializedName("process") private String process;
    @SerializedName("datetime") private String datetime;
    @SerializedName("user_action") private String user_action;
    @SerializedName("description") private String description;
    @SerializedName("sys_status") private String sys_status;
    @SerializedName("location") private String location;
    @SerializedName("sys_process") private String sys_process;
    @SerializedName("sys_pk") private String sys_pk;
    @SerializedName("file_url") private String file_url;
    @Nullable
    @SerializedName("file_name") private String file_name;
    @SerializedName("log_downloaded") private boolean log_downloaded;

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getUser_action() {
        return user_action;
    }

    public void setUser_action(String user_action) {
        this.user_action = user_action;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSys_status() {
        return sys_status;
    }

    public void setSys_status(String sys_status) {
        this.sys_status = sys_status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSys_process() {
        return sys_process;
    }

    public void setSys_process(String sys_process) {
        this.sys_process = sys_process;
    }

    public String getSys_pk() {
        return sys_pk;
    }

    public void setSys_pk(String sys_pk) {
        this.sys_pk = sys_pk;
    }

    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    @Nullable
    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(@Nullable String file_name) {
        this.file_name = file_name;
    }

    public boolean isLog_downloaded() {
        return log_downloaded;
    }

    public void setLog_downloaded(boolean log_downloaded) {
        this.log_downloaded = log_downloaded;
    }

    public String[] getSplitedPk(){
        try {
            String[] pk = sys_pk.replace("|", Constant.MAIN_CONCAT_STRING).split(Constant.MAIN_CONCAT_STRING);
            return  pk;
        }catch (Exception e){
            ToolBox_Inf.registerException(getClass().getName(),e);
            e.printStackTrace();
            return new String[]{};
        }

    }
}
