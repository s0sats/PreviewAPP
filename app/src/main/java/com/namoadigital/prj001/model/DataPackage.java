package com.namoadigital.prj001.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by DANIEL.LUCHE on 23/01/2017.
 */

public class DataPackage {
    public static final String DATA_PACKAGE_MAIN = "MAIN";
    public static final String DATA_PACKAGE_CHECKLIST = "CHECKLIST";
    public static final String DATA_PACKAGE_SO = "SO";
    public static final String DATA_PACKAGE_SCHEDULE = "SCHEDULE";
    public static final String DATA_PACKAGE_AP = "AP";
    public static final String DATA_PACKAGE_SERIAL = "SERIAL";

    @SerializedName("MAIN") private ArrayList<String> MAIN;
    @SerializedName("CHECKLIST") private ArrayList<Long> CHECKLIST;
    @SerializedName("SO") private ArrayList<T_DataPackage_SM_SO_Env> SO;
    @SerializedName("SCHEDULE") private ArrayList<String> SCHEDULE;
    @SerializedName("AP") private ArrayList<TSearch_Ap_Env.ObjAp> AP;
    @SerializedName("TICKET") private ArrayList<T_DataPackage_TK_Ticket_Env> TICKET;
    @SerializedName("SERIAL") private ArrayList<T_DataPackage_MD_Product_Serial_Structure_Env> SERIAL;

    public DataPackage() {
    }

    public ArrayList<String> getMAIN() {
        return MAIN;
    }

    public void setMAIN(ArrayList<String> MAIN) {
        this.MAIN = MAIN;
    }

    public ArrayList<Long> getCHECKLIST() {
        return CHECKLIST;
    }

    public void setCHECKLIST(ArrayList<Long> CHECKLIST) {
        this.CHECKLIST = CHECKLIST;
    }

    public ArrayList<T_DataPackage_SM_SO_Env> getSO() {
        return SO;
    }

    public void setSO(ArrayList<T_DataPackage_SM_SO_Env> SO) {
        this.SO = SO;
    }

    public ArrayList<String> getSCHEDULE() {
        return SCHEDULE;
    }

    public void setSCHEDULE(ArrayList<String> SCHEDULE) {
        this.SCHEDULE = SCHEDULE;
    }

    public ArrayList<TSearch_Ap_Env.ObjAp> getAP() {
        return AP;
    }

    public void setAP(ArrayList<TSearch_Ap_Env.ObjAp> AP) {
        this.AP = AP;
    }

    public ArrayList<T_DataPackage_TK_Ticket_Env> getTICKET() {
        return TICKET;
    }

    public void setTICKET(ArrayList<T_DataPackage_TK_Ticket_Env> TICKET) {
        this.TICKET = TICKET;
    }

    public ArrayList<T_DataPackage_MD_Product_Serial_Structure_Env> getSERIAL() {
        return SERIAL;
    }

    public void setSERIAL(ArrayList<T_DataPackage_MD_Product_Serial_Structure_Env> SERIAL) {
        this.SERIAL = SERIAL;
    }
}
