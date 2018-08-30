package com.namoadigital.prj001.model;

public class ErrorCfg {
    private String code;
    private String description;

    public ErrorCfg() {
        this.code = "";
        this.description = "";
    }

    public ErrorCfg(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public boolean isError() {
        if (code.trim().equalsIgnoreCase("")) {
            return false;
        } else {
            return true;
        }
    }

    public void copyError(ErrorCfg mError){
        if (mError == null) {
            return;
        }

        this.code = mError.code;
        this.description = mError.description;
    }

    public void clearError(){
        this.code = "";
        this.description = "";
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
