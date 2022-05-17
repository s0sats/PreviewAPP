package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SO_Creation_Env extends Main_Header_Env {

    @SerializedName("token") private String token;
    @SerializedName("so") private ArrayList<SO_Creation_Obj> so;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<SO_Creation_Obj> getSo() {
        return so;
    }

    public void setSo(ArrayList<SO_Creation_Obj> so) {
        this.so = so;
    }
}
