package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by neomatrix on 20/02/17.
 */

public class TUploadImg_Env extends Main_Header_Env {

    @SerializedName("device_code") private String device_code;
    @SerializedName("file_path") private String file_path;

    public String getDevice_code() {
        return device_code;
    }

    public void setDevice_code(String device_code) {
        this.device_code = device_code;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

}
