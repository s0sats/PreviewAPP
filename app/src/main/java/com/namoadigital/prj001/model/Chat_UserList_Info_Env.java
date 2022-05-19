package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by d.luche on 10/01/2018.
 */

public class Chat_UserList_Info_Env {

    @SerializedName("session_app") private String session_app;
    @SerializedName("customer_code") private String customer_code;
    @SerializedName("profile") private String profile;
    @SerializedName("room_code") private String room_code;

    public String getSession_app() {
        return session_app;
    }

    public void setSession_app(String session_app) {
        this.session_app = session_app;
    }

    public String getCustomer_code() {
        return customer_code;
    }

    public void setCustomer_code(String customer_code) {
        this.customer_code = customer_code;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }
}
