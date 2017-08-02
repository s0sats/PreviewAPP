package com.namoadigital.prj001.model;

import java.util.ArrayList;

/**
 * Created by DANIEL.LUCHE on 23/01/2017.
 */

public class DataPackage {


    public  static final String DATA_PACKAGE_MAIN = "MAIN";
    public  static final String DATA_PACKAGE_CHECKLIST = "CHECKLIST";
    public  static final String DATA_PACKAGE_SO = "SO";
    public  static final String DATA_PACKAGE_SCHEDULE = "SCHEDULE";

    private ArrayList<String> MAIN;
    private ArrayList<Long> CHECKLIST;
    private ArrayList<String> SO;
    private ArrayList<String> SCHEDULE;

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

    public ArrayList<String> getSO() {
        return SO;
    }

    public void setSO(ArrayList<String> SO) {
        this.SO = SO;
    }

    public ArrayList<String> getSCHEDULE() {
        return SCHEDULE;
    }

    public void setSCHEDULE(ArrayList<String> SCHEDULE) {
        this.SCHEDULE = SCHEDULE;
    }
}
