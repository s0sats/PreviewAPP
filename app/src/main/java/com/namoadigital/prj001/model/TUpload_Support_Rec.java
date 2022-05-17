package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by neomatrix on 20/02/17.
 */

public class TUpload_Support_Rec {

    @SerializedName("save") private String save;

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }
}
