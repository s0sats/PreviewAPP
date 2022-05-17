package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by neomatrix on 20/02/17.
 */

public class TUploadImg_Rec {

    @SerializedName("save") private String save;

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }
}
