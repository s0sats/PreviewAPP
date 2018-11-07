package com.namoadigital.prj001.model;

public class ErrorCfg {

    public static final String INSERT = "INSERT";
    public static final String UPDATE = "UPDATE";

    private boolean hasError;
    private String action;
    private String code;
    private String description;
    private String rawMessage;

    public ErrorCfg() {
        this.hasError = false;
        this.action = "";
        this.code = "";
        this.description = "";
        this.rawMessage = "";
    }

    public ErrorCfg(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public void copyError(ErrorCfg mError){
        if (mError == null) {
            return;
        }
        //
        this.hasError = mError.hasError();
        this.action = mError.getAction();
        this.code = mError.code;
        this.description = mError.description;
        this.rawMessage = mError.getRawMessage();
    }

    public void clearError(){
        this.hasError = false;
        this.action = "";
        this.code = "";
        this.description = "";
        this.rawMessage = "";
    }

    public boolean isError(){
        return hasError;
    }


    public boolean hasError() {
        return hasError;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
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

    public String getRawMessage() {
        return rawMessage;
    }

    public void setRawMessage(String rawMessage) {
        this.rawMessage = rawMessage;
    }
}
