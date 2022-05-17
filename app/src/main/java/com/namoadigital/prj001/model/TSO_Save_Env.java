package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by d.luche on 27/06/2017.
 */

public class TSO_Save_Env extends Main_Header_Env {


    @Expose
    @SerializedName("token") private String token;
    @Expose
    @SerializedName("so") private ArrayList<SM_SO> so;
    @Expose
    @SerializedName("reprocess") private int reprocess;

    public ArrayList<SM_SO> getSo() {
        return so;
    }

    public void setSo(ArrayList<SM_SO> so) {
        this.so = so;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getReprocess() {
        return reprocess;
    }

    public void setReprocess(int reprocess) {
        this.reprocess = reprocess;
    }
}
