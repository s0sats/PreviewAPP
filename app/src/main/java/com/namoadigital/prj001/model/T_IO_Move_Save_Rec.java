package com.namoadigital.prj001.model;

import java.util.ArrayList;

public class T_IO_Move_Save_Rec {
    private String app;
    private String validation;
    private String save;
    private ArrayList<IO_Move_Return> result;

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getValidation() {
        return validation;
    }

    public void setValidation(String validation) {
        this.validation = validation;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public ArrayList<IO_Move_Return> getResult() {
        return result;
    }

    public void setResult(ArrayList<IO_Move_Return> result) {
        this.result = result;
    }
}
