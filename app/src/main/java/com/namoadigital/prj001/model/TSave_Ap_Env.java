package com.namoadigital.prj001.model;

import com.google.gson.annotations.Expose;

import java.util.ArrayList;

/**
 * Created by d.luche on 23/02/2018.
 *
 * Luche - 22/01/2019
 *
 * Add extends da classe Main_Header_Env e comentado propriedades da
 * propria classe.
 *
 */

public class TSave_Ap_Env extends Main_Header_Env {

    @Expose
    private String token;
    @Expose
    private ArrayList<GE_Custom_Form_Ap> AP;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<GE_Custom_Form_Ap> getAP() {
        return AP;
    }

    public void setAP(ArrayList<GE_Custom_Form_Ap> AP) {
        this.AP = AP;
    }

}
