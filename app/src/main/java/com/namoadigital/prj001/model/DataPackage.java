package com.namoadigital.prj001.model;

import java.util.ArrayList;

/**
 * Created by DANIEL.LUCHE on 23/01/2017.
 */

public class DataPackage {

    private ArrayList<String> MAIN;
    private ArrayList<Long> CHECKLIST;
    private ArrayList<String> OS;

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

    public ArrayList<String> getOS() {
        return OS;
    }

    public void setOS(ArrayList<String> OS) {
        this.OS = OS;
    }
}
