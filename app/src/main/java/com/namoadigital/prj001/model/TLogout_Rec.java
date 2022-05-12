package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DANIEL.LUCHE on 24/03/2017.
 */

public class TLogout_Rec {

    @SerializedName("logout") private String logout;

    public String getLogout() {
        return logout;
    }

    public void setLogout(String logout) {
        this.logout = logout;
    }
}
