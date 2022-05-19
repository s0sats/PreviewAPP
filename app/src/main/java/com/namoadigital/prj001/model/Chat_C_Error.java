package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by d.luche on 13/12/2017.
 */

public class Chat_C_Error {

    @SerializedName("error_msg") private String error_msg;
    @SerializedName("error_caller") private String error_caller;

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getError_caller() {
        return error_caller;
    }

    public void setError_caller(String error_caller) {
        this.error_caller = error_caller;
    }
}
