package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class T_IO_Move_Save_Env extends Main_Header_Env {
    @Expose
    @SerializedName("token")
    private String token;
    @Expose
    @SerializedName("move")
    private List<IO_Move>  move;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<IO_Move> getMove() {
        return move;
    }

    public void setMove(List<IO_Move> move) {
        this.move = move;
    }
}
