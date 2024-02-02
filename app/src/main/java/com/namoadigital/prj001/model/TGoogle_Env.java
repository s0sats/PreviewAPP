package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by neomatrix on 17/04/17.
 *
 * Luche - 22/01/2019
 *
 * Add extends da classe Main_Header_Env e comentado propriedades da
 * propria classe.
 */

public class TGoogle_Env extends Main_Header_Env {

    @SerializedName("gcm_id") private String gcm_id;

    public String getGcm_id() {
        return gcm_id;
    }

    public void setGcm_id(String gcm_id) {
        this.gcm_id = gcm_id;
    }
}
