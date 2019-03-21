package com.namoadigital.prj001.model;

import java.util.ArrayList;

public class T_IO_Serial_Process_Download_Rec{

    private String app;
    private String validation;
    private String link_url;
    private String error_msg;
    private String process_type;
    private ArrayList<T_IO_Serial_Process_Download_Move> move;
    private ArrayList<IO_Outbound> outbound;

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

    public String getLink_url() {
        return link_url;
    }

    public void setLink_url(String link_url) {
        this.link_url = link_url;
    }

    public String getError_msg() {
        return error_msg;
    }

    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    public String getProcess_type() {
        return process_type;
    }

    public void setProcess_type(String process_type) {
        this.process_type = process_type;
    }

    public ArrayList<T_IO_Serial_Process_Download_Move> getMove() {
        return move;
    }

    public void setMove(ArrayList<T_IO_Serial_Process_Download_Move> move) {
        this.move = move;
    }

    public ArrayList<IO_Outbound> getOutbound() {
        return outbound;
    }

    public void setOutbound(ArrayList<IO_Outbound> outbound) {
        this.outbound = outbound;
    }
}
