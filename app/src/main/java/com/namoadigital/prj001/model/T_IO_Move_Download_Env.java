package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

public class T_IO_Move_Download_Env extends Main_Header_Env {

    @SerializedName("move") private String move;

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }
}
