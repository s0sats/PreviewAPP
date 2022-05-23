package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TSO_SO_Service_Env extends Main_Header_Env {

    @SerializedName("token") private String token;
    @SerializedName("so") private ArrayList<TSO_SO_Service> so;

    public TSO_SO_Service_Env() {
        this.so = new ArrayList<>();
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<TSO_SO_Service> getSo() {
        return so;
    }

    public void setSo(ArrayList<TSO_SO_Service> so) {
        this.so = so;
    }
}
