package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 7/15/16.
 */

public class TSync_Env {

    private String app_code;
    private String app_version;
    private String session_app;
    private DataPackage data_package;

    public String getApp_code() {
        return app_code;
    }

    public void setApp_code(String app_code) {
        this.app_code = app_code;
    }

    public String getApp_version() {
        return app_version;
    }

    public void setApp_version(String app_version) {
        this.app_version = app_version;
    }

    public String getSession_app() {
        return session_app;
    }

    public void setSession_app(String session_app) {
        this.session_app = session_app;
    }

    public DataPackage getData_package() {
        return data_package;
    }

    public void setData_package(DataPackage data_package) {
        this.data_package = data_package;
    }
}
