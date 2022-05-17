package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by d.luche on 27/06/2017.
 */

public class TSerial_Save_Env extends Main_Header_Env {

    @SerializedName("token") private String token;
    @SerializedName("serial") private ArrayList<MD_Product_Serial> serial;

    public ArrayList<MD_Product_Serial> getSerial() {
        return serial;
    }

    public void setSerial(ArrayList<MD_Product_Serial> serial) {
        this.serial = serial;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
