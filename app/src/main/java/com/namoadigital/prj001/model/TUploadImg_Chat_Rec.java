package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by neomatrix on 20/02/17.
 */

public class TUploadImg_Chat_Rec {

    @SerializedName("app") private String app;
    @SerializedName("result") private String result;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
