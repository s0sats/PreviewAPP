package com.namoadigital.prj001.model;

/**
 * Created by d.luche on 22/05/2017.
 */

public class Serial_Log_Obj {

    private String process;
    private String datetime;
    private String user_action;
    private String description;
    private String sys_status;
    private String location;

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
}
