package com.namoadigital.prj001.model;

/**
 * Created by neomatrix on 5/9/16.
 */
public class TGC_Rec extends Main_Header_Rec {

    private String version;
    private String login;
    private String zip;
    private Integer db_version;
    private Integer user_code;
    private int valid_time = -1 ;
    private long tolerance_time = -1;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Integer getDb_version() {
        return db_version;
    }

    public void setDb_version(Integer db_version) {
        this.db_version = db_version;
    }

    public Integer getUser_code() {
        return user_code;
    }

    public void setUser_code(Integer user_code) {
        this.user_code = user_code;
    }

    public int getValid_time() {
        return valid_time;
    }

    public void setValid_time(int valid_time) {
        this.valid_time = valid_time;
    }

    public long getTolerance_time() {
        return tolerance_time;
    }

    public void setTolerance_time(long tolerance_time) {
        this.tolerance_time = tolerance_time;
    }
}
