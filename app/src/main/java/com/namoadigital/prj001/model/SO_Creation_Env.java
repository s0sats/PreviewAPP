package com.namoadigital.prj001.model;

import java.util.ArrayList;

public class SO_Creation_Env extends Main_Header_Env {

    private String token;
    private ArrayList<SO_Creation_Obj> so;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<SO_Creation_Obj> getSo() {
        return so;
    }

    public void setSo(ArrayList<SO_Creation_Obj> so) {
        this.so = so;
    }
}
