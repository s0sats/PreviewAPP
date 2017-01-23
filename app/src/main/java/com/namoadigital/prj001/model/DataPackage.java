package com.namoadigital.prj001.model;

import java.util.ArrayList;

/**
 * Created by DANIEL.LUCHE on 23/01/2017.
 */

public class DataPackage {

    private ArrayList<String> MAIN;
    private ArrayList<Integer> CHECKLIST;
    private ArrayList<String> OS;

    public DataPackage(ArrayList<String> MAIN , ArrayList<Integer> CHECKLIST, ArrayList<String> OS) {
        this.MAIN = MAIN;
        this.CHECKLIST = CHECKLIST;
        this.OS = OS;
    }

    public ArrayList<String> getMAIN() {
        return MAIN;
    }

    public void setMAIN(ArrayList<String> MAIN) {
        this.MAIN = MAIN;
    }

    public ArrayList<Integer> getCHECKLIST() {
        return CHECKLIST;
    }

    public void setCHECKLIST(ArrayList<Integer> CHECKLIST) {
        this.CHECKLIST = CHECKLIST;
    }

    public ArrayList<String> getOS() {
        return OS;
    }

    public void setOS(ArrayList<String> OS) {
        this.OS = OS;
    }
}
