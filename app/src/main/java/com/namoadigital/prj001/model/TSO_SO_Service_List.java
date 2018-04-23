package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

public class TSO_SO_Service_List {
    @Expose
    private ArrayList<SM_SO> so;

    public ArrayList<SM_SO> getSo() {
        return so;
    }

    public void setSo(ArrayList<SM_SO> so) {
        this.so = so;
    }
}
