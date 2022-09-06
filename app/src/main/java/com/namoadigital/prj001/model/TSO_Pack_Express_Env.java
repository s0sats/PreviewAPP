package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by d.luche on 23/02/2018.
 */

public class TSO_Pack_Express_Env extends Main_Header_Env {
    @Expose
    private String token;
    @Expose
    private ArrayList<SO_Pack_Express_Local> pack_express;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<SO_Pack_Express_Local> getPack_express() {
        return pack_express;
    }

    public void setPack_express(ArrayList<SO_Pack_Express_Local> pack_express) {
        this.pack_express = pack_express;
    }
}
