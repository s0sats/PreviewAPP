package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

public class T_IO_Move_Search_Env extends Main_Header_Env {

    @SerializedName("site_code") private String site_code;
    @SerializedName("move_type") private String move_type;
    @SerializedName("zone_code") private String zone_code;
    @SerializedName("orientation") private String orientation;

    public String getSite_code() {
        return site_code;
    }

    public void setSite_code(String site_code) {
        this.site_code = site_code;
    }

    public String getMove_type() {
        return move_type;
    }

    public void setMove_type(String move_type) {
        this.move_type = move_type;
    }

    public String getZone_code() {
        return zone_code;
    }

    public void setZone_code(String zone_code) {
        this.zone_code = zone_code;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
}
