package com.namoadigital.prj001.model;

/**
 * Created by DANIEL.LUCHE on 09/01/2017.
 */

public class WSValidationResult {
    private String type;
    private String msg;
    private String id;
    private boolean isValid;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public WSValidationResult() {
    }

    public WSValidationResult(String type, String msg, String id, boolean isValid) {
        this.type = type;
        this.msg = msg;
        this.id = id;
        this.isValid = isValid;
    }
}