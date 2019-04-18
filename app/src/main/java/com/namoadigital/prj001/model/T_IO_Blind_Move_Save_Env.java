package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class T_IO_Blind_Move_Save_Env extends Main_Header_Env {
    @Expose
    private String token;
    @Expose
    private List<IO_Blind_Move>  blind;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<IO_Blind_Move> getBlind() {
        return blind;
    }

    public void setBlind(List<IO_Blind_Move> blind) {
        this.blind = blind;
    }
}
