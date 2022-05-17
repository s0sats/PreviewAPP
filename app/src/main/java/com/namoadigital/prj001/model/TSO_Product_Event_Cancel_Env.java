package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TSO_Product_Event_Cancel_Env extends Main_Header_Env  {
    @Expose
    @SerializedName("so_prefix")
    private int so_prefix; //pk
    @Expose
    @SerializedName("so_code")
    private int so_code; //pk
    @Expose
    @SerializedName("so_scn")
    private int so_scn;
    @Expose
    @SerializedName("seq")
    private int seq; //pk - server
    @Expose
    @SerializedName("token")
    private String token;

    public int getSo_prefix() {
        return so_prefix;
    }

    public void setSo_prefix(int so_prefix) {
        this.so_prefix = so_prefix;
    }

    public int getSo_code() {
        return so_code;
    }

    public void setSo_code(int so_code) {
        this.so_code = so_code;
    }

    public int getSo_scn() {
        return so_scn;
    }

    public void setSo_scn(int so_scn) {
        this.so_scn = so_scn;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
